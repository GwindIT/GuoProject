/**
 * Copyright 2013 Mani Selvaraj
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy2Clipboard of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idsmanager.eagleeye.net;

import com.android.volley.Response;
import com.idsmanager.eagleeye.net.response.BaseResponse;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MultipartRequest - To handle the large file uploads.
 * Extended from JSONRequest. You might want to change to StringRequest based on your response type.
 *
 * @author Mani Selvaraj
 */
public class IDsManagerMultiPartRequest<T extends BaseResponse> extends IDsManagerBaseRequest<T> {
    protected Map<String, String> mParams;
    protected boolean isRepeatable;

    protected ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    protected ConcurrentHashMap<String, FileWrapper> fileParams = new ConcurrentHashMap<>();
    protected String contentEncoding = HTTP.UTF_8;

    public IDsManagerMultiPartRequest(String url, Class cls, Map<String, String> headers, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, cls, headers, listener, errorListener);
        mParams = params;
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, File file) throws FileNotFoundException {
        put(key, file, null, null);
    }

    public void put(String key, File file, String contentType, String customFileName)
            throws FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        if (key != null) {
            fileParams.put(key, new FileWrapper(file, contentType, customFileName));
        }
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data";
    }

    public void setHttpEntityIsRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public HttpEntity getEntity() throws IOException {
        initUrlParams();
        SimpleMultipartEntity entity = new SimpleMultipartEntity();
        entity.setIsRepeatable(isRepeatable);
        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPartWithCharset(entry.getKey(), entry.getValue(), contentEncoding);
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType,
                    fileWrapper.customFileName);
        }

        return entity;
    }

    private void initUrlParams() {
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            urlParams.put(entry.getKey(), entry.getValue());
        }
    }

    public static class FileWrapper {
        public final File file;
        public final String contentType;
        public final String customFileName;

        public FileWrapper(File file, String contentType, String customFileName) {
            this.file = file;
            this.contentType = contentType;
            this.customFileName = customFileName;
        }
    }

}