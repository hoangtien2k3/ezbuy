package com.ezbuy.notiservice.repository.query;

public interface NotificationCategoryQuery {
    String findCategoryIdByType = "SELECT nc.id from notification_category nc WHERE nc.type=:categoryType AND status = 1";
}
