<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1>Welcome
</h1>
<br/>

<form action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="command" value="login"/>
         Email:
        <input type="text" name="email" placeholder="please write your e-mail here">
    <br/>
         Password :
        <input type="password" name="password" placeholder="please write your password here">
    <br/>
        <input type="submit" class="button" name="push" value="login" />
</form>
<form action="${pageContext.request.contextPath}/controller" >
    <input type="hidden" name="command" value="go_to_registration_page"/>
    <input type="submit"  name="push" value="registration" />
</form>
<br/>
${index_user_page}
<br/>

</body>
</html>