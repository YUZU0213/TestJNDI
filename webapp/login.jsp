<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>ログイン画面</title>
</head>
<body>
<form method="post" action="${fn:escapeXml('j_security_check')}">
<table>
  <tr>
    <td>ユーザーID</td>
    <td><input type="text" name="j_username"></td>
  </tr>
  <tr>
    <td>パスワード</td>
    <td><input type="password" name="j_password"></td>
  </tr>
</table>
<br>
<input type="submit" value="ログイン" name="submit">
<input type="reset" value="リセット" name="reset">
</form>
</body>
</html>