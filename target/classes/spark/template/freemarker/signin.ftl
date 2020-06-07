<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <title>Sign in</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Sign In</h1>

  <!-- Provide a navigation bar -->
  <#--  <#include "nav-bar.ftl" />  -->
  
  <div class="body">
  <#if (message??)>
  ${message.text}
  </#if>

  <#--  Posting works now :)  -->
  <form action="./signin" method="POST">
        <br/>
        <input name="userName" />
        <br/><br/>
        <button type="submit">Sign In</button>
      </form>
  </div>

</div>
</body>

</html>
