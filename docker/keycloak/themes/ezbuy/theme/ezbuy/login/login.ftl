<#import "template.ftl" as layout>
<#import "components/link/primary.ftl" as linkPrimary>

<@layout.registrationLayout displayInfo=social.displayInfo; section>
    <#if section = "title">
        ${msg("loginTitle",(realm.displayName!''))}
    <#elseif section = "header">
        <link href="https://fonts.googleapis.com/css?family=Muli" rel="stylesheet"/>
        <script>
            function togglePassword() {
                var x = document.getElementById("password");
                var v = document.getElementById("vi");
                if (x.type === "password") {
                    x.type = "text";
                    v.src = "${url.resourcesPath}/img/eye.png";
                } else {
                    x.type = "password";
                    v.src = "${url.resourcesPath}/img/eye-off.png";
                }
            }
        </script>
            <div class="logoyas">
           <img class="logo" src="${url.resourcesPath}/img/ezbuy-logo.png" alt="ezbuy">
        </div>
    <#elseif section = "form">
    
        <div class="box-container">
            <div>
                <p class="application-name">Welcome to Yas store</p>
            </div>
        <#if realm.password>
            <div>
               <form id="kc-form-login" class="form" onsubmit="return true;" action="${url.loginAction}" method="post">
                    <input id="username" class="login-field" placeholder="${msg("username")}" type="text" name="username" tabindex="1">
                    <div>
                        <label class="visibility" id="v" onclick="togglePassword()"><img id="vi" src="${url.resourcesPath}/img/eye-off.png"></label>
                    </div>
                <input id="password" class="login-field" placeholder="${msg("password")}" type="password" name="password" tabindex="2">
                <input class="submit" type="submit" value="${msg("doLogIn")}" tabindex="3">
                </form>
            </div>
            <div class="register">
               <@linkPrimary.kw href=url.registrationUrl>
                    <input class="register" type="button" value="${msg("doRegister")}" tabindex="3" href=url.registrationUrl>
                </@linkPrimary.kw>
            </div>
        </#if>
        <div>
            <p class="copyright">&copy; copyright - ezbuy.vn ${.now?string('yyyy')}</p>
        </div>
    </#if>
</@layout.registrationLayout>
