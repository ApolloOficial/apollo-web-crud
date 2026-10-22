<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo - Empresa</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, Helvetica, sans-serif;
        }

        body {
            background: #f8f8f8;
            color: #111;
            min-height: 100vh;
        }



        .pagina {
            display: flex;
            min-height: 100vh;
        }


        .sidebar {
            width: 245px;
            background: #ffffff;
            border-right: 1px solid #e5e5e5;
            padding: 30px 30px;
            flex-shrink: 0;
        }

        .logo {
            margin-bottom: 45px;
            padding-left: 5px;
        }

        .logo span {
            font-size: 34px;
            font-weight: 500;
            color: #ff932f;
            letter-spacing: -2px;
        }


        .menu {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .menu-item {
            height: 48px;
            border-radius: 25px;

            display: flex;
            align-items: center;

            padding: 0 18px;

            text-decoration: none;
            color: #222;

            font-size: 15px;

            transition: 0.2s;
        }

        .menu-item:hover {
            background: #fff3e9;
        }

        .menu-item.ativo {
            background: #ff9633;
            color: white;
        }

        .menu-baixo {
            margin-top: 55px;
        }

        .conteudo {
            flex: 1;
            padding: 32px 38px;
            min-width: 0;
        }

        .topo {
            display: flex;
            justify-content: space-between;
            align-items: center;

            margin-bottom: 35px;
        }

        .titulo {
            font-size: 29px;
            font-weight: 700;
        }

        .usuario {
            display: flex;
            align-items: center;
            gap: 10px;

            font-size: 13px;
            font-weight: 600;
        }

        .seta {
            font-size: 10px;
        }

        .status-titulo {
            display: inline-block;

            border: 1px solid #ff4f4f;

            color: #ff3030;

            border-radius: 20px;

            padding: 3px 12px;

            font-size: 12px;

            margin-bottom: 17px;
        }


        .cards-status {
            width: 100%;

            background: white;

            border-radius: 10px;

            display: grid;
            grid-template-columns: repeat(4, 1fr);

            align-items: center;

            padding: 13px 25px;

            margin-bottom: 32px;
        }

        .status-card {
            display: flex;
            align-items: center;

            min-height: 60px;
        }

        .status-info {
            display: flex;
            flex-direction: column;

            gap: 5px;
        }

        .status-info .nome {
            font-size: 15px;
            color: #151515;
        }

        .status-info .numero {
            font-size: 15px;
            font-weight: 500;
        }

        .area-membros {
            background: white;

            border-radius: 10px;

            padding: 16px 15px 20px;
        }


        .barra-superior {
            display: flex;

            align-items: center;
            justify-content: space-between;

            margin-bottom: 13px;
        }

        .busca-area {
            display: flex;
            align-items: center;

            gap: 13px;
        }



        .busca {
            width: 337px;
            height: 34px;

            border: 1px solid #222;

            border-radius: 20px;

            display: flex;
            align-items: center;

            padding: 0 13px;

            gap: 10px;
        }

        .busca input {
            width: 100%;

            border: none;
            outline: none;

            font-size: 12px;
        }

        .busca input::placeholder {
            color: #222;
        }


        .lupa {
            width: 14px;
            height: 14px;

            border: 2px solid #111;

            border-radius: 50%;

            position: relative;

            flex-shrink: 0;
        }

        .lupa::after {
            content: "";

            position: absolute;

            width: 7px;
            height: 2px;

            background: #111;

            right: -6px;
            bottom: -3px;

            transform: rotate(45deg);

            border-radius: 2px;
        }

        .filtro {
            width: 32px;
            height: 32px;

            background: transparent;

            border: none;

            font-size: 17px;

            cursor: pointer;

            transform: rotate(90deg);
        }

        .botao-adicionar {
            border: none;

            background: #ff6e2f;

            color: white;

            padding: 11px 17px;

            border-radius: 20px;

            font-size: 12px;

            cursor: pointer;

            transition: 0.2s;
        }

        .botao-adicionar:hover {
            background: #f45d1b;
        }


        .tabela-container {
            width: 100%;

            overflow-x: auto;
        }

        table {
            width: 100%;

            border-collapse: collapse;

            table-layout: fixed;
        }

        th {
            height: 42px;

            font-size: 12px;
            font-weight: 700;

            text-align: left;

            border-bottom: 1px solid #ddd;

            padding: 0 12px;
        }

        td {
            height: 47px;

            font-size: 11px;

            padding: 0 12px;

            border-bottom: 1px solid #ddd;

            white-space: nowrap;
        }


        th:nth-child(1) {
            width: 15%;
        }

        th:nth-child(2) {
            width: 27%;
        }

        th:nth-child(3) {
            width: 17%;
        }

        th:nth-child(4) {
            width: 18%;
        }

        th:nth-child(5) {
            width: 10%;
        }

        .cargo {
            display: inline-block;

            padding: 3px 8px;

            border-radius: 10px;

            font-size: 9px;
        }

        .engenheiro {
            background: #bdd5ff;
            color: #6399e0;
        }

        .tecnico {
            background: #f3dd9b;
            color: #836524;
        }


        .perfil {
            border: none;

            background: #f5f5f5;

            color: #a9a9a9;

            border-radius: 18px;

            padding: 8px 20px;

            font-size: 12px;

            cursor: pointer;

            transition: 0.2s;
        }

        .perfil:hover {
            background: #eeeeee;
            color: #555;
        }

        @media (max-width: 1000px) {

            .sidebar {
                width: 190px;
                padding: 25px 18px;
            }

            .conteudo {
                padding: 25px;
            }

            .cards-status {
                grid-template-columns: repeat(2, 1fr);

                gap: 15px;
            }
        }


        @media (max-width: 750px) {

            .sidebar {
                width: 160px;
                padding: 25px 12px;
            }

            .logo span {
                font-size: 25px;
            }

            .menu-item {
                padding: 0 13px;
                font-size: 13px;
            }

            .conteudo {
                padding: 20px;
            }

            .cards-status {
                grid-template-columns: 1fr;
            }

            .titulo {
                font-size: 24px;
            }

            .busca {
                width: 250px;
            }
        }


        .topo-texto {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        .subtitulo {
            font-size: 12px;
            color: #666;
            max-width: 400px;
            line-height: 1.4;
        }


        .card-form {
            background: white;
            border: 1px solid #e5e5e5;
            border-radius: 25px;

            display: grid;
            grid-template-columns: 1fr 220px;

            padding: 25px 35px 22px 40px;
        }

        .form-coluna {
            padding-right: 10px;
        }

        .form-coluna + .logo-coluna {
            border-left: 1px solid #ddd;
        }

        .secao-titulo {
            display: flex;
            align-items: center;
            gap: 10px;

            font-size: 15px;
            font-weight: 500;

            margin-bottom: 22px;
        }

        .secao-titulo::after {
            content: "";
            flex: 1;
            height: 1px;
            background: #ffb866;
        }

        .secao-titulo + .grade {
            margin-bottom: 26px;
        }

        .grade {
            display: grid;
            grid-template-columns: repeat(12, 1fr);
            gap: 8px 18px;
        }

        .col-4  { grid-column: span 4; }
        .col-6  { grid-column: span 6; }
        .col-12 { grid-column: span 12; }


        .campo {
            display: flex;
            flex-direction: column;
            gap: 5px;
            min-width: 0;
        }

        .campo label {
            font-size: 12px;
            font-weight: 600;
        }

        .campo input,
        .campo select,
        .campo textarea {
            width: 100%;

            border: 1px solid #ff9633;
            border-radius: 20px;
            background: white;

            padding: 0 12px;
            height: 26px;

            font-size: 10px;
            color: #222;

            outline: none;
            transition: 0.2s;
        }

        .campo textarea {
            height: 52px;
            padding: 8px 12px;
            resize: none;
            border-radius: 12px;
        }

        .campo input::placeholder,
        .campo textarea::placeholder {
            color: #888;
        }

        .campo input:focus,
        .campo select:focus,
        .campo textarea:focus {
            border-color: #ff6e2f;
            box-shadow: 0 0 0 3px #ffe3cf;
        }

        .campo input:disabled {
            background: #ececec;
            border-color: #ddd;
            color: #777;
            cursor: not-allowed;
        }


        .logo-coluna {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 12px;

            padding-left: 22px;
        }

        .logo-coluna > span {
            font-size: 12px;
            font-weight: 600;
            text-align: center;
        }

        .upload {
            width: 138px;
            height: 128px;

            border: 1px solid #ddd;
            border-radius: 18px;

            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 6px;

            text-align: center;
            font-size: 9px;
            color: #222;

            padding: 10px;
            cursor: pointer;
            transition: 0.2s;
        }

        .upload:hover {
            border-color: #ff9633;
            background: #fff8f2;
        }

        .upload input {
            display: none;
        }

        .upload-mais {
            width: 34px;
            height: 34px;
            border: 2px solid #777;
            border-radius: 8px;
            position: relative;
            margin-bottom: 4px;
        }

        .upload-mais::before,
        .upload-mais::after {
            content: "";
            position: absolute;
            background: #777;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
        }

        .upload-mais::before { width: 14px; height: 2px; }
        .upload-mais::after  { width: 2px; height: 14px; }

        .upload small {
            font-size: 8px;
            color: #555;
            margin-top: 6px;
        }

        .acoes-form {
            grid-column: 1 / -1;

            display: flex;
            justify-content: flex-end;
            gap: 22px;

            margin-top: 18px;
        }

        .botao-cancelar {
            border: 1px solid #ff6e2f;
            background: white;
            color: #ff6e2f;

            padding: 11px 38px;
            border-radius: 20px;

            font-size: 12px;
            cursor: pointer;
            transition: 0.2s;
        }

        .botao-cancelar:hover {
            background: #fff3e9;
        }

        .acoes-form .botao-adicionar {
            padding: 11px 38px;
        }

        .sidebar {
            display: flex;
            flex-direction: column;
        }

        .sidebar .menu-baixo {
            margin-top: auto;
        }

        @media (max-width: 1000px) {

            .card-form {
                grid-template-columns: 1fr;
            }

            .form-coluna + .logo-coluna {
                border-left: none;
                border-top: 1px solid #ddd;
                padding: 20px 0 0;
                margin-top: 10px;
            }
        }

        @media (max-width: 750px) {

            .card-form {
                padding: 20px;
            }

            .col-4,
            .col-6 {
                grid-column: span 12;
            }

            .acoes-form {
                justify-content: stretch;
            }

            .acoes-form button {
                flex: 1;
            }
        }

        body.tela-string {
            background: #f7f9ff;
        }

        .tela-string .sidebar {
            position: relative;
            overflow: hidden;
            padding: 30px 24px;
        }

        .tela-string .sidebar::before {
            content: "";
            position: absolute;
            left: 0;
            bottom: 0;
            width: 100%;
            height: 280px;
            background: #fdf1e6;
            clip-path: polygon(0 0, 0 100%, 100% 100%);
            z-index: 0;
        }

        .tela-string .sidebar > * {
            position: relative;
            z-index: 1;
        }

        .tela-string .logo {
            margin-bottom: 40px;
        }

        .tela-string .menu-item {
            gap: 10px;
            height: 44px;
            font-size: 14px;
        }

        .tela-string .menu-item:not(.ativo):not(.menu-baixo) {
            border: 1px solid #eee;
            background: white;
        }

        .tela-string .menu-item.ativo {
            box-shadow: 0 3px 10px rgba(255, 150, 51, 0.35);
        }

        .tela-string .menu-baixo {
            margin-top: auto;
            padding: 0 10px;
        }

        .icone {
            width: 20px;
            height: 20px;
            flex-shrink: 0;
        }


        .tela-string .topo {
            align-items: flex-start;
            margin-bottom: 22px;
        }

        .tela-string .subtitulo {
            font-size: 12px;
            max-width: 430px;
        }

        .tela-string .usuario {
            gap: 12px;
        }

        .sino {
            width: 16px;
            height: 16px;
            margin-right: 6px;
        }

        .avatar {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: linear-gradient(135deg, #b8c0cc, #6b7686);
            border: 2px solid #e2e2e2;
        }


        .layout-string {
            display: grid;
            grid-template-columns: 1fr 330px;
            gap: 14px;
            align-items: start;
        }

        .card-string,
        .card-resumo {
            background: white;
            border: 1px solid #e5e5e5;
            border-radius: 12px;
        }

        .card-string {
            min-height: 360px;
            padding: 18px 16px 22px;

            display: flex;
            flex-direction: column;
        }

        .card-string .secao-titulo {
            font-size: 13px;
            font-weight: 700;
            margin-bottom: 18px;
        }

        .icone-secao {
            width: 24px;
            height: 24px;
            border-radius: 7px;
            background: #ffe9d8;
            padding: 4px;
        }

        .card-string .grade {
            gap: 18px 24px;
        }

        .card-string .campo {
            gap: 7px;
        }

        .card-string .campo label {
            font-size: 11px;
        }

        .card-string .campo input {
            height: 34px;
            padding: 0 16px;
            font-size: 12px;

            border: 1px solid #e0e0e0;
            border-radius: 20px;
        }

        .card-string .campo input:focus {
            border-color: #ff9633;
        }

        .card-string .acoes-form {
            margin-top: auto;
            padding-top: 24px;
            gap: 14px;
        }

        .card-string .botao-cancelar,
        .card-string .acoes-form .botao-adicionar {
            padding: 10px 30px;
        }


        .card-resumo {
            padding: 16px 18px 22px;
        }

        .resumo-titulo {
            display: flex;
            align-items: center;
            gap: 10px;

            font-size: 12px;
            font-weight: 700;

            margin-bottom: 16px;
        }

        .icone-info {
            width: 22px;
            height: 22px;
            border-radius: 50%;
            border: 2px solid #ff6e2f;
            color: #ff6e2f;

            display: flex;
            align-items: center;
            justify-content: center;

            font-size: 12px;
            font-weight: 700;
        }

        .resumo-lista {
            list-style: none;
            display: flex;
            flex-direction: column;
            gap: 10px;

            padding-left: 32px;

            font-size: 11px;
            font-weight: 700;
        }


        @media (max-width: 1000px) {
            .layout-string {
                grid-template-columns: 1fr;
            }
        }

        .tela-empresa .conteudo {
            padding: 32px 38px 40px;
        }

        .tela-empresa .titulo {
            font-size: 29px;
        }

        .tela-empresa .topo {
            margin-bottom: 38px;
        }

        .avatar-escuro {
            background: linear-gradient(135deg, #5a4a40, #1c1c1c);
        }

        .tela-empresa .usuario {
            font-size: 11px;
            font-weight: 700;
        }

        .card-empresa {
            background: white;
            border: 1px solid #e5e5e5;
            border-radius: 20px;
            overflow: hidden;

            margin-bottom: 22px;
        }

        .empresa-topo {
            display: flex;
            align-items: center;
            justify-content: space-between;

            background: #fff6ef;
            padding: 12px 24px 12px 30px;
        }

        .empresa-id {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .empresa-logo {
            width: 52px;
            height: 52px;
            background: white;
            border-radius: 10px;

            display: flex;
            align-items: center;
            justify-content: center;

            font-size: 14px;
            color: #555;
            letter-spacing: 0.5px;
        }

        .empresa-nome {
            font-size: 15px;
            font-weight: 600;
        }

        .botao-editar {
            display: flex;
            align-items: center;
            gap: 6px;

            background: white;
            color: #ff6e2f;
            border: 1px solid #ff6e2f;
            border-radius: 20px;

            padding: 6px 14px;
            font-size: 9px;
            font-weight: 600;

            cursor: pointer;
            transition: 0.2s;
        }

        .botao-editar:hover {
            background: #fff3e9;
        }

        .botao-editar svg {
            width: 11px;
            height: 11px;
        }

        .empresa-dados {
            display: grid;
            grid-template-columns: repeat(4, 1fr);

            padding: 22px 0 24px;
        }

        .dado {
            display: flex;
            flex-direction: column;
            gap: 8px;

            padding: 4px 24px 4px 46px;
            border-left: 1px solid #e3e3e3;
        }

        .dado:first-child {
            border-left: none;
        }

        .dado-rotulo {
            font-size: 11px;
            font-weight: 600;
            color: #333;
        }

        .dado-valor {
            font-size: 10px;
            font-weight: 600;
            line-height: 1.4;
            color: #222;
        }


        .filiais-titulo {
            display: flex;
            align-items: center;
            gap: 8px;

            font-size: 15px;
            font-weight: 500;

            margin-bottom: 16px;
        }

        .filiais-titulo svg {
            width: 22px;
            height: 22px;
        }

        .lista-filiais {
            display: flex;
            flex-direction: column;
            gap: 12px;

            margin-bottom: 20px;
        }

        .filial {
            display: flex;
            align-items: center;
            gap: 16px;

            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 16px;

            padding: 14px 20px 14px 16px;
        }

        .filial-icone {
            width: 46px;
            height: 46px;
            border-radius: 10px;
            background: #ffd6b0;

            display: flex;
            align-items: center;
            justify-content: center;

            flex-shrink: 0;
        }

        .filial-icone svg {
            width: 24px;
            height: 24px;
        }

        .filial-info {
            flex: 1;

            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .filial-info strong {
            font-size: 11px;
        }

        .filial-info span {
            font-size: 9px;
            line-height: 1.4;
            color: #333;
        }

        .botao-detalhes {
            display: flex;
            align-items: center;
            gap: 8px;

            background: white;
            color: #222;
            border: 1px solid #ccc;
            border-radius: 20px;

            padding: 8px 16px;
            font-size: 10px;
            font-weight: 600;

            cursor: pointer;
            transition: 0.2s;
        }

        .botao-detalhes:hover {
            background: #fff3e9;
            border-color: #ff9633;
        }

        .botao-detalhes svg {
            width: 18px;
            height: 18px;
        }

        .botao-mais {
            display: flex;
            flex-direction: column;
            gap: 3px;

            background: transparent;
            border: none;
            padding: 6px 8px;
            cursor: pointer;
        }

        .botao-mais span {
            width: 3px;
            height: 3px;
            border-radius: 50%;
            background: #444;
        }


        .nova-filial {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 10px;

            height: 118px;

            border: 1px dashed #c9c9c9;
            border-radius: 16px;

            text-decoration: none;
            color: #111;

            font-size: 12px;
            font-weight: 600;

            transition: 0.2s;
        }

        .nova-filial:hover {
            border-color: #ff9633;
            background: #fff8f2;
        }

        .nova-filial svg {
            width: 30px;
            height: 30px;
        }

        @media (max-width: 1000px) {
            .empresa-dados {
                grid-template-columns: repeat(2, 1fr);
                row-gap: 18px;
            }

            .dado:nth-child(3) {
                border-left: none;
            }
        }

        @media (max-width: 750px) {
            .empresa-topo {
                flex-direction: column;
                align-items: flex-start;
                gap: 12px;
            }

            .empresa-dados {
                grid-template-columns: 1fr;
            }

            .dado {
                border-left: none;
                padding-left: 30px;
            }

            .filial {
                flex-wrap: wrap;
            }
        }
    </style>
</head>
<body class="tela-string tela-empresa">

<div class="pagina">

    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="logo">
            <span>apollo</span>
        </div>

        <nav class="menu">
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="5" y="3" width="14" height="18" rx="1"/><path d="M9 7h2M13 7h2M9 11h2M13 11h2M10 21v-4h4v4"/></svg>
                Empresas
            </a>
            <a href="#" class="menu-item ativo">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="8" r="3"/><circle cx="17" cy="9" r="2.3"/><path d="M3 20c0-3.3 2.7-6 6-6s6 2.7 6 6M15.5 14.3c3 0 5.5 2.2 5.5 5"/></svg>
                Usuários
            </a>
        </nav>

        <a href="#" class="menu-item menu-baixo">
            <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.7 1.7 0 0 0 .3 1.8l.1.1a2 2 0 1 1-2.8 2.8l-.1-.1a1.7 1.7 0 0 0-1.8-.3 1.7 1.7 0 0 0-1 1.5V21a2 2 0 1 1-4 0v-.1a1.7 1.7 0 0 0-1.1-1.5 1.7 1.7 0 0 0-1.8.3l-.1.1a2 2 0 1 1-2.8-2.8l.1-.1a1.7 1.7 0 0 0 .3-1.8 1.7 1.7 0 0 0-1.5-1H3a2 2 0 1 1 0-4h.1a1.7 1.7 0 0 0 1.5-1.1 1.7 1.7 0 0 0-.3-1.8l-.1-.1a2 2 0 1 1 2.8-2.8l.1.1a1.7 1.7 0 0 0 1.8.3H9a1.7 1.7 0 0 0 1-1.5V3a2 2 0 1 1 4 0v.1a1.7 1.7 0 0 0 1 1.5 1.7 1.7 0 0 0 1.8-.3l.1-.1a2 2 0 1 1 2.8 2.8l-.1.1a1.7 1.7 0 0 0-.3 1.8V9a1.7 1.7 0 0 0 1.5 1H21a2 2 0 1 1 0 4h-.1a1.7 1.7 0 0 0-1.5 1z"/></svg>
            Configurações
        </a>
    </aside>

    <!-- CONTEÚDO -->
    <main class="conteudo">

        <header class="topo">
            <div class="topo-texto">
                <h1 class="titulo">Empresa</h1>
                <p class="subtitulo">Visualize as informações da sua empresa e suas filiais cadastradas</p>
            </div>

            <div class="usuario">
                <span class="avatar avatar-escuro"></span>
                <span>ADM DOS BOL TUDO</span>
                <span class="seta">▼</span>
            </div>
        </header>

        <!-- CARD DA EMPRESA -->
        <section class="card-empresa">

            <div class="empresa-topo">
                <div class="empresa-id">
                    <div class="empresa-logo">Âmbar</div>
                    <h2 class="empresa-nome">Âmbar Energia</h2>
                </div>

                <button type="button" class="botao-editar">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 20h9M16.5 3.5a2.1 2.1 0 0 1 3 3L7 19l-4 1 1-4z"/></svg>
                    Editar empresa
                </button>
            </div>

            <div class="empresa-dados">
                <div class="dado">
                    <span class="dado-rotulo">CNPJ</span>
                    <span class="dado-valor">11.111.111/0001-11</span>
                </div>
                <div class="dado">
                    <span class="dado-rotulo">Email</span>
                    <span class="dado-valor">Ambar@gmail.com.br</span>
                </div>
                <div class="dado">
                    <span class="dado-rotulo">Telefone</span>
                    <span class="dado-valor">(11) 99999-9999</span>
                </div>
                <div class="dado">
                    <span class="dado-rotulo">Endereço</span>
                    <span class="dado-valor">Av. das nações, 123 - centro<br>São Paulo - SP, 00000-000</span>
                </div>
            </div>
        </section>

        <!-- FILIAIS -->
        <h2 class="filiais-titulo">
            <svg viewBox="0 0 24 24" fill="none" stroke="#ff9633" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="6" cy="5" r="2.2"/><circle cx="18" cy="5" r="2.2"/><circle cx="18" cy="19" r="2.2"/><path d="M6 7.5V12a3 3 0 0 0 3 3h6.5M18 7.5v9"/></svg>
            Filiais cadastradas
        </h2>

        <div class="lista-filiais">

            <article class="filial">
                <span class="filial-icone">
                    <svg viewBox="0 0 24 24" fill="none" stroke="#e5302b" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 10c0 6-8 12-8 12S4 16 4 10a8 8 0 0 1 16 0z"/><circle cx="12" cy="10" r="3"/></svg>
                </span>

                <div class="filial-info">
                    <strong>Matriz</strong>
                    <span>Av. das nações, 123 - centro<br>São Paulo - SP, 00000-000</span>
                </div>

                <button type="button" class="botao-detalhes">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"/><circle cx="12" cy="12" r="3"/></svg>
                    Ver detalhes
                </button>

                <button type="button" class="botao-mais" aria-label="Mais opções">
                    <span></span><span></span><span></span>
                </button>
            </article>

            <article class="filial">
                <span class="filial-icone">
                    <svg viewBox="0 0 24 24" fill="none" stroke="#e5302b" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 10c0 6-8 12-8 12S4 16 4 10a8 8 0 0 1 16 0z"/><circle cx="12" cy="10" r="3"/></svg>
                </span>

                <div class="filial-info">
                    <strong>Filial centro</strong>
                    <span>Rua da consolação, 456 - centro<br>São Paulo - SP, 00000-000</span>
                </div>

                <button type="button" class="botao-detalhes">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"/><circle cx="12" cy="12" r="3"/></svg>
                    Ver detalhes
                </button>

                <button type="button" class="botao-mais" aria-label="Mais opções">
                    <span></span><span></span><span></span>
                </button>
            </article>

        </div>

        <!-- ADICIONAR FILIAL -->
        <a href="#" class="nova-filial">
            <svg viewBox="0 0 24 24" fill="none" stroke="#ff7a1a" stroke-width="1.6" stroke-linecap="round"><circle cx="12" cy="12" r="10.5"/><path d="M12 7v10M7 12h10"/></svg>
            <span>Adicionar nova filial</span>
        </a>

    </main>
</div>
</body>
</html>
