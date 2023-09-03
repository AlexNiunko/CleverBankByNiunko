<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 03.09.2023
  Time: 22:55
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1> Please choice the number of bank</h1>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="create_account"/>
    <ol>
<c:forEach  var="bank"  items="${banks}">
            <li> ${bank}</li> <br/>
</c:forEach>
    </ol>
    <input type="text" name="bank" placeholder="input number of bank here"
                required pattern="[1-5]"   \>
    <h2> Please choice the number currency</h2>

    <ol>
        <c:forEach  var="currency"  items="${currencies}">
            <li> ${currency}</li> <br/>
        </c:forEach>
    </ol>
    <input type="text" name="currency" placeholder="input number of bank here"
           required pattern="[1-4]"   \>
    <br/>
    <input type="submit"  name="push" value="create" />

</form>
</body>
</html>
