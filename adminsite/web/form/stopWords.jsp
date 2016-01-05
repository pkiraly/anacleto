<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"  prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Stop words</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
  <script language="JavaScript" src="js/select.js" type="text/javascript"></script>
</head>

<body>
<html:form action="/stopWords">
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="stopWords.title" /></td>
  </tr>
</table>

<table align="center" cellspacing="10">
  <tr>
    <!-- Add new stopword -->
    <td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
      <bean:message key="stopWords.addnew" />
      <html:text styleClass="header2" property="newStopWord"/>
      <html:image property="addNewButt"
				titleKey="stopWords.addnew" src="icons/e_forward.gif" />
    </td>
     
    <!-- Delete selected stopwords --> 
    <td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
       <bean:message key="stopWords.delsel" />
       <html:image property="deleteButt" titleKey="stopWords.delsel"
                   src="icons/e_forward.gif" />
    </td>
  </tr>
</table>

<table cellspacing="0" cellpadding="5" border="0" width="50%" align="center">
  <tr class="header2">
    <td align="center"><a href=""
      onclick="selectAll('selectedWords'); return false;">X</a><br>
      <a href="" onclick="deSelectAll('selectedWords');return false;">0</a>
    </td>
    <td align="center">Stop Word</td>
  </tr>
<logic:iterate id="stopWord" name="stopWordsForm" property="stopWordList">
  <tr>
    <td align="center">
      <html:multibox property="selectedWords">
        <bean:write name="stopWord" />
      </html:multibox>
    </td>
    <td align="center"><bean:write name="stopWord" /></td>
  </tr>
</logic:iterate>
</table>
</html:form>
</body>
</html>
