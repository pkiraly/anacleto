<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"   prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"   prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<%@ taglib uri="/WEB-INF/anacleto.tld"                        prefix="anacleto"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <title>Log Viewer</title>
  <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
  <link rel="stylesheet" type="text/css" href="css/logger.css">
<script type="text/javascript">
function showSelect(id) {
  var oDiv = document.getElementById('logFiles');
  if(oDiv != null) {
    oDiv.innerHTML = document.getElementById(id).innerHTML;
  }
}
</script>
</head>

<body>
<table cellspacing="0" cellpadding="5" border="0" width="100%">
  <tr>
    <td class="header2"><bean:message key="indmaint.title" /></td>
  </tr>
</table>

<bean:define id="logFileName" name="logViewerForm" property="logFileName" type="java.lang.String"/>
<bean:define id="logFileType" name="logViewerForm" property="logFileType" type="java.lang.String"/>

<html:form action="/logViewer" method="GET">
  <table align="center" cellspacing="0">
    <tr>
      <td>
        Select log file: 
      </td>
      <td>
<logic:iterate id="item" name="logViewerForm" property="logFileTypes">
<html:radio property="logFileType" 
  idName="item" value="optionValue" 
  styleId="${item.optionValue}FileType" 
  onclick="showSelect('${item.optionValue}LogFiles');">
  <label for="${item.optionValue}FileType"><bean:write
  name="item" property="optionLabel" /></label>
</html:radio>
</logic:iterate>
      </td>
    </tr>
    <tr>
      <td>Select archive:</td>
      <td>

<div id="LogFiles">
<select name="logFileName">
<option value="">-- please select! --</option>
<% if(logFileType.equals("userevents")){ %>
<logic:iterate id="logFile" name="logViewerForm" property="usereventsLogFiles" 
><bean:define id="currentLogFile" name="logFile" property="optionValue" type="java.lang.String"/>
<option value="<bean:write name="logFile" property="optionValue" />"<% 
if(currentLogFile.equals(logFileName)){ 
%> selected="selected" <% } 
%>><bean:write name="logFile" property="optionLabel" /></option></logic:iterate>
<% } 
else if(logFileType.equals("admin")){ %>
<logic:iterate id="logFile" name="logViewerForm" property="adminLogFiles" 
><bean:define id="currentLogFile" name="logFile" property="optionValue" 
type="java.lang.String" />
<option value="<bean:write name="logFile" property="optionValue" />"<% 
if(currentLogFile.equals(logFileName)){ 
%> selected="selected" 
<% } 
%>><bean:write name="logFile" property="optionLabel" /></option></logic:iterate>
<% } 
else if(logFileType.equals("indexing")){ %>
<logic:iterate id="logFile" name="logViewerForm" property="indexingLogFiles" 
><bean:define id="currentLogFile" name="logFile" property="optionValue" 
type="java.lang.String" />
<option value="<bean:write name="logFile" property="optionValue" />"<% 
if(currentLogFile.equals(logFileName)){
%> selected="selected" <% } 
%>><bean:write name="logFile" property="optionLabel" /></option></logic:iterate>
<% } %>
</select>
</div>

      </td>
    </tr>
    <tr>
      <td>
        Select log level: 
      </td>
      <td>
        <input type="radio" name="logLevel" value="DEBUG" id="logDEBUG"
        <logic:match name="logViewerForm" property="logLevel" value="DEBUG"> 
        checked="checked"
        </logic:match>
        />
        <label for="logDEBUG">DEBUG</label>
        <input type="radio" name="logLevel" value="INFO" id="logINFO"
        <logic:match name="logViewerForm" property="logLevel" value="INFO"> 
        checked="checked"
        </logic:match>
        />
        <label for="logINFO">INFO</label>
        <input type="radio" name="logLevel" value="WARN" id="logWARN"
        <logic:match name="logViewerForm" property="logLevel" value="WARN"> 
        checked="checked"
        </logic:match>
        />
        <label for="logWARN">WARNING</label>
        <input type="radio" name="logLevel" value="ERROR" id="logERROR"
        <logic:match name="logViewerForm" property="logLevel" value="ERROR"> 
        checked="checked"
        </logic:match>
        />
        <label for="logERROR">ERROR</label>
        <input type="radio" name="logLevel" value="FATAL" id="logFATAL"
        <logic:match name="logViewerForm" property="logLevel" value="FATAL"> 
        checked="checked"
        </logic:match>
        />
        <label for="logFATAL">CRITICAL</label>
        <input type="radio" name="logLevel" value="ALL" id="logALL"
        <logic:match name="logViewerForm" property="logLevel" value="ALL"> 
        checked="checked"
        </logic:match>
        />
        <label for="logALL">ALL</label>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="right">
        <html:image title="submit" src="icons/e_forward.gif" />
      </td>
    </tr>
  </table>
</html:form>

<bean:define id="length" name="logViewerForm" property="length" />
<bean:define id="offset" name="logViewerForm" property="offset" />
<bean:define id="size"   name="logViewerForm" property="numberOfPages" />
<anacleto:pager size="${size}" 
                offset="<%= Integer.parseInt(offset.toString()) %>"
                length="${length}"
                url="<%= request.getQueryString() %>"
                separator=" &middot; "
                />

<table width="100%" class="main">
  <tr>
    <td class="header2" width="200"><bean:message key="logs.eventTime" /></td>
    <td class="header2" width="200"><bean:message key="logs.eventClass" /></td>
    <td class="header2"><bean:message key="logs.eventMessage" /></td>
  </tr>

<logic:iterate id="logLine" name="logViewerForm" property="logLines">
  <tr class="<bean:write name="logLine" property="eventType"/>" valign="top">
    <td><bean:write name="logLine" property="eventTime"/></td>
    <td><bean:write name="logLine" property="eventClass"/></td>
    <td><bean:write name="logLine" property="eventMessage" filter="false" locale="UTF-8"/></td>
  </tr>
</logic:iterate>
		
</table>


<div id="indexingLogFiles" class="log">
<html:select property="logFileName" value="logFileName">
<option value="">-- please select! --</option>
<logic:iterate id="logFile" name="logViewerForm" property="indexingLogFiles" indexId="id"
><bean:define id="currentLogFile" name="logFile" property="optionValue" type="java.lang.String"/>
<option value="<bean:write name="logFile" property="optionValue" />"<% if(currentLogFile.equals(logFileName)){ %> selected="selected" <% } %>><bean:write name="logFile" property="optionLabel" /></option></logic:iterate>
</html:select>
</div>

<div id="usereventsLogFiles" class="log">
<select name="logFileName">
<option value="">-- please select! --</option>
<logic:iterate id="logFile" name="logViewerForm" property="usereventsLogFiles" indexId="id"
><bean:define id="currentLogFile" name="logFile" property="optionValue" type="java.lang.String"/>
<option value="<bean:write name="logFile" property="optionValue" />"<% if(currentLogFile.equals(logFileName)){ %> selected="selected" <% } %>><bean:write name="logFile" property="optionLabel" /></option></logic:iterate>
</select>
</div>

<div id="adminLogFiles" class="log">
<select name="logFileName">
<option value="">-- please select! --</option>
<logic:iterate id="logFile" name="logViewerForm" property="adminLogFiles" indexId="id"
><bean:define id="currentLogFile" name="logFile" property="optionValue" type="java.lang.String"/>
<option value="<bean:write name="logFile" property="optionValue" />"<% if(currentLogFile.equals(logFileName)){ %> selected="selected" <% } %>><bean:write name="logFile" property="optionLabel" /></option></logic:iterate>
</select>
</div>

</body>
</html>
