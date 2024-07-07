package com.ezbuy.framework.config.exception;

import com.ezbuy.framework.constants.CommonErrorCode;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.exception.ValidationException;
import com.ezbuy.framework.model.response.TraceErrorResponse;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.Translator;
import io.micrometer.tracing.Tracer;
//import io.r2dbc.spi.R2dbcException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionResponseConfig {
    private final Tracer tracer;

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> runtimeException(RuntimeException ex, ServerWebExchange serverWebExchange) {
        String traceId = tracer.currentSpan().context().traceId();
        log.error("Runtime exception trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR, "Server error", null, traceId), HttpStatus.INTERNAL_SERVER_ERROR));
    }

//    @ExceptionHandler(R2dbcException.class)
//    public Mono<ResponseEntity<TraceErrorResponse>> r2dbcException(R2dbcException ex, ServerWebExchange serverWebExchange) {
//        String traceId = tracer.currentSpan().context().traceId();
//        log.error("R2dbc trace-id {} , error ", traceId, ex);
//        return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.SQL_ERROR, "Server error", null, traceId), HttpStatus.INTERNAL_SERVER_ERROR));
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> accessDeniedException(AccessDeniedException ex, ServerWebExchange serverWebExchange) {
        String traceId = tracer.currentSpan().context().traceId();
        log.error("Access denied trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.ACCESS_DENIED, "Access denied", null, traceId), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(DataBufferLimitException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> dataBufferLimitException(DataBufferLimitException ex) {
        String traceId = tracer.currentSpan().context().traceId();
        log.error("DataBuffer limit trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.BAD_REQUEST, Translator.toLocale("request.databuffer.limit"), null, traceId), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> serverInputException(ServerWebInputException ex, ServerWebExchange serverWebExchange) {
        String traceId = tracer.currentSpan().context().traceId();
        log.error("Request Input invalid format trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.INVALID_PARAMS, ex.getReason(), null, traceId), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> serverInputException(WebExchangeBindException ex, ServerWebExchange serverWebExchange) {
        String traceId = tracer.currentSpan().context().traceId();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(Translator::toLocaleVi)
                .collect(Collectors.toList());

        String errorValue = String.join(", ", errors);

        // exception convert data fail
        if (errorValue.contains("Failed to convert property value")) {
            return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.invalid.format"), null, traceId), HttpStatus.BAD_REQUEST));
        }

        return Mono.just(new ResponseEntity<>(new TraceErrorResponse(CommonErrorCode.INVALID_PARAMS, errorValue, null, traceId), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> businessException(BusinessException ex, ServerWebExchange serverWebExchange) {
        String traceId = tracer.currentSpan().context().traceId();
        String errorCode = ex.getErrorCode();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (!DataUtil.isNullOrEmpty(errorCode)) {
            if (errorCode.equals(CommonErrorCode.NOT_FOUND)) {
                httpStatus = HttpStatus.NOT_FOUND;
            } else if (errorCode.equals(CommonErrorCode.NO_PERMISSION)) {
                httpStatus = HttpStatus.FORBIDDEN;
            }
        }
        final HttpStatus status = httpStatus;
        return Translator.toLocaleMono(ex.getMessage(), null, ex.getParamsMsg())
                .map(msg -> new ResponseEntity<>(new TraceErrorResponse<>(ex.getErrorCode(), msg, null, traceId), status));
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<TraceErrorResponse>> validationException(ValidationException ex, ServerWebExchange serverWebExchange) {
        String traceId = tracer.currentSpan().context().traceId();
        log.error("Validation exception trace-id {} , error ", traceId, ex);
        return Translator.toLocaleMono(ex.getErrorMessage(), null, ex.getParamsMsg())
                .map(msg -> new ResponseEntity<>(new TraceErrorResponse<>(CommonErrorCode.INVALID_PARAMS, msg, null, traceId), HttpStatus.BAD_REQUEST));
    }
}
