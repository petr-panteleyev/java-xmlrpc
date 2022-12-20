/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodStructParametersTest {
    private static List<Arguments> testData() {
        return List.of(
                Arguments.of(Map.ofEntries(entry("1", "param1")),
                        "<param><value><struct><member><name>1</name><value><string>param1</string></value></member>"
                                + "</struct></value></param>"),
                Arguments.of(Map.ofEntries(entry("2", Boolean.TRUE)),
                        "<param><value><struct><member><name>2</name><value><boolean>1</boolean></value></member>"
                                + "</struct></value></param>"),
                Arguments.of(Map.ofEntries(entry("3", List.of(1, 2, 3))),
                        "<param><value><struct><member><name>3</name><value><array><data><value><int>1</int></value>"
                                + "<value><int>2</int></value><value><int>3</int></value></data></array></value>"
                                + "</member></struct></value></param>")
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testStructParameters(Map<?, ?> value, String expected) {
        var p = new XMLRPCParameters(TimeZone.getDefault())
                .appendParameter(value);
        assertEquals(expected, p.getParametersString());
    }
}
