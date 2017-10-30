<#-- @ftlvariable name="draw" type="de.ahus1.lottery.domain.Draw" -->
<#-- @ftlvariable name="idToken" type="org.keycloak.representations.IDToken" -->
<html>
<head><title>Lottery Calculator</title>

<body>

<p>
<#-- tag::principal[] -->
Hello, ${idToken.name?html}!
(<a href="logout">logout</a>)
<#-- end::principal[] -->

</p>

<form name="user" action="" method="post">
    Date: <input type="text" name="date"/> <br/>
    <input type="submit" value="Draw!"/>
</form>

<br />

<#if draw??>
<b>Draw for ${draw.date}</b>
<br>
The lucky numbers are:<br>
    <#list draw.numbers as number>
    ${number}<br>
    </#list>
</#if>
</body>
</html>
