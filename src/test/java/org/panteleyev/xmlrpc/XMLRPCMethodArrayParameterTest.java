/*
  Copyright (c) Petr Panteleyev. All rights reserved.
  Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.xmlrpc;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.TimeZone;
import static org.testng.Assert.assertEquals;

@Test
public class XMLRPCMethodArrayParameterTest {
    
    @DataProvider(name="arrayParameters")
    public Object[][] arrayParametersDataProvider() {
        return new Object[][] {
            { List.of(1, "string"),
                "<param><value><array><data><value><int>1</int></value><value><string>string</string></value></data></array></value></param>" },
            { List.of(123.45, "new string"),
                "<param><value><array><data><value><double>123.45</double></value><value><string>new string</string></value></data></array></value></param>" },
            { List.of(Boolean.FALSE, Boolean.TRUE),
                "<param><value><array><data><value><boolean>0</boolean></value><value><boolean>1</boolean></value></data></array></value></param>" },
        };
    }
    
    @Test(dataProvider="arrayParameters")
    public void testArrayParameters(List value, String expected) {
        var p = new XMLRPCParameters(TimeZone.getDefault());
        p.appendParameter(value);
        assertEquals(p.getParametersString(), expected);
    }
}
