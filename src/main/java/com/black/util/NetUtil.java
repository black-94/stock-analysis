package com.black.util;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.function.Function;

public class NetUtil {
    static {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1).setConnectTimeout(1).setSocketTimeout(1).build();
        httpClient = HttpClientBuilder.create().setMaxConnPerRoute(10).setDefaultRequestConfig(requestConfig).build();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static CloseableHttpClient httpClient;

    public static Logger root = LoggerFactory.getLogger(NetUtil.class);

    public static RateLimiter rateLimiter = RateLimiter.create(100);

    public static String get(String link, Object... params) {
        return get(e -> e, link, params);
    }

    public static String get(Function<String, String> fun, String link, Object... params) {
        String u = String.format(link, params);
        rateLimiter.acquire();
        for (int i = 0; i < 3; i++) {
            try {
                HttpGet httpGet = new HttpGet(u);
                httpGet.addHeader("Content-Type", "charset=UTF-8");
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    StringWriter writer = new StringWriter();
                    try (InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8")) {
                        reader.transferTo(writer);
                    }
                    EntityUtils.consume(response.getEntity());
                    String res = writer.toString();
                    if (response.getStatusLine().getStatusCode() == 200) {
                        return fun.apply(res);
                    } else {
                        root.info("code:" + response.getStatusLine().getStatusCode() + ",res:" + res);
                    }
                }
            } catch (Exception e) {
                root.error("request fail,url:" + u, e);
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException interruptedException) {
                }
            }
        }
        return null;
    }

}
