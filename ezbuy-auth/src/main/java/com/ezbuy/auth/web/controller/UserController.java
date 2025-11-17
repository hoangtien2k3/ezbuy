package com.ezbuy.auth.web.controller;

import com.ezbuy.auth.application.dto.UserProfileDTO;
import com.ezbuy.auth.application.dto.request.QueryUserRequest;
import com.ezbuy.auth.application.dto.request.UpdateUserRequest;
import com.ezbuy.auth.application.dto.response.QueryUserResponse;
import com.ezbuy.auth.application.dto.response.UserContact;
import com.ezbuy.auth.domain.model.entity.UserProfileEntity;
import com.ezbuy.auth.application.service.UserService;
import com.ezbuy.core.constants.MessageConstant;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.Translator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<UserProfileEntity>>> getUser() {
        return userService
                .getUserProfile()
                .map(rs -> rs.map(userProfile -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, userProfile)))
                        .orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<UserProfileEntity>> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        return userService.update(user).map(rs -> new DataResponse<>(Translator.toLocaleVi("user.update.success"), rs));
    }

    @GetMapping("/contacts")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<List<UserContact>>>> getUserContacts(@RequestBody List<UUID> ids) {
        return userService
                .getUserContacts(ids)
                .collectList()
                .map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<UserProfileEntity>>> getUserById(@PathVariable String id) {
        return userService
                .getUserById(id)
                .map(rs -> rs.map(userProfile -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, userProfile)))
                        .orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/search")
    public Mono<ResponseEntity<DataResponse<QueryUserResponse>>> getUserProfiles(QueryUserRequest request) {
        return userService.queryUserProfile(request)
                .map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse<UserProfileDTO>>> getUserProfile(@PathVariable("id") String userId) {
        return userService
                .getUserProfile(userId)
                .map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping("/keycloak")
    @PreAuthorize("hasAnyAuthority('system', 'admin')")
    public Mono<String> getEmailByKeycloakUsername(@RequestParam String username) {
        return userService.getEmailsByKeycloakUsername(username).map(UserRepresentation::getEmail);
    }
}
