<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sistema Apollo - Gerenciamento de Usuários</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f9f9f9; }
        h1 { color: #333; }
        .tabela-usuarios { width: 100%; border-collapse: collapse; margin-top: 20px; background-color: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .tabela-usuarios th, .tabela-usuarios td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        .tabela-usuarios th { background-color: #0056b3; color: white; font-weight: bold; }
        .tabela-usuarios tr:nth-child(even) { background-color: #f2f2f2; }
        .tabela-usuarios tr:hover { background-color: #ddd; }
        .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
        .badge-admin { background-color: #d4edda; color: #155724; }
        .badge-user { background-color: #e2e3e5; color: #383d41; }
    </style>
</head>
<body>

<h1>Usuários Cadastrados - Sistema Apollo 🚀</h1>
<p>Abaixo estão listados todos os registros integrados ao banco de dados.</p>

<table class="tabela-usuarios">
    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>E-mail</th>
        <th>CPF</th>
        <th>Senha</th>
        <th>Tipo de Perfil</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="u" items="${usuarios}">
        <tr>
            <td><strong>#${u.id_usuario}</strong></td>
            <td>${u.nome}</td>
            <td>${u.email}</td>
            <td>${u.cpf}</td>
            <td><code>${u.senha}</code></td>
            <td>
                <span class="badge ${u.tipo == 'ADMIN' ? 'badge-admin' : 'badge-user'}">
                        ${u.tipo}
                </span>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
