<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Моя еда</h2>

<c:set var="dateFormatter" value="${requestScope.dateFormatter}"/>

<table style="border: solid; width: 60%">
    <tr style="text-align: left">
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>

    <c:forEach var="mealTo" items="${requestScope.mealsTo}">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'}">
            <td>${mealTo.dateTime.format(dateFormatter)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&id=${mealTo.id}">Изменить</a></td>
            <td><a href="meals?action=delete&id=${mealTo.id}">Удалить</a></td>
        </tr>
    </c:forEach>

</table>

<p><a href="meals?action=insert">Добавить</a></p>

</body>
</html>