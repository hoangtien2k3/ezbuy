package com.ezbuy.notificationsend.client

import com.ezbuy.notimodel.dto.response.ContactResponse
import reactor.core.publisher.Mono

interface AuthClient {
    fun getContacts(userIds: List<String>): Mono<ContactResponse>
}
