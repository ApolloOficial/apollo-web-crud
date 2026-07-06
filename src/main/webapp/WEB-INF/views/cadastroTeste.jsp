<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teste</title>
</head>
<body>
<h1>Cadastrar usuário</h1>
<form action="usuarios" method="POST">
    <table>
        <tr>
            <td><input type="text" name="nome" placeholder="Nome"></td>
        </tr>
        <tr>
            <td><input type="text" name="email" placeholder="E-mail"></td>
        </tr>
        <tr>
            <td><input type="text" name="cpf" placeholder="CPF"></td>
        </tr>
        <tr>
            <td><input type="password" name="senha" placeholder="Senha"></td>
        </tr>
        <tr>
            <td><input type="text" name="tipo" placeholder="Tipo"></td>
        </tr>
        <tr>
            <td><button type="submit">Cadastrar</button></td>
        </tr>
    </table>
</form>
</body>
</html>
