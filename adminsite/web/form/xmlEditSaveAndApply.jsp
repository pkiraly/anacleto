<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"  prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<logic:notPresent name="success" scope="request">
<html:errors property="errors.writecontent" />
<html:errors property="errors.filenotfound" />
<html:errors property="errors.configuration" />
</logic:notPresent>