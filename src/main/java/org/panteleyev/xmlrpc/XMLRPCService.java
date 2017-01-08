/*
 * Copyright (c) 2012, 2017, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
 * @author Petr Panteleyev
 */
public class XMLRPCService {
    private static final String GMT = "GMT";

    private final String url;
    private final TimeZone tzIn;
    private final TimeZone tzOut;

    /**
     * Creates XMLRPCService object with default time zone settings.
     * @param url   an absolute URL of the target service
     */
    public XMLRPCService(String url) {
        this(url, TimeZone.getTimeZone(GMT), TimeZone.getTimeZone(GMT));
    }

    /**
     * Creates XMLRPCService object.
     * @param url   an absolute URL of the target parsed
     * @param tzIn  time zone from which Date result values must be represented
     */
    public XMLRPCService(String url, TimeZone tzIn) {
        this(url, tzIn, TimeZone.getTimeZone(GMT));
    }

    /**
     * Creates XMLRPCService object.
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
     * @param method        method name
     * @param parameters    method parameters
     * @return  result of the call
     * @throws IOException      in case of network issue
     * @throws XMLRPCException  in case of XMLRPC related error
     */
    public XMLRPCResult call(String method, Object... parameters) throws IOException, XMLRPCException {
        HttpURLConnection conn = null;
        XMLRPCMethod m = new XMLRPCMethod(method);
        final URL server = new URL(url);

        try {
            conn = (HttpURLConnection)server.openConnection();
            conn.setRequestMethod("POST");

            XMLRPCParameters params = new XMLRPCParameters(tzOut);
            for (Object p : parameters) {
                params.appendParameter(p);
            }

            final String cmd = m.getMethodString(params);
            byte[] bytes = cmd.getBytes(StandardCharsets.UTF_8);

            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));
            conn.setRequestProperty("Content-Type", "text/xml");

            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
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
