package com.ezbuy.cartmodel.constants;

public class UrlPaths {
    public interface CartItem {
        String PREFIX = "v1/cart-item";
        String DELETE = "delete";
        String ADD = "add-cart";
        String UPDATE = "update-cart";
        String LIST = "list-cart";

        String DELETE_LIST_ITEM = "/delete-list-item";
    }
}
