# Gestão de Produtos

Aplicação desktop em Java para controle de estoque de produtos, com interface gráfica em JavaFX e persistência de dados em banco SQLite.

## Sobre o projeto

O sistema permite cadastrar, listar, atualizar e excluir produtos de um estoque, exibindo os dados em uma tabela interativa. Cada produto possui nome, quantidade, preço e status (por exemplo, "Estoque Normal" ou "Estoque Baixo"), e todas as operações são refletidas diretamente no banco de dados.

## Funcionalidades

- **Cadastrar produto**: adiciona um novo produto ao estoque a partir do formulário.
- **Atualizar produto**: edita os dados de um produto selecionado na tabela.
- **Excluir produto**: remove um produto selecionado do banco de dados.
- **Listar produtos**: exibe todos os produtos cadastrados em uma tabela (`TableView`).
- **Limpar campos**: reseta o formulário de entrada.

## Tecnologias utilizadas

- **Java**
- **JavaFX** — construção da interface gráfica (janelas, formulários, tabela)
- **JDBC (SQLite)** — conexão e persistência dos dados em `meu_banco_de_dados.db`
- **CSS** — estilização da interface (`styles-produtos.css`)

## Estrutura do projeto

```
GestaoDeProdutos/
├── src/
│   ├── Produto.java          # Classe modelo (entidade) do produto
│   ├── ProdutoDAO.java       # Camada de acesso a dados (CRUD via JDBC)
│   ├── ConexaoDB.java        # Gerenciamento da conexão com o banco SQLite
│   ├── CriadorTabela.java    # Script para criação da tabela "produtos"
│   ├── ProdutoGUI.java       # Interface gráfica (JavaFX) da aplicação
│   ├── Main.java             # Classe de testes: inserção e listagem
│   ├── Main2.java            # Classe de testes: consulta e atualização
│   ├── Main3.java            # Classe de testes: exclusão de produtos
│   └── styles-produtos.css   # Estilos da interface
├── lib/                       # Dependências (JavaFX, SQLite JDBC, SLF4J)
├── .vscode/                    # Configurações do VS Code (launch e settings)
└── meu_banco_de_dados.db      # Banco de dados SQLite
```

A classe `ProdutoDAO` concentra as operações de banco de dados:

| Método | Descrição |
|---|---|
| `inserir(Produto)` | Insere um novo produto |
| `consultarPorId(int)` | Busca um produto pelo ID |
| `atualizar(Produto)` | Atualiza os dados de um produto |
| `excluir(int)` | Remove um produto pelo ID |
| `excluirTodos()` | Remove todos os produtos |
| `listarTodos()` | Retorna todos os produtos cadastrados |

## Pré-requisitos

- JDK instalado (compatível com JavaFX)
- [JavaFX SDK](https://openjfx.io/) instalado localmente
- Driver JDBC do SQLite (já incluído em `lib/sqlite-jdbc-3.46.0.0.jar`)
- VS Code com a extensão de suporte a Java (recomendado)

## Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/DaviOrtolan/GestaoDeProdutos.git
   ```
2. Abra a pasta no VS Code.
3. Garanta que o `.vscode/launch.json` aponte para o caminho correto do seu JavaFX SDK, ajustando o `vmArgs`:
   ```
   --module-path "CAMINHO/PARA/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```
4. Execute a classe `CriadorTabela.java` uma vez, para garantir que a tabela `produtos` exista no banco.
5. Execute a classe `ProdutoGUI.java` para abrir a interface gráfica do gerenciador de estoque.

> As classes `Main.java`, `Main2.java` e `Main3.java` são exemplos de uso do `ProdutoDAO` via terminal (inserção, consulta/atualização e exclusão), úteis para testar a camada de dados sem a interface gráfica.

## Banco de dados

A tabela `produtos` é criada com a seguinte estrutura:

```sql
CREATE TABLE IF NOT EXISTS produtos (
    id_produto INTEGER PRIMARY KEY,
    nome_produto TEXT NOT NULL,
    quantidade INTEGER,
    preco REAL,
    status TEXT
);
```

## 👤 Autor

Desenvolvido por **Davi Ortolan**.
