/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sunwayworld.common.utils.retrofit2.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * @author yanghan ${2016/5/13}
 * @version V1.0
 * @Description 网络数据下载进度条
 */
public class Progress {
    private static ProgressListener listener;

    public Progress(ProgressListener listener) {
        this.listener = listener;
    }

    public static class ProgressInterceptor implements Interceptor {

//        public ProgressInterceptor(ProgressListener progressListener) {
//            listener = progressListener;
//        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body()))
                    .build();
        }
    }

    /***
     * 可以显示进度条的下载请求
     ***/
    protected static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        //        private ProgressListener listener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                private long bytesRead = 0;   //当前写入字节数
                private long contentLength = 0;  //总字节长度，避免多次调用contentLength()方法
                private long lastRefreshUiTime;  //最后一次刷新的时间
                private long lastReadBytes;     //最后一次写入字节数据

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytes = super.read(sink, byteCount);
                    if (contentLength <= 0)
                        contentLength = contentLength(); //获得contentLength的值，后续不再调用
                    bytesRead += bytes;
                    long curTime = System.currentTimeMillis();
                    //每10毫秒刷新一次数据
                    if (curTime - lastRefreshUiTime >= 10) {
                        //计算下载速度
                        long diffTime = (curTime - lastRefreshUiTime) / 1000;
                        if (diffTime == 0) diffTime += 1;
                        long diffBytes = bytesRead - lastReadBytes;
                        long networkSpeed = diffBytes / diffTime;
                        if (listener != null) {
                            listener.downloadProgress(bytesRead, contentLength, bytesRead == -1, networkSpeed);
                        }
                        lastRefreshUiTime = System.currentTimeMillis();
                        lastReadBytes = bytesRead;
                    }
                    return bytes;
                }
            };
        }
    }

    interface ProgressListener {
        void uploadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed);

        void downloadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed);
    }

    public RequestBody wrapRequestBody(RequestBody requestBody) {
        return new ProgressRequestBody(requestBody, listener);
    }

    /**
     * 上传显示经度条的下载请求（适用于文件等上传）
     */
    private class ProgressRequestBody extends RequestBody {
        protected RequestBody delegate;  //实际的待包装请求体
        protected ProgressListener listener;     //进度回调接口
        protected CountingSink countingSink; //包装完成的BufferedSink

        public ProgressRequestBody(RequestBody delegate, ProgressListener listener) {
            this.delegate = delegate;
            this.listener = listener;
        }

        /**
         * 重写调用实际的响应体的contentType
         */
        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        /**
         * 重写调用实际的响应体的contentLength
         */
        @Override
        public long contentLength() {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                return -1;
            }
        }

        /**
         * 重写进行写入
         */
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);
            delegate.writeTo(bufferedSink);
            bufferedSink.flush();  //必须调用flush，否则最后一部分数据可能不会被写入
        }

        /**
         * 包装
         */
        protected final class CountingSink extends ForwardingSink {
            private long bytesWritten = 0;   //当前写入字节数
            private long contentLength = 0;  //总字节长度，避免多次调用contentLength()方法
            private long lastRefreshUiTime;  //最后一次刷新的时间
            private long lastWriteBytes;     //最后一次写入字节数据

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength <= 0) contentLength = contentLength(); //获得contentLength的值，后续不再调用
                bytesWritten += byteCount;
                long curTime = System.currentTimeMillis();
                //每100毫秒刷新一次数据
                if (curTime - lastRefreshUiTime >= 10 || bytesWritten == contentLength) {
                    //计算下载速度
                    long diffTime = (curTime - lastRefreshUiTime) / 1000;
                    if (diffTime == 0) diffTime += 1;
                    long diffBytes = bytesWritten - lastWriteBytes;
                    long networkSpeed = diffBytes / diffTime;
                    if (listener != null)
                        listener.uploadProgress(bytesWritten, contentLength, byteCount == -1, networkSpeed);

                    lastRefreshUiTime = System.currentTimeMillis();
                    lastWriteBytes = bytesWritten;
                }
            }
        }
    }
}
