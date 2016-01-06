<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 
<html> 
	<head>
		<title>JSP for GetLocationsForm form</title>
	</head>
	<body>
		<html:form action="/getLocations">
			<html:submit/><html:cancel/>
		</html:form>
	</body>
</html>

