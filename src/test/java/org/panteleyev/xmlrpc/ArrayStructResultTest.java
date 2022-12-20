/*
 Copyright © 2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayStructResultTest {

    private static final String ARRAY_STRUCT_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
                        <value>
                            <array>
                                <data>
                                    <value>
                                        <i4>12</i4>
                                    </value>
                                    <value>
                                        <string>Egypt</string>
                                    </value>
                                    <value>
                                        <boolean>0</boolean>
                                    </value>
                                    <value>
                                        <double>123.45</double>
                                    </value>
                                    <value>
                                        <struct>
                                            <member>
                                                <name>lowerBound</name>
                                                <value>
                                                    <i4>18</i4>
                                                </value>
                                            </member>
                                            <member>
                                                <name>upperBound</name>
                                                <value>
                                                    <i4>139</i4>
                                                </value>
                                            </member>
                                        </struct>
                                    </value>
                                </data>
                            </array>
                        </value>
                    </param>
                </params>
            </methodResponse>""";

    @Test
    public void testArrayStructValue() throws Exception {
        var res = TestUtil.parseResult(ARRAY_STRUCT_RESPONSE);
        var data = res.getArrayValue(0);
        var expected = List.of(12, "Egypt", false, 123.45, Map.of(
                "lowerBound", 18,
                "upperBound", 139
        ));

        assertEquals(1, res.getValueCount());
        assertEquals(expected, data);
    }
}
