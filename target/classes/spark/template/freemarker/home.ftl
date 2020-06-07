<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">

  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->
    <#if players??>
        <u>Current Online Players</u>
        <ul>
            <#list players as player>
            <li><a href="/game?name=${player.name}" name="userName">${player.name}</a></li>
           </#list>
        </ul>
    <#else>
        <b>${playersSignedIn}</b>
    </#if>

  </div>

</div>
</body>

</html>
