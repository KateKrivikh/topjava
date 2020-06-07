<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Adding meal</title>
    <style>
        label {
            display: inline-block;
            width: 100px
        }
    </style>
</head>
<body>
<h3><a href="meals">Meals</a></h3>
<hr>
<h2>${meal.id == null ? 'Добавляем новую еду' : 'Редактируем еду'}</h2>

<form method="POST" action='meals'>
    <input type="hidden" name="id" value="${meal.id}">
    <label id="DateTime">Дата/время</label>
    <input
            type="datetime-local" name="dateTime" width="20"
            value="${meal.dateTime}"/> <br/>
    <label id="Description">Описание</label>
    <input
            type="text" name="description" required="required" width="20"
            value="${meal.description}"/> <br/>
    <label id="Calories">Калории</label>
    <input
            type="number" name="calories" required="required" min="1" width="20"
            value="${meal.calories}"/> <br/>
    <input
            type="submit" value="Сохранить"/>
</form>
</body>
</html>