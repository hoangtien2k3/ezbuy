package com.ezbuy.cartservice.service;

import com.ezbuy.cartservice.domain.dto.DeleteUserProductCartDTO;
import com.ezbuy.cartservice.domain.dto.request.Product;
import com.ezbuy.cartservice.domain.dto.response.PageCart;
import com.ezbuy.cartservice.domain.model.CartItem;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface CartItemService {
    Mono<DataResponse<CartItem>> deleteCartItem(String id);

    Mono<DataResponse<CartItem>> addCartItem(Product product);

    Mono<DataResponse<CartItem>> updateQuantity(String cartItemId, Long quantity);

    Mono<DataResponse<PageCart>> getListCartItem(Integer pageSize, Integer pageIndex);

    Mono<DataResponse<Object>> deleteListItem(DeleteUserProductCartDTO deleteUserProductCartDTO);
}
