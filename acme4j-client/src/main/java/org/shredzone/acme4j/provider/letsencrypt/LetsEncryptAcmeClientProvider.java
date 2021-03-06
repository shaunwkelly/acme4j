/*
 * acme4j - Java ACME client
 *
 * Copyright (C) 2015 Richard "Shred" Körber
 *   http://acme4j.shredzone.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.shredzone.acme4j.provider.letsencrypt;

import java.net.URI;
import java.net.URISyntaxException;

import org.shredzone.acme4j.connector.HttpConnector;
import org.shredzone.acme4j.exception.AcmeProtocolException;
import org.shredzone.acme4j.provider.AbstractAcmeClientProvider;
import org.shredzone.acme4j.provider.AcmeClientProvider;

/**
 * An {@link AcmeClientProvider} for <em>Let's Encrypt</em>.
 * <p>
 * The {@code serverUri} is {@code "acme://letsencrypt.org"} for the production server, and
 * {@code "acme://letsencrypt.org/staging"} for a testing server.
 * <p>
 * If you want to use <em>Let's Encrypt</em>, always prefer to use this provider, as it
 * takes care for the correct connection and SSL certificates.
 *
 * @author Richard "Shred" Körber
 * @see <a href="https://letsencrypt.org/">Let's Encrypt</a>
 */
public class LetsEncryptAcmeClientProvider extends AbstractAcmeClientProvider {

    private static final String V01_DIRECTORY_URI = "https://acme-v01.api.letsencrypt.org/directory";
    private static final String STAGING_DIRECTORY_URI = "https://acme-staging.api.letsencrypt.org/directory";

    @Override
    public boolean accepts(URI serverUri) {
        return "acme".equals(serverUri.getScheme())
                && "letsencrypt.org".equals(serverUri.getHost());
    }

    @Override
    protected URI resolve(URI serverUri) {
        String path = serverUri.getPath();
        String directoryUri;
        if (path == null || "".equals(path) || "/".equals(path) || "/v01".equals(path)) {
            directoryUri = V01_DIRECTORY_URI;
        } else if ("/staging".equals(path)) {
            directoryUri = STAGING_DIRECTORY_URI;
        } else {
            throw new IllegalArgumentException("Unknown URI " + serverUri);
        }

        try {
            return new URI(directoryUri);
        } catch (URISyntaxException ex) {
            throw new AcmeProtocolException(directoryUri, ex);
        }
    }

    @Override
    protected HttpConnector createHttpConnector() {
        return new LetsEncryptHttpConnector();
    }

}
