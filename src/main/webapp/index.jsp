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
         Email:<br/>
        <input type="text" name="email" placeholder="please write your e-mail here">
        <br/> Password :<br/>
        <input type="password" name="password" placeholder="please write your password here">
         <br/>
               ${errorLoginPassMessage}
         <br/>
               ${wrongAction}
         <br/>
               ${nullPage}
        <input type="submit" class="button" name="push" value="login" />
</form>
</body>
</html>