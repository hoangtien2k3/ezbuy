package com.ezbuy.cartservice.web.controller;

import com.ezbuy.cartservice.application.dto.DeleteUserProductCartDTO;
import com.ezbuy.cartservice.application.dto.request.Product;
import com.ezbuy.cartservice.application.dto.response.PageCart;
import com.ezbuy.cartservice.application.service.CartItemService;
import com.ezbuy.cartservice.domain.model.CartItem;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/cart-item")
public class CartItemController {
    private final CartItemService cartItemService;

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<CartItem>> deleteCartItem(@RequestParam(defaultValue = "") String cartItemId) {
        return cartItemService.deleteCartItem(cartItemId);
    }

    @PostMapping("/add-cart")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<CartItem>> addCart(@Valid @RequestBody Product product) {
        return cartItemService.addCartItem(product);
    }

    @PutMapping("/update-cart")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<CartItem>> updateQuantity(
            @RequestParam(defaultValue = "") String cartItemId, @RequestParam(defaultValue = "") Long quantity) {
        return cartItemService.updateQuantity(cartItemId, quantity);
    }

    @GetMapping("/list-cart")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<PageCart>> getListCartItem(
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex) {
        return cartItemService.getListCartItem(pageSize, pageIndex);
    }

    @PostMapping("/delete-list-item")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse<Object>> deleteListItem(@RequestBody DeleteUserProductCartDTO deleteUserProductCartDTO) {
        return cartItemService.deleteListItem(deleteUserProductCartDTO);
    }
}
