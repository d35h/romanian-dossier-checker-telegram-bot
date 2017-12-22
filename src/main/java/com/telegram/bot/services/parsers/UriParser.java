package com.telegram.bot.services.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.io.EmptyInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UriParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(UriParser.class);

    public InputStream getStreamFromPdfUri(URI urlToFetch, HttpClient httpclient)  {
        try {
            LOGGER.info("Trying to get stream from the specified URI: {}", urlToFetch);
            HttpResponse response = httpclient.execute(new HttpGet(urlToFetch));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return entity.getContent();
            }
        } catch (IOException e) {
            LOGGER.debug("Failed when getting stream from the specified URI {}: {}", urlToFetch, e);
        }

        return EmptyInputStream.INSTANCE;
    }
}
