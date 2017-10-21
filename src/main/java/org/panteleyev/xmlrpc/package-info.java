/*
 * Copyright (c) 2017, Petr Panteleyev <petr@panteleyev.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/**
 This package provides a lightweight library to access XMLRPC services according
 to the <a href="http://xmlrpc.scripting.com/spec.html">XMLRPC Specification</a>.

 <h1>Data Types</h1>
 <p>The following Java classes are used to represent XMLRPC data types:</p>
 <table border="1">
 <caption></caption>
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

 <h1>Date Representation</h1>

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