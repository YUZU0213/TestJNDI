<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/preUpload.css">
<jsp:include page="header.jsp" />
<title>TODOタスクのアップロード画面</title>
<script type="text/javascript">

</script>
</head>
<body>
	<jsp:include page="nav.jsp" />
	<div class="container">
		アップロードするファイルを選択し、[アップロード]ボタンを押してください。
		<form name="upload" action="../todo/upload" method="post"
			enctype="multipart/form-data">
			<input type="file" name="uploadfile"/>
			<br />
			<input type="submit" class="btn btn-primary" value="アップロード" />
			<input type="hidden" name="id" value="<c:out value="${id}"/>" />
			<input type="hidden" name="token" value="<c:out value="${ token }" />" />
		</form>
	</div>
</body>
</html>