<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Anacleto Digital Library</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>
<body>

<!-- Header -->
<table cellpadding="0" cellspacing="0" width="100%" border="0">
  <tr height="65">
    <td align="left"><img src="image/anacleto.gif" alt="Anacleto Digital Library" ></td>
  </tr>
<!-- /Header -->

<!-- Body -->
  <tr bgcolor="#4682B4" valign="middle">
    <td align="right" nowrap="nowrap">
      |&nbsp;<html:link styleClass="white" action="/configure" target="body"><bean:message key="header.conf" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/xmlEdit" target="body"><bean:message key="header.xmledit" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/indexMaintenance" target="body"><bean:message key="header.indexmaint" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/indexStatistics" target="body"><bean:message key="header.indstat" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/stopWords" target="body"><bean:message key="header.stopwords" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/accentConversion" target="body"><bean:message key="header.accents" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/authentication.jsp" target="body"><bean:message key="header.authentication" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" action="/logViewer" target="body"><bean:message key="header.logViewer" /></html:link>&nbsp;
      |&nbsp;<html:link styleClass="white" href="form/logout.jsp" target="body"><bean:message key="header.logout" /></html:link>
    </td>
  </tr>
</table>	
	
</body>
</html>
	