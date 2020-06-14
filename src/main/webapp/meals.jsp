<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        dl {
            margin: 8px 0;
            padding: 0;
        }

        dt {
            display: inline-block;
            margin-left: 8px;
            width: 150px;
        }

        dd {
            display: inline-block;
            vertical-align: top;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>

    <form action="meals" method="get">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt>Date from (included):</dt>
            <dd><input type="date" name="dateFrom" value="${param.dateFrom}"/></dd>
            <dt>Time from (included):</dt>
            <dd><input type="time" name="timeFrom" value="${param.timeFrom}"/></dd>
        </dl>
        <dl>
            <dt>Date to (included):</dt>
            <dd><input type="date" name="dateTo" value="${param.dateTo}"/></dd>
            <dt>Time to (excluded):</dt>
            <dd><input type="time" name="timeTo" value="${param.timeTo}"/></dd>
        </dl>
        <button type="submit">Filter</button>
    </form>

    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>