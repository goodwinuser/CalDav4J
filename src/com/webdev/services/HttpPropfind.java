package com.webdev.services;

import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public class HttpPropfind extends HttpRequestBase {

    public final static String METHOD_NAME = "PROPFIND";

    public HttpPropfind() {
        super();
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpPropfind(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public String getName() {
        return "PROPFIND";
    }
}
