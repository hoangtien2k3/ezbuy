//package com.ezbuy.framework.utils;
//
//import io.netty.buffer.UnpooledByteBufAllocator;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.core.io.buffer.NettyDataBufferFactory;
//
//import java.io.InputStream;
//
//public class LogUtils {
//
//    @SuppressWarnings("unchecked")
//    public static <T extends DataBuffer> T loggingRequest(Logger log, T buffer) {
//        return logging(log, "request: ", buffer);
//    }
//
//    public static <T extends DataBuffer> T loggingResponse(Logger log, T buffer) {
//        return logging(log, "response: ", buffer);
//    }
//
//    private static <T extends DataBuffer> T logging(Logger log, String inOrOut, T buffer) {
//        InputStream dataBuffer = buffer.asInputStream();
//        byte[] bytes = StreamUtil.streamToByteArray(dataBuffer);
//        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
//        log.info("{}: {}", inOrOut, new String(bytes));
//        DataBufferUtils.release(buffer);
//        return (T) nettyDataBufferFactory.wrap(bytes);
//    }
//}
