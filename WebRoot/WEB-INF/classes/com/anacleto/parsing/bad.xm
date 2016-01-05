<?xml version="1.0" encoding="UTF-8"?>
<xml-body>
	<shelf name="root" title="Anacleto" location="something">
		<shelf name="fs" title="File System Directory">
			<book name="fsdir"
				    title="Files on the local filesystem (C:/java/temp/fstest)"
					location="C:/java/temp/fstest" 
					scheduled="true"
					schedulingCronExpression="0/100 * * * * ?"
					contentType="FSCONTENT"/>
		</shelf>
		
		<shelf name="wd" title="WebDav Destination">			
			<book name="webdav"
				    title="Local WebDav resource (http://localhost:8000/webdav)"
					location="http://localhost:8080/slide/files" 
					user="robi"
					password="robi"
					scheduled="false"
					schedulingCronExpression="0/100 * * * * ?"
					contentType="WEBDAVCONTENT"/>
					
			<book name="book2"
				    title="Local WebDav resource (http://localhost:8000/webdav)"
					location="http://localhost:8080/slide/files" 
					user="robi"
					password="robi"
					scheduled="false"
					schedulingCronExpression="0/100 * * * * ?"
					contentType="WEBDAVCONTENT"/>
					
		
	</shelf>
</xml-body>