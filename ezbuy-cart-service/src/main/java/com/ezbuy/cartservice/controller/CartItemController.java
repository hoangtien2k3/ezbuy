package com.ezbuy.cartservice.controller;

import com.ezbuy.cartmodel.constants.UrlPaths;
import com.ezbuy.cartmodel.dto.DeleteUserProductCartDTO;
import com.ezbuy.cartmodel.dto.request.Product;
import com.ezbuy.cartmodel.dto.response.PageCart;
import com.ezbuy.cartmodel.model.CartItem;
import com.ezbuy.cartservice.service.CartItemService;
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
@RequestMapping(UrlPaths.CartItem.PREFIX)
public class CartItemController {
    private final CartItemService cartItemService;

    @DeleteMapping(value = UrlPaths.CartItem.DELETE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<CartItem>> deleteCartItem(@RequestParam(defaultValue = "") String cartItemId) {
        return cartItemService.deleteCartItem(cartItemId);
    }

    @PostMapping(value = UrlPaths.CartItem.ADD)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<CartItem>> addCart(@Valid @RequestBody Product product) {
        return cartItemService.addCartItem(product);
    }

    @PutMapping(value = UrlPaths.CartItem.UPDATE)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<CartItem>> updateQuantity(
            @RequestParam(defaultValue = "") String cartItemId, @RequestParam(defaultValue = "") Long quantity) {
        return cartItemService.updateQuantity(cartItemId, quantity);
    }

    @GetMapping(value = UrlPaths.CartItem.LIST)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<PageCart>> getListCartItem(
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex) {
        return cartItemService.getListCartItem(pageSize, pageIndex);
    }

    @PostMapping(value = UrlPaths.CartItem.DELETE_LIST_ITEM)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse<Object>> deleteListItem(@RequestBody DeleteUserProductCartDTO deleteUserProductCartDTO) {
        return cartItemService.deleteListItem(deleteUserProductCartDTO);
    }
}
