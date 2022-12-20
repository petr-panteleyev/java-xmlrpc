/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * This class provides wrapper for result of XMLRPC call.
 */
public class XMLRPCResult {
    private static final String VALUE = "value";
    private static final String NAME = "name";
    private static final String MEMBER = "member";
    private static final String FAULT = "fault";
    private static final String DATA = "data";
    private static final String PARAM = "param";

    private final TimeZone tz;

    private final List<Object> values = new ArrayList<>();

    XMLRPCResult() {
        // for unit testing purposes only
        tz = TimeZone.getTimeZone("GMT");
    }

    XMLRPCResult(TimeZone tz) {
        // for unit testing purposes only
        this.tz = tz;
    }

    XMLRPCResult(InputStream in, TimeZone tz) throws IOException, XMLRPCException {
        this.tz = tz;

        var factory = DocumentBuilderFactory.newInstance();
        try {
            var builder = factory.newDocumentBuilder();
            var doc = builder.parse(in);
            parse(doc);
        } catch (ParserConfigurationException | ParseException | SAXException ex) {
            throw new XMLRPCException("XML parser error", ex);
        }
    }

    final XMLRPCResult parse(Document doc) throws ParseException, XMLRPCException {
        var root = doc.getDocumentElement();

        // Try to get fault information
        NodeList faults = root.getElementsByTagName(FAULT);
        if (faults.getLength() != 0) {
            var values = ((Element) faults.item(0)).getElementsByTagName(VALUE);
            if (values.getLength() != 0) {
                Object value = parseValue(values.item(0));
                if (value instanceof HashMap<?, ?> hashMap) {
                    int faultCode = (Integer) hashMap.get("faultCode");
                    var faultString = (String) hashMap.get("faultString");
                    throw new XMLRPCException(faultCode, faultString);
                } else {
                    throw new XMLRPCException("Undefined fault response");
                }
            }
        } else {
            var params = root.getElementsByTagName(PARAM);
            for (int i = 0; i < params.getLength(); i++) {
                var param = params.item(i);
                var valueNodes = param.getChildNodes();
                for (int j = 0; j < valueNodes.getLength(); j++) {
                    var valueNode = valueNodes.item(j);
                    if (VALUE.equals(valueNode.getNodeName())) {
                        var v = parseValue(valueNode);
                        if (v != null) {
                            values.add(parseValue(valueNode));
                        }
                        break;
                    }
                }
            }
        }
        return this;
    }

    private Object parseValue(Node valueNode) throws ParseException {
        var children = valueNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            var firstChild = children.item(i);
            var childName = firstChild.getNodeName();

            var text = firstChild.getTextContent();

            switch (childName) {
                case "string" -> {
                    return text;
                }
                case "int", "i4" -> {
                    return Integer.parseInt(text);
                }
                case "double" -> {
                    return Double.parseDouble(text);
                }
                case "boolean" -> {
                    return ("1".equals(text)) ? Boolean.TRUE : Boolean.FALSE;
                }
                case "base64" -> {
                    return Base64.getDecoder().decode(text);
                }
                case "struct" -> {
                    return parseStruct(firstChild);
                }
                case "array" -> {
                    return parseArray(firstChild);
                }
                case "dateTime.iso8601" -> {
                    var f = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
                    f.setTimeZone(tz);
                    return f.parse(text);
                }
            }
        }

        return null;
    }

    Map<String, Object> parseStruct(Node valueNode) throws ParseException {
        var res = new HashMap<String, Object>();

        var memberElements = getChildElementsByName(valueNode, MEMBER);
        for (var child : memberElements) {
            var memberChildren = child.getChildNodes();

            String name = null;
            Object value = null;
            for (int j = 0; j < memberChildren.getLength(); j++) {
                var memberChild = memberChildren.item(j);
                if (NAME.equals(memberChild.getNodeName())) {
                    name = memberChild.getTextContent();
                }
                if (VALUE.equals(memberChild.getNodeName())) {
                    value = parseValue(memberChild);
                }
            }
            if (name != null && value != null) {
                res.put(name, value);
            }
        }
        return res;
    }

    List<Object> parseArray(Node valueNode) throws ParseException {
        var res = new ArrayList<>();

        var dataElements = getChildElementsByName(valueNode, DATA);
        if (dataElements.size() == 1) {
            var valueElements = getChildElementsByName(dataElements.get(0), VALUE);
            for (var value : valueElements) {
                res.add(parseValue(value));
            }
        }

        return res;
    }

    /**
     * Returns number of top level result values.
     *
     * @return number of result values
     */
    public int getValueCount() {
        return values.size();
    }

    /**
     * Returns result value as string.
     *
     * @param index index of requested value
     * @return value as string
     * @throws IllegalStateException in case of requested value is not string
     */
    public String getStringValue(int index) {
        var val = values.get(index);
        if (val instanceof String stringValue) {
            return stringValue;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as integer.
     *
     * @param index index of requested value
     * @return value as integer
     * @throws IllegalStateException in case of requested value is not integer
     */
    public Integer getIntegerValue(int index) {
        var val = values.get(index);
        if (val instanceof Integer intValue) {
            return intValue;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as boolean.
     *
     * @param index index of requested value
     * @return value as boolean
     * @throws IllegalStateException in case of requested value is not boolean
     */
    public Boolean getBooleanValue(int index) {
        var val = values.get(index);
        if (val instanceof Boolean intValue) {
            return intValue;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as double.
     *
     * @param index index of requested value
     * @return value as double
     * @throws IllegalStateException in case of requested value is not double
     */
    public Double getDoubleValue(int index) {
        var val = values.get(index);
        if (val instanceof Double intValue) {
            return intValue;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as date.
     *
     * @param index index of requested value
     * @return value as date
     * @throws IllegalStateException in case of requested value is not date
     */
    public Date getDateValue(int index) {
        var val = values.get(index);
        if (val instanceof Date date) {
            return date;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as binary data.
     *
     * @param index index of requested value
     * @return value as binary data
     * @throws IllegalStateException in case of requested value is not binary data
     */
    public byte[] getBinaryValue(int index) {
        var val = values.get(index);
        if (val instanceof byte[] bytes) {
            return bytes;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as array.
     *
     * @param index index of requested value
     * @return value as array
     * @throws IllegalStateException in case of requested value is not array
     */
    public List<?> getArrayValue(int index) {
        var val = values.get(index);
        if (val instanceof List<?> list) {
            return list;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as struct.
     *
     * @param index index of requested value
     * @return value as struct
     * @throws IllegalStateException in case of requested value is not struct
     */
    public Map<String, Object> getStructValue(int index) {
        var val = values.get(index);
        if (val instanceof Map map) {
            return map;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns all result values.
     *
     * @return result values
     */
    public List<?> getValues() {
        return values;
    }

    private static List<Element> getChildElementsByName(Node node, String name) {
        var nodes = node.getChildNodes();
        var result = new ArrayList<Element>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            if (!(nodes.item(i) instanceof Element childElement)) {
                continue;
            }
            if (childElement.getTagName().equals(name)) {
                result.add(childElement);
            }
        }
        return result;
    }
}
