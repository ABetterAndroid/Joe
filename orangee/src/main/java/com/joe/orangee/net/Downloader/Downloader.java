package com.joe.orangee.net.Downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.androidplus.io.IOUtils;
import com.androidplus.net.HttpConnection;
import com.androidplus.net.HttpRequest;
import com.androidplus.util.LogUtils;
import com.androidplus.util.StringUtil;

public class Downloader {
    private static final String TAG = "Downloader";
    private Context mContext;
    private HttpConnection mHttpConnection;

    public Downloader(Context context) {
        mContext = context.getApplicationContext();
//        mUser = RunTimeAccount.getInstance(mContext);
        mHttpConnection = new HttpConnection();
    }

    /**
     * 获取JSON数据，返回结果用gzip解压
     *
     * @param url
     * @param formParams
     * @return
     */
    public String getJsonContentGZip(String url, Map<String, String> formParams, short method) {
        return getJsonContentGZip(url, formParams, null, method);
    }

    public String getJsonContentGZip(String url, Map<String, String> formParams, HashMap<String, String> headerParams, short method) {
        HttpRequest request = new HttpRequest(mContext, url, method, formParams);
        if (headerParams != null) {
            request.setHeaderParams(headerParams);
        }
        return getStringFromGZip(request);
    }

    public String getJsonContent(String url, Map<String, String> formParams, short method) {
//        formParams.put("uuid", Constants.getUuid());
        HttpRequest request = new HttpRequest(mContext, url, method, formParams);
//        request.setApiSecret(API_SECRET);
        String jsonStr = mHttpConnection.getContent(request);
        return jsonStr;
    }

    public String getStringFromGZip(HttpRequest request) {
        InputStream inputStream = null;
        HttpURLConnection conn;

        try {
            conn = mHttpConnection.prepareConnection(request);
            final String contentType = conn.getContentType();
            inputStream = new InflaterInputStream(conn.getInputStream());

            // 1.check header
            // 2.check in file
            final String PATTERN_CHARSET = "(?<=charset=)[\\w-]+";
            String charset = StringUtil.findString(PATTERN_CHARSET, contentType);
            if (!StringUtil.isNullOrEmpty(charset)) {
                return IOUtils.toString(inputStream, charset);
                // find charset in file
            } else {
                String content = IOUtils.toString(inputStream, HttpRequest.DEFAULT_ENCODE);

                if (contentType.contains("html")) {
                    final String PATTERN_META = "<meta[^>]*?charset=[a-zA-Z0-9-]+";
                    charset = StringUtil.findString(PATTERN_META, content);
                    if (!StringUtil.isNullOrEmpty(charset)) {
                        charset = StringUtil.findString(PATTERN_CHARSET, charset);
                        if (!StringUtil.isNullOrEmpty(charset)) {
                            return new String(content.getBytes(HttpRequest.DEFAULT_ENCODE), charset);
                        }
                    }
                } else if (contentType.contains("xml")) {
                    final String PATTERN_ENCODING = "(?<=encoding=\")[\\w-]+";
                    charset = StringUtil.findString(PATTERN_ENCODING, content);
                    if (!StringUtil.isNullOrEmpty(charset))
                        return new String(content.getBytes(HttpRequest.DEFAULT_ENCODE), charset);
                }

                return content;
            }
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(TAG, "UnsupportedEncodingException in getStringFromGZip: " + e.getMessage());
        } catch (IOException e) {
            LogUtils.e(TAG, "IOException in getStringFromGZip: " + e.getMessage());
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
        }
        return "";
    }

    /**
     * 获取JSON数据, 允许自定义超时时�?
     *
     * @param url
     * @param formParams
     * @return
     */
    public String getJsonContent(String url, HashMap<String, String> formParams, boolean needHeader, int connectTimeOut, int readTimeOut, short method) {
        HttpRequest request = new HttpRequest(mContext, url, method, formParams);
        if (needHeader) {
            HashMap<String, String> headers = new HashMap<String, String>();
//            headers.put("Authorization", "Token token=\"" + mUser.getAppUser().getToken() + "\"");
            request.setHeaderParams(headers);
        }
        request.setConnectTimeOut(connectTimeOut);
        request.setReadTimeOut(readTimeOut);
        return getStringFromGZip(request);
    }

    public Bitmap downloadBitmap(String url) {
        byte[] byteArray = mHttpConnection.getByteArray(new HttpRequest(mContext, url, HttpRequest.METHOD_GET, null));
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public byte[] downloadBytes(String url) {
        return mHttpConnection.getByteArray(new HttpRequest(mContext, url, HttpRequest.METHOD_GET, null));
    }

    public byte[] downloadBytes(String url, String referer) {
        HttpRequest request = new HttpRequest(mContext, url, HttpRequest.METHOD_GET, null);
        request.setReferer(referer);
        return mHttpConnection.getByteArray(request);
    }

}
