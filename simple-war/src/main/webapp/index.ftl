<html>
<head><title>Lottery Calculator</title>

<body>
<form name="user" action="" method="post">
    Date: <input type="text" name="date"/> <br/>
    <input type="submit" value="Draw!"/>
</form>

<#if draw??>
<b>Draw for ${draw.date}</b>
<br>
The lucky numbers are:<br>
    <#list draw.numbers as number>
    ${number}<br>
    </#list>
</table>
</#if>
</body>
</html>
