<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<form action="users" method="post">
    <input type="hidden" name="userId" value="${param.userId}">
    <button type="submit">Apply</button>
</form>
</body>
</html>