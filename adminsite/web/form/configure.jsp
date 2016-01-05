<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>JSP for configureForm form</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>
<body>
<html:form action="/configure">
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="config.title" /></td>
  </tr>
</table>

<table width="60%" align="center">
  <tr align="center">
    <td>
      <div style="border-width: 1px; border-collapse: separate; border-style: solid; border-color:#2d598a; vertical-align: middle; ">
			Save and apply changes <html:image property="saveButton"
			title="Save and apply changes" src="icons/e_forward.gif" /></div>
    </td>
			<td>
			<div
				style="border-width: 1px; border-style: solid; border-color:#2d598a; vertical-align: middle;">
			Apply changes without save <html:image property="applyButton"
				title="Apply changes" src="icons/e_forward.gif" /></div>
			</td>
			<td>
			<div
				style="border-width: 1px; border-style: solid; border-color:#2d598a; vertical-align: middle;">
			Reload saved values <html:image property="reloadButton"
				title="Reload saved values" src="icons/e_forward.gif" /></div>
			</td>
		</tr>
	</table>

	<table cellpadding="5">

		<tr>
			<td>
				<bean:message key="config.configdir" />
				<html:errors  property="params.configDir" />
			</td>
			<td>
				<html:text styleClass="header2" 
					property="params.configDir" size="50" />
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="config.indexdir" />
				<html:errors property="params.indexDir" />
			</td>
			<td>
				<html:text styleClass="header2" 
					property="params.indexDir" size="50" />
			</td>
		</tr>
		
		<tr>
			<td>
				<bean:message key="config.logdir" />
				<html:errors property="params.indexDir" />
			</td>
			<td>
				<html:text styleClass="header2" 
					property="params.logParams.logDir" size="50" />
			</td>
		</tr>
		
		<tr>
			<td><bean:message key="config.booksFile" /><html:errors
				property="params.booksFile" /></td>
			<td>
				<html:text styleClass="header2" 
					property="params.booksFile" />
			</td>
		</tr>

		<tr>
			<td><bean:message key="config.locale" /><html:errors
				property="params.locale" /></td>
			<td>
				<html:select styleClass="header2" property="params.locale">
         			<html:options property="locales"/>
         		</html:select>
         
			</td>
		</tr>
		
		<tr>
			<td><bean:message key="config.sort" /><html:errors
				property="params.sort" /></td>
			<td>
				<html:select styleClass="header2" property="params.sortType">
         			<html:option styleClass="header2" value="0" key="config.sort.0"/>
					<html:option styleClass="header2" value="1" key="config.sort.1"/>
         		</html:select>
         
			</td>
		</tr>
		
		<tr>
			<td>
				<bean:message key="config.securityLevel" />
				<html:errors property="params.securityLevel" />
			</td>
			<td>
				<html:select property="params.securityLevel" styleClass="header2" >
					<html:option styleClass="header2" value="0" key="config.securityLevel.1"/>
					<html:option styleClass="header2" value="1" key="config.securityLevel.2"/>
					<html:option styleClass="header2" value="2" key="config.securityLevel.3"/>
				</html:select>
				
			</td>
		</tr>
		
		<tr>
			<td>
				<bean:message key="config.indexingPriority" /> 
				<html:errors property="params.indexingPriority" />
			</td>
			<td><html:text styleClass="header2" 
				property="params.indexingPriority" 
				maxlength="1" size="1" /></td>
		</tr>

		<tr>
			<td><bean:message key="config.defaultContentStyleSheet" /> <html:errors
				property="params.defaultContentStyleSheet" /></td>
			<td><html:text styleClass="header2" 
					property="params.defaultContentStyleSheet" />
			</td>
		</tr>

		<tr>
			<td><bean:message key="config.defaultHtmlIndexingscheme" /> <html:errors
				property="params.defaultHtmlIndexingscheme" /></td>
			<td><html:text styleClass="header2" 
					property="params.defaultHtmlIndexingscheme" />
			</td>
		</tr>

		<tr>
			<td><bean:message key="config.defaultTei2IndexingStyleSheet" /> <html:errors
				property="params.defaultTei2IndexingStyleSheet" /></td>
			<td><html:text styleClass="header2" 
					property="params.defaultTei2IndexingStyleSheet" />
			</td>
		</tr>

		<tr>
			<td><bean:message key="config.defaultXmlDisplayScheme" /> <html:errors
				property="params.defaultXmlDisplayScheme" /></td>
			<td><html:text styleClass="header2" 
			property="params.defaultXmlDisplayScheme" /></td>
		</tr>

		<tr>
			<td><bean:message key="config.maxTocEntryNumber" /> <html:errors
				property="params.maxTocEntryNumber" /></td>
			<td><html:text styleClass="header2" 
			property="params.maxTocEntryNumber" /></td>
		</tr>


	</table>
	<p></p>
<!--	<table border="1" width="100%" bordercolor="#2d598a"-->
<!--		style="border-collapse: collapse; vertical-align: middle; color: #2d598a">-->
<!--		<tr>-->
<!--			<td>Errors</td>-->
<!--		</tr>-->
<!--		<tr>-->
<!--			<td><html:errors /></td>-->
<!--		</tr>-->
<!--	</table>-->



</html:form>
</body>
</html>
