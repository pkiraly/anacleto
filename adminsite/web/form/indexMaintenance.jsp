<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"  prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
  <title>Index Maintenance</title>

<script type="text/javascript">
function selectAll()
{
  var el = document.forms['indexMaintenanceForm'].elements;
  
  for (var i = 0; i < el.length; i++) {
    if (el[i].name == 'selectedBooks'){
      el[i].checked = true;
    }
  }
}

function deSelectAll()
{
  var el = document.forms['indexMaintenanceForm'].elements;

  for (var i = 0; i < el.length; i++) {
    if (el[i].name == 'selectedBooks'){
      el[i].checked = false;
    }
  }
}
</script>
</head>

<body>
<html:form action="/indexMaintenance">
	<table cellspacing="0" cellpadding="5" border="0" width="100%">
		<tr>
			<td class="header2"><bean:message key="indmaint.title" /></td>
		</tr>
	</table>

	<table align="center" cellspacing="10">
		<tr align="center">
			<td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
			Optimize index <html:image property="optimizeButt"
				title="Optimize index" src="icons/e_forward.gif" />
			</td>
			<td>
			<td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
			Refresh <html:image property="refreshButt"
				title="Refresh" src="icons/e_synch_nav.gif" />
			</td>
		</tr>
	</table>

	<logic:notEmpty property="missingBookColl" name="indexMaintenanceForm">
		<table width="100%" align="center">
			<tr class="header2">
				<td align="center">Removed books already present in the index</td>
			</tr>
			<logic:iterate id="missingBook" property="missingBookColl" name="indexMaintenanceForm">
				<tr>
					<td align="center">
						<html:link action="/bookdetails" paramId="bookName"
							paramName="missingBook">
							<bean:write name="missingBook"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		<tr>
			<td align="center">
			<div style="border-width: 1px; border-style: solid; border-color:#2d598a; vertical-align: middle;">
				Remove these books from the index 
				<html:image property="removeButt"
				title="Remove" src="icons/e_forward.gif" /></div>
			</td>
		</tr>
		</table>
	</logic:notEmpty>
	
	<table width="100%" align="center">
		<tr class="header2">
			<td colspan="4" align="center">Indexing Details</td>
		</tr>
		<tr class="header2">
			<td>Category</td>
			<td>Book</td>
			<td>Next Fire Time</td>
			<td>Cron Expression</td>
		</tr>
		<tr>
			<td>Currently Indexing</td>
			<td><bean:write property="currentlyIndexing"
					name="indexMaintenanceForm"/></td>
		</tr>
		
		<tr>
			<td colspan="2">Books waiting for indexing</td>
		</tr>
		<logic:notEmpty property="booksInQueue" name="indexMaintenanceForm">
		<logic:iterate id="book" property="booksInQueue" 
				name="indexMaintenanceForm">
			<tr>
				<td></td>
				<td><bean:write name="book"/></td>
				<td></td>
				<td></td>
			</tr>
		</logic:iterate>
		</logic:notEmpty>
		<tr>
			<td colspan="2">One time jobs</td>
		</tr>
		<logic:notEmpty property="simpleJobIndexing" name="indexMaintenanceForm">
		<logic:iterate id="simpleJob" property="simpleJobIndexing" 
				name="indexMaintenanceForm">
			<tr>
				<td></td>
				<td><bean:write name="simpleJob" property="book"/></td>
				<td><bean:write name="simpleJob" property="nextFireTime"/></td>
				<td></td>
			</tr>
		</logic:iterate>
		</logic:notEmpty>
		<tr>
			<td colspan="2">Scheduled jobs</td>
		</tr>
		<logic:notEmpty property="cronJobIndexing" name="indexMaintenanceForm">
		<logic:iterate id="cronJob" property="cronJobIndexing" 
				name="indexMaintenanceForm">
			<tr>
				<td></td>
				<td><bean:write name="cronJob" property="book"/></td>
				<td><bean:write name="cronJob" property="nextFireTime"/></td>
				<td><bean:write name="cronJob" property="cronExpression"/></td>
			</tr>
		</logic:iterate>
		</logic:notEmpty>
	</table>
	<p>
	
	
	<table align="center" cellspacing="10">
		<tr align="center">
			<td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
			Index selected resources now <html:image property="indexNowButt"
				title="Index resources now" src="icons/e_forward.gif" />
			</td>
			<td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
				Index selected resources at (yyyy/mm/dd)
				<html:text styleClass="header2" property="schedDate" size="10"/> (hh:mm)
				<html:text styleClass="header2" property="schedTime" size="5"/>
				<html:image property="indexAtButt"
				title="Index resources at" src="icons/e_forward.gif" />
			</td>
			
			<td style="border-width: 1px; border-style: solid; border-color:#2d598a;">
				Remove selected books from the index 
				<html:image property="removeSelectedButt" title="Remove" 
							src="icons/e_forward.gif" />
			</td>
		</tr>
	</table>

	<table width="100%">
		<tr class="header2">
			<td colspan="4" align="center">Mounted Books</td>
		</tr>
		<tr class="header2" align="center">
			<td><a href="" onclick="selectAll(); return false;">X</a><br>
			<a href="" onclick="deSelectAll();return false;">0</a></td>
			<td><bean:message key="indmaint.name" /></td>
			<td><bean:message key="indmaint.pagenum" /></td>
			<td><bean:message key="indmaint.booktitle" /></td>
			<!--
			<td><bean:message key="indmaint.url" /></td>
			-->
			
		</tr>
		<logic:notEmpty property="bookColl" name="indexMaintenanceForm">
		<logic:iterate id="book" property="bookColl"
			name="indexMaintenanceForm">
			<tr>
				<td><html:multibox property="selectedBooks">
					<bean:write name="book" property="name" />
				</html:multibox></td>
				
				<td><html:link action="/bookdetails" paramId="bookName"
					paramName="book" paramProperty="name">
					<bean:write name="book" property="name" />
				</html:link></td>
				
				<td align="center" >
					<bean:write name="book" property="pageNum" />
				</td>

				<td><bean:write name="book" property="title" /></td>
			</tr>
		</logic:iterate>
		</logic:notEmpty>
	</table>
</html:form>
</body>
</html>
