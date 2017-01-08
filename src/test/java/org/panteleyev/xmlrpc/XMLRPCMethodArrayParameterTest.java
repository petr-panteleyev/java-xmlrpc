/*
 * Copyright (c) 2015, 2017, Petr Panteleyev <petr@panteleyev.org>
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class XMLRPCMethodArrayParameterTest {
    
    @DataProvider(name="arrayParameters")
    public Object[][] arrayParametersDataProvider() {
        return new Object[][] {
            { new ArrayList<>(Arrays.asList(1, "string")),
                "<param><value><array><data><value><int>1</int></value><value><string>string</string></value></data></array></value></param>" },
            { new ArrayList<>(Arrays.asList(123.45, "new string")),
                "<param><value><array><data><value><double>123.45</double></value><value><string>new string</string></value></data></array></value></param>" },
            { new ArrayList<>(Arrays.asList(Boolean.FALSE, Boolean.TRUE)),
                "<param><value><array><data><value><boolean>0</boolean></value><value><boolean>1</boolean></value></data></array></value></param>" },
        };
    }
    
    @Test(dataProvider="arrayParameters")
    public void testArrayParameters(ArrayList value, String expected) {
        XMLRPCParameters p = new XMLRPCParameters(TimeZone.getDefault());
        p.appendParameter(value);
        Assert.assertEquals(p.getParametersString(), expected);        
    }
}
