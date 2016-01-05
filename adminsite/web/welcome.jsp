<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>

<body>

<div style="height: 25px;">
  <div class="header2" style="padding: 4px 0 3px 10px;">
 <bean:message key="welcome.head" />
  </div>
</div>

<table cellspacing="0" cellpadding="5" border="0" width="100%">
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td width="20%" valign="top"><bean:message key="welcome.intro.1" /></td>
		<td>
				<ul>
			<li><bean:message key="welcome.intro.2" /></li>
			<li><bean:message key="welcome.intro.3" /></li>
			<li><bean:message key="welcome.intro.4" /></li>
		</ul>
		</td>
	</tr>

	<tr>
		<td colspan="2"><bean:message key="welcome.open" /></td>
	</tr>


	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>


	<tr>
		<td width="20%" valign="top"><bean:message key="welcome.search" /></td>
		<td>
				<ul>
			<li><bean:message key="welcome.search.1" /></li>
			<li><bean:message key="welcome.search.2" /></li>
			<li><bean:message key="welcome.search.3" /></li>
			<li><bean:message key="welcome.search.4" /></li>
			<li><bean:message key="welcome.search.5" /></li>
			<li><bean:message key="welcome.search.6" /></li>
			
		</ul>
		</td>
	</tr>

	<tr>
		<td colspan="2"><bean:message key="welcome.person" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>

<table cellspacing="0" cellpadding="5" border="0" width="100%">


	<tr>
		<td width="20%" valign="top"><bean:message key="welcome.ideal" /></td>
		<td>
		<ul>
			<li><bean:message key="welcome.ideal.1" /></li>
			<li><bean:message key="welcome.ideal.2" /></li>
			<li><bean:message key="welcome.ideal.3" /></li>
		</ul>
		</td>
	</tr>

<%--	<tr>--%>
<%--		<td colspan="2"><bean:message key="welcome.epilog" /></td>--%>
<%--	</tr>--%>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2"><bean:message key="welcome.helpus" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>

</body>
</html>
