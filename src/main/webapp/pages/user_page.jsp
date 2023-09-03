<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 29.08.2023
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<H1> Welcome ${user.name} ${user.surname}</H1>
<br/> ${message_user_page}
<table>
        <tr>
       <td> <form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden"  name="command" value="show_accounts"/>
                <input type="submit"  name="push" value="show_accounts" />
        </form>
                </td>
        <td>
        <form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="command" value="go_create_account_page"/>
                <input type="submit"  name="push" value="create_account" />
        </form>
        </td>
                <td>
        <form action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="command" value="logout"/>
                <input type="submit"  name="push" value="logout" />
        </form>
                </td>

        </tr>
</table>

</body>
</html>
