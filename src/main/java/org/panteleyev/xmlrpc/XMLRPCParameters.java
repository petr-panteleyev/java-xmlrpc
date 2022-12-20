/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * This class defines XML RPC call parameters.
 */
public final class XMLRPCParameters {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'00:00:00");
    private final StringBuilder paramString = new StringBuilder();
    private final TimeZone tz;


    XMLRPCParameters(TimeZone tz) {
        this.tz = tz;
    }

    String getParametersString() {
        return paramString.toString();
    }

    XMLRPCParameters appendParameter(Object param) {
        paramString.append("<param><value>").append(appendValue(param)).append("</value></param>");
        return this;
    }

    private String appendValue(Object value) {
        // TODO: replace with switch pattern matching
        if (value instanceof String stringValue) {
            return appendStringValue(stringValue);
        } else if (value instanceof Map<?, ?> map) {
            return appendStructValue(map);
        } else if (value instanceof List<?> list) {
            return appendArrayValue(list);
        } else if (value instanceof Boolean booleanValue) {
            return appendBooleanValue(booleanValue);
        } else if (value instanceof Integer intValue) {
            return appendIntegerValue(intValue);
        } else if (value instanceof Double doubleValue) {
            return appendDoubleValue(doubleValue);
        } else if (value instanceof Date date) {
            return appendDateValue(date);
        } else if (value instanceof LocalDateTime localDateTime) {
            return appendDateValue(localDateTime);
        } else if (value instanceof LocalDate localDate) {
            return appendDateValue(localDate);
        } else if (value instanceof byte[] bytes) {
            return appendDataValue(bytes);
        } else {
            throw new RuntimeException("Unsupported parameter type");
        }
    }

    private String appendStringValue(String value) {
        return "<string>"
                + value.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                + "</string>";
    }

    private String appendBooleanValue(Boolean value) {
        return "<boolean>" + ((value) ? "1" : "0") + "</boolean>";
    }

    private String appendIntegerValue(Integer value) {
        return "<int>" + value.toString() + "</int>";
    }

    private String appendDoubleValue(Double value) {
        return "<double>" + value.toString() + "</double>";
    }

    private String appendStructValue(Map<?, ?> map) {
        return "<struct>" +
                map.entrySet().stream()
                        .filter(entry -> entry.getKey() instanceof String)
                        .map(entry -> "<member><name>" + entry.getKey() + "</name><value>"
                                + appendValue(entry.getValue())
                                + "</value></member>").collect(Collectors.joining(""))
                + "</struct>";
    }

    private String appendArrayValue(List<?> array) {
        return "<array><data>" +
                array.stream()
                        .map(x -> "<value>" + appendValue(x) + "</value>")
                        .collect(Collectors.joining(""))
                + "</data></array>";
    }

    private String appendDateValue(Date date) {
        var f = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        f.setTimeZone(tz);
        return "<dateTime.iso8601>" + f.format(date) + "</dateTime.iso8601>";
    }

    private String appendDateValue(LocalDateTime date) {
        return "<dateTime.iso8601>" + DATE_TIME_FORMATTER.format(date) + "</dateTime.iso8601>";
    }

    private String appendDateValue(LocalDate date) {
        return "<dateTime.iso8601>" + DATE_FORMATTER.format(date) + "</dateTime.iso8601>";
    }

    private String appendDataValue(byte[] data) {
        return "<base64>" + Base64.getEncoder().encodeToString(data) + "</base64>";
    }
}
