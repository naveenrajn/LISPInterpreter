ironment entry. The declaration consists of an optional
description, the name of the environment entry, and an optional
value.  If a value is not specified, one must be supplied
during deployment.
-->
<!ELEMENT env-entry (description?, env-entry-name, env-entry-value?,
env-entry-type)>

<!--
The env-entry-name element contains the name of a web application's
environment entry.  The name is a JNDI name relative to the
java:comp/env context.  The name must be unique within a web application.

Example:

<env-entry-name>minAmount</env-entry-name>

Used in: env-entry
-->
<!ELEMENT env-entry-name (#PCDATA)>

<!--
The env-entry-type element contains the fully-qualified Java type of
the environment entry value that is expected by the web application's
code.

The following are the legal values of env-entry-type:

	java.lang.Boolean
	java.lang.Byte
	java.lang.Character
	java.lang.String
	java.lang.Short
	java.lang.Integer
	java.lang.Long
	java.lang.Float
	java.lang.Double

Used in: env-entry
-->
<!ELEMENT env-entry-type (#PCDATA)>

<!--
The env-entry-value element contains the value of a web application's
environment entry. The value must be a String that is valid for the
constructor of the specified type that takes a single String
parameter, or for java.lang.Character, a single character.

Example:

<env-entry-value>100.00</env-entry-value>

Used in: env-entry
-->
<!ELEMENT env-entry-value (#PCDATA)>

<!--
The error-code contains an HTTP error code, ex: 404

Used in: error-page
-->
<!ELEMENT error-code (#PCDATA)>

<!--
The error-page element contains a mapping between an error code
or exception type to the path of a resource in the web application

Used in: web-app
-->
<!ELEMENT error-page ((error-code | exception-type), location)>

<!--
The exception type contains a fully qualified class name of a
Java exception type.

Used in: error-page
-->
<!ELEMENT exception-type (#PCDATA)>

<!--
The extension element contains a string describing an
extension. example: "txt"

Used in: mime-mapping
-->
<!ELEMENT extension (#PCDATA)>

<!--
Declares a filter in the web application. The filter is mapped to
either a servlet or a URL pattern in the filter-mapping element, using
the filter-name value to reference. Filters can access the
initialization parameters declared in the deployment descriptor at
runtime via the FilterConfig interface.

Used in: web-app
-->
<!ELEMENT filter (icon?, filter-name, display-name?, description?,
filter-class, init-param*)>

<!--
The fully qualified classname of the filter.

Used in: filter
-->
<!ELEMENT filter-class (#PCDATA)>

<!--
Declaration of the filter mappings in this web application. The
container uses the filter-mapping declarations to decide which filters
to apply to a request, and in what order. The container matches the
request URI to a Servlet in the normal way. To determine which filters
to apply it matches filter-mapping declarations either on servlet-name,
or on url-pattern for each filter-mapping element, depending on which
style is used. The order in which filters are invoked is the order in
which filter-mapping declarations that match a request URI for a
servlet appear in the list of filter-mapping elements.The filter-name
value must be the value of the <filter-name> sub-elements of one of the
<filter> d