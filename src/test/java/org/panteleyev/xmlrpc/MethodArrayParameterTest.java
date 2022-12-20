/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodArrayParameterTest {

    private static List<Arguments> testData() {
        return List.of(
                Arguments.of(List.of(1, "string"),
                        "<param><value><array><data><value><int>1</int></value><value><string>string</string>" +
                                "</value></data></array></value></param>"),
                Arguments.of(List.of(123.45, "new string"),
                        "<param><value><array><data><value><double>123.45</double></value><value>" +
                                "<string>new string</string></value></data></array></value></param>"),
                Arguments.of(List.of(Boolean.FALSE, Boolean.TRUE),
                        "<param><value><array><data><value><boolean>0</boolean></value><value><boolean>1</boolean>" +
                                "</value></data></array></value></param>")
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testArrayParameters(List<?> value, String expected) {
        var p = new XMLRPCParameters(TimeZone.getDefault())
                .appendParameter(value);
        assertEquals(expected, p.getParametersString());
    }
}
