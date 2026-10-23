# Sistema Apollo 🚀

O **Sistema Apollo** é uma aplicação Web focada em operações de CRUD (Create, Read, Update, Delete). Este repositório centraliza o código oficial da nossa equipe.

Este documento serve como o **Guia de Bordo Oficial**. Siga os padrões descritos abaixo para garantir que o projeto funcione na sua máquina e que nosso histórico no GitHub permaneça organizado.

---

## 🛠️ Tecnologias e Configurações Globais

Para que o projeto compile sem erros, todos os desenvolvedores devem alinhar suas ferramentas:
* **Linguagem:** Java 21 (JDK 21)
* **Gerenciador de Dependências:** Maven
* **Banco de Dados:** PostgreSQL (`42.7.3`)
* **Especificação Web:** Jakarta Servlet (`6.1.0`)

---

## 📦 1. Gerenciamento de Bibliotecas (Maven & Jakarta)

O Maven gerencia nossas bibliotecas através do arquivo `pom.xml`. Você não precisa baixar arquivos `.jar` manualmente da internet.

### Como atualizar e baixar as dependências no IntelliJ:
1. Sempre que o arquivo `pom.xml` for alterado, um pequeno ícone flutuante com o símbolo do Maven (um "M" com duas setas circulares) aparecerá no canto superior direito do código.
2. **Clique sempre nesse ícone** (ou use o atalho `Ctrl + Shift + O`) para que o IntelliJ faça o download das novas bibliotecas (como o driver do PostgreSQL ou o Jakarta Servlet).

> ⚠️ **Aviso de Migração Tecnológica Importante para o Time:**
> O nosso projeto usa o **Jakarta EE 11** (versão 6.1.0 do Servlet). 
> Certifique-se de que todas as suas classes de Servlet usem a importação moderna:
> `import jakarta.servlet.*;` 
> Evite usar os pacotes antigos que começam com `javax.servlet.*`, pois eles serão descontinuados.

---

## 📂 2. Estrutura de Pastas do Projeto

Adotamos a estrutura de arquitetura padrão do Maven. Trabalhe rigorosamente dentro das seguintes pastas:

* `src/main/java/org/example/`
  * 📁 `DAO/`: Classes responsáveis pelas conexões e consultas ao banco de dados PostgreSQL.
  * 📁 `model/`: Suas entidades e objetos de negócio (ex: Usuario, Lote).
  * 📁 `servlet/`: Suas classes controladoras web (ex: `UsuarioServlet.java`, `LoteServlet.java`) que herdam de `HttpServlet`.
  * 📁 `util/`: Classes utilitárias compartilhadas e configurações gerais.
* `src/main/resources/`: Arquivos de configuração de ambiente.

---

## 🐙 3. Manual do GitHub: Como Trabalhar em Equipe

Para não gerarmos conflitos destruindo o código uns dos outros, seguimos um fluxo de trabalho estruturado.

### O que são Branches e Commits?
* **Branch (Ramificação):** É uma cópia segura do projeto. A branch `main` guarda o código oficial que funciona. Nunca faça experimentos diretamente na `main`. Sempre crie uma branch com o seu nome ou com o nome da tarefa para testar suas alterações.
* **Commit:** É uma "foto" ou ponto de salvamento do seu código atual. O Git registra permanentemente o seu nome de autor a cada commit.

### Fluxo de Trabalho Diário (Siga estes passos):

#### Passo 1: Atualize seu código local antes de começar o dia
Antes de digitar qualquer linha de código, baixe o trabalho que seus colegas enviaram para o GitHub:
```bash
git checkout main
git pull origin main
```

#### Passo 2: Crie a sua Branch de Trabalho
Vá para o seu espaço seguro de desenvolvimento (substitua o nome pelo seu objetivo):
```bash
git checkout -b feature/nome-da-sua-tarefa
```

#### Passo 3: Salve seu progresso (Commit)
Terminou uma funcionalidade ou corrigiu um bug? Salve o estado usando mensagens profissionais (de preferência em inglês):
```bash
git add .
git commit -m "feat: add user creation validation"
```

#### Passo 4: Envie seu trabalho para análise
Envie a sua ramificação para o GitHub para que a equipe possa avaliar o seu código:
```bash
git push origin feature/nome-da-sua-tarefa
```
*Após o envio, acesse o GitHub do projeto e abra um **Pull Request** para integrarmos o seu código à branch `main` de forma segura.*

---

## 👥 Integrantes do Time
* [@Nichiruto0](https://github.com/Nichiruto0)
* [@Migs345](https://github.com/Migs345)
* [@pczr](https://github.com/pczr)
* [@ikarobueno05](https://github.com/ikarobueno05)
* [@Sabor-Fernandex (Fernandes)](https://github.com/Sabor-Fernandex)
