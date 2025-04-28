package com.mycompany.ecommerce.service;

import com.mycompany.ecommerce.config.Constants;
import com.mycompany.ecommerce.domain.Authority;
import com.mycompany.ecommerce.domain.User;
import com.mycompany.ecommerce.repository.AuthorityRepository;
import com.mycompany.ecommerce.repository.UserRepository;
import com.mycompany.ecommerce.security.AuthoritiesConstants;
import com.mycompany.ecommerce.security.SecurityUtils;
import com.mycompany.ecommerce.service.dto.AdminUserDTO;
import com.mycompany.ecommerce.service.dto.UserDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.jhipster.security.RandomUtil;
import com.mycompany.ecommerce.web.rest.vm.ManagedUserVM;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.domain.Pageable;


/**
 * Service class for managing users.
 */
@Service
public class UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthorityRepository authorityRepository;

  private final WebClient webClient = WebClient.builder().baseUrl("http://customer-service").build();

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authorityRepository = authorityRepository;
  }

  public Mono<User> activateRegistration(String key) {
    LOG.debug("Activating user for activation key {}", key);
    return userRepository
      .findOneByActivationKey(key)
      .flatMap(user -> {
        // activate given user for the registration key.
        user.setActivated(true);
        user.setActivationKey(null);
        return saveUser(user);
      })
      .doOnNext(user -> LOG.debug("Activated user: {}", user));
  }

  public Mono<User> completePasswordReset(String newPassword, String key) {
    LOG.debug("Reset user password for reset key {}", key);
    return userRepository
      .findOneByResetKey(key)
      .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
      .publishOn(Schedulers.boundedElastic())
      .map(user -> {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        user.setResetDate(null);
        return user;
      })
      .flatMap(this::saveUser);
  }

  public Mono<User> requestPasswordReset(String mail) {
    return userRepository
      .findOneByEmailIgnoreCase(mail)
      .filter(User::isActivated)
      .publishOn(Schedulers.boundedElastic())
      .map(user -> {
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        return user;
      })
      .flatMap(this::saveUser);
  }

  // Patch: modify registerUser(ManagedUserVM managedUserVM, String password) if it exists
  public Mono<User> registerUser(ManagedUserVM managedUserVM, String password) {
    AdminUserDTO userDTO = new AdminUserDTO();
    userDTO.setLogin(managedUserVM.getLogin());
    userDTO.setFirstName(managedUserVM.getFirstName());
    userDTO.setLastName(managedUserVM.getLastName());
    userDTO.setEmail(managedUserVM.getEmail());
    userDTO.setImageUrl(managedUserVM.getImageUrl());
    userDTO.setLangKey(managedUserVM.getLangKey());

    return userRepository.findOneByLogin(userDTO.getLogin().toLowerCase())
      .flatMap(existingUser -> Mono.error(new UsernameAlreadyUsedException()))
      .switchIfEmpty(userRepository.findOneByEmailIgnoreCase(userDTO.getEmail())
        .flatMap(existingUser -> Mono.error(new EmailAlreadyUsedException())))
      .then(generateNewUserId())
      .flatMap(newId -> {
        User newUser = new User();
        newUser.setId(newId);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setActivated(true);
        return authorityRepository.findById(AuthoritiesConstants.USER)
          .map(Collections::singleton)
          .doOnNext(newUser::setAuthorities)
          .then(userRepository.save(newUser))
          .flatMap(savedUser -> createCustomerProfile(savedUser).thenReturn(savedUser));
      });
  }

  public Mono<User> createUser(AdminUserDTO userDTO) {
    User user = new User();
    user.setLogin(userDTO.getLogin().toLowerCase());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    if (userDTO.getEmail() != null) {
      user.setEmail(userDTO.getEmail().toLowerCase());
    }
    user.setImageUrl(userDTO.getImageUrl());
    if (userDTO.getLangKey() == null) {
      user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
    } else {
      user.setLangKey(userDTO.getLangKey());
    }
    return Flux.fromIterable(userDTO.getAuthorities() != null ? userDTO.getAuthorities() : new HashSet<>())
      .flatMap(authorityRepository::findById)
      .doOnNext(authority -> user.getAuthorities().add(authority))
      .then(Mono.just(user))
      .publishOn(Schedulers.boundedElastic())
      .map(newUser -> {
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        newUser.setPassword(encryptedPassword);
        newUser.setResetKey(RandomUtil.generateResetKey());
        newUser.setResetDate(Instant.now());
        newUser.setActivated(true);
        return newUser;
      })
      .flatMap(this::saveUser)
      .doOnNext(user1 -> LOG.debug("Created Information for User: {}", user1));
  }

  /**
   * Update all information for a specific user, and return the modified user.
   *
   * @param userDTO user to update.
   * @return updated user.
   */
  public Mono<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
    return userRepository
      .findById(userDTO.getId())
      .flatMap(user -> {
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
          user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        user.setActivated(userDTO.isActivated());
        user.setLangKey(userDTO.getLangKey());
        Set<Authority> managedAuthorities = user.getAuthorities();
        managedAuthorities.clear();
        return Flux.fromIterable(userDTO.getAuthorities())
          .flatMap(authorityRepository::findById)
          .map(managedAuthorities::add)
          .then(Mono.just(user));
      })
      .flatMap(this::saveUser)
      .doOnNext(user -> LOG.debug("Changed Information for User: {}", user))
      .map(AdminUserDTO::new);
  }

  public Mono<Void> deleteUser(String login) {
    return userRepository
      .findOneByLogin(login)
      .flatMap(user -> userRepository.delete(user).thenReturn(user))
      .doOnNext(user -> LOG.debug("Deleted User: {}", user))
      .then();
  }

  /**
   * Update basic information (first name, last name, email, language) for the current user.
   *
   * @param firstName first name of user.
   * @param lastName  last name of user.
   * @param email     email id of user.
   * @param langKey   language key.
   * @param imageUrl  image URL of user.
   * @return a completed {@link Mono}.
   */
  public Mono<Void> updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
    return SecurityUtils.getCurrentUserLogin()
      .flatMap(userRepository::findOneByLogin)
      .flatMap(user -> {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (email != null) {
          user.setEmail(email.toLowerCase());
        }
        user.setLangKey(langKey);
        user.setImageUrl(imageUrl);
        return saveUser(user);
      })
      .doOnNext(user -> LOG.debug("Changed Information for User: {}", user))
      .then();
  }

  private Mono<User> saveUser(User user) {
    return SecurityUtils.getCurrentUserLogin()
      .switchIfEmpty(Mono.just(Constants.SYSTEM))
      .flatMap(login -> {
        if (user.getCreatedBy() == null) {
          user.setCreatedBy(login);
        }
        user.setLastModifiedBy(login);
        return userRepository.save(user);
      });
  }

  public Mono<Void> changePassword(String currentClearTextPassword, String newPassword) {
    return SecurityUtils.getCurrentUserLogin()
      .flatMap(userRepository::findOneByLogin)
      .publishOn(Schedulers.boundedElastic())
      .map(user -> {
        String currentEncryptedPassword = user.getPassword();
        if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
          throw new InvalidPasswordException();
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        return user;
      })
      .flatMap(this::saveUser)
      .doOnNext(user -> LOG.debug("Changed password for User: {}", user))
      .then();
  }

  // Patch: Add generateNewUserId()
  private Mono<String> generateNewUserId() {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    return userRepository.findTopByOrderByCreatedDateDesc()
      .map(user -> {
        String lastId = user.getId();
        if (lastId != null && lastId.startsWith(today)) {
          int sequence = Integer.parseInt(lastId.substring(8)) + 1;
          return today + String.format("%04d", sequence);
        }
        return today + "0001";
      })
      .switchIfEmpty(Mono.just(today + "0001"));
  }

  // Patch: Add createCustomerProfile(User user)
  private Mono<Void> createCustomerProfile(User user) {
    Map<String, String> customerData = new HashMap<>();
    customerData.put("userId", user.getId());
    customerData.put("email", user.getEmail());

    return webClient.post()
      .uri("/api/customers/create-by-user")
      .bodyValue(customerData)
      .retrieve()
      .bodyToMono(Void.class)
      .onErrorResume(error -> Mono.empty());
  }

  public Flux<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
    return userRepository.findAllByIdNotNull(pageable).map(AdminUserDTO::new);
  }

  public Flux<UserDTO> getAllPublicUsers(Pageable pageable) {
    return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
  }

  public Mono<Long> countManagedUsers() {
    return userRepository.count();
  }

  public Mono<User> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findOneByLogin(login);
  }

  public Mono<User> getUserWithAuthorities() {
    return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
  }

  /**
   * Not activated users should be automatically deleted after 3 days.
   * <p>
   * This is scheduled to get fired every day, at 01:00 (am).
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    removeNotActivatedUsersReactively().blockLast();
  }

  public Flux<User> removeNotActivatedUsersReactively() {
    return userRepository
      .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
      .flatMap(user -> userRepository.delete(user).thenReturn(user))
      .doOnNext(user -> LOG.debug("Deleted User: {}", user));
  }

  /**
   * Gets a list of all the authorities.
   * @return a list of all the authorities.
   */
  public Flux<String> getAuthorities() {
    return authorityRepository.findAll().map(Authority::getName);
  }
}
