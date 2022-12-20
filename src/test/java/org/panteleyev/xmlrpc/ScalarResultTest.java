/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScalarResultTest {

    private static final String STRING_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
                        <value>
                            <string>South Dakota</string>
                        </value>
                    </param>
                </params>
            </methodResponse>
            """;

    private static final String INTEGER_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
              <params>
                  <param>
                      <value>
                          <int>123</int>
                      </value>
                  </param>
                  <param>
                      <value>
                          <i4>-123</i4>
                      </value>
                  </param>
              </params>
            </methodResponse>
            """;

    private static final String BOOLEAN_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
                        <value>
                            <boolean>1</boolean>
                        </value>
                    </param>
                    <param>
                        <value>
                            <boolean>0</boolean>
                        </value>
                    </param>
                </params>
            </methodResponse>
            """;

    private static final String DOUBLE_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
                        <value>
                            <double>123.45</double>
                        </value>
                    </param>
                    <param>
                        <value>
                            <double>-567.89</double>
                        </value>
                    </param>
                </params>
            </methodResponse>
            """;

    private static final String DATE_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
                        <value>
                            <dateTime.iso8601>19980717T14:08:55</dateTime.iso8601>
                        </value>
                    </param>
                </params>
            </methodResponse>
            """;

    private static final String BINARY_RESPONSE = """
            <?xml version="1.0"?>
            <methodResponse>
                <params>
                    <param>
                        <value>
                            <base64>FPucA9l+</base64>
                        </value>
                    </param>
                </params>
            </methodResponse>
            """;

    @Test
    public void stringValue() throws Exception {
        var res = TestUtil.parseResult(STRING_RESPONSE);

        assertEquals(1, res.getValueCount());
        assertEquals("South Dakota", res.getStringValue(0));
    }

    @Test
    public void integerValue() throws Exception {
        var res = TestUtil.parseResult(INTEGER_RESPONSE);

        assertEquals(2, res.getValueCount());
        assertEquals(123, res.getIntegerValue(0).intValue());
        assertEquals(-123, res.getIntegerValue(1).intValue());
    }

    @Test
    public void booleanValue() throws Exception {
        var res = TestUtil.parseResult(BOOLEAN_RESPONSE);

        assertEquals(2, res.getValueCount());
        assertTrue(res.getBooleanValue(0));
        assertFalse(res.getBooleanValue(1));
    }

    @Test
    public void doubleValue() throws Exception {
        var res = TestUtil.parseResult(DOUBLE_RESPONSE);

        assertEquals(2, res.getValueCount());
        assertEquals(123.45, res.getDoubleValue(0), 0.0);
        assertEquals(-567.89, res.getDoubleValue(1), 0.0);
    }

    @Test
    public void dateValue() throws Exception {
        var res = TestUtil.parseResult(DATE_RESPONSE, TimeZone.getDefault());

        var date = res.getDateValue(0);
        var cal = new GregorianCalendar();
        cal.clear();
        cal.set(1998, Calendar.JULY, 17, 14, 8, 55);
        Date exp = cal.getTime();

        assertEquals(1, res.getValueCount());
        assertEquals(exp, date);
    }

    @Test
    public void binaryValue() throws Exception {
        var res = TestUtil.parseResult(BINARY_RESPONSE);
        var data = res.getBinaryValue(0);

        assertEquals(1, res.getValueCount());
        assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0xfb, (byte) 0x9c, (byte) 0x03, (byte) 0xd9, (byte) 0x7e}, data);
    }
}
