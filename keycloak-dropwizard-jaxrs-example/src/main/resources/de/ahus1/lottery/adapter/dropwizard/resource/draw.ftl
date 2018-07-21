<#-- @ftlvariable name="" type="de.ahus1.lottery.adapter.dropwizard.resource.DrawView" -->
<html>
<head><title>Lottery Calculator</title>

<body>

<p>
    Hello, ${bean.name}! (<a href="logout" name="logout">logout</a>)
</p>

<form name="draw" action="/draw" method="post">
    Date: <input type="text" name="date"/> <br/>
    <input type="submit" value="Draw!" name="draw"/>
</form>

<#if bean.draw??>
<b>Draw for ${bean.draw.date}</b>
<br>
The lucky numbers are:<br>
    <#list bean.draw.numbers as number>
        ${number}<br>
    </#list>
</#if>
</body>
</html>
