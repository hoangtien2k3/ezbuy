package com.ezbuy.auth.web.controller;

import com.ezbuy.auth.shared.constants.UrlPaths;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.User.PREFIX)
public class UserController {
    private final UserService userService;

    @GetMapping(UrlPaths.User.GET_USER)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<UserProfileEntity>>> getUser() {
        return userService
                .getUserProfile()
                .map(rs -> rs.map(userProfile -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, userProfile)))
                        .orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping(UrlPaths.User.UPDATE_USER)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse<UserProfileEntity>> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        return userService.update(user).map(rs -> new DataResponse<>(Translator.toLocaleVi("user.update.success"), rs));
    }

    @GetMapping(UrlPaths.User.CONTACTS)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<List<UserContact>>>> getUserContacts(@RequestBody List<UUID> ids) {
        return userService
                .getUserContacts(ids)
                .collectList()
                .map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping(UrlPaths.User.GET_USER_BY_ID)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<UserProfileEntity>>> getUserById(@PathVariable String id) {
        return userService
                .getUserById(id)
                .map(rs -> rs.map(userProfile -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, userProfile)))
                        .orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping(UrlPaths.User.USER_PROFILES)
    public Mono<ResponseEntity<DataResponse<QueryUserResponse>>> getUserProfiles(QueryUserRequest request) {
        return userService.queryUserProfile(request)
                .map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping(UrlPaths.User.EXPORT_PROFILES)
    public Mono<ResponseEntity<Resource>> exportUserProfiles(QueryUserRequest request) {
        return userService.exportUser(request).map(resource -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource));
    }

    @GetMapping(UrlPaths.User.GET_PROFILES)
    public Mono<ResponseEntity<DataResponse<UserProfileDTO>>> getUserProfile(@PathVariable("id") String userId) {
        return userService
                .getUserProfile(userId)
                .map(rs -> ResponseEntity.ok(DataResponse.success(rs)));
    }

    @GetMapping(UrlPaths.User.KEYCLOAK)
    @PreAuthorize("hasAnyAuthority('system', 'admin')")
    public Mono<String> getEmailByKeycloakUsername(@RequestParam String username) {
        return userService.getEmailsByKeycloakUsername(username).map(UserRepresentation::getEmail);
    }
}
