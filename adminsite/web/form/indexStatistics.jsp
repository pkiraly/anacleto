<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Index statistics</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>

<body bottommargin="0" topmargin="0" leftmargin="0" rightmargin="0" marginheight="0" marginwidth="0">

<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="indstat.title" /></td>
  </tr>
  <tr>
    <td><bean:message key="indstat.desc" /></td>
  </tr>
</table>

<table cellspacing="0" cellpadding="5" border="0" align="center" width="80%">
<html:form action="/indexStatistics">
  <tr>
    <td><bean:message key="indstat.numTerms" /></td>
    <td><html:text property="numTerms" size="4" styleClass="header2" /></td>
    <td><html:image src="icons/e_synch_nav.gif" title="Update" property="submit" /></td>
  </tr>
</html:form>
</table>

<table cellspacing="0" cellpadding="5" border="0" align="center" width="80%">
  <tr>
    <td><bean:message key="indstat.termdesc" /></td>
  </tr>
  <tr>
    <td><bean:message key="indstat.fielddesc" /></td>
  </tr>
  <tr>
    <td><bean:message key="indstat.occurdesc" /></td>
  </tr>
</table>

<table align="center" width="80%">
<logic:present property="termFreq" name="indexStatisticsForm">
  <tr class="header2" align="center">
    <td width="20%"><bean:message key="indstat.term" /></td>
    <td width="30%"><bean:message key="indstat.field" /></td>
    <td width="20%"><bean:message key="indstat.occur" /></td>
  </tr>
  
<logic:iterate id="term" property="termFreq" name="indexStatisticsForm">
  <tr align="center">
    <td><bean:write name="term" property="text" /></td>
    <td><bean:write name="term" property="field" /></td>
    <td><bean:write name="term" property="docFreq" /></td>
  </tr>
</logic:iterate>
</logic:present>
</table>

</body>
</html>
