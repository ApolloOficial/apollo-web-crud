<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo - Contrato</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Arquivos do projeto (mesma pasta) -->
    <link rel="stylesheet" href="../../docs/css/apollostyle.css">
    <link rel="stylesheet" href="../../docs/css/logoApollo.css">
    <link rel="stylesheet" href="../../docs/css/contrato.css">
    <link rel="stylesheet" href="../../docs/css/sidebar.css">
</head>
<body>
<div class="pagina">

    <!-- ===== Sidebar (montada por sidebar.js conforme o tipo de usuário) ===== -->
    <aside class="sidebar" data-ativo="contrato"></aside>
    <script src="../../docs/js/sidebar.js"></script>

    <main class="conteudo">

        <header class="topo">
            <div>
                <h1 class="titulo">Contrato</h1>
                <p class="subtitulo-pagina">Gerencie seus contratos. O contrato define o período de vigência, o valor mensal e o status da sua prestação de serviço.</p>
            </div>
            <div class="usuario">
                <span class="avatar-usuario" aria-hidden="true">CM</span>
                <span>Carlos Menezes</span>
            </div>
        </header>

        <div class="contrato-grade">

            <!-- Somente leitura: os dados chegam após o login (data-contrato no <form> ou preencherContrato({...}) no contrato.js) -->
            <form class="cartao" id="form-contrato" data-contrato="">
                <h2 class="cartao-titulo">
                    <span class="icone-caixa"><svg viewBox="0 0 24 24"><rect x="4" y="3" width="16" height="18" rx="1"/><path d="M9 7h2M13 7h2M9 11h2M13 11h2M9 21v-4h6v4"/></svg></span>
                    Dados da empresa
                </h2>

                <div class="form-grade">
                    <label class="campo"><span>Empresa</span>
                        <input type="text" name="empresa" readonly tabindex="-1" aria-label="Empresa">
                    </label>
                    <label class="campo"><span>Contratante</span>
                        <input type="text" name="contratante" readonly tabindex="-1" aria-label="Contratante">
                    </label>
                    <label class="campo"><span>Valor mensal</span>
                        <input type="text" name="valor" readonly tabindex="-1" aria-label="Valor mensal">
                    </label>
                    <label class="campo"><span>Data de início</span>
                        <input type="text" name="inicio" readonly tabindex="-1" aria-label="Data de início">
                    </label>
                    <label class="campo"><span>Status</span>
                        <input type="text" name="status" readonly tabindex="-1" aria-label="Status">
                    </label>
                </div>

                <section class="resumo" aria-label="Resumo do contrato">
                    <h3 class="cartao-titulo">
                        <span class="icone-caixa"><svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="9"/><path d="M12 11v5M12 8h.01"/></svg></span>
                        Resumo do contrato
                    </h3>
                    <dl class="resumo-lista" aria-live="polite">
                        <dt>Empresa</dt><dd data-resumo="empresa"></dd>
                        <dt>Contratante</dt><dd data-resumo="contratante"></dd>
                        <dt>Valor mensal</dt><dd data-resumo="valor"></dd>
                        <dt>Data de início</dt><dd data-resumo="inicio"></dd>
                        <dt>Status</dt><dd data-resumo="status"></dd>
                    </dl>
                    <div class="resumo-marca marca-apollo" aria-hidden="true"><i class="m1"></i><i class="m2"></i></div>
                </section>
            </form>

            <blockquote class="frase">"Menos estimativa, mais resultado. Renove seu contrato e siga gerindo seus ativos solares com dados reais."</blockquote>

        </div>
    </main>
</div>

<script src="../../docs/js/contrato.js"></script>
</body>
</html>