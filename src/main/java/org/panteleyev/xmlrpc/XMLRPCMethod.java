/*
  Copyright (c) Petr Panteleyev. All rights reserved.
  Licensed under the BSD license. See LICENSE file in the project root for full license information.
 */
package org.panteleyev.xmlrpc;

/**
 * This class defines XMLRPC method.
 */
public record XMLRPCMethod(String name) {

    public XMLRPCMethod {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Method name cannot be empty");
        }
    }

    public String getMethodString(XMLRPCParameters params) {
        return "<?xml version=\"1.0\"?>"
                + "<methodCall><methodName>"
                + name
                + "</methodName><params>"
                + params.getParametersString()
                + "</params></methodCall>";
    }
}
