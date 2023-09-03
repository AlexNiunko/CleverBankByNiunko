<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 31.08.2023
  Time: 0:21
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1> ${user.name}  ${user.surname} , please input amount ${account.currency}</h1>
</br>
<form action="${pageContext.request.contextPath}/controller" method="post">
 <table>
     <tr>
         <td><input type="text" name="refill_amount" placeholder="please input refill amount here"> </td>
         <td>
             <input type="hidden" name="command" value="refill_account"/>
             <input type="submit"  name="push" value="refill" />
         </td>
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
                <input type="hidden" name="command" value="back_to_account_page"/>
                <input type="submit"  name="push" value="back" />
            </form>
        </td>
    </tr>
</table>
</body>
</html>
