<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<html>
<head>
    <title>Add new meal</title>
</head>
<body>

<form method="POST" action='meals' name="frmAddMeal" accept-charset="UTF-8">
    ID : <input type="hidden" readonly="readonly" name="Id"
                     value="<c:out value="${meal.id}" />" /> <br />
    dateTime : <input type="text" name="dateTime"
                      value="<c:out  value="${MealsUtil.getFormattedDateTime(meal.dateTime)}" />"/> <br />
    description : <input type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    calories : <input  type="text" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />
    <input type="submit" value="Submit" />
    <input type="button" name="cancel" value="Cancel" onclick="location.href='meals'">
</form>
</body>
</html>