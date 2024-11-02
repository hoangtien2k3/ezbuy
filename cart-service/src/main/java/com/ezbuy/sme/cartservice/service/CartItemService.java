package com.ezbuy.sme.cartservice.service;

import com.ezbuy.cartmodel.dto.DeleteUserProductCartDTO;
import com.ezbuy.cartmodel.dto.request.Product;
import com.ezbuy.cartmodel.dto.response.PageCart;
import com.ezbuy.cartmodel.model.CartItem;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface CartItemService {
    Mono<DataResponse<CartItem>> deleteCartItem(String id);

    Mono<DataResponse<CartItem>> addCartItem(Product product);

    Mono<DataResponse<CartItem>> updateQuantity(String cartItemId, Long quantity);

    Mono<DataResponse<PageCart>> getListCartItem(Integer pageSize, Integer pageIndex);

    Mono<DataResponse<Object>> deleteListItem(DeleteUserProductCartDTO deleteUserProductCartDTO);
}
