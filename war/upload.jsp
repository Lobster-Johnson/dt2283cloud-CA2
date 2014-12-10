<%@ page 
import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%BlobstoreService blobstoreService = 
BlobstoreServiceFactory.getBlobstoreService(); %>

<html>
	<head>
		<title>Upload Test</title>
	</head>
<body>
	<form action="<%= blobstoreService.createUploadUrl("/upload") %>" 
method="post" enctype="multipart/form-data">
		
		<input type="file" name="myFile">
		<input type="submit" value="Submit">
		<BR>
		<INPUT TYPE="radio" NAME="radios" VALUE="Public">
             Public
            <BR>
            <INPUT TYPE="radio" NAME="radios" VALUE="Private">
             Private
            <BR>
	</form>
</body>
</html>