package com.ezbuy.framework.utils;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author: hoangtien2k3qx1@gmail.com
 * @since: 20/07/2024 11:16
 * description: LogUtils
 * @update:
 */
public class LogUtils {

    public static final List<MediaType> legalLogMediaTypes = Arrays.asList(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.MULTIPART_FORM_DATA
    );

    @SuppressWarnings("unchecked")
    public static <T extends DataBuffer> T loggingRequest(org.apache.logging.log4j.Logger log, T buffer) {
        return logging(log, "request: ", buffer);
    }

    public static <T extends DataBuffer> T loggingResponse(org.apache.logging.log4j.Logger log, T buffer) {
        return logging(log, "response: ", buffer);
    }

    private static <T extends DataBuffer> T logging(Logger log, String inOrOut, T buffer) {
        InputStream dataBuffer = buffer.asInputStream();
        byte[] bytes = toByteArray(dataBuffer);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        log.info("{}: {}", inOrOut, new String(bytes));
        DataBufferUtils.release(buffer);
        return (T) nettyDataBufferFactory.wrap(bytes);
    }

    private static byte[] toByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        byte[] in_b = new byte[]{};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in_b;
    }
}
