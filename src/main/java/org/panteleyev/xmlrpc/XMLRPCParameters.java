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

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

final class XMLRPCParameters {
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
        if (value instanceof String) {
            return appendStringValue((String) value);
        } else {
            if (value instanceof Map) {
                return appendStructValue((Map) value);
            } else {
                if (value instanceof List) {
                    return appendArrayValue((List) value);
                } else {
                    if (value instanceof Boolean) {
                        return appendBooleanValue((Boolean) value);
                    } else {
                        if (value instanceof Integer) {
                            return appendIntegerValue((Integer) value);
                        } else {
                            if (value instanceof Double) {
                                return appendDoubleValue((Double) value);
                            } else {
                                if (value instanceof Date) {
                                    return appendDateValue((Date) value);
                                } else {
                                    if (value instanceof byte[]) {
                                        return appendDataValue((byte[]) value);
                                    } else {
                                        throw new RuntimeException("Unsupported parameter type");
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

    private String appendStructValue(Map<Object, Object> map) {
        return "<struct>" +
                map.entrySet().stream()
                        .filter(entry -> entry.getKey() instanceof String)
                        .map(entry -> "<member><name>" + entry.getKey() + "</name><value>"
                                + appendValue(entry.getValue())
                                + "</value></member>").collect(Collectors.joining(""))
                + "</struct>";
    }

    private String appendArrayValue(List<Object> array) {
        return "<array><data>" +
                array.stream()
                        .map(x -> "<value>" + appendValue(x) + "</value>")
                        .collect(Collectors.joining(""))
                + "</data></array>";
    }

    private String appendDateValue(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        f.setTimeZone(tz);
        return "<dateTime.iso8601>" + f.format(date) + "</dateTime.iso8601>";
    }

    private String appendDataValue(byte[] data) {
        return "<base64>" + Base64.getEncoder().encodeToString(data) + "</base64>";
    }
}
