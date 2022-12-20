/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodScalarParametersTest {

    private static List<Arguments> testData() {
        var c = new GregorianCalendar();
        c.set(1972, Calendar.OCTOBER, 5, 0, 0, 0);
        var d1 = c.getTime();
        c.set(1972, Calendar.OCTOBER, 5, 23, 3, 54);
        var d2 = c.getTime();

        var localDateTime1 = LocalDateTime.of(1972, 10, 5, 0, 0, 0);
        var localDateTime2 = LocalDateTime.of(1972, 10, 5, 23, 3, 54);
        var localDate = LocalDate.of(1972, 10, 5);

        return List.of(
                Arguments.of("Append TRUE", Boolean.TRUE,
                        "<param><value><boolean>1</boolean></value></param>"),
                Arguments.of("Append FALSE", Boolean.FALSE,
                        "<param><value><boolean>0</boolean></value></param>"),
                Arguments.of("Append Integer", 123,
                        "<param><value><int>123</int></value></param>"),
                Arguments.of("Append Integer", -123,
                        "<param><value><int>-123</int></value></param>"),
                Arguments.of("Append String", "Hello World",
                        "<param><value><string>Hello World</string></value></param>"),
                Arguments.of("Append Double", 123.45,
                        "<param><value><double>123.45</double></value></param>"),
                Arguments.of("Append Double", -123.45,
                        "<param><value><double>-123.45</double></value></param>"),
                Arguments.of("Append Data",
                        new byte[]{(byte) 0x14, (byte) 0xfb, (byte) 0x9c, (byte) 0x03, (byte) 0xd9, (byte) 0x7e},
                        "<param><value><base64>FPucA9l+</base64></value></param>"),
                Arguments.of("Append Date", d1,
                        "<param><value><dateTime.iso8601>19721005T00:00:00</dateTime.iso8601></value></param>"),
                Arguments.of("Append Date", d2,
                        "<param><value><dateTime.iso8601>19721005T23:03:54</dateTime.iso8601></value></param>"),
                Arguments.of("Append Date", localDateTime1,
                        "<param><value><dateTime.iso8601>19721005T00:00:00</dateTime.iso8601></value></param>"),
                Arguments.of("Append Date", localDateTime2,
                        "<param><value><dateTime.iso8601>19721005T23:03:54</dateTime.iso8601></value></param>"),
                Arguments.of("Append Date", localDate,
                        "<param><value><dateTime.iso8601>19721005T00:00:00</dateTime.iso8601></value></param>")
        );
    }


    @ParameterizedTest
    @MethodSource("testData")
    public void appendParameter(String message, Object value, String expected) {
        var p = new XMLRPCParameters(TimeZone.getDefault())
                .appendParameter(value);
        assertEquals(expected, p.getParametersString(), message);
    }
}
