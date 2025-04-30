// File: AccountResource.java
package com.mycompany.ecommerce.web.rest;

import com.mycompany.ecommerce.repository.UserRepository;
import com.mycompany.ecommerce.security.SecurityUtils;
import com.mycompany.ecommerce.service.UserService;
import com.mycompany.ecommerce.service.dto.AdminUserDTO;
import com.mycompany.ecommerce.service.dto.PasswordChangeDTO;
import com.mycompany.ecommerce.web.rest.errors.*;
import com.mycompany.ecommerce.web.rest.vm.KeyAndPasswordVM;
import com.mycompany.ecommerce.web.rest.vm.ManagedUserVM;
import jakarta.validation.Valid;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AccountResource {

  private static class AccountResourceException extends RuntimeException {
    private AccountResourceException(String message) {
      super(message);
    }
  }

  private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

  private final UserRepository userRepository;
  private final UserService userService;

  public AccountResource(UserRepository userRepository, UserService userService) {
    this.userRepository = userRepository;
    this.userService = userService;
  }

  @PostMapping("/register")
  public Mono<ResponseEntity<String>> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
    if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
      throw new InvalidPasswordException();
    }
    return userService.registerUser(managedUserVM, managedUserVM.getPassword())
      .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully."));
  }

  @GetMapping("/account")
  public Mono<AdminUserDTO> getAccount() {
    return userService
      .getUserWithAuthorities()
      .map(AdminUserDTO::new)
      .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")));
  }

  @PostMapping("/account")
  public Mono<Void> saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
    return SecurityUtils.getCurrentUserLogin()
      .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
      .flatMap(userLogin ->
        userRepository
          .findOneByEmailIgnoreCase(userDTO.getEmail())
          .filter(existingUser -> !existingUser.getLogin().equalsIgnoreCase(userLogin))
          .hasElement()
          .flatMap(emailExists -> {
            if (emailExists) {
              throw new EmailAlreadyUsedException();
            }
            return userRepository.findOneByLogin(userLogin);
          })
      )
      .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")))
      .flatMap(user ->
        userService.updateUser(
          userDTO.getFirstName(),
          userDTO.getLastName(),
          userDTO.getEmail(),
          userDTO.getLangKey(),
          userDTO.getImageUrl()
        )
      );
  }

  @PostMapping("/account/change-password")
  public Mono<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
    if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
      throw new InvalidPasswordException();
    }
    return userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
  }

  private static boolean isPasswordLengthInvalid(String password) {
    return (
      StringUtils.isEmpty(password) ||
        password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
        password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
    );
  }
}
