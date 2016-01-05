<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"	prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <link rel="stylesheet" type="text/css" href="../css/mainstyle.css">
  <title>login</title>
</head>

<body>
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="login.title" /></td>
  </tr>
</table>

<form method="POST" action="j_security_check">
<table width="80%">
  <tr>
    <td width="20%" align="right"><bean:message key="login.user" /></td>
    <td><input type="text" name="j_username" class="header2" /></td>
  </tr>
  <tr>
    <td align="right"><bean:message key="login.password" /></td>
    <td><input type="password" name="j_password" class="header2"/></td>
  </tr>
  <tr>
    <td></td>
    <td>
      <input type="submit" value="Login" />&nbsp;
      <input type="reset"	value="Clear"/>
    </td>
  </tr>
</table>
</form>
</body>
</html>
