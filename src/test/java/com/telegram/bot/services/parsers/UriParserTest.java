package com.telegram.bot.services.parsers;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.io.EmptyInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UriParserTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse httpResponse;

    @InjectMocks
    private UriParser uriParser;

    private static final String FAKE_URI = "FAKE_URI";

    @Before
    public void init() throws IOException {
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
    }

    @Test
    public void testGetStreamFromPdfUriShouldReturnEmptyStream() {
        URI fakeUri = URI.create(FAKE_URI);
        assertThat(uriParser.getStreamFromPdfUri(fakeUri, httpClient), is(EmptyInputStream.INSTANCE));
    }

    @Test
    public void testGetStreamFromPdfUriShouldReturnInputStream() {
        URI fakeUri = URI.create(FAKE_URI);
        InputStream expectedStream = new ByteArrayInputStream(new byte[2]);
        HttpEntity inputStreamEntity = new InputStreamEntity(expectedStream);

        when(httpResponse.getEntity()).thenReturn(inputStreamEntity);
        assertThat(uriParser.getStreamFromPdfUri(fakeUri, httpClient), is(expectedStream));
    }
}
