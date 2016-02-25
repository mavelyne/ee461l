<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Page Visits</title>
</head>
<body>
<div>
    The message is: ${message}
</div>
<div>
    This page has been visited at these times:
    <ul>
        <#list ticks as tick>
            <li>${tick}</li>
        </#list>
    </ul>
</div>
<div>
    <a href="/deleteall">Delete All</a>
</div>
</body>
</html>