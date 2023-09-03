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
<br/>
<form action="${pageContext.request.contextPath}/controller" method="post">
             <input type="hidden" name="command" value="register_command"/>
             Name:<input type="text" name="user_name" placeholder="input your name here">
         <br/>
             Surname:
             <input type="text" name="user_surname" placeholder="input your surname here">
    <br/>
             Email:
             <input type="text" name="user_email" placeholder="input your email here">
    <br/>
             Password:
             <input type="text" name="user_password" placeholder="input password here">
    <br/>
             <input type="submit"  name="push" value="register" />
</form>
<br/>
<form action="${pageContext.request.contextPath}/controller" >
    <input type="hidden" name="command" value="back_to_index_page"/>
    <input type="submit"  name="push" value="back" />
</form>
</body>
</html>
