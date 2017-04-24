package com.preyearegmi.httpclient;

import com.preyearegmi.httpclient.abs.FileDownloadCallback;
import com.preyearegmi.httpclient.abs.RequestCompleteCallback;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by preyea on 2/16/17.
 */
public final class HTTPClient {

//    private static Handler handler = null;
//
//    static Handler getMainThreadHandler() {
//        if (handler == null)
//            handler = new Handler(Looper.getMainLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                }
//            };
//        return handler;
//    }

    public static synchronized Runnable request(String url, METHODTYPE methodType,Map<String, String> header,
                                                String body, RequestCompleteCallback listener) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Url: " + url);
        }

        switch (methodType) {
            case POST:
                return new PostTask(urlObj, header, body, listener);
            case GET:
                return new GetTask(urlObj, header, listener);
            default:
                throw new IllegalArgumentException("Undefined http method");
        }
    }

    public static synchronized Runnable getFile(String url, Map<String, String> header, FileDownloadCallback callback) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Url: " + url);
        }

        return new GetFileTask(urlObj, header, callback);

    }

    public static synchronized Runnable formUpload(String url, Map<String, String> header, Map<String, String> body, String[] files, RequestCompleteCallback callback) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Url: " + url);
        }
        return new FormUploadTask(urlObj, header, body, files, callback);
    }

    public static void enqueue(Runnable command) {
        if (command instanceof GetTask || command instanceof PostTask || command instanceof GetFileTask || command instanceof FormUploadTask)
            TaskExecutor.getTaskExecutor().execute(command);
        else
            throw new IllegalArgumentException("External task is forbidden, please use task provided by HTTPClient.request method");
    }
}
