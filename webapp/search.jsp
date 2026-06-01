<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/detail.css">
<jsp:include page="header.jsp" />
<title>TODOタスクの一覧</title>
</head>
<body>
	<jsp:include page="nav.jsp" />
	<div class="container">
		<table class="table table-bordered">
			<tr>
				<th>番号</th>
				<th>タイトル</th>
				<th>タスク内容</th>
				<th>期限</th>
				<th>最終更新</th>
				<th>ユーザID</th>
				<th>状況</th>
				<th>詳細画面へ</th>
				<th>添付ファイル</th>
			</tr>
			<c:forEach items="${todoList}" var="dto">
				<tr>
					<td><c:out value="${dto.id}" /></td>
					<td><c:out value="${dto.title}" /></td>
					<td><c:out value="${dto.task}" /></td>
					<td><fmt:formatDate value="${dto.limitdate }"
							pattern="yyyy-MM-dd" /></td>
					<td><fmt:formatDate value="${dto.lastupdate }"
							pattern="yyyy-MM-dd" /></td>
					<td><c:out value="${dto.userid}" /></td>
					<td><c:out value="${dto.label}" /></td>
					<td><a href="detail?id=<c:out value="${dto.id}" />">詳細画面へ</a></td>
					<td><c:choose>
							<c:when test="${dto.filename != null}">
								<a href="download?id=<c:out value="${dto.id}" />" class="btn btn-primary">ダウンロード</a>
							</c:when>
							<c:otherwise>
						-
					</c:otherwise>
						</c:choose></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>