package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.AccessToken;
import com.ezbuy.authmodel.dto.request.*;
import com.ezbuy.authmodel.dto.response.GetActionLoginReportResponse;
import com.ezbuy.authmodel.dto.response.GetTwoWayPasswordResponse;
import com.ezbuy.authmodel.dto.response.Permission;
import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.UserOtp;
import com.reactify.model.response.DataResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Provides authentication services including login, logout, token management,
 * and user management.
 */
public interface AuthService {

    /**
     * Retrieves an access token for the given login request.
     *
     * @param loginRequest
     *            the login request containing user credentials
     * @return a Mono emitting an Optional containing the access token if
     *         authentication is successful
     */
    Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest);

    /**
     * Retrieves an access token for the given provider login request.
     *
     * @param providerLogin
     *            the provider login request containing provider credentials
     * @return a Mono emitting an Optional containing the access token if
     *         authentication is successful
     */
    Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin);

    /**
     * Refreshes the access token using the given refresh token request.
     *
     * @param refreshTokenRequest
     *            the refresh token request containing the refresh token
     * @return a Mono emitting an Optional containing the new access token if the
     *         refresh is successful
     */
    Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * Logs out the user based on the given logout request.
     *
     * @param logoutRequest
     *            the logout request containing user information
     * @return a Mono emitting a Boolean indicating whether the logout was
     *         successful
     */
    Mono<Boolean> logout(LogoutRequest logoutRequest);

    /**
     * Retrieves permissions for the given client ID.
     *
     * @param clientId
     *            the client ID for which to retrieve permissions
     * @return a Mono emitting a list of permissions for the client
     */
    Mono<List<Permission>> getPermission(String clientId);

    /**
     * Retrieves organizational permissions for the given client ID, ID number, and
     * organization ID.
     *
     * @param clientId
     *            the client ID for which to retrieve permissions
     * @param idNo
     *            the ID number of the user
     * @param orgId
     *            the organization ID
     * @return a Mono emitting a list of organizational permissions for the client
     */
    Mono<List<Permission>> getOrgPermission(String clientId, String idNo, String orgId);

    /**
     * Retrieves permissions for the given client ID, organization ID, and user ID.
     *
     * @param clientId
     *            the client ID for which to retrieve permissions
     * @param orgId
     *            the organization ID
     * @param userId
     *            the user ID
     * @return a Mono emitting a list of permissions for the client
     */
    Mono<List<Permission>> getPermission(String clientId, String orgId, String userId);

    /**
     * Signs up a new user with the given signup request.
     *
     * @param signupRequest
     *            the signup request containing user information
     * @return a Mono emitting the user OTP if the signup is successful
     */
    Mono<UserOtp> signUp(SignupRequest signupRequest);

    /**
     * Creates an account for the given organization account request.
     *
     * @param createOrgAccount
     *            the organization account request containing user information
     * @return a Mono emitting the created user if the account is created
     *         successfully
     */
    // Mono<Individual> createAccount(CreateOrgAccount createOrgAccount);

    /**
     * Initiates the forgot password process for the given request.
     *
     * @param forgotPasswordRequest
     *            the forgot password request containing user information
     * @return a Mono emitting the user OTP if the process is initiated successfully
     */
    Mono<UserOtp> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    /**
     * Resets the password based on the given request and server exchange.
     *
     * @param resetPasswordRequest
     *            the reset password request containing new password information
     * @param serverWebExchange
     *            the server web exchange
     * @return a Mono emitting a DataResponse indicating the result of the password
     *         reset
     */
    Mono<DataResponse<Object>> resetPassword(
            ResetPasswordRequest resetPasswordRequest, ServerWebExchange serverWebExchange);

    /**
     * Changes the password based on the given request and server exchange.
     *
     * @param request
     *            the change password request containing new password information
     * @param serverWebExchange
     *            the server web exchange
     * @return a Mono indicating when the password change is complete
     */
    Mono<Void> changePassword(ChangePasswordRequest request, ServerWebExchange serverWebExchange);

    /**
     * Confirms the OTP and creates a new user based on the given account
     * information.
     *
     * @param createAccount
     *            the account information for the new user
     * @return a Mono emitting the created user if the OTP is confirmed successfully
     */
    Mono<Individual> confirmOtpAndCreateUser(CreateAccount createAccount);

    /**
     * Retrieves all user IDs.
     *
     * @return a Mono emitting a list of all user IDs
     */
    Mono<List<String>> getAllUserId();

    /**
     * Retrieves the two-way password for the given request.
     *
     * @param request
     *            the request containing user information
     * @return a Mono emitting the two-way password response
     */
    Mono<GetTwoWayPasswordResponse> getTwoWayPassword(String request);

    /**
     * Retrieves the action login report based on the given request.
     *
     * @param request
     *            the action login report request containing search parameters
     * @return a Mono emitting the action login report response
     */
    Mono<GetActionLoginReportResponse> getActionLoginReport(GetActionLoginReportRequest request);

    /**
     * Confirms the OTP based on the given request and server exchange.
     *
     * @param confirmOTPRequest
     *            the confirm OTP request containing OTP information
     * @param serverWebExchange
     *            the server web exchange
     * @return a Mono emitting a DataResponse indicating the result of the OTP
     *         confirmation
     */
    Mono<DataResponse<Map<String, String>>> confirmOTP(
            ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);

    /**
     * Generates an OTP based on the given request and server exchange.
     *
     * @param confirmOTPRequest
     *            the confirm OTP request containing user information
     * @param serverWebExchange
     *            the server web exchange
     * @return a Mono emitting a DataResponse containing the generated OTP
     */
    Mono<DataResponse<String>> generateOtp(ConfirmOTPRequest confirmOTPRequest, ServerWebExchange serverWebExchange);
}
