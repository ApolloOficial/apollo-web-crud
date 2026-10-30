<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teste</title>
</head>
<body>
<h1>Cadastrar usuário</h1>
<form action="cadastro" method="POST">
    <table>
        <tr>
            <td><input type="text" name="nome" value="${param.nome}" placeholder="Nome"></td>
        </tr>
        <tr>
            <td><input type="text" name="email" value="${param.email}" placeholder="E-mail" required></td>
        </tr>
        <tr>
            <td><input type="text" name="cpf" value="${param.cpf}" placeholder="CPF" required></td>
        </tr>
        <tr>
            <td><input type="password" name="senha" placeholder="Senha" required></td>
        </tr>
        <tr>
            <td><input type="text" name="tipo" value="${param.tipo}" placeholder="Tipo"></td>
        </tr>
        <tr>
            <td><input type="text" name="empresa" value="${param.empresa}" placeholder="Empresa"></td>
        </tr>
        <tr>
            <!-- pega uma lista de filiais-->
            <td><input type="text" name="filial" value="${param.filial}" placeholder="Filial"></td>
        </tr>
        <tr>
            <td><input type="text" name="telefone" value="${param.telefone}" placeholder="Telefone"></td>
        </tr>
        <tr>
            <td><input type="text" name="status" value="${param.status}" placeholder="Status"></td>
        </tr>
        <tr>
            <td><input type="text" name="urlFoto" value="${param.urlFoto}" placeholder="URL da foto"></td>
        </tr>
        <tr>
            <td><button type="submit">Cadastrar</button></td>
        </tr>
    </table>
</form>
<c:if test="${not empty ErroNoCadastro}">
    <script>
        window.alert("Erro no Cadastro!\n"+"${ErroNoCadastro}");
    </script>
</c:if>
</body>
</html>
