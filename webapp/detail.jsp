<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>TODOタスクの詳細画面</title>
<link rel="stylesheet" type="text/css" href="../css/input.css">
<jsp:include page="header.jsp" />
<script type="text/javascript">
window.onload = function() {
	var status = document.getElementById("status");
	status.selectedIndex = ${dto.status};
};
</script>
</head>
<body>
<jsp:include page="nav.jsp" />
<div class="container">
<form id="sender" action="register" method="POST">
<table class="table">
	<tr>
		<th>番号</th>
		<td>
			<c:choose>
				<c:when test="${dto.id > 0}">
					<c:out value="${dto.id}" />
				</c:when>
				<c:otherwise>
					(新規)
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<th>タイトル</th>
		<td><input type="text" name="title" value="<c:out value="${dto.title}" />" size="20"/></td>
	</tr>
	<tr>
		<th>タスク内容</th>
		<td><input type="text" name="task" value="<c:out value="${dto.task}" />" maxlength="128" size="60"/></td>
	</tr>
	<tr>
		<th>期限</th>
		<td>
		<c:choose>
			<c:when test="${dto.inputLimitdate != null }">
				<input type="text" name="limitdate" value="<c:out value="${dto.inputLimitdate}" />" size="10"/>
			</c:when>
			<c:otherwise>
				<input type="text" name="limitdate" value="<fmt:formatDate value="${dto.limitdate }" pattern="yyyy-MM-dd"/>" size="10"/>
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	<tr>
		<th>ユーザID</th>
		<td><input type="text" name="userid" value="<c:out value="${dto.userid}" />" size="16"/></td>
	</tr>
	<tr>
		<th>状況</th>
		<td>
			<select name="status" id="status">
				<option value="0">未着手</option>
				<option value="1">着手</option>
				<option value="2">完了</option>
				<option value="3">凍結</option>
			</select>
		</td>
	</tr>
	<c:if test="${dto.id != 0}">
		<tr>
			<th>添付ファイル</th>
			<td>
				<a href="preUpload?id=<c:out value="${dto.id}"/>" class="btn btn-primary">アップロード</a>
				<c:choose>
					<c:when test="${dto.filename != null}">
						<c:out value="${dto.filename}" />
					</c:when>
					<c:otherwise>
						添付ファイルはありません
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</c:if>
</table>
<input type="hidden" name="id" value="<c:out value="${ dto.id }" />" />
<input type="hidden" name="token" value="<c:out value="${ token }" />" />
<input type="submit" class="btn btn-success" value="登録する" />
</form>
<c:if test="${dto.id > 0}">
<br />
<form id="delete" action="delete" method="POST">
	<input type="hidden" name="id" value="<c:out value="${ dto.id }" />" />
	<input type="hidden" name="token" value="<c:out value="${ token }" />" />
	<input type="submit" class="btn btn-warning" value="削除する" />
</form>
</c:if>
<br />
<div class="alert alert-danger" >
	<c:forEach items="${errorMessages}" var="errorMessage">
	・${errorMessage }<br />
	</c:forEach>
</div>
</div>
</body>
</html>