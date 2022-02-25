/*
  Copyright (c) Petr Panteleyev. All rights reserved.
  Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.xmlrpc;

import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class XMLRPCResultTest {
    private static final String FAULT_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<fault>" +
            "<value>" +
            "<struct>" +
            "<member>" +
            "<name>faultCode</name>\n" +
            "<value><int>4</int></value>\n" +
            "</member>" +
            "<member>" +
            "<name>faultString</name>" +
            "<value><string>Too many parameters.</string></value>" +
            "</member>" +
            "</struct>" +
            "</value>" +
            "</fault>" +
            "</methodResponse>";

    private static final String STRING_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>\n" +
            "<value><string>South Dakota</string></value>\n" +
            "</param>\n" +
            "</params>" +
            "</methodResponse>";

    private static final String INTEGER_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value><int>123</int></value>" +
            "</param>" +
            "<param>" +
            "<value><i4>-123</i4></value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String BOOLEAN_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value><boolean>1</boolean></value>" +
            "</param>" +
            "<param>" +
            "<value><boolean>0</boolean></value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String DOUBLE_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value><double>123.45</double></value>" +
            "</param>" +
            "<param>" +
            "<value><double>-567.89</double></value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String DATE_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value><dateTime.iso8601>19980717T14:08:55</dateTime.iso8601></value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String BINARY_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value><base64>FPucA9l+</base64></value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String ARRAY_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value>" +
            "<array>" +
            "<data>" +
            "<value><i4>12</i4></value>" +
            "<value><string>Egypt</string></value>" +
            "<value><boolean>0</boolean></value>" +
            "<value><double>123.45</double></value>" +
            "</data>" +
            "</array>" +
            "</value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String STRUCT_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value>" +
            "<struct>" +
            "<member>\n" +
            "<name>lowerBound</name>\n" +
            "<value><i4>18</i4></value>\n" +
            "</member>\n" +
            "<member>" +
            "<name>upperBound</name>" +
            "<value><i4>139</i4></value>" +
            "</member>" +
            "</struct>" +
            "</value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    private static final String ARRAY_STRUCT_RESPONSE =
        "<?xml version=\"1.0\"?>" +
            "<methodResponse>" +
            "<params>" +
            "<param>" +
            "<value>" +
            "<array>" +
            "<data>" +
            "<value><i4>12</i4></value>" +
            "<value><string>Egypt</string></value>" +
            "<value><boolean>0</boolean></value>" +
            "<value><double>123.45</double></value>" +
            "<value>" +
            "<struct>" +
            "<member>" +
            "<name>lowerBound</name>" +
            "<value><i4>18</i4></value>" +
            "</member>" +
            "<member>" +
            "<name>upperBound</name>" +
            "<value><i4>139</i4></value>" +
            "</member>" +
            "</struct>" +
            "</value>" +
            "</data>" +
            "</array>" +
            "</value>" +
            "</param>" +
            "</params>" +
            "</methodResponse>";

    public XMLRPCResultTest() {
    }

    @Test
    public void faultCode() throws ParserConfigurationException, SAXException, IOException, ParseException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(FAULT_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        try {
            res.parse(doc);
            fail();
        } catch (XMLRPCException ex) {
            assertEquals(4, ex.getFaultCode());
            assertEquals("Too many parameters.", ex.getMessage());
        }
    }

    @Test
    public void stringValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(STRING_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);
        var value = res.getStringValue(0);

        assertEquals(1, res.getValueCount());
        assertEquals("South Dakota", value);
    }

    @Test
    public void integerValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(INTEGER_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        assertEquals(2, res.getValueCount());
        assertEquals(123, res.getIntegerValue(0).intValue());
        assertEquals(-123, res.getIntegerValue(1).intValue());
    }

    @Test
    public void booleanValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(BOOLEAN_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        assertEquals(2, res.getValueCount());
        assertTrue(res.getBooleanValue(0));
        assertFalse(res.getBooleanValue(1));
    }

    @Test
    public void doubleValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(DOUBLE_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        assertEquals(2, res.getValueCount());
        assertEquals(123.45, res.getDoubleValue(0), 0.0);
        assertEquals(-567.89, res.getDoubleValue(1), 0.0);
    }

    @Test
    public void dateValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(DATE_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult(TimeZone.getDefault());
        res.parse(doc);

        var date = res.getDateValue(0);
        var cal = new GregorianCalendar();
        cal.clear();
        cal.set(1998, Calendar.JULY, 17, 14, 8, 55);
        Date exp = cal.getTime();

        assertEquals(1, res.getValueCount());
        assertEquals(exp, date);
    }

    @Test
    public void binaryValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(BINARY_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        var data = res.getBinaryValue(0);

        assertEquals(1, res.getValueCount());
        assertEquals(new byte[]{(byte) 0x14, (byte) 0xfb, (byte) 0x9c, (byte) 0x03, (byte) 0xd9, (byte) 0x7e}, data);
    }

    @Test
    public void arrayValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(ARRAY_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        var data = res.getArrayValue(0);

        var expected = new ArrayList<>(4);
        expected.add(12);
        expected.add("Egypt");
        expected.add(false);
        expected.add(123.45);

        assertEquals(1, res.getValueCount());
        assertEquals(data, expected);
    }

    @Test
    public void structValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(STRUCT_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        var data = res.getStructValue(0);

        var expected = new HashMap<String, Object>();
        expected.put("lowerBound", 18);
        expected.put("upperBound", 139);


        assertEquals(1, res.getValueCount());
        assertEquals(data, expected);
    }

    @Test
    public void arrayStructValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();

        var is = new InputSource();
        is.setCharacterStream(new StringReader(ARRAY_STRUCT_RESPONSE));

        var doc = builder.parse(is);
        var res = new XMLRPCResult();
        res.parse(doc);

        var data = res.getArrayValue(0);

        var map = new HashMap<>();
        map.put("lowerBound", 18);
        map.put("upperBound", 139);

        var expected = new ArrayList<>(4);
        expected.add(12);
        expected.add("Egypt");
        expected.add(false);
        expected.add(123.45);
        expected.add(map);

        assertEquals(1, res.getValueCount());
        assertEquals(data, expected);
    }
}
