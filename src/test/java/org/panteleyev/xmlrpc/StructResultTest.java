/*
 Copyright © 2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StructResultTest {
    private static final String STRUCT_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
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
                    </param>
                </params>
            </methodResponse>
            """;

    @Test
    public void testStructValue() throws Exception {
        var res = TestUtil.parseResult(STRUCT_RESPONSE);
        var data = res.getStructValue(0);
        var expected = Map.of(
                "lowerBound", 18,
                "upperBound", 139
        );

        assertEquals(1, res.getValueCount());
        assertEquals(expected, data);
    }
}
