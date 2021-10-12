<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="editaction" type=""--%>
<c:set var="editActionName" scope="session" value="${editaction=='edit'?'Edit meal':'Add meal'}"/>
<html>
<head>
    <link rel="stylesheet" href="mealedit.css" type="text/css">
    <title>${editActionName}</title>
</head>

<body>
<h2>${editActionName}</h2>
<div class="main">
    <form method="POST" action='meals' name="frmAddMeal" accept-charset="UTF-8">
        <input type="hidden" readonly="readonly" name="id" value="${meal.id}"/>
        <label for="dateTime">DateTime:</label>
        <input type="datetime-local" name="dateTime" value="${meal.dateTime}" id="dateTime"/>

        <label for="description">Description:</label>
        <input type="text" name="description" value="${meal.description}" id="description"/>

        <label for="calories">Calories :</label>
        <input type="number" name="calories" value="${meal.calories}" id="calories"/>

        <div>
            <input type="submit" value="Submit"/>
            <input type="button" name="Cancel" value="Cancel" onclick="location.href='meals'">
        </div>
    </form>
</div>
</body>
</html>