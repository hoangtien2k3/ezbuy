package com.ezbuy.productmodel.response;

import lombok.Data;

@Data
public class TotalSubscriberResponse {

    private Long totalSubscriber;
    private Long totalAboutExpiredSubscriber;
    private Long totalExpiredSubscriber;

    public TotalSubscriberResponse(Long totalSubscriber, Long totalAboutExpiredSubscriber, Long totalExpiredSubscriber) {
        this.totalSubscriber = totalSubscriber;
        this.totalAboutExpiredSubscriber = totalAboutExpiredSubscriber;
        this.totalExpiredSubscriber = totalExpiredSubscriber;
    }
}
