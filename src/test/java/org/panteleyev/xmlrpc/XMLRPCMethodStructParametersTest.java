/*
 * Copyright (c) 2017, Petr Panteleyev <petr@panteleyev.org>
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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import static java.util.Map.entry;

public class XMLRPCMethodStructParametersTest {
    @DataProvider(name = "structParameters")
    public Object[][] structParametersDataProvider() {
        return new Object[][]{
                {Map.ofEntries(entry("1", "param1")),
                        "<param><value><struct><member><name>1</name><value><string>param1</string></value></member>"
                                + "</struct></value></param>"},
                {Map.ofEntries(entry("2", Boolean.TRUE)),
                        "<param><value><struct><member><name>2</name><value><boolean>1</boolean></value></member>"
                                + "</struct></value></param>"},
                {Map.ofEntries(entry("3", List.of(1, 2, 3))),
                        "<param><value><struct><member><name>3</name><value><array><data><value><int>1</int></value>"
                                + "<value><int>2</int></value><value><int>3</int></value></data></array></value>"
                                + "</member></struct></value></param>"}
        };
    }

    @Test(dataProvider = "structParameters")
    public void testStructParameters(Map value, String expected) {
        XMLRPCParameters p = new XMLRPCParameters(TimeZone.getDefault());
        p.appendParameter(value);
        Assert.assertEquals(p.getParametersString(), expected);
    }
}
