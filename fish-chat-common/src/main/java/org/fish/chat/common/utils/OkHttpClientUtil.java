package org.fish.chat.common.utils;

import okhttp3.*;

import java.io.IOException;

/**
 * @author adre
 * @version 创建时间：2018/2/25 下午8:23
 */
public class OkHttpClientUtil {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
