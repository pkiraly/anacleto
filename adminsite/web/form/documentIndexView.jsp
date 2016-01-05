<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Document index</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>

<body>

<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="indexDetails.title" /></td>
  </tr>
</table>

<table cellspacing="0" cellpadding="1" border="0" width="100%">
<logic:present property="fieldMap" name="documentIndexViewForm">
  <tr>
    <td>Field Name</td>
    <td width="70%">Field Value</td>
    <td>Indexed</td>
    <td>Stored</td>
    <td>Tokenized</td>
    <td>TermVectorStored</td>
  </tr>
 <logic:iterate id="fieldLine" property="fieldMap" name="documentIndexViewForm">
  <tr valign="top">
    <td><strong><bean:write name="fieldLine" property="name"/></strong></td>
    <td width="70%"><bean:write name="fieldLine" property="value"/></td>
    <td><html:checkbox name="fieldLine" property="indexed"/></td>
    <td><html:checkbox name="fieldLine" property="stored"/></td>
    <td><html:checkbox name="fieldLine" property="tokenized"/></td>
    <td><html:checkbox name="fieldLine" property="termVectorStored"/></td>
  </tr>
</logic:iterate>
</logic:present>
</table>
</body>
</html>
