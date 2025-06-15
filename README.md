# 🛠️ Static Checker CangaCode2025-1

## 📋 Descrição do Projeto

O **Static Checker CangaCode2025-1** é uma ferramenta de análise estática de código, desenvolvida em **Java 21**, com interface gráfica em **Swing**. Seu principal objetivo é ler um arquivo fonte `.251`, realizar a análise léxica, gerar a tabela de símbolos e produzir os arquivos de saída `.LEX` e `.TAB`. 


✅ Ler um arquivo fonte `.251`  
✅ Realizar a análise léxica  
✅ Gerar a tabela de símbolos  
✅ Produzir os arquivos de saída `.LEX` e `.TAB`

O sistema segue uma arquitetura modular e incremental, com etapas bem definidas: leitura do arquivo, análise léxica, geração da tabela de símbolos e gravação dos resultados. 

Ele foi projetado para trabalhar com a linguagem **CangaCode2025-1**, obedecendo regras de truncagem, identificação de tipos e controle de escopo de identificadores. Além disso, o sistema é **case-insensitive** e filtra comentários e símbolos inválidos.

### 🧰 Tecnologias utilizadas:

- ☕ **Java 21**
- 🖥️ **Swing (Java GUI)**
- 🧑‍💻 **IntelliJ IDEA 2025**

---

## 🧱 Estrutura Geral do Sistema

O Static Checker é composto por quatro etapas principais, refletindo uma arquitetura incremental. Essas etapas são coordenadas pelo **AnalisadorSintático**:

1. 📂 Leitura e Controle do Arquivo Fonte (`.251`)
2. 🕵️ Análise Léxica
3. 📊 Geração da Tabela de Símbolos
4. 📝 Geração dos Arquivos `.LEX` e `.TAB`

Cada módulo interage através do **AnalisadorSintático**, que centraliza o fluxo de execução.

---

## 📌 Módulos e Estruturas

### 🧠 AnalisadorSintatico.java
- Controla o fluxo geral de execução.
- Preenche o vocabulário da linguagem alvo.
- Inicializa o Analisador Léxico, o Gerador de Tabela de Símbolos e os Geradores de Arquivos de Saída.
- Mantém as listas de tokens e itens da tabela de símbolos.

### 📚 AtomoCangaCode.java
- Contém o vocabulário da linguagem **CangaCode2025-1**.
- Responsável pela validação de tokens.

### 🖱️ LauncherGUI.java
- Interface gráfica simples para seleção de arquivo e exibição de feedback de execução.

### 📑 Buffer.java
- Realiza a leitura do arquivo `.251`.
- Armazena as linhas em memória para análise sequencial.

### 🔖 Token.java
- Estrutura para armazenar os tokens válidos.
- Atributos: `lexeme`, `código`, `linha`, `índice na tabela de símbolos`.

### 🔍 AnalisadorLexico.java
- Lê os caracteres do buffer linha por linha.
- Remove espaços, tokens inválidos e comentários.
- Classifica os tokens com base no vocabulário (AtomoCangaCode.java).
- Implementa escopo para diferenciar identificadores válidos de caracteres inválidos.
- Armazena até 35 caracteres do lexema.

### 🗂️ GeradorTabelaSimbolo.java
- Constrói a Tabela de Símbolos.
- Armazena apenas os 35 primeiros caracteres do lexema e as 5 primeiras ocorrências no arquivo fonte.
- Aplica truncagem em strings, substituindo o último caractere por `"`, se necessário.
- Faz a inferência do tipo dos identificadores com base em declarações anteriores (exemplo: `integer numero`).

### 🧾 ItemTabelaSimbolo.java
- Representa um item na tabela de símbolos.
- Atributos: Número da Entrada, Código, Tamanhos (antes/depois da truncagem), Tipo, Linhas de Ocorrência.

### 📝 GeradorDeArquivoLEX.java
- Gera o arquivo `.LEX` com a lista de tokens válidos.

### 📝 GeradorDeArquivoTAB.java
- Gera o arquivo `.TAB` com a Tabela de Símbolos.

---

## 🚦 Fluxo Geral de Execução

1. **LauncherGUI:** Usuário escolhe o arquivo `.251`.
2. **Buffer:** Carrega o conteúdo do arquivo.
3. **Analisador Léxico:** Remove comentários/tokens inválidos e monta a lista de tokens válidos.
4. **GeradorTabelaSimbolo:** Filtra identificadores, infere tipos e preenche a Tabela de Símbolos.
5. **Geradores de Arquivos:** Salva os arquivos `.LEX` e `.TAB`.
6. **Interface:** Exibe mensagens de sucesso ou erro.

---

## ⚠️ Regras e Restrições Importantes

- Sistema **case-insensitive** 🔡
- Limite de **35 caracteres por lexema** ✂️
- Apenas **5 primeiras ocorrências** de cada símbolo são armazenadas 🧮
- Comentários e espaços são filtrados 🗑️
- Leitura bufferizada linha a linha 📖

---

## 🖼️ Interface Swing

![Interface](https://github.com/CauZy-Goes/Static_Checker_CangaCode/blob/main/imagens_static_checker/interface.png)

---

## 🗺️ Diagrama de Contexto

![Diagrama de Contexto](https://github.com/CauZy-Goes/Static_Checker_CangaCode/blob/main/imagens_static_checker/diagrama%20de%20contexto.png)

---

## 🏗️ Diagrama de Classes

![Diagrama de Classes](https://github.com/CauZy-Goes/Static_Checker_CangaCode/blob/main/imagens_static_checker/StaticChecker.png)

---


## 📄 Licença

Este projeto está licenciado sob a [Licença MIT](https://github.com/CauZy-Goes/Static_Checker_CangaCode/blob/main/LICENSE).

## 👨‍💻 Autor

Desenvolvido por [Cauã Farias (CauZy-Goes)](https://github.com/CauZy-Goes)  
---
