<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 30.08.2023
  Time: 21:50
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1> ${user.name} ${user.surname} here is your account number ${account.accountNumber} </h1>
<table>
  <tr>
    <td>Bank: ${account.bank}</td>
    <td>Currency: ${account.bank}</td>
    <td>Opening date: ${account.openingDate}</td>
    <td>Current amount: ${account.amount}</td>
  </tr>
</table>
<table>
  <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
</table>

</body>
</html>
