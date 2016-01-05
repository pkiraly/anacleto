<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Accent ConversionForm form</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
  <script language="JavaScript" src="js/select.js"></script>
</head>
<body>

<html:form action="/accentConversion">
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="accents.title" /></td>
  </tr>
  <tr>
    <td><bean:message key="accents.desc" /></td>
  </tr>
</table>

<table align="center" cellspacing="10">
  <tr align="center">
    <td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
      <!-- Add new Conversion Rule -->
      <bean:message key="accents.addnew" />
      <html:text styleClass="header2" property="newFrom" size="1" maxlength="1"/>
      <html:text styleClass="header2" property="newTo" size="1" maxlength="2"  name="accentConversionForm"/>
      <html:image property="addButt" titleKey="accents.addnew" src="icons/e_forward.gif" />
    </td>
    
    <td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
      <bean:message key="accents.delsel" />
      <html:image property="delButt" titleKey="accents.delsel" src="icons/e_forward.gif" />
    </td>
    
    <td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
      <bean:message key="accents.reload" />
      <html:image property="reloadButt" titleKey="accents.reload"
         src="icons/e_forward.gif" />
    </td>
  </tr>
</table>

<table cellspacing="0" cellpadding="5" border="0" width="50%" align="center">
  <tr class="header2">
    <td align="center">
      <a href="" onclick="selectAll('selectedAccents'); return false;">X</a><br>
      <a href="" onclick="deSelectAll('selectedAccents');return false;">0</a>
    </td>
    <td align="center">Accented char</td>
    <td align="center">Converted into</td>
  </tr>
  
<logic:iterate id="accent" name="accentConversionForm" property="accents">
  <tr>
    <td align="center">
      <html:multibox property="selectedAccents">
        <bean:write name="accent" property="key"/>
      </html:multibox>
    </td>
    <td align="center">
      <bean:write name="accent" property="key"/>
    </td>
    <td align="center">
      <bean:write name="accent" property="value"/>
    </td>
  </tr>
</logic:iterate>	

</table>
</html:form>

</body>
</html>
