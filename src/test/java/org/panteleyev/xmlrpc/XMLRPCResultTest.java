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
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(FAULT_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        try {
            res.parse(doc);
            Assert.fail();
        } catch (XMLRPCException ex) {
            Assert.assertEquals(4, ex.getFaultCode());
            Assert.assertEquals("Too many parameters.", ex.getMessage());
        }        
    }
    
    @Test
    public void stringValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(STRING_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        String value = res.getStringValue(0);
        
        Assert.assertEquals(1, res.getValueCount());
        Assert.assertEquals("South Dakota", value);        
    }

    @Test
    public void integerValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(INTEGER_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        Assert.assertEquals(2, res.getValueCount());
        Assert.assertEquals(123, res.getIntegerValue(0).intValue());    
        Assert.assertEquals(-123, res.getIntegerValue(1).intValue());    
    }
    
    @Test
    public void booleanValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(BOOLEAN_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        Assert.assertEquals(2, res.getValueCount());
        Assert.assertEquals(true, res.getBooleanValue(0).booleanValue());    
        Assert.assertEquals(false, res.getBooleanValue(1).booleanValue());    
    }
    
    @Test
    public void doubleValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(DOUBLE_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        Assert.assertEquals(2, res.getValueCount());
        Assert.assertEquals(123.45, res.getDoubleValue(0), 0.0);    
        Assert.assertEquals(-567.89, res.getDoubleValue(1), 0.0);    
    }
    
    @Test
    public void dateValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(DATE_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult(TimeZone.getDefault());
        res.parse(doc);
        
        Date date = res.getDateValue(0);
        GregorianCalendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(1998, 7-1, 17, 14, 8, 55);
        Date exp = cal.getTime();
        
        
        Assert.assertEquals(1, res.getValueCount());
        Assert.assertEquals(exp, date);
    }
    
    @Test
    public void binaryValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(BINARY_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        byte[] data = res.getBinaryValue(0);
        
        
        Assert.assertEquals(1, res.getValueCount());
        Assert.assertEquals(new byte[] { (byte)0x14, (byte)0xfb, (byte)0x9c, (byte)0x03, (byte)0xd9, (byte)0x7e }, data);
    }
    
    @Test
    public void arrayValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(ARRAY_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        List<Object> data = res.getArrayValue(0);
        
        List<Object> expected = new ArrayList<>(4);
        expected.add(12);
        expected.add("Egypt");
        expected.add(false);
        expected.add(123.45);
        
        
        Assert.assertEquals(1, res.getValueCount());
        Assert.assertEquals(data, expected);
    }
    
    @Test
    public void structValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(STRUCT_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        Map<String,Object> data = res.getStructValue(0);
        
        Map<String,Object> expected = new HashMap<>();
        expected.put("lowerBound", 18);
        expected.put("upperBound", 139);
        
        
        Assert.assertEquals(1, res.getValueCount());
        Assert.assertEquals(data, expected);
    }
    
    @Test
    public void arrayStructValue() throws ParserConfigurationException, SAXException, IOException, ParseException, XMLRPCException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
            
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(ARRAY_STRUCT_RESPONSE));
        
        Document doc = builder.parse(is);
        XMLRPCResult res = new XMLRPCResult();
        res.parse(doc);
        
        List<Object> data = res.getArrayValue(0);
        
        Map<String,Object> map = new HashMap<>();
        map.put("lowerBound", 18);
        map.put("upperBound", 139);        
        
        List<Object> expected = new ArrayList<>(4);
        expected.add(12);
        expected.add("Egypt");
        expected.add(false);
        expected.add(123.45);
        expected.add(map);
        
        
        Assert.assertEquals(1, res.getValueCount());
        Assert.assertEquals(data, expected);
    }
}
