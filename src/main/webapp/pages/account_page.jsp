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
    <td>Currency: ${account.currency}</td>
    <td>Opening date: ${account.openingDate}</td>
    <td>Current amount: ${account.amount}</td>
  </tr>
</table>
<form action="${pageContext.request.contextPath}/controller" method="post">
<table>
  <tr>
    <td> <input type="hidden" name="command" value="go_to_refill_account"/>
         <input type="submit"  name="push" value="refill" />
    </td>
    <td>
      <input type="hidden" name="command" value="go_to_withdrawals_account"/>
      <input type="submit"  name="push" value="withdrawals" />
    </td>
    <td>
      <input type="hidden" name="command" value="go_to_transfer_account"/>
      <input type="submit"  name="push" value="transfer" />
    </td>
    <td></td>
  </tr>
</table>
</form>
</br>
${message_user_page}
</br>
<table>
  <tr>
    <td>
      <form action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="command" value="logout"/>
        <input type="submit"  name="push" value="logout" />
      </form>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/controller" >
        <input type="hidden" name="command" value="back_to_users_accounts_page"/>
        <input type="submit"  name="push" value="back" />
      </form>
    </td>
  </tr>
</table>
</body>
</html>
