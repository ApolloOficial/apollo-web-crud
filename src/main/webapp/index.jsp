<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
<h1>Login de Usuário</h1>
<form action="login" method="POST">
    <table>
        <tr>
            <td><input type="text" name="email" placeholder="Email" required></td>
        </tr>
        <tr>
            <td><input type="text" name="cpf" placeholder="CPF" required></td>
        </tr>
        <tr>
            <td><input type="password" name="senha" placeholder="Senha" required></td>
        </tr>
        <tr>
            <td><button type="submit">Entrar</button></td>
        </tr>
    </table>
    <p>Novo acesso?</p>
    <a href="cadastro">Clique aqui</a>
</form>
<c:if test="${not empty ErroNoLogin}">
    <script>
        alert("Não foi possível realizar o login\nAlgum destes dados está incorreto")
    </script>
</c:if>
</body>