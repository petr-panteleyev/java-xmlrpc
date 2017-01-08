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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XMLRPCMethodScalarParametersTest {

    @DataProvider(name="scalarParameters")
    public Object[][] getTestParameters() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1972, 10-1, 5, 0, 0, 0);
        Date d1 = c.getTime();
        c.set(1972, 10-1, 5, 23, 3, 54);
        Date d2 = c.getTime();

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
        XMLRPCParameters p = new XMLRPCParameters(TimeZone.getDefault());
        p.appendParameter(value);
        Assert.assertEquals(p.getParametersString(), expected, message);
    }
}
