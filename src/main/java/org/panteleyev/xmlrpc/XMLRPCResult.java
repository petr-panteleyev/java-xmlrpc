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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class provides wrapper for result of XMLRPC call.
 */
public class XMLRPCResult {
    private static final String VALUE   = "value";
    private static final String NAME    = "name";
    private static final String MEMBER  = "member";
    private static final String FAULT   = "fault";

    private TimeZone    tz;

    private ArrayList<Object> values = new ArrayList<>();

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

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

          //  BufferedReader d = new BufferedReader(new InputStreamReader(in));
          //  String s;
          //  do {
          //      s = d.readLine();
          //      System.out.println(s);
          //  } while (s != null);


            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);
            parse(doc);
        } catch (ParserConfigurationException | ParseException | SAXException ex) {
            throw new XMLRPCException("XML parser error", ex);
        }
    }

    final void parse(Document doc) throws ParseException, XMLRPCException {
        Element root = doc.getDocumentElement();

        // Try to get fault information
        NodeList faults = root.getElementsByTagName(FAULT);
        if (faults.getLength() != 0) {
            NodeList vals = ((Element)faults.item(0)).getElementsByTagName(VALUE);
            if (vals.getLength() != 0) {
                Object value = parseValue((Element)vals.item(0));
                if (value instanceof HashMap) {
                    int faultCode = (Integer)((HashMap)value).get("faultCode");
                    String faultString = (String)((HashMap)value).get("faultString");
                    throw new XMLRPCException(faultCode, faultString);
                } else {
                    throw new XMLRPCException("Undefined fault response");
                }
            }
        } else {
            NodeList params = root.getElementsByTagName("param");
            for (int i = 0; i < params.getLength(); i++) {
                Node param = params.item(i);
                NodeList valueNodes = param.getChildNodes();
                for (int j = 0; j < valueNodes.getLength(); j++) {
                    Node valueNode = valueNodes.item(j);
                    if (VALUE.equals(valueNode.getNodeName())) {
                        Object v = parseValue(valueNode);
                        if (v != null) {
                            values.add(parseValue(valueNode));
                        }
                        break;
                    }
                }
            }
        }
    }

    private Object parseValue(Node valueNode) throws ParseException {
        NodeList children = valueNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node firstChild = children.item(i);
            String childName = firstChild.getNodeName();

            String text = firstChild.getTextContent();

            if (childName != null) {
                switch (childName) {
                    case "string" :
                        return text;

                    case "int" :
                    case "i4" :
                        return Integer.parseInt(text);

                    case "double" :
                        return Double.parseDouble(text);

                    case "boolean" :
                        return ("1".equals(text))? Boolean.TRUE : Boolean.FALSE;

                    case "base64" :
                        return Base64.getDecoder().decode(text);

                    case "struct" :
                        return parseStruct(firstChild);

                    case "array" :
                        return parseArray(firstChild);

                    case "dateTime.iso8601" :
                        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
                        f.setTimeZone(tz);
                        return f.parse(text);
                }
            }
        }

        return null;
    }

    HashMap<String, Object> parseStruct(Node valueNode) throws ParseException {
        HashMap<String, Object> res = new HashMap<>();

        NodeList childNodes = valueNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            String childName = child.getNodeName();
            if (MEMBER.equals(childName)) {
                NodeList memberChildren = child.getChildNodes();

                String name = null;
                Object value = null;
                for (int j = 0; j < memberChildren.getLength(); j++) {
                    Node memberChild = memberChildren.item(j);
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

        }
        return res;
    }

    ArrayList<Object> parseArray(Node valueNode) throws ParseException {
        ArrayList<Object> res = new ArrayList<>();

        NodeList dataNodes = valueNode.getChildNodes();
        if (dataNodes.getLength() > 0) {
            NodeList elemNodes = dataNodes.item(0).getChildNodes();
            for (int i = 0; i < elemNodes.getLength(); i++) {
                Node vNode = elemNodes.item(i);
                if (VALUE.equals(vNode.getNodeName())) {
                    res.add(parseValue(vNode));
                }
            }
        }

        return res;
    }

    /**
     * Returns number of top level result values.
     * @return number of result values
     */
    public int getValueCount() {
        return values.size();
    }

    /**
     * Returns result value as string.
     * @param index index of requested value
     * @return value as string
     * @throws IllegalStateException in case of requested value is not string
     */
    public String getStringValue(int index) {
        Object val = values.get(index);
        if (val instanceof String) {
            return (String)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as integer.
     * @param index index of requested value
     * @return value as integer
     * @throws IllegalStateException in case of requested value is not integer
     */
    public Integer getIntegerValue(int index) {
        Object val = values.get(index);
        if (val instanceof Integer) {
            return (Integer)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as boolean.
     * @param index index of requested value
     * @return value as boolean
     * @throws IllegalStateException in case of requested value is not boolean
     */
    public Boolean getBooleanValue(int index) {
        Object val = values.get(index);
        if (val instanceof Boolean) {
            return (Boolean)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as double.
     * @param index index of requested value
     * @return value as double
     * @throws IllegalStateException in case of requested value is not double
     */
    public Double getDoubleValue(int index) {
        Object val = values.get(index);
        if (val instanceof Double) {
            return (Double)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as date.
     * @param index index of requested value
     * @return value as date
     * @throws IllegalStateException in case of requested value is not date
     */
    public Date getDateValue(int index) {
        Object val = values.get(index);
        if (val instanceof Date) {
            return (Date)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as binary data.
     * @param index index of requested value
     * @return value as binary data
     * @throws IllegalStateException in case of requested value is not binary data
     */
    public byte[] getBinaryValue(int index) {
        Object val = values.get(index);
        if (val instanceof byte[]) {
            return (byte[])val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as array.
     * @param index index of requested value
     * @return value as array
     * @throws IllegalStateException in case of requested value is not array
     */
    public List<Object> getArrayValue(int index) {
        Object val = values.get(index);
        if (val instanceof List) {
            return (List)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns result value as struct.
     * @param index index of requested value
     * @return value as struct
     * @throws IllegalStateException in case of requested value is not struct
     */
    public Map<String,Object> getStructValue(int index) {
        Object val = values.get(index);
        if (val instanceof Map) {
            return (Map)val;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns all result values.
     * @return result values
     */
    public List getValues() {
        return values;
    }
}
