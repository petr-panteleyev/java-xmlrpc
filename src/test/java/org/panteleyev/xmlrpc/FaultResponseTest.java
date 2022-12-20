/*
 Copyright © 2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FaultResponseTest {
    private static final String FAULT_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <fault>
                    <value>
                        <struct>
                            <member>
                                <name>faultCode</name>
                                <value>
                                    <int>4</int>
                                </value>
                            </member>
                            <member>
                                <name>faultString</name>
                                <value>
                                    <string>Too many parameters.</string>
                                </value>
                            </member>
                        </struct>
                    </value>
                </fault>
            </methodResponse>
            """;

    @Test
    public void testFaultResponse() throws Exception {
        try {
            TestUtil.parseResult(FAULT_RESPONSE);
            fail();
        } catch (XMLRPCException ex) {
            assertEquals(4, ex.getFaultCode());
            assertEquals("Too many parameters.", ex.getMessage());
        }
    }
}
