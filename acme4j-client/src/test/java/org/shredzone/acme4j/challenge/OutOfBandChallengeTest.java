/*
 * acme4j - Java ACME client
 *
 * Copyright (C) 2016 Richard "Shred" Körber
 *   http://acme4j.shredzone.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.shredzone.acme4j.challenge;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.util.ClaimBuilder;
import org.shredzone.acme4j.util.TestUtils;

/**
 * Unit tests for {@link OutOfBand01Challenge}.
 *
 * @author Richard "Shred" Körber
 */
public class OutOfBandChallengeTest {

    /**
     * Test that {@link OutOfBand01Challenge} is returning the validation URL.
     */
    @Test
    public void testHttpChallenge() throws IOException {
        OutOfBand01Challenge challenge = new OutOfBand01Challenge();
        challenge.unmarshall(TestUtils.getJsonAsMap("oobChallenge"));

        assertThat(challenge.getType(), is(OutOfBand01Challenge.TYPE));
        assertThat(challenge.getStatus(), is(Status.PENDING));
        assertThat(challenge.getValidationUrl(),
                is(new URL("https://example.com/validate/evaGxfADs6pSRb2LAv9IZ")));

        ClaimBuilder cb = new ClaimBuilder();
        challenge.respond(cb);

        assertThat(cb.toString(), sameJSONAs("{\"type\": \"oob-01\"}"));
    }

}
