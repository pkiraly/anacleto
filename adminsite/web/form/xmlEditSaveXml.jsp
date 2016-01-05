<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"  prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<logic:notPresent name="registration-successful" scope="request">
<html:errors property="writecontent" />
<html:errors property="filenotfound" />
</logic:notPresent>