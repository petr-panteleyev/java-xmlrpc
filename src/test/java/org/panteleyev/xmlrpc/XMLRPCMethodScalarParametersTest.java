/*
  Copyright (c) Petr Panteleyev. All rights reserved.
  Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.xmlrpc;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.testng.Assert.assertEquals;

public class XMLRPCMethodScalarParametersTest {

    @DataProvider(name="scalarParameters")
    public Object[][] getTestParameters() {
        var c = new GregorianCalendar();
        c.set(1972, Calendar.OCTOBER, 5, 0, 0, 0);
        var d1 = c.getTime();
        c.set(1972, Calendar.OCTOBER, 5, 23, 3, 54);
        var d2 = c.getTime();

        return new Object[][] {
            {"Append TRUE", Boolean.TRUE, "<param><value><boolean>1</boolean></value></param>" },
            {"Append FALSE", Boolean.FALSE, "<param><value><boolean>0</boolean></value></param>" },
            {"Append Integer", 123, "<param><value><int>123</int></value></param>" },
            {"Append Integer", -123, "<param><value><int>-123</int></value></param>" },
            {"Append String", "Hello World", "<param><value><string>Hello World</string></value></param>" },
            {"Append Double", 123.45, "<param><value><double>123.45</double></value></param>" },
            {"Append Double", -123.45, "<param><value><double>-123.45</double></value></param>" },
            {"Append Data", new byte[] { (byte)0x14, (byte)0xfb, (byte)0x9c, (byte)0x03, (byte)0xd9, (byte)0x7e }, "<param><value><base64>FPucA9l+</base64></value></param>" },
            {"Append Date", d1, "<param><value><dateTime.iso8601>19721005T00:00:00</dateTime.iso8601></value></param>" },
            {"Append Date", d2, "<param><value><dateTime.iso8601>19721005T23:03:54</dateTime.iso8601></value></param>" },
        };
    }
    
    
    @Test(dataProvider="scalarParameters")
    public void appendParameter(String message, Object value, String expected) throws XMLRPCException {
        var p = new XMLRPCParameters(TimeZone.getDefault());
        p.appendParameter(value);
        assertEquals(p.getParametersString(), expected, message);
    }
}
