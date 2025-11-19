package com.ezbuy.noti.repository.query;

public interface NotificationCategoryQuery {
    String FIND_CATEGORY_ID_BY_TYPE = "SELECT nc.id FROM notification_category nc WHERE nc.type = : categoryType AND status = 1";
}
