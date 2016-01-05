<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"	prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>login error</title>
  <link rel="stylesheet" type="text/css" href="../css/mainstyle.css">
</head>

<body>
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="loginerror.title" /></td>
  </tr>
</table>

<table width="80%" cellspacing="10" cellpadding="5">
  <tr>
    <html:errors property="loginerror.message"/>
    <td>
      <bean:message key="loginerror.message" />
    </td>
  </tr>
  <tr>
    <td>
      <html:link href="login.jsp">Go back to the login</html:link>
    </td>
  </tr>
</table>
</body>
</html>
