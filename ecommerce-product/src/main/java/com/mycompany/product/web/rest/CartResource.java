package com.mycompany.product.web.rest;

import com.mycompany.product.web.rest.dto.CartItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/cart")
public class CartResource {

    // In-memory store: userId -> list of items
    private final Map<Long, List<CartItemDto>> carts = new ConcurrentHashMap<>();

    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addToCart(
        @PathVariable Long userId,
        @RequestBody CartItemDto item
    ) {
        carts.computeIfAbsent(userId, id -> new ArrayList<>()).add(item);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDto>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(carts.getOrDefault(userId, Collections.emptyList()));
    }
}
