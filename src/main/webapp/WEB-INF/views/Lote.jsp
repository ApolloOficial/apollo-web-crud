<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo - Lotes</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
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

        .tela-lotes,
        .tela-lotes * {
            font-family: "Poppins", Arial, Helvetica, sans-serif;
        }

        .tela-lotes .conteudo {
            padding: 30px;
        }

        .tela-lotes .topo {
            margin-bottom: 56px;
            padding-left: 9px;
            margin-right: -2px;
        }

        .tela-lotes .topo-texto {
            gap: 2px;
        }

        .tela-lotes .titulo {
            font-size: 25px;
            font-weight: 500;
            line-height: 1.2;
            letter-spacing: -0.04em;
        }

        .tela-lotes .subtitulo {
            max-width: 520px;
            font-size: 10px;
            line-height: 1.4;
            letter-spacing: 0;
            word-spacing: 0.1em;
        }

        .tela-lotes .usuario {
            gap: 6px;
            min-height: 36px;
            margin-top: -6px;
            font-size: 10px;
            font-weight: 600;
            letter-spacing: 0;
            word-spacing: 0.1em;
            text-transform: uppercase;
        }

        .tela-lotes .seta {
            width: 0;
            height: 0;
            margin-left: 22px;
            font-size: 0;
            border-left: 4.5px solid transparent;
            border-right: 4.5px solid transparent;
            border-top: 5px solid #111;
        }

        @media (min-width: 751px) {
            .tela-lotes .sidebar {
                width: 239px;
                padding: 24px 50px 52px 43px;
            }

            .tela-lotes .sidebar::before {
                height: 175px;
                background: linear-gradient(90deg, #fdf0e2, #fff8f0);
            }

            .tela-lotes .logo {
                margin-bottom: 43px;
                padding-left: 0;
            }

            .tela-lotes .menu {
                display: flex;
                flex-direction: column;
                gap: 16px;
            }

            .tela-lotes .menu-item {
                height: 38px;
                gap: 8px;
                padding-left: 13px;
                font-size: 13.5px;
                font-weight: 500;
            }

            .tela-lotes .icone {
                width: 21px;
                height: 21px;
                stroke-width: 2.1;
            }

            .tela-lotes .menu-item.ativo {
                font-weight: 500;
            }

            .tela-lotes .menu-baixo {
                gap: 6px;
                padding-left: 9px;
                font-weight: 500;
            }
        }

        .card-lotes {
            width: 100%;
            max-width: 819px;
            position: relative;
            left: 1px;
            margin: 0 auto;
            padding: 28px 39px 28px 39px;

            background: #ffffff;
            border: 1px solid #e5e5e5;
            border-radius: 28px;
            box-shadow: 0 2px 14px rgba(20, 30, 60, 0.04);
        }

        .card-lotes .secao-titulo {
            gap: 10px;
            margin-bottom: 20px;
            font-size: 15px;
            font-weight: 500;
            line-height: 20px;
            letter-spacing: -0.015em;
        }

        .card-lotes .secao-titulo::after {
            background: #ffc27a;
        }

        .card-lotes .grade {
            align-items: start;
            row-gap: 3px;
        }

        .card-lotes .grade-2 {
            grid-template-columns: repeat(2, 1fr);
            column-gap: 95px;
            margin-bottom: 38px;
        }

        .card-lotes .grade-3 {
            grid-template-columns: repeat(3, 1fr);
            column-gap: 40px;
            margin-bottom: 0;
        }

        .card-lotes .secao-titulo + .grade-3 {
            margin-bottom: 0;
        }

        .card-lotes .grade-2 + .secao-titulo {
            margin-bottom: 14px;
        }

        .card-lotes .campo-largo {
            grid-column: 1 / -1;
        }

        .card-lotes .campo {
            gap: 5px;
        }

        .card-lotes .campo label {
            font-size: 10.5px;
            font-weight: 600;
            line-height: 15px;
            letter-spacing: -0.01em;
        }

        .card-lotes .campo input,
        .card-lotes .campo select {
            width: 100%;
            height: 30px;
            padding: 0 10px;

            font-size: 10px;
            color: #222;

            background-color: #ffffff;
            border: 1px solid #e2e2e2;
            border-radius: 20px;
        }

        .card-lotes .campo input::placeholder {
            color: #8f8f8f;
        }

        .card-lotes .campo input:focus,
        .card-lotes .campo select:focus {
            border-color: #ff9633;
            box-shadow: 0 0 0 3px #ffe3cf;
        }

        .card-lotes .campo input:disabled {
            color: #8a8a8a;
            background: #ebebeb;
            border-color: #dedede;
            cursor: not-allowed;
        }

        .card-lotes .campo select {
            appearance: none;
            -webkit-appearance: none;
            padding-right: 34px;
            font-size: 10px;
            color: #666;
            cursor: pointer;
            background-image: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='%23444' stroke-width='1.8' stroke-linecap='round' stroke-linejoin='round'><polyline points='6 9 12 15 18 9'/></svg>");
            background-repeat: no-repeat;
            background-position: right 10px center;
            background-size: 14px 14px;
        }

        .card-lotes .campo select:valid {
            color: #222;
        }

        .card-lotes .acoes-form {
            justify-content: center;
            gap: 7px;
            margin-top: 14px;
            padding-top: 0;
        }

        .card-lotes .botao-cancelar,
        .card-lotes .acoes-form .botao-adicionar {
            height: 21px;
            padding: 0 20px;
            min-width: 87px;
            font-size: 7.5px;
            font-weight: 500;
            line-height: 1;
        }

        @media (max-width: 1000px) {
            .card-lotes .grade-2 {
                column-gap: 40px;
            }

            .card-lotes .grade-3 {
                grid-template-columns: repeat(2, 1fr);
                column-gap: 30px;
                row-gap: 8px;
            }
        }

        @media (max-width: 750px) {
            .card-lotes {
                padding: 20px;
            }

            .card-lotes .grade-2,
            .card-lotes .grade-3 {
                grid-template-columns: 1fr;
                row-gap: 10px;
            }
        }

        .tela-lotes,
        .tela-lotes .conteudo {
            background: #fbfbfb;
        }

        @media (min-width: 751px) {
            .tela-lotes .sidebar {
                position: sticky;
                top: 0;
                align-self: flex-start;
                width: 242px;
                height: 100vh;
                padding: 34px 49px 39px 48px;
            }

            .tela-lotes .logo {
                margin-bottom: 45px;
            }

            .tela-lotes .menu {
                gap: 19px;
            }
        }

        .tela-lotes .sidebar::before,
        .tela-lotes .sidebar::after {
            display: none;
        }

        .tela-lotes .conteudo {
            padding: 41px 30px 30px 36px;
        }

        .tela-lotes .topo {
            margin-bottom: 12px;
            padding-left: 0;
        }

        .tela-lotes .usuario {
            margin-top: -3px;
            gap: 22px;
            text-transform: none;
        }

        .tela-lotes .sino-box {
            position: relative;
            display: block;
            width: 17px;
            height: 17px;
        }

        .tela-lotes .sino {
            display: block;
            width: 17px;
            height: 17px;
        }

        .tela-lotes .sino-box::after {
            content: "";
            position: absolute;
            top: -1px;
            right: -1px;
            width: 6px;
            height: 6px;
            border-radius: 50%;
            background: #e53935;
        }

        .tela-lotes .titulo {
            display: flex;
            align-items: baseline;
            gap: 8px;
        }

        .tela-lotes .migalha {
            font-size: 13px;
            font-weight: 400;
            letter-spacing: 0;
            color: #666;
        }

        .area-lotes {
            display: flex;
            flex-direction: column;
            gap: 26px;
            max-width: 877px;
        }

        .lote-card {
            position: relative;
            display: flex;
            gap: 15px;
            padding: 12px 12px 11px 15px;
            background: #ffffff;
            border: 1px solid #efefef;
            border-radius: 14px;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
        }

        .foto-lote {
            position: relative;
            flex: 0 0 213px;
            height: 133px;
            overflow: hidden;
            border-radius: 12px;
            background: linear-gradient(180deg, #3e8fdc 0%, #8fc1ee 35%, #cfe3f5 55%, #7da36a 100%);
        }

        .foto-lote i {
            position: absolute;
            inset: 0;
            display: block;
        }

        .f-parede {
            background:
                linear-gradient(#7a5a3a, #7a5a3a) 72% 80% / 9% 15% no-repeat,
                linear-gradient(#7a5a3a, #7a5a3a) 52% 82% / 9% 14% no-repeat,
                linear-gradient(180deg, #efe3c8, #c9b58a);
            clip-path: polygon(9% 100%, 9% 64%, 100% 50%, 100% 100%);
        }

        .f-telhado {
            background:
                repeating-linear-gradient(98deg, rgba(0, 0, 0, 0.14) 0 1.5px, transparent 1.5px 8px),
                linear-gradient(120deg, #e0663a, #b94a1f);
            clip-path: polygon(0 0, 60% 0, 100% 34%, 100% 56%, 14% 80%, 0 86%);
        }

        .f-paineis {
            background:
                repeating-linear-gradient(0deg, rgba(255, 255, 255, 0.18) 0 1px, transparent 1px 11px),
                repeating-linear-gradient(90deg, rgba(255, 255, 255, 0.18) 0 1px, transparent 1px 20px),
                linear-gradient(135deg, #264690 0%, #142a5e 60%, #254287 100%);
            clip-path: polygon(6% 9%, 52% 9%, 86% 36%, 84% 47%, 19% 66%, 6% 70%);
        }

        .f-verde {
            background:
                radial-gradient(circle at 92% 98%, #3d6b2f 0 22%, transparent 23%),
                radial-gradient(circle at 80% 104%, #4f8a3c 0 18%, transparent 19%),
                radial-gradient(circle at 6% 102%, #4a7a37 0 14%, transparent 15%);
        }

        .lote-dir {
            flex: 1;
            display: grid;
            grid-template-columns: 1fr 120px 100px 90px 104px;
            grid-template-rows: 1fr auto;
            min-width: 0;
        }

        .lote-info {
            padding-top: 3px;
        }

        .lote-nome {
            font-size: 14px;
            font-weight: 500;
            line-height: 20px;
        }

        .lote-end {
            margin-top: 6px;
            font-size: 9.5px;
            line-height: 14px;
            color: #555;
        }

        .metrica {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-top: -2px;
        }

        .metrica-rotulo {
            font-size: 14px;
            line-height: 21px;
            color: #333;
        }

        .metrica-valor {
            margin-top: 16px;
            font-size: 15px;
            line-height: 21px;
            color: #222;
        }

        .selo-ativo {
            margin-top: 15px;
            padding: 0 13px;
            font-size: 9px;
            font-weight: 600;
            line-height: 23px;
            color: #3fae52;
            background: #c9f5cb;
            border-radius: 999px;
        }

        .ponto-info {
            display: inline-block;
            width: 11px;
            height: 11px;
            margin-left: 3px;
            font-size: 7px;
            font-style: normal;
            font-weight: 600;
            line-height: 9px;
            text-align: center;
            color: #4a8df0;
            border: 1px solid #4a8df0;
            border-radius: 50%;
            position: relative;
            top: -7px;
        }

        .verde {
            font-weight: 400;
            color: #6bc04b;
        }

        .lote-obs {
            grid-column: 1 / -1;
            display: flex;
            align-self: end;
            gap: 28px;
            font-size: 8.5px;
            line-height: 14px;
            color: #333;
        }

        .lote-obs b {
            font-size: 9px;
            font-weight: 600;
        }

        .lote-fechar {
            position: absolute;
            top: 21px;
            right: 22px;
            display: grid;
            place-items: center;
            width: 28px;
            height: 28px;
            padding: 0;
            color: #555;
            background: none;
            border: 0;
            cursor: pointer;
        }

        .lote-fechar svg {
            width: 28px;
            height: 28px;
        }

        .lote-fechar:hover {
            color: #ff7a1a;
        }

        .resumo-lote {
            display: flex;
            align-items: center;
            justify-content: space-between;
            height: 90px;
            padding: 0 40px;
            background: #ffffff;
            border: 1px solid #efefef;
            border-radius: 14px;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
        }

        .resumo-item {
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .bolha {
            display: grid;
            place-items: center;
            flex: 0 0 52px;
            width: 52px;
            height: 52px;
            color: #ffffff;
            border-radius: 50%;
        }

        .bolha svg {
            width: 28px;
            height: 28px;
        }

        .bolha.azul { background: #3dbff5; }
        .bolha.amarela { background: #fddd35; }
        .bolha.laranja { background: #ff9a1a; }

        .bolha.verde-is {
            font-size: 22px;
            font-weight: 700;
            background: #7ab52c;
        }

        .resumo-txt {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 6px;
        }

        .resumo-txt span {
            font-size: 14px;
            font-weight: 500;
            line-height: 18px;
            white-space: nowrap;
        }

        .resumo-txt strong {
            font-size: 14px;
            font-weight: 400;
            line-height: 18px;
        }

        .grade-placas {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 19px 20px;
        }

        .placa,
        .placa-vazia {
            height: 113px;
            background: #ffffff;
            border: 1px solid #e6e6e6;
            border-radius: 14px;
        }

        .placa {
            display: flex;
            flex-direction: column;
            padding: 3px 14px 6px 24px;
        }

        .placa-topo {
            display: flex;
            align-items: center;
            gap: 10px;
            height: 16px;
            font-size: 10px;
            line-height: 16px;
            white-space: nowrap;
        }

        .placa-topo b {
            font-weight: 700;
        }

        .placa-topo span {
            color: #444;
        }

        .placa-corpo {
            display: grid;
            grid-template-columns: 46px minmax(0, 1fr) 40px;
            column-gap: 10px;
            flex: 1;
            margin-top: 8px;
        }

        .placa-img {
            height: 100%;
            border-radius: 2px;
            background-color: #10131f;
            background-image:
                linear-gradient(#262d49 1px, transparent 1px),
                linear-gradient(90deg, #262d49 1px, transparent 1px);
            background-size: 5px 5px;
        }

        .placa-info {
            padding-top: 6px;
        }

        .placa-info p {
            display: flex;
            gap: 8px;
            font-size: 8px;
            line-height: 17.5px;
            color: #777;
            white-space: nowrap;
        }

        .placa-info b {
            font-weight: 600;
            color: #111;
        }

        .placa-info .placa-material {
            justify-content: flex-end;
            margin-left: -14px;
        }

        .placa-un {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .placa-un strong {
            margin-top: 2px;
            font-size: 28px;
            font-weight: 500;
            line-height: 30px;
        }

        .placa-un span {
            font-size: 12px;
            line-height: 18px;
            color: #777;
        }

        .placa-vazia {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 42px 0 60px;
            font-family: inherit;
            font-size: 14px;
            color: #cfcfcf;
            cursor: pointer;
            transition: color 0.2s, border-color 0.2s;
        }

        .placa-vazia svg {
            width: 47px;
            height: 47px;
        }

        .placa-vazia:hover,
        .placa-vazia:focus-visible {
            color: #ff8a30;
            border-color: #ffc58f;
            outline: none;
        }

        body.modal-aberto {
            overflow: hidden;
        }

        .modal-fundo {
            position: fixed;
            inset: 0;
            z-index: 1000;
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px;
            background: rgba(0, 0, 0, 0.5);
        }

        .modal-fundo.aberto {
            display: flex;
            animation: modal-fade 0.18s ease-out;
        }

        .modal {
            width: min(470px, 100%);
            max-height: 100%;
            overflow: auto;
            padding: 30px 30px 0;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 24px 70px rgba(0, 0, 0, 0.35);
            animation: modal-sobe 0.2s ease-out;
        }

        .modal-linha {
            display: grid;
            grid-template-columns: 165px minmax(0, 1fr);
            align-items: center;
            margin-bottom: 20px;
        }

        .modal-linha label {
            font-size: 13px;
            font-weight: 600;
        }

        .modal-linha input,
        .modal-linha select {
            width: 100%;
            height: 30px;
            padding: 0 10px;
            font-family: inherit;
            font-size: 12px;
            color: #222;
            background-color: #ffffff;
            border: 1px solid #b9b9b9;
            border-radius: 5px;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .modal-linha select {
            appearance: none;
            -webkit-appearance: none;
            padding-right: 32px;
            color: #777;
            cursor: pointer;
            background-image: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='%23444' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><polyline points='6 9 12 15 18 9'/></svg>");
            background-repeat: no-repeat;
            background-position: right 10px center;
        }

        .modal-linha input:focus,
        .modal-linha select:focus {
            border-color: #ff9633;
            box-shadow: 0 0 0 3px #ffe3cf;
        }

        .modal-linha input.invalido {
            border-color: #e53935;
            background-color: #fff7f7;
        }

        .modal-linha input.invalido::placeholder {
            color: #e53935;
        }

        .modal-rodape {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin: 8px -14px 0;
            padding: 18px 14px;
            border-top: 1px solid #dddddd;
        }

        .modal-rodape button {
            all: unset;
            box-sizing: border-box;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            height: 36px;
            padding: 0 22px;
            font-family: inherit;
            font-size: 12px;
            font-weight: 500;
            white-space: nowrap;
            cursor: pointer;
            border-radius: 999px;
            transition: filter 0.2s, background 0.2s;
        }

        .modal-rodape svg {
            width: 14px;
            height: 14px;
        }

        .modal-rodape .botao-cancelar {
            min-width: 122px;
            color: #555;
            background: #ffffff;
            border: 1px solid #b9b9b9;
        }

        .modal-rodape .botao-cancelar:hover {
            background: #f4f4f4;
        }

        .modal-rodape .botao-adicionar {
            min-width: 156px;
            color: #ffffff;
            background: #ff6a2f;
        }

        .modal-rodape .botao-adicionar:hover {
            filter: brightness(1.06);
        }

        .modal-rodape button:focus-visible {
            outline: 2px solid #ff9633;
            outline-offset: 2px;
        }

        @keyframes modal-fade {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        @keyframes modal-sobe {
            from { transform: translateY(12px) scale(0.98); }
            to { transform: none; }
        }

        @media (max-width: 1100px) {
            .grade-placas {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }

            .resumo-lote {
                flex-wrap: wrap;
                height: auto;
                gap: 16px 24px;
                padding: 20px 30px;
            }

            .lote-card {
                flex-direction: column;
            }

            .foto-lote {
                flex-basis: auto;
                width: 100%;
            }

            .lote-dir {
                grid-template-columns: 1fr 90px 90px 70px;
                row-gap: 14px;
            }

            .lote-dir > :nth-child(n+5) {
                grid-column: 1 / -1;
            }
        }

        @media (max-width: 750px) {
            .tela-lotes .conteudo {
                padding: 24px 16px;
            }

            .grade-placas {
                grid-template-columns: minmax(0, 1fr);
            }

            .modal {
                padding: 22px 20px 0;
            }

            .modal-linha {
                grid-template-columns: 1fr;
                row-gap: 6px;
            }

            .modal-rodape {
                flex-wrap: wrap;
            }
        }

        .logo-apollo {
          position: relative;
          display: block;
          width: calc(166.79 * var(--u, 1px));
          height: calc(50.23 * var(--u, 1px));
        }

        .logo-apollo .nome {
          position: absolute;
          left: 0;
          top: calc(4.11 * var(--u, 1px));
          width: calc(122 * var(--u, 1px));
          height: calc(42 * var(--u, 1px));
        }

        .logo-apollo .nome i {
          position: absolute;
          display: block;
          left: calc(var(--x) * var(--u, 1px));
          top: calc(var(--y) * var(--u, 1px));
          width: calc(var(--w) * var(--u, 1px));
          height: calc(var(--h) * var(--u, 1px));
          background: linear-gradient(180deg, #FF672C 14.15%, #FCD83A 95.28%) no-repeat;
          background-size: calc(122 * var(--u, 1px)) calc(42 * var(--u, 1px));
          background-position: calc(var(--x) * var(--u, 1px) * -1) calc(var(--y) * var(--u, 1px) * -1);
        }

        .logo-apollo .nome .a-anel,
        .logo-apollo .nome .p-anel,
        .logo-apollo .nome .o1-anel,
        .logo-apollo .nome .o2-anel {
          border-radius: 50%;
          -webkit-mask: radial-gradient(circle at center, transparent calc(var(--ri) * var(--u, 1px)), #000 calc(var(--ri) * var(--u, 1px) + 0.5px));
                  mask: radial-gradient(circle at center, transparent calc(var(--ri) * var(--u, 1px)), #000 calc(var(--ri) * var(--u, 1px) + 0.5px));
        }

        .logo-apollo .nome .a-haste,
        .logo-apollo .nome .p-haste,
        .logo-apollo .nome .l1,
        .logo-apollo .nome .l2 {
          border-radius: 999px;
        }

        .logo-apollo .a-anel{--x:0.00;--y:12.05;--w:21.52;--h:21.08;--ri:6.135;}
        .logo-apollo .a-haste{--x:17.87;--y:22.59;--w:3.65;--h:10.36;}
        .logo-apollo .p-anel{--x:26.78;--y:12.09;--w:21.52;--h:21.04;--ri:6.13;}
        .logo-apollo .p-haste{--x:26.78;--y:22.61;--w:4.38;--h:19.26;}
        .logo-apollo .o1-anel{--x:53.57;--y:11.83;--w:21.3;--h:21.3;--ri:6.16;}
        .logo-apollo .l1{--x:80.13;--y:0.00;--w:5.02;--h:33.13;}
        .logo-apollo .l2{--x:90.42;--y:0.00;--w:5.02;--h:33.13;}
        .logo-apollo .o2-anel{--x:100.70;--y:11.83;--w:21.3;--h:21.3;--ri:6.16;}

        .logo-apollo .marca-apollo {
          position: absolute;
          left: calc(135 * var(--u, 1px));
          top: 0;
        }

        .marca-apollo {
          position: relative;
          display: block;
          width: calc(31.79 * var(--u, 1px));
          height: calc(50.23 * var(--u, 1px));
        }

        .marca-apollo i {
          position: absolute;
          display: block;
        }

        .marca-apollo .m1 {
          inset: 0;
          background: linear-gradient(166.98deg, #FF672C 28.26%, #FCD83A 70.32%);
          clip-path: polygon(2.50% 0.00%,1.82% 1.02%,1.70% 1.45%,1.36% 1.81%,1.25% 2.47%,0.91% 2.90%,0.91% 3.26%,0.45% 4.42%,0.45% 5.58%,0.23% 6.60%,0.00% 6.74%,0.00% 13.56%,0.34% 14.00%,0.45% 15.01%,0.91% 15.88%,0.91% 16.24%,1.36% 16.90%,1.36% 17.19%,1.82% 17.69%,1.82% 17.98%,2.16% 18.35%,2.27% 18.78%,4.09% 21.17%,5.34% 22.48%,5.45% 22.77%,7.39% 24.66%,8.64% 25.60%,10.45% 26.76%,12.95% 27.85%,13.18% 27.85%,16.59% 29.30%,16.82% 29.30%,18.98% 30.17%,21.25% 30.82%,21.59% 31.04%,22.50% 31.33%,22.84% 31.33%,24.43% 31.91%,25.91% 32.20%,28.41% 33.07%,28.75% 33.07%,29.32% 33.36%,29.66% 33.36%,31.25% 33.94%,33.64% 34.52%,37.16% 35.68%,39.55% 36.26%,43.07% 37.42%,45.45% 38.00%,49.89% 39.45%,51.36% 39.74%,52.95% 40.32%,53.30% 40.32%,53.86% 40.61%,54.20% 40.61%,61.93% 43.07%,64.77% 44.23%,66.36% 45.18%,67.61% 46.19%,68.30% 46.99%,68.98% 48.59%,68.98% 50.40%,68.07% 52.50%,66.36% 54.31%,65.45% 55.04%,63.75% 56.06%,62.95% 56.35%,60.68% 57.51%,59.55% 57.87%,56.48% 59.25%,56.02% 59.32%,55.68% 59.54%,50.80% 61.35%,50.45% 61.57%,50.23% 61.57%,49.89% 61.78%,47.27% 62.73%,46.82% 62.80%,46.48% 63.02%,46.14% 63.02%,45.57% 63.31%,45.23% 63.31%,44.66% 63.60%,44.32% 63.60%,42.05% 64.47%,38.30% 65.63%,36.93% 66.21%,36.59% 66.21%,36.02% 66.50%,35.68% 66.50%,35.11% 66.79%,33.52% 67.30%,32.84% 67.66%,32.50% 67.66%,32.16% 67.88%,30.80% 68.31%,29.66% 68.82%,28.07% 69.33%,27.39% 69.69%,27.05% 69.69%,26.70% 69.91%,25.34% 70.34%,24.20% 70.85%,22.61% 71.36%,21.93% 71.72%,21.59% 71.72%,21.25% 71.94%,19.89% 72.37%,18.75% 72.88%,17.16% 73.39%,16.48% 73.75%,16.14% 73.75%,11.93% 75.78%,7.27% 78.75%,5.00% 80.86%,5.00% 81.00%,4.09% 81.87%,3.30% 82.89%,3.18% 83.32%,2.39% 84.34%,2.27% 84.77%,1.82% 85.28%,1.82% 85.64%,1.48% 86.08%,1.25% 87.16%,1.02% 87.38%,0.68% 89.12%,0.91% 93.55%,1.25% 94.20%,1.36% 95.00%,1.82% 95.79%,1.93% 96.30%,2.27% 96.66%,2.39% 97.17%,2.73% 97.53%,2.73% 97.82%,3.18% 98.26%,3.18% 98.48%,3.64% 98.84%,3.64% 99.06%,4.09% 99.64%,4.66% 99.93%,4.77% 99.85%,4.66% 99.56%,4.77% 99.49%,5.00% 97.24%,5.23% 97.03%,5.45% 95.94%,5.80% 95.50%,5.80% 95.21%,6.36% 94.05%,7.61% 92.24%,9.89% 89.85%,10.91% 88.98%,15.68% 85.93%,22.05% 82.96%,24.77% 82.09%,26.14% 81.51%,26.48% 81.51%,27.05% 81.22%,27.39% 81.22%,27.95% 80.93%,28.30% 80.93%,28.86% 80.64%,29.20% 80.64%,29.77% 80.35%,30.11% 80.35%,30.68% 80.06%,31.02% 80.06%,31.59% 79.77%,31.93% 79.77%,32.50% 79.48%,32.84% 79.48%,33.41% 79.19%,33.75% 79.19%,35.23% 78.61%,35.57% 78.61%,43.75% 76.00%,45.00% 75.71%,45.68% 75.42%,46.02% 75.42%,46.59% 75.13%,46.93% 75.13%,47.50% 74.84%,47.84% 74.84%,48.41% 74.55%,48.75% 74.55%,49.32% 74.26%,49.66% 74.26%,51.14% 73.68%,51.70% 73.60%,53.07% 73.10%,53.64% 73.02%,62.50% 70.20%,62.84% 70.20%,63.41% 69.91%,63.75% 69.91%,64.32% 69.62%,64.66% 69.62%,65.23% 69.33%,68.18% 68.46%,68.52% 68.24%,73.07% 66.79%,73.64% 66.72%,76.14% 65.84%,76.48% 65.84%,77.05% 65.55%,77.39% 65.55%,77.95% 65.26%,78.30% 65.26%,78.86% 64.97%,79.20% 64.97%,79.77% 64.68%,80.11% 64.68%,80.68% 64.39%,81.02% 64.39%,81.59% 64.10%,81.93% 64.10%,82.50% 63.81%,82.84% 63.81%,83.41% 63.52%,83.75% 63.52%,87.39% 62.36%,90.34% 61.13%,92.16% 60.26%,94.77% 58.59%,96.70% 56.85%,97.16% 56.20%,97.61% 55.84%,98.98% 53.95%,99.43% 53.08%,99.66% 52.14%,99.89% 52.07%,99.89% 48.95%,99.55% 48.66%,99.43% 47.86%,99.09% 47.43%,98.86% 46.77%,98.52% 46.41%,98.52% 46.19%,98.18% 45.90%,98.07% 45.47%,97.73% 45.25%,96.70% 44.02%,94.43% 42.20%,93.07% 41.33%,89.09% 39.52%,84.55% 38.07%,83.07% 37.78%,80.45% 36.91%,78.98% 36.62%,76.36% 35.75%,74.89% 35.46%,72.27% 34.59%,70.80% 34.30%,70.11% 34.01%,69.77% 34.01%,68.07% 33.43%,66.70% 33.14%,66.02% 32.85%,65.68% 32.85%,65.00% 32.56%,64.32% 32.49%,61.93% 31.76%,61.25% 31.69%,60.57% 31.40%,58.07% 30.82%,57.61% 30.60%,54.55% 29.88%,53.30% 29.44%,52.61% 29.37%,50.91% 28.79%,50.23% 28.72%,47.61% 27.92%,47.27% 27.92%,44.55% 27.05%,43.86% 26.98%,41.36% 26.18%,39.89% 25.89%,37.27% 25.02%,35.80% 24.73%,33.98% 24.15%,30.57% 22.84%,29.89% 22.70%,24.77% 20.67%,24.55% 20.67%,21.48% 19.22%,21.14% 19.14%,19.66% 18.35%,19.32% 18.27%,17.95% 17.48%,15.57% 16.32%,12.61% 14.43%,10.91% 13.20%,10.80% 12.98%,9.89% 12.33%,9.32% 11.68%,7.73% 10.30%,4.77% 6.45%,3.52% 4.13%,3.52% 3.77%,3.07% 2.97%,2.95% 1.89%,2.73% 1.38%,2.73% 0.22%);
        }

        .marca-apollo .m2 {
          left: calc(3.47 * var(--u, 1px));
          top: calc(15.41 * var(--u, 1px));
          width: calc(10.01 * var(--u, 1px));
          height: calc(16.50 * var(--u, 1px));
          background: linear-gradient(180deg, #FF672C 0%, #FCD83A 100%);
          clip-path: polygon(0.00% 0.00%,0.72% 0.88%,15.16% 9.93%,17.69% 11.04%,29.96% 18.10%,33.57% 20.75%,41.52% 25.61%,44.40% 27.81%,50.90% 32.01%,53.79% 34.66%,55.23% 35.54%,56.32% 36.87%,57.76% 37.75%,58.12% 38.41%,60.29% 39.96%,62.45% 42.60%,63.54% 43.27%,63.54% 43.93%,64.62% 44.59%,68.59% 49.23%,69.31% 49.67%,69.31% 50.33%,71.84% 53.42%,72.20% 54.75%,73.65% 56.29%,74.01% 57.62%,75.09% 58.72%,75.45% 60.49%,76.53% 62.03%,76.53% 63.13%,77.98% 65.34%,77.98% 66.45%,78.34% 66.67%,78.34% 67.55%,79.42% 69.76%,79.42% 71.74%,79.78% 71.96%,79.78% 73.51%,80.14% 73.73%,80.14% 83.22%,79.78% 83.44%,79.78% 86.98%,79.42% 87.20%,79.06% 90.29%,78.34% 91.17%,77.98% 93.38%,76.53% 96.25%,76.17% 98.68%,75.45% 99.34%,75.81% 99.78%,78.70% 98.01%,79.42% 97.13%,81.95% 95.36%,85.92% 91.61%,86.64% 90.51%,87.73% 89.85%,88.09% 88.74%,89.53% 87.20%,89.53% 86.75%,90.25% 86.31%,90.61% 84.99%,91.70% 84.11%,92.06% 82.78%,93.14% 81.90%,93.50% 80.13%,94.58% 78.81%,94.95% 77.48%,96.39% 75.28%,96.75% 73.29%,97.47% 72.63%,98.19% 68.87%,98.92% 68.21%,99.28% 63.58%,99.64% 63.36%,99.64% 55.63%,99.28% 55.41%,98.92% 48.34%,98.56% 48.12%,98.56% 46.58%,97.83% 45.03%,97.83% 43.27%,97.11% 41.94%,96.39% 38.41%,94.95% 36.42%,94.95% 35.32%,93.50% 32.67%,92.06% 31.35%,92.06% 30.91%,90.61% 30.02%,89.53% 28.92%,83.75% 26.27%,81.95% 26.05%,81.23% 25.61%,79.06% 25.17%,76.90% 24.28%,75.81% 24.28%,75.09% 23.84%,72.92% 23.40%,70.76% 22.52%,68.59% 22.08%,67.87% 21.63%,66.06% 21.41%,65.34% 20.97%,63.54% 20.75%,61.01% 19.87%,60.29% 19.87%,59.57% 19.43%,57.40% 18.98%,55.23% 18.10%,54.15% 18.10%,52.35% 17.22%,51.26% 17.22%,43.68% 14.79%,41.52% 14.57%,40.79% 14.13%,38.63% 13.69%,37.91% 13.25%,35.74% 12.80%,35.02% 12.36%,32.85% 11.92%,30.69% 11.04%,29.96% 11.04%,28.16% 10.15%,24.19% 9.05%,23.10% 8.39%,22.38% 8.39%,18.77% 7.28%,17.69% 6.62%,14.80% 5.74%,10.83% 3.97%,9.75% 3.75%,8.66% 3.09%,3.61% 1.32%,2.17% 0.44%);
        }

        .logo .logo-apollo {
          --u: 0.9px;
        }

        @media (max-width: 750px) {
          .logo .logo-apollo {
            --u: 0.75px;
          }
        }
    </style>
</head>
<body class="tela-string tela-lotes">

<div class="pagina">

    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="logo">
            <div class="logo-apollo" role="img" aria-label="Apollo">
                <div class="nome">
                    <i class="a-anel"></i><i class="a-haste"></i>
                    <i class="p-anel"></i><i class="p-haste"></i>
                    <i class="o1-anel"></i>
                    <i class="l1"></i><i class="l2"></i>
                    <i class="o2-anel"></i>
                </div>
                <div class="marca-apollo"><i class="m1"></i><i class="m2"></i></div>
            </div>
        </div>

        <nav class="menu">
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7" rx="1.5"/><rect x="14" y="3" width="7" height="7" rx="1.5"/><rect x="14" y="14" width="7" height="7" rx="1.5"/><rect x="3" y="14" width="7" height="7" rx="1.5"/></svg>
                Dashboard
            </a>
            <a href="#" class="menu-item ativo">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M14 3H7a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V8z"/><path d="M14 3v5h5"/><path d="M9.5 16.5l.7-2.2 4-4 1.5 1.5-4 4z"/></svg>
                Lotes
            </a>
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>
                Manutenções
            </a>
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="6" cy="5" r="2"/><circle cx="6" cy="19" r="2"/><circle cx="18" cy="8" r="2"/><path d="M6 7v10M18 10c0 4-6 3-12 7"/></svg>
                Filiais
            </a>
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="8" r="3"/><circle cx="17" cy="9" r="2.3"/><path d="M3 20c0-3.3 2.7-6 6-6s6 2.7 6 6M15.5 14.3c3 0 5.5 2.2 5.5 5"/></svg>
                Membros
            </a>
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 21s7-6.2 7-11a7 7 0 0 0-14 0c0 4.8 7 11 7 11z"/><circle cx="12" cy="10" r="2.5"/></svg>
                Realocações
            </a>
            <a href="#" class="menu-item">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="9"/><path d="M8 12.5l2.7 2.7L16 9.8"/></svg>
                Metas
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
                <h1 class="titulo">Lotes <span class="migalha">&gt; Lote estúdio</span></h1>
            </div>

            <div class="usuario">
                <span class="sino-box"><svg class="sino" viewBox="0 0 24 24" fill="none" stroke="#111" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.7 21a2 2 0 0 1-3.4 0"/></svg></span>
                <span>Neymar júnior césar</span>
                <span class="seta">▼</span>
            </div>
        </header>

        <div class="area-lotes">

            <section class="lote-card">
                <div class="foto-lote" role="img" aria-label="Casa com painéis solares no telhado">
                    <i class="f-parede"></i><i class="f-telhado"></i><i class="f-paineis"></i><i class="f-verde"></i>
                </div>

                <div class="lote-dir">
                    <div class="lote-info">
                        <h2 class="lote-nome">Lote estúdio</h2>
                        <p class="lote-end">Rua das flores, 123 - São Paulo SP</p>
                    </div>

                    <div class="metrica">
                        <span class="metrica-rotulo">Situação</span>
                        <span class="selo-ativo">Ativo</span>
                    </div>

                    <div class="metrica">
                        <span class="metrica-rotulo">Potência</span>
                        <span class="metrica-valor">8,4 kWp</span>
                    </div>

                    <div class="metrica">
                        <span class="metrica-rotulo">IS<i class="ponto-info">i</i></span>
                        <span class="metrica-valor"><b class="verde">90</b>%</span>
                    </div>

                    <p class="lote-obs"><b>Observação:</b><span>"Lorem ipsum indor do sei lá oque das quantas do bgl lá tik taka real madrid 2017 barcelona 2015"</span></p>
                </div>

                <button type="button" class="lote-fechar" aria-label="Fechar">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"><path d="M5 5l14 14M19 5L5 19"/></svg>
                </button>
            </section>

            <section class="resumo-lote" aria-label="Resumo do lote">
                <div class="resumo-item">
                    <span class="bolha azul"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M12 3l8 4.5v9L12 21l-8-4.5v-9z"/><path d="M4 7.5l8 4.5 8-4.5M12 12v9"/></svg></span>
                    <div class="resumo-txt"><span>Modelos</span><strong id="total-modelos">6</strong></div>
                </div>
                <div class="resumo-item">
                    <span class="bolha amarela"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M13 2L4 14h7l-1 8 9-12h-7z"/></svg></span>
                    <div class="resumo-txt"><span>Eficiência geral</span><strong>10</strong></div>
                </div>
                <div class="resumo-item">
                    <span class="bolha laranja"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M4 20l2.5-9h11L20 20z"/><path d="M5.3 15.5h13.4M10 11l-1 9M14 11l1 9"/><path d="M12 2v3M5 5l2 2M19 5l-2 2"/></svg></span>
                    <div class="resumo-txt"><span>Total de painéis</span><strong id="total-paineis">24</strong></div>
                </div>
                <div class="resumo-item">
                    <span class="bolha verde-is">IS</span>
                    <div class="resumo-txt"><span>Índice de sustentabilidade</span><strong><b class="verde">90</b>%</strong></div>
                </div>
            </section>

            <section class="grade-placas" id="grade-placas">
                <article class="placa">
                    <header class="placa-topo"><b>Aiko Solar</b><span>Aiko Comet 5NT2</span></header>
                    <div class="placa-corpo">
                        <div class="placa-img"></div>
                        <div class="placa-info">
                            <p><span>Eficiência:</span><b>24,8%</b></p>
                            <p><span>Vida Útil:</span><b>30 anos</b></p>
                            <p><span>Material Principal:</span></p>
                            <p class="placa-material"><b>Silício Monocristalino (BSC)</b></p>
                        </div>
                        <div class="placa-un"><strong>10</strong><span>Unid.</span></div>
                    </div>
                </article>
                <article class="placa">
                    <header class="placa-topo"><b>Aiko Solar</b><span>Aiko Comet 5NT2</span></header>
                    <div class="placa-corpo">
                        <div class="placa-img"></div>
                        <div class="placa-info">
                            <p><span>Eficiência:</span><b>24,8%</b></p>
                            <p><span>Vida Útil:</span><b>30 anos</b></p>
                            <p><span>Material Principal:</span></p>
                            <p class="placa-material"><b>Silício Monocristalino (BSC)</b></p>
                        </div>
                        <div class="placa-un"><strong>10</strong><span>Unid.</span></div>
                    </div>
                </article>
                <article class="placa">
                    <header class="placa-topo"><b>Aiko Solar</b><span>Aiko Comet 5NT2</span></header>
                    <div class="placa-corpo">
                        <div class="placa-img"></div>
                        <div class="placa-info">
                            <p><span>Eficiência:</span><b>24,8%</b></p>
                            <p><span>Vida Útil:</span><b>30 anos</b></p>
                            <p><span>Material Principal:</span></p>
                            <p class="placa-material"><b>Silício Monocristalino (BSC)</b></p>
                        </div>
                        <div class="placa-un"><strong>10</strong><span>Unid.</span></div>
                    </div>
                </article>
                <article class="placa">
                    <header class="placa-topo"><b>Aiko Solar</b><span>Aiko Comet 5NT2</span></header>
                    <div class="placa-corpo">
                        <div class="placa-img"></div>
                        <div class="placa-info">
                            <p><span>Eficiência:</span><b>24,8%</b></p>
                            <p><span>Vida Útil:</span><b>30 anos</b></p>
                            <p><span>Material Principal:</span></p>
                            <p class="placa-material"><b>Silício Monocristalino (BSC)</b></p>
                        </div>
                        <div class="placa-un"><strong>10</strong><span>Unid.</span></div>
                    </div>
                </article>
                <article class="placa">
                    <header class="placa-topo"><b>Aiko Solar</b><span>Aiko Comet 5NT2</span></header>
                    <div class="placa-corpo">
                        <div class="placa-img"></div>
                        <div class="placa-info">
                            <p><span>Eficiência:</span><b>24,8%</b></p>
                            <p><span>Vida Útil:</span><b>30 anos</b></p>
                            <p><span>Material Principal:</span></p>
                            <p class="placa-material"><b>Silício Monocristalino (BSC)</b></p>
                        </div>
                        <div class="placa-un"><strong>10</strong><span>Unid.</span></div>
                    </div>
                </article>

                <button type="button" class="placa-vazia" id="abrir-popup" aria-haspopup="dialog">
                    <span>Estoque vazio</span>
                    <svg viewBox="0 0 48 48" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"><circle cx="24" cy="24" r="22"/><path d="M24 12v24M12 24h24"/></svg>
                </button>
            </section>

        </div>

    </main>

    <!-- POP-UP CENTRAL: ADICIONAR PLACA -->
    <div class="modal-fundo" id="modal-placa" aria-hidden="true">
        <form class="modal" role="dialog" aria-modal="true" aria-label="Adicionar placa" novalidate>
            <div class="modal-linha">
                <label for="p-marca">Marca:</label>
                <input type="text" id="p-marca" name="marca" required>
            </div>

            <div class="modal-linha">
                <label for="p-modelo">Modelo:</label>
                <input type="text" id="p-modelo" name="modelo" required>
            </div>

            <div class="modal-linha">
                <label for="p-tempo">Tempo de uso (anos):</label>
                <input type="number" id="p-tempo" name="tempo" min="0" inputmode="numeric">
            </div>

            <div class="modal-linha">
                <label for="p-material">Material principal:</label>
                <select id="p-material" name="material">
                    <option value="Monocristalino" selected>Monocristalino</option>
                    <option value="Policristalino">Policristalino</option>
                    <option value="Filme fino">Filme fino</option>
                </select>
            </div>

            <div class="modal-linha">
                <label for="p-unidades">Unidades:</label>
                <input type="number" id="p-unidades" name="unidades" min="1" inputmode="numeric" required>
            </div>

            <div class="modal-rodape">
                <button type="button" class="botao-cancelar" id="cancelar-popup">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M6 6l12 12M18 6L6 18"/></svg>
                    Cancelar
                </button>
                <button type="submit" class="botao-adicionar">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round"><path d="M12 5v14M5 12h14"/></svg>
                    Adicionar modelo
                </button>
            </div>
        </form>
    </div>
</div>

    <script>
        (function () {
            var fundo = document.getElementById('modal-placa');
            var form = fundo.querySelector('form');
            var abrir = document.getElementById('abrir-popup');
            var cancelar = document.getElementById('cancelar-popup');
            var grade = document.getElementById('grade-placas');
            var ultimoFoco = null;

            function abrirPopup() {
                ultimoFoco = document.activeElement;
                fundo.classList.add('aberto');
                fundo.setAttribute('aria-hidden', 'false');
                document.body.classList.add('modal-aberto');
                setTimeout(function () { form.querySelector('input').focus(); }, 30);
            }

            function limparErros() {
                form.querySelectorAll('.invalido').forEach(function (c) {
                    c.classList.remove('invalido');
                    c.placeholder = '';
                });
            }

            function fecharPopup() {
                fundo.classList.remove('aberto');
                fundo.setAttribute('aria-hidden', 'true');
                document.body.classList.remove('modal-aberto');
                limparErros();
                form.reset();
                if (ultimoFoco) ultimoFoco.focus();
            }

            function esc(texto) {
                var d = document.createElement('div');
                d.textContent = texto;
                return d.innerHTML;
            }

            abrir.addEventListener('click', abrirPopup);
            cancelar.addEventListener('click', fecharPopup);

            fundo.addEventListener('mousedown', function (e) {
                if (e.target === fundo) fecharPopup();
            });

            document.addEventListener('keydown', function (e) {
                if (!fundo.classList.contains('aberto')) return;

                if (e.key === 'Escape') {
                    fecharPopup();
                    return;
                }

                if (e.key === 'Tab') {
                    var itens = form.querySelectorAll('input, select, button');
                    var primeiro = itens[0];
                    var ultimo = itens[itens.length - 1];
                    if (e.shiftKey && document.activeElement === primeiro) {
                        e.preventDefault();
                        ultimo.focus();
                    } else if (!e.shiftKey && document.activeElement === ultimo) {
                        e.preventDefault();
                        primeiro.focus();
                    }
                }
            });

            form.addEventListener('input', function (e) {
                if (e.target.classList.contains('invalido')) {
                    e.target.classList.remove('invalido');
                    e.target.placeholder = '';
                }
            });

            form.addEventListener('submit', function (e) {
                e.preventDefault();

                var primeiroErro = null;
                form.querySelectorAll('[required]').forEach(function (campo) {
                    var vazio = !campo.value.trim();
                    campo.classList.toggle('invalido', vazio);
                    campo.placeholder = vazio ? 'Preencha o campo' : '';
                    if (vazio && !primeiroErro) primeiroErro = campo;
                });

                if (primeiroErro) {
                    primeiroErro.focus();
                    return;
                }

                var marca = form.marca.value.trim();
                var modelo = form.modelo.value.trim();
                var tempo = form.tempo.value.trim();
                var material = form.material.value;
                var un = parseInt(form.unidades.value, 10) || 0;

                var card = document.createElement('article');
                card.className = 'placa';
                card.innerHTML =
                    '<header class="placa-topo"><b>' + esc(marca) + '</b><span>' + esc(modelo) + '</span></header>' +
                    '<div class="placa-corpo">' +
                        '<div class="placa-img"></div>' +
                        '<div class="placa-info">' +
                            '<p><span>Eficiência:</span><b>—</b></p>' +
                            '<p><span>Tempo de uso:</span><b>' + (tempo ? esc(tempo) + ' anos' : '—') + '</b></p>' +
                            '<p><span>Material Principal:</span></p>' +
                            '<p class="placa-material"><b>' + esc(material) + '</b></p>' +
                        '</div>' +
                        '<div class="placa-un"><strong>' + un + '</strong><span>Unid.</span></div>' +
                    '</div>';

                grade.insertBefore(card, abrir);

                var modelos = document.getElementById('total-modelos');
                var paineis = document.getElementById('total-paineis');
                modelos.textContent = (parseInt(modelos.textContent, 10) || 0) + 1;
                paineis.textContent = (parseInt(paineis.textContent, 10) || 0) + un;

                fecharPopup();
            });
        })();
    </script>
</body>
</html>