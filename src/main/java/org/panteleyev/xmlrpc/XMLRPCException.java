/*
 Copyright © 2017-2022 Petr Panteleyev <petr@panteleyev.org>
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

/**
 * This class provides XMLRPC error reporting.
 */
public class XMLRPCException extends Exception {
    private final int faultCode;

    XMLRPCException(String msg) {
        super(msg);
        this.faultCode = 0;
    }

    XMLRPCException(int faultCode, String msg) {
        super(msg);
        this.faultCode = faultCode;
    }

    XMLRPCException(String msg, Throwable cause) {
        super(msg, cause);
        this.faultCode = 0;
    }

    /**
     * Returns XMLRPC fault code if applicable.
     *
     * @return fault code
     */
    public int getFaultCode() {
        return faultCode;
    }
}
