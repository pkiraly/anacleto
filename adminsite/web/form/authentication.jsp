<%@ page language="java" contentType="text/html; charset=UTF-8"
%><%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean"
%><%@ taglib uri="http://jakarta.apache.org/struts/tags-html"	prefix="html"
%><%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Authentication form</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
  <script language="JavaScript" src="js/select.js" type="text/javascript"></script>
</head>
<body>

<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="auth.title" /></td>
  </tr>
</table>
<br>

<!-- Add new User -->
<html:form action="/authentication" method="POST">
<table style="border-width: 1px; border-style: solid; border-color:#2d598a;"
  align="center" cellspacing="10">
  <tr class="header2">
    <logic:equal name="authenticationForm" property="pageStatusIsInsert" value="true">
      <td colspan="2"><bean:message key="auth.addnew" /></td>
    </logic:equal>
	
    <logic:equal name="authenticationForm" property="pageStatusIsInsert" value="false">
      <td colspan="2"><bean:message key="auth.modify" /></td>
    </logic:equal>
  </tr>
		<tr>
			<td><bean:message key="auth.user" /></td>
			<td>
				<font color="red"><html:errors property="newUser.user"/></font>
				<logic:equal name="authenticationForm" property="pageStatusIsInsert" value="true">
					<html:text styleClass="header2" property="newUser.user" />
				</logic:equal>
				<logic:equal name="authenticationForm" property="pageStatusIsInsert" value="false">
					<html:hidden property="newUser.user"/>
					<html:text styleClass="header2" property="newUser.user" 
						disabled="true"/>
				</logic:equal>
			</td>	
		</tr>
	<logic:equal name="authenticationForm" property="pageStatusIsInsert" value="false">
		<tr>
			<td><bean:message key="auth.enabled" /></td>
			<td>
				<html:select property="newUser.enabled" styleClass="header2" >
					<html:option styleClass="header2" value="true"/>
					<html:option styleClass="header2" value="false"/>
				</html:select>
			</td>
		</tr>
	</logic:equal>		
		<tr>
			<td><bean:message key="auth.realName" /></td>
			<td><html:text styleClass="header2" property="newUser.realName" /></td>
		</tr>
		<tr>
			<td><bean:message key="auth.email" /></td>
			<td><html:text styleClass="header2" property="newUser.email" /></td>
		</tr>
		<tr>
			<td><bean:message key="auth.password" /></td>
			<td>
				<font color="red"><html:errors property="newUser.password"/></font>
				<html:text styleClass="header2" property="newUser.password" />
			</td>
		</tr>

	<logic:equal name="authenticationForm" property="pageStatusIsInsert" value="false">
		<tr>
			<td><bean:message key="auth.ipFilter" /></td>
			<td>
				<html:select property="newUser.ipFilterEnabled" styleClass="header2" >
					<html:option styleClass="header2" value="true"/>
					<html:option styleClass="header2" value="false"/>
				</html:select>
			</td>
		</tr>
	</logic:equal>		
		
		<tr>
			<td><bean:message key="auth.userType"/></td>
			<td>
				<html:select property="newUser.admin" styleClass="header2">
					<html:option styleClass="header2" value="true" key="auth.adminUser"/>
					<html:option styleClass="header2" value="false" key="auth.normalUser"/>
				</html:select>
			</td>
		</tr>
	<logic:equal name="authenticationForm" property="pageStatusIsInsert" value="true">
		<tr>
			<td><bean:message key="auth.addnew" /></td>
			<td><html:image property="addNewButt" titleKey="auth.addnew"
						src="icons/e_forward.gif" /></td>
		</tr>
	</logic:equal>
	<logic:equal name="authenticationForm" property="pageStatusIsInsert" value="false">
		<tr>
			<td><bean:message key="auth.modify" /></td>
			<td><html:image property="modifyButt" titleKey="auth.modify" 
					src="icons/e_forward.gif" /></td>
		</tr>
		<tr>
			<td><bean:message key="auth.delete" /></td>
			<td><html:image property="deleteButt" titleKey="auth.delete" 
				src="icons/e_forward.gif" /></td>
		</tr>
	</logic:equal>
	</table>
</html:form>
<!--	    /Add new User      -->

<br>
<!---------------Admin Users--------------->
<table cellspacing="0" cellpadding="5" border="0" width="50%" align="center">
	<tr class="header2">
		<td colspan="6" align="center"><bean:message key="auth.adminUsers" /></td>
	</tr>
	<tr class="header2">
		<td align="center"><bean:message key="auth.user" /></td>
		<td align="center"><bean:message key="auth.enabled" /></td>
		<td align="center"><bean:message key="auth.realName" /></td>
		<td align="center"><bean:message key="auth.email" /></td>
		<td align="center"><bean:message key="auth.password" /></td>
		<td align="center"><bean:message key="auth.ipFilter" /></td>
	</tr>
<logic:iterate id="adminUser" name="authenticationForm"	property="adminUsers">
	<tr valign="top">
		<td><html:link action="/authentication" paramId="modifyUser"
				paramName="adminUser" paramProperty="user">
			<bean:write name="adminUser" property="user" />
			</html:link>
		</td>
		<td align="center"><html:checkbox name="adminUser" 
					property="enabled" disabled="true"/></td>
		<td><bean:write name="adminUser" property="realName"/></td>
		<td><bean:write name="adminUser" property="email"/></td>
		<td><!-- bean:write name="adminUser" property="password" / --></td>
		<td align="center"><html:checkbox name="adminUser" 
			property="ipFilterEnabled" disabled="true"/></td>
	</tr>
</logic:iterate>

<!---------------Empty line--------------->
	<tr>
		<td colspan="6" align="center">&nbsp;</td>
	</tr>

<!---------------Normal Users--------------->
	<tr class="header2" >
		<td colspan="6" align="center"><bean:message key="auth.normalUsers" /></td>
	</tr>
	<tr class="header2">
		<td align="center"><bean:message key="auth.user" /></td>
		<td align="center"><bean:message key="auth.enabled" /></td>
		<td align="center"><bean:message key="auth.realName" /></td>
		<td align="center"><bean:message key="auth.email" /></td>
		<td align="center"><bean:message key="auth.password" /></td>
		<td align="center"><bean:message key="auth.ipFilter" /></td>
	</tr>
<logic:iterate id="normalUser" name="authenticationForm" property="normalUsers">
	<tr valign="top">
		<td><html:link action="/authentication" paramId="modifyUser"
					paramName="normalUser" paramProperty="user">
				<bean:write name="normalUser" property="user" />
			</html:link>
		</td>
		<td align="center"><html:checkbox name="normalUser" 
				property="enabled" disabled="true"/></td>
		<td><bean:write name="normalUser" property="realName"/></td>
		<td><bean:write name="normalUser" property="email"/></td>
		<td><!-- bean:write name="normalUser" property="password"/ --></td>
		<td align="center"><html:checkbox  name="normalUser" 
				property="ipFilterEnabled" disabled="true"/></td>
	</tr>
</logic:iterate>
</table>

</body>
</html>
