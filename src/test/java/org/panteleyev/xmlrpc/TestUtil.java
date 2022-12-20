/*
 Copyright © 2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.TimeZone;

final class TestUtil {
    private static final DocumentBuilder BUILDER;

    private TestUtil() {
    }

    static {
        try {
            BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    static XMLRPCResult parseResult(String str) throws Exception {
        var inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(str));
        var doc = BUILDER.parse(inputSource);
        return new XMLRPCResult().parse(doc);
    }

    static XMLRPCResult parseResult(String str, TimeZone tz) throws Exception {
        var inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(str));
        var doc = BUILDER.parse(inputSource);
        return new XMLRPCResult(tz).parse(doc);
    }
}
