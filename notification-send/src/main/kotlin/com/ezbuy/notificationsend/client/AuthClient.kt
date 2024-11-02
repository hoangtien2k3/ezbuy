package com.ezbuy.notificationsend.client

import com.ezbuy.notificationmodel.dto.response.ContactResponse
import reactor.core.publisher.Mono

interface AuthClient {
    fun getContacts(userIds: List<String>): Mono<ContactResponse>
}
