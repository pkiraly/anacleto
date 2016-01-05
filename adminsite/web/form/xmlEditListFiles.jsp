<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"  prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<logic:equal name="xmlEditListFilesForm" property="isAjaxian" value="false">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>Insert title here</title>
</head>
<body>

<h1>files</h1>

<logic:iterate id="fileName" name="xmlEditListFilesForm" property="fileNames">
<bean:write name="fileName"/><br>
</logic:iterate>

</body>
</html>
</logic:equal>
<logic:equal name="xmlEditListFilesForm" property="isAjaxian" value="true">
<bean:size id="size"  name="xmlEditListFilesForm" property="fileNames" />
[<logic:iterate id="fileName" name="xmlEditListFilesForm" property="fileNames" 
indexId="index">"<bean:write name="fileName"/>"<logic:notEqual 
  value="<%= String.valueOf(size.intValue()-1) %>"
  name="index">, </logic:notEqual></logic:iterate>]
</logic:equal>
