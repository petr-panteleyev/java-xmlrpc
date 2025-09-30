/*
 Copyright © 2017-2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */
package org.panteleyev.xmlrpc;

/**
 * This class defines XMLRPC method.
 *
 * @param name name of the method
 */
public record XMLRPCMethod(String name) {

    public XMLRPCMethod {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Method name cannot be empty");
        }
    }

    /**
     * Returns XML representation of the method call.
     *
     * @param params call parameters
     * @return XML for the method call
     */
    public String getMethodString(XMLRPCParameters params) {
        return "<?xml version=\"1.0\"?>"
                + "<methodCall><methodName>"
                + name
                + "</methodName><params>"
                + params.getParametersString()
                + "</params></methodCall>";
    }
}
