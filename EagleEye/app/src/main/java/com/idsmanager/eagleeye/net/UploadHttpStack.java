
/**
 * Copyright 2013 Mani Selvaraj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy2Clipboard of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.idsmanager.eagleeye.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom implementation of com.android.volley.toolboox.HttpStack
 * Uses apache HttpClient-4.2.5 jar to take care of . You can download it from here
 * http://hc.apache.org/downloads.cgi
 *
 * @author Mani Selvaraj
 */
public class UploadHttpStack implements HttpStack {

    private boolean mIsConnectingToYourServer = false;
    private final static String HEADER_CONTENT_TYPE = "Content-Type";

    public UploadHttpStack(boolean isYourServer) {
        mIsConnectingToYourServer = isYourServer;
    }

    private static void addHeaders(HttpUriRequest httpRequest, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            httpRequest.setHeader(key, headers.get(key));
        }
    }

    @SuppressWarnings("unused")
    private static List<NameValuePair> getPostParameterPairs(Map<String, String> postParams) {
        List<NameValuePair> result = new ArrayList<NameValuePair>(postParams.size());
        for (String key : postParams.keySet()) {
            result.add(new BasicNameValuePair(key, postParams.get(key)));
        }
        return result;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        //create post http request
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(((IDsManagerMultiPartRequest) request).getEntity());

        addHeaders(httpPost, additionalHeaders);
        addHeaders(httpPost, request.getHeaders());
        onPrepareRequest(httpPost);
        HttpParams httpParams = httpPost.getParams();
        int timeoutMs = request.getTimeoutMs();
        HttpConnectionParams.setConnectionTimeout(httpParams, 50000);
        HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);

        /* Register schemes, HTTP and HTTPS */
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory sslSocketFactory;

        if (mIsConnectingToYourServer) {
            sslSocketFactory = MySSLSocketFactory.getFixedSocketFactory();
        } else {
            sslSocketFactory = SSLSocketFactory.getSocketFactory();
        }

        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sslSocketFactory, 443));

        /* Make a thread safe connection manager for the client */
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(httpParams, registry);
        HttpClient httpClient = new DefaultHttpClient(manager, httpParams);

        return httpClient.execute(httpPost);
    }

//    /**
//     * Creates the appropriate subclass of HttpUriRequest for passed in request.
//     */
//    @SuppressWarnings("deprecation")
//    /* protected */ static HttpUriRequest createHttpRequest(Request<?> request, Map<String, String> additionalHeaders) throws AuthFailureError {
//        switch (request.getMethod()) {
//            case Method.DEPRECATED_GET_OR_POST: {
//                // This is the deprecated way that needs to be handled for backwards compatibility.
//                // If the request's post body is null, then the assumption is that the request is
//                // GET.  Otherwise, it is assumed that the request is a POST.
//                byte[] postBody = request.getPostBody();
//                if (postBody != null) {
//                    HttpPost postRequest = new HttpPost(request.getUrl());
//                    postRequest.addHeader(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
//                    HttpEntity entity;
//                    entity = new ByteArrayEntity(postBody);
//                    postRequest.setEntity(entity);
//                    return postRequest;
//                } else {
//                    return new HttpGet(request.getUrl());
//                }
//            }
//            case Method.GET:
//                return new HttpGet(request.getUrl());
//            case Method.DELETE:
//                return new HttpDelete(request.getUrl());
//            case Method.POST: {
//                HttpPost postRequest = new HttpPost(request.getUrl());
////                postRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
////                setMultiPartBody(postRequest, request);
////                setEntityIfNonEmptyBody(postRequest, request);
//
//                return postRequest;
//            }
//            case Method.PUT: {
//                HttpPut putRequest = new HttpPut(request.getUrl());
//                putRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
////                setMultiPartBody(putRequest, request);
//                setEntityIfNonEmptyBody(putRequest, request);
//                return putRequest;
//            }
//            default:
//                throw new IllegalStateException("Unknown request method.");
//        }
//    }

//    private static void setEntityIfNonEmptyBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) throws AuthFailureError {
//        byte[] body = request.getBody();
//        if (body != null) {
//            HttpEntity entity = new ByteArrayEntity(body);
//            httpRequest.setEntity(entity);
//        }
//    }
//
//    /**
//     * If Request is IDsManagerMultiPartRequest type, then set MultipartEntity in the httpRequest object.
//     *
//     * @param httpRequest
//     * @param request
//     * @throws com.android.volley.AuthFailureError
//     */
//    private static void setMultiPartBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) throws AuthFailureError {
//
//        // Return if Request is not IDsManagerMultiPartRequest
//        if (request instanceof IDsManagerMultiPartRequest == false) {
//            return;
//        }
//
//        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//        //Iterate the fileUploads
//        Map<String, File> fileUpload = ((IDsManagerMultiPartRequest) request).getFileUploads();
//        for (Map.Entry<String, File> entry : fileUpload.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            multipartEntity.addPart(((String) entry.getKey()), new FileBody((File) entry.getValue()));
//        }
//
//        //Iterate the stringUploads
//        Map<String, String> stringUpload = ((IDsManagerMultiPartRequest) request).getStringUploads();
//        for (Map.Entry<String, String> entry : stringUpload.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            try {
//                multipartEntity.addPart(((String) entry.getKey()), new StringBody((String) entry.getValue()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        httpRequest.setEntity(multipartEntity);
//    }

    /**
     * Called before the request is executed using the underlying HttpClient.
     * <p/>
     * <p>Overwrite in subclasses to augment the request.</p>
     */
    protected void onPrepareRequest(HttpUriRequest request) throws IOException {
        // Nothing.
    }
}
 


