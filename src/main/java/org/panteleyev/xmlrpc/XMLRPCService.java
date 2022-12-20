/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

/**
 * This class provide an entry point for XMLRPC service calls.
 *
 * @author Petr Panteleyev
 */
public class XMLRPCService {
    private static final String GMT = "GMT";

    private final String url;
    private final TimeZone tzIn;
    private final TimeZone tzOut;

    /**
     * Creates XMLRPCService object with default time zone settings.
     *
     * @param url an absolute URL of the target service
     */
    public XMLRPCService(String url) {
        this(url, TimeZone.getTimeZone(GMT), TimeZone.getTimeZone(GMT));
    }

    /**
     * Creates XMLRPCService object.
     *
     * @param url  an absolute URL of the target parsed
     * @param tzIn time zone from which Date result values must be represented
     */
    public XMLRPCService(String url, TimeZone tzIn) {
        this(url, tzIn, TimeZone.getTimeZone(GMT));
    }

    /**
     * Creates XMLRPCService object.
     *
     * @param url   an absolute URL of the target service
     * @param tzIn  time zone from which Date result values must be parsed
     * @param tzOut time zone to which Date parameter values must be represented
     */
    public XMLRPCService(String url, TimeZone tzIn, TimeZone tzOut) {
        this.url = url;
        this.tzIn = tzIn;
        this.tzOut = tzOut;
    }

    /**
     * Calls XMLRPC method with specified parameters.
     * This is a synchronous call.
     *
     * @param method     method name
     * @param parameters method parameters
     * @return result of the call
     * @throws IOException     in case of network issue
     * @throws XMLRPCException in case of XMLRPC related error
     */
    public XMLRPCResult call(String method, Object... parameters) throws IOException, XMLRPCException {
        HttpURLConnection conn = null;
        var m = new XMLRPCMethod(method);
        var server = new URL(url);

        try {
            conn = (HttpURLConnection) server.openConnection();
            conn.setRequestMethod("POST");

            var params = new XMLRPCParameters(tzOut);
            for (var p : parameters) {
                params.appendParameter(p);
            }

            var cmd = m.getMethodString(params);
            var bytes = cmd.getBytes(StandardCharsets.UTF_8);

            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));
            conn.setRequestProperty("Content-Type", "text/xml");

            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            try (var out = new DataOutputStream(conn.getOutputStream())) {
                out.write(bytes, 0, bytes.length);
                out.flush();
            }

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new IOException("HTTP server returned error code - " + Integer.toString(code));
            }

            return new XMLRPCResult(conn.getInputStream(), tzIn);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
