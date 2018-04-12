package com.bjypt.vipcard.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by admin on 2017/6/28.
 */

public class CustomHurlStack implements HttpStack {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private final HurlStack.UrlRewriter mUrlRewriter;
    private final SSLSocketFactory mSslSocketFactory;

    public CustomHurlStack() {
        this((HurlStack.UrlRewriter)null);
    }

    public CustomHurlStack(HurlStack.UrlRewriter urlRewriter) {
        this(urlRewriter, (SSLSocketFactory)null);
    }

    public CustomHurlStack(HurlStack.UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sslSocketFactory;
    }

    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap map = new HashMap();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if(this.mUrlRewriter != null) {
            String parsedUrl = this.mUrlRewriter.rewriteUrl(url);
            if(parsedUrl == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }

            url = parsedUrl;
        }

        URL parsedUrl1 = new URL(url);
        HttpURLConnection connection = this.openConnection(parsedUrl1, request);
        Iterator responseCode = map.keySet().iterator();

        while(responseCode.hasNext()) {
            String protocolVersion = (String)responseCode.next();
            connection.addRequestProperty(protocolVersion, (String)map.get(protocolVersion));
        }

        setConnectionParametersForRequest(connection, request);
        ProtocolVersion protocolVersion1 = new ProtocolVersion("HTTP", 1, 1);
        int responseCode1 = connection.getResponseCode();
        if(responseCode1 == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        } else {
            BasicStatusLine responseStatus = new BasicStatusLine(protocolVersion1, connection.getResponseCode(), connection.getResponseMessage());
            BasicHttpResponse response = new BasicHttpResponse(responseStatus);
            response.setEntity(entityFromConnection(connection));
            Iterator var12 = connection.getHeaderFields().entrySet().iterator();

            while(var12.hasNext()) {
                Map.Entry header = (Map.Entry)var12.next();
                if(header.getKey() != null) {
                    BasicHeader h = new BasicHeader((String)header.getKey(), (String)((List)header.getValue()).get(0));
                    response.addHeader(h);
                }
            }

            return response;
        }
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();

        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException var4) {
            inputStream = connection.getErrorStream();
        }

        entity.setContent(inputStream);
        entity.setContentLength((long)connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection)url.openConnection();
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = this.createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        if("https".equals(url.getProtocol()) && this.mSslSocketFactory != null) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(this.mSslSocketFactory);
            ((HttpsURLConnection)connection).setHostnameVerifier(new CustomHostnameVerifier());
        }

        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch(request.getMethod()) {
            case -1:
                byte[] postBody = request.getPostBody();
                if(postBody != null) {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty("Content-Type", request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                }
                break;
            case 0:
                connection.setRequestMethod("GET");
                break;
            case 1:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;
            case 2:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;
            case 3:
                connection.setRequestMethod("DELETE");
                break;
            case 4:
                connection.setRequestMethod("HEAD");
                break;
            case 5:
                connection.setRequestMethod("OPTIONS");
                break;
            case 6:
                connection.setRequestMethod("TRACE");
                break;
            case 7:
                addBodyIfExists(connection, request);
                connection.setRequestMethod("PATCH");
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }

    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        byte[] body = request.getBody();
        if(body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }

    }

    public interface UrlRewriter {
        String rewriteUrl(String var1);
    }
}
