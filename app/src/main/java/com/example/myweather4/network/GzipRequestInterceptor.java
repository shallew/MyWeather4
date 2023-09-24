package com.example.myweather4.network;

import okhttp3.*;
import okio.GzipSource;
import okio.Okio;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipRequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        Headers headers = originalResponse.headers();
        // 判断响应头中是否包含 Gzip 压缩标识
        if (headers != null && headers.get("Content-Encoding") != null
                && headers.get("Content-Encoding").equalsIgnoreCase("gzip")) {
            // 获取原始的响应体
            ResponseBody originalResponseBody = originalResponse.body();
            if (originalResponseBody != null) {
                // 使用 GzipInputStream 进行解压缩
                GZIPInputStream gzipInputStream = new GZIPInputStream(originalResponseBody.byteStream());
                // 将解压缩后的数据包装成新的响应体
                ResponseBody decompressedResponseBody = ResponseBody.create(originalResponseBody.contentType(), -1, Okio.buffer(Okio.source(gzipInputStream)));
                // 构建新的响应
                return originalResponse.newBuilder()
                        .header("Content-Encoding", "identity") // 移除 Gzip 压缩标识
                        .removeHeader("Content-Length") // 移除 Content-Length 标识，避免解压缩后的数据长度与原始数据不一致导致异常
                        .body(decompressedResponseBody)
                        .build();
            }
        }
        return originalResponse;
    }
}