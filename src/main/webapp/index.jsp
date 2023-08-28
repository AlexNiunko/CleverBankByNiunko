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
    <div >
        <input type="text" name="email" placeholder="please write your e-mail here"><div>email</div>
    </div>
    <div >
        <input type="password" name="password" placeholder="please write your password here"><div>password</div>
    </div>
<div>
    <input type="hidden" name="command" value="login"/>
    <input type="submit" class="button" name="push" value="login" />
</div>
</form>
</body>
</html>