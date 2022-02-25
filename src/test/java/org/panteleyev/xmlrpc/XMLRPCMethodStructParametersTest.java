/*
  Copyright (c) Petr Panteleyev. All rights reserved.
  Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.xmlrpc;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import static java.util.Map.entry;
import static org.testng.Assert.assertEquals;

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
        var p = new XMLRPCParameters(TimeZone.getDefault());
        p.appendParameter(value);
        assertEquals(p.getParametersString(), expected);
    }
}
