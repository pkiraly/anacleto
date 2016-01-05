<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"  prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/anacleto.tld"                       prefix="anacleto"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Book details form</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>

<body>
<html:form action="/bookdetails">
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="bookDetails.title" /></td>
  </tr>
</table>

<logic:present name="bookDetailsForm" property="book">
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <bean:define id="bookBean" name="bookDetailsForm" property="book" />
  <tr>
    <td colspan="2"><b><u>General Parameters</u></b></td>
  </tr>
  <tr>
    <td>Name</td>
    <td><bean:write name="bookBean" property="name" /></td>
  </tr>
  <tr>
    <td>Title</td>
    <td><bean:write name="bookBean" property="title" /></td>
  </tr>
  <tr>
    <td>URL</td>
    <td><bean:write name="bookBean" property="URL" /></td>
  </tr>
  <tr>
    <td>Encoding</td>
    <td><bean:write name="bookBean" property="encoding" /></td>
  </tr>
  <tr>
    <td>Content type</td>
    <td><bean:write name="bookBean" property="contentType" /></td>
  </tr>

  <tr>
    <td colspan="2"><b><u>Authentication</u></b></td>
  </tr>
  <tr>
    <td>User</td>
    <td><bean:write name="bookBean" property="user" /></td>
  </tr>
  <tr>
    <td>Password</td>
    <td><bean:write name="bookBean" property="password" /></td>
  </tr>

  <tr>
    <td colspan="2"><b><u>TEI2 Support</u></b></td>
  </tr>
  <tr>
    <td>tei2IndexingStyleSheet</td>
    <td><bean:write name="bookBean" property="tei2IndexingStyleSheet" /></td>
  </tr>

  <tr>
    <td colspan="2"><b><u>NXT3 Support</u></b></td>
  </tr>
  <tr>
    <td>NXT3 content descriptor</td>
    <td><bean:write name="bookBean" property="nxt3Descriptor" /></td>
  </tr>
  <tr>
    <td>NXT3 content descriptor</td>
    <td><bean:write name="bookBean" property="nxt3IndexingStyleSheet" /></td>
  </tr>
  <tr>
    <td colspan="2"><b><u>Scheduling details</u></b></td>
  </tr>
  <tr>
    <td>Scheduled</td>
    <td><bean:write name="bookBean" property="scheduled" /></td>
  </tr>
  <tr>
    <td>Cron Expression</td>
    <td><bean:write name="bookBean" property="schedulingCronExpression" /></td>
  </tr>
</table>
</logic:present>
	
<p align="center">
<bean:define id="length" name="bookDetailsForm" property="length" />
<bean:define id="offset" name="bookDetailsForm" property="offset" />
<bean:define id="size"   name="bookDetailsForm" property="numberOfPages" />
<!-- bean:size   id="size"   name="bookDetailsForm" property="pages"/ -->
<anacleto:pager size="${size}" 
                offset="<%= Integer.parseInt(offset.toString()) %>"
                length="${length}"
                url="<%= request.getQueryString() %>"
                separator=" &middot; "
                />
</p>

<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr class="header2">
    <td colspan="3">Indexed pages</td>
  </tr>
  <tr class="header2">
    <td>Name</td>
    <td>Title</td>
    <td>Location</td>
  </tr>

<logic:iterate id="currPage" name="bookDetailsForm" 
               property="pages" indexId="index">
  <tr>
    <td><%= 
    	(Integer.parseInt(offset.toString()) 
    	+ Integer.parseInt(index.toString())
    	+ 1) %>.)
      <html:link action="documentIndexView" paramId="name"
                 paramName="currPage" paramProperty="name">
        <bean:write name="currPage" property="name" />
      </html:link>
    </td>
    <td><bean:write name="currPage" property="title" /></td>
    <td><bean:write name="currPage" property="location" /></td>
  </tr>
</logic:iterate>
</table>
</html:form>
</body>
</html>
