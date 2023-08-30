<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 29.08.2023
  Time: 21:47
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h1> Here are your bank accounts ${user.name} ${user.surname}!!!</h1>
<br/> ${message_user_page}
<table border="1" cellspacing="0" cellpadding="4">
<c:forEach var="account" items="${account_list}">
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <tr>
            <td>Number: ${account.accountNumber}  </td>
            <td>Bank: ${account.bank}  </td>
            <td>Opening date: ${account.openingDate}  </td>
            <td>Currency: ${account.currency}  </td>
            <td>Amount: ${account.amount} <input type="hidden" name="id_account" value="${account.id}"></td>
            <td> <input type="hidden" name="command" value="chose_account"/> <input type="submit"  name="push" value="chose" /></td>
        </tr>
    </form>
</c:forEach>
</table>
</br>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="logout"/>
    <input type="submit"  name="push" value="logout" />
</form>
</body>
</html>
