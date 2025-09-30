/*
 Copyright © 2017-2025 Petr Panteleyev
 SPDX-License-Identifier: BSD-2-Clause
 */

/**
 This package provides a lightweight library to access XMLRPC services according
 to the <a href="http://xmlrpc.scripting.com/spec.html">XMLRPC Specification</a>.

 <h2>Data Types</h2>
 <p>The following Java classes are used to represent XMLRPC data types:</p>
 <table border="1">
 <caption>Data types</caption>
 <tr><th>XMLRPC</th><th>Java</th></tr>
 <tr><td>&lt;int&gt;, &lt;i4&gt;</td><td>Integer</td></tr>
 <tr><td>&lt;boolean&gt;</td><td>Boolean</td></tr>
 <tr><td>&lt;string&gt;</td><td>String</td></tr>
 <tr><td>&lt;double&gt;</td><td>Double</td></tr>
 <tr><td>&lt;dateTime.iso8601&gt;</td><td>java.util.Date</td></tr>
 <tr><td>&lt;base64&gt;</td><td>byte[]</td></tr>
 <tr><td>array</td><td>java.util.ArrayList</td></tr>
 <tr><td>struct</td><td>java.util.HashMap</td></tr>
 </table>

 <h2>Date Representation</h2>

 <p>XMLRPC specification provides the following statement about timezones:<br>
 <i>Don't assume a timezone. It should be specified by the server in its
 documentation what assumptions it makes about timezones.</i>
 </p>
 <p>By default Date parameters and result values are converted to/from string representation
 assuming GMT timezone. In case of server requires different timezones it must be specified when
 creating {@link org.panteleyev.xmlrpc.XMLRPCService XMLRPCService} object.
 </p>
 */

package org.panteleyev.xmlrpc;
