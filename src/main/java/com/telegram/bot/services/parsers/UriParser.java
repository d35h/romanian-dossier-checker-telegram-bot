package com.telegram.bot.services.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.io.EmptyInputStream;
import org.springframework.stereotype.Service;

@Service
public class UriParser {

    public InputStream getStreamFromPdfUrl(URI urlToFetch)  {
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpResponse response = httpclient.execute(new HttpGet(urlToFetch));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return entity.getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return EmptyInputStream.INSTANCE;
    }
}
