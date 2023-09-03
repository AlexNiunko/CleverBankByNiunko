<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 03.09.2023
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>Welcome to CleverBank, please fill registration form</h1>
</br>
<form action="${pageContext.request.contextPath}/controller" method="post">
            Input name
            <input type="text" name="transfer_amount" placeholder="input transfer amount here">
            Input surname
            <input type="text" name="beneficiary's_account" placeholder="input account number here">
             Input email
            <input type="text" name="beneficiary's_account" placeholder="input account number here">
             Input password
              <input type="text" name="beneficiary's_account" placeholder="input account number here">

                <input type="hidden" name="command" value="transfer_account"/>
                <input type="submit"  name="push" value="transfer" />



</form>
</br>
${message_user_page}
</body>
</html>
