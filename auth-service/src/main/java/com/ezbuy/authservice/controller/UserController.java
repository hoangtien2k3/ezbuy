//package com.ezbuy.authservice.controller;
//
//import com.ezbuy.authmodel.constants.UrlPaths;
//import com.ezbuy.authmodel.dto.request.QueryUserRequest;
//import com.ezbuy.authmodel.dto.request.SignHashRequest;
//import com.ezbuy.authmodel.dto.request.UpdateUserRequest;
//import com.ezbuy.authmodel.model.UserProfile;
//import com.ezbuy.authservice.service.UserService;
//import com.ezbuy.framework.constants.MessageConstant;
//import com.ezbuy.framework.model.response.DataResponse;
//import com.ezbuy.framework.utils.Translator;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(value = UrlPaths.User.PREFIX)
//public class UserController {
//    private final UserService userService;
//
//    @GetMapping(UrlPaths.User.GET_USER)
//    @PreAuthorize("hasAnyAuthority('user')")
//    public Mono<ResponseEntity<DataResponse>> getUser() {
//        return userService.getUserProfile()
//                .map(rs -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, rs)));
//    }
//
//    @PostMapping(UrlPaths.User.UPDATE_USER)
//    @PreAuthorize("hasAnyAuthority('user')")
//    public Mono<DataResponse<UserProfile>> updateUser(@Valid @RequestBody UpdateUserRequest user) {
//        return userService.update(user)
//                .map(rs -> new DataResponse<>(Translator.toLocaleVi("user.update.success"), rs));
//    }
//
//    @GetMapping(UrlPaths.User.CONTACTS)
//    @PreAuthorize("hasAnyAuthority('system')")
//    public Mono<ResponseEntity<DataResponse>> getUserContacts(@RequestBody List<UUID> ids) {
//        return userService.getUserContacts(ids).collectList()
//                .map(rs -> ResponseEntity.ok(new DataResponse<>(Translator.toLocaleVi("success"), rs)));
//    }
//
//    @GetMapping(UrlPaths.User.GET_USER_BY_ID)
//    @PreAuthorize("hasAnyAuthority('system')")
//    public Mono<ResponseEntity<DataResponse>> getUserById(@PathVariable String id) {
//        return userService.getUserById(id)
//                .map(rs -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, rs)));
//    }
//
//    @GetMapping(UrlPaths.User.GET_CREDENTIALS)
//    @PreAuthorize("hasAnyAuthority('user')")
//    public Mono<ResponseEntity<DataResponse>> getCredentialsList(String idNo) {
//        return userService.getCredentialsList(idNo)
//                .map(rs -> ResponseEntity.ok(new DataResponse<>(MessageConstant.SUCCESS, rs)));
//    }
//
//    @PostMapping(UrlPaths.User.SIGN_HASH)
//    @PreAuthorize("hasAnyAuthority('user')")
//    public Mono<DataResponse> signHash(@RequestBody SignHashRequest request) {
//        return userService.sighHash(request);
//    }
//
//    @PreAuthorize("hasAnyAuthority('admin')")
//    @GetMapping(UrlPaths.User.USER_PROFILES)
//    public Mono<ResponseEntity<DataResponse>> getUserProfiles(QueryUserRequest request) {
//        return userService.queryUserProfile(request)
//                .map(rs -> ResponseEntity.ok(new DataResponse(MessageConstant.SUCCESS, rs)));
//    }
//
//    @GetMapping(UrlPaths.User.EXPORT_PROFILES)
//    public Mono<ResponseEntity<Resource>> exportUserProfiles(QueryUserRequest request) {
//        return userService.exportUser(request)
//                .map(resource -> ResponseEntity.ok()
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
//                        .body(resource)
//                );
//    }
//
//    @GetMapping(UrlPaths.User.GET_PROFILES)
//    public Mono<ResponseEntity<DataResponse>> getUserProfile(@PathVariable("id") String userId) {
//        return userService.getUserProfile(userId)
//                .map(rs -> ResponseEntity.ok(new DataResponse(MessageConstant.SUCCESS, rs)));
//    }
//
//    @GetMapping(UrlPaths.User.TAX_BRANCHES)
//    public Mono<ResponseEntity<DataResponse>> getTaxBranches() {
//        return userService.getTaxBranches().collectList()
//                .map(rs -> ResponseEntity.ok(new DataResponse(MessageConstant.SUCCESS, rs)));
//    }
//
//    @GetMapping(UrlPaths.User.ID_TYPES)
//    public Mono<ResponseEntity<DataResponse>> getIdTypes() {
//        return userService.getIdTypes().collectList()
//                .map(rs -> ResponseEntity.ok(new DataResponse(MessageConstant.SUCCESS, rs)));
//    }
//
//    /**
//     * Đây là hàm đồng bộ ký với các bản ký đang ở trang thái WAITING
//     * @return
//     */
//    @PostMapping(UrlPaths.User.SYNC_SIGN_HASH)
//    @PreAuthorize("hasAnyAuthority('system')")
//    public Mono<ResponseEntity<DataResponse>> syncStatusSighHash() {
//        return userService.getStatusSignHash()
//                .map(rs -> ResponseEntity.ok(new DataResponse(MessageConstant.SUCCESS, rs)));
//    }
//}
