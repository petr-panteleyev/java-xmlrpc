/*
  Copyright (c) Petr Panteleyev. All rights reserved.
  Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.xmlrpc;

/**
 * This class is used to provides XMLRPC error reporting.
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
