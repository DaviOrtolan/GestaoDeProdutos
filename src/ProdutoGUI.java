import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProdutoGUI extends Application {
    private ProdutoDAO produtoDAO;
    private ObservableList<Produto> produtos;
    private TableView<Produto> tableView;
    private TextField nomeInput, quantidadeInput, precoInput;
    private ComboBox<String> statusComboBox;
    private Connection conexaoDB;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        conexaoDB = ConexaoDB.conectar();
        produtoDAO = new ProdutoDAO(conexaoDB); // Inicializa o DAO
        produtos = FXCollections.observableArrayList(produtoDAO.listarTodos()); // Carrega todos os produtos do Banco de Dados 

        primaryStage.setTitle("Gerenciador de Estoque de Produtos");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10)); // espaçamento entre o conteúdo do VBox e a borda
        vBox.setSpacing(10); // espaçamento enrte cada componente interno do VBox

        HBox nomeProdutoHBox = new HBox();
        nomeProdutoHBox.setSpacing(10);
        Label nomeLabel = new Label("Produto: ");
        nomeInput = new TextField();
        nomeProdutoHBox.getChildren().addAll(nomeLabel, nomeInput);

        HBox quantidadeHBox = new HBox();
        quantidadeHBox.setSpacing(10);
        Label quantidadeLabel = new Label("Quantidade: ");
        quantidadeInput = new TextField();
        quantidadeHBox.getChildren().addAll(quantidadeLabel, quantidadeInput);

        HBox precoHBox = new HBox();
        precoHBox.setSpacing(10);
        Label precoLabel = new Label("Preço: ");
        precoInput = new TextField();
        precoHBox.getChildren().addAll(precoLabel, precoInput);

        HBox statusHBox = new HBox();
        statusHBox.setSpacing(10);
        Label statusLabel = new Label("Status: ");
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Estoque Normal", "Estoque Baixo");
        statusHBox.getChildren().addAll(statusLabel, statusComboBox);

        Button botaoAdicionar = new Button("Adicionar");
        botaoAdicionar.setOnAction(e -> {
            String preco = precoInput.getText().replace(',', '.'); // substitui vírgula por ponto no preço

            Produto produto = new Produto(nomeInput.getText(), 
            Integer.parseInt(quantidadeInput.getText()),
            Double.parseDouble(preco),
            statusComboBox.getValue());

            produtoDAO.inserir(produto); // Insere novo produto na base de dados
            produtos.setAll(produtoDAO.listarTodos()); // Atualiza a lista de produtos na tela
            limparCampos(); // Limpa os campos de entrada para uma nova digitação 
        });

        Button botaoAtualizar = new Button("Atualizar");
        botaoAtualizar.setOnAction(e -> {
            Produto selectedProduto = tableView.getSelectionModel().getSelectedItem(); // Obtém o produto selecionado
            if(selectedProduto != null) {
                selectedProduto.setNome(nomeInput.getText());
                selectedProduto.setQuantidade(Integer.parseInt(quantidadeInput.getText()));
                String preco = precoInput.getText().replace(',', '.');
                selectedProduto.setPreco(Double.parseDouble(preco));
                selectedProduto.setStatus(statusComboBox.getValue());
                produtoDAO.atualizar(selectedProduto); // Atualiza o produto no Banco 
                produtos.setAll(produtoDAO.listarTodos()); // Atualiza a lista de produtos
                limparCampos(); // Limpar os campos de entrada
            }
        });

        Button botaoExcluir = new Button("Excluir");
        botaoExcluir.setOnAction(e -> {
            Produto selectedProduto = tableView.getSelectionModel().getSelectedItem(); // Obtém o produto selecionado
            if(selectedProduto != null) {
                produtoDAO.excluir(selectedProduto.getId()); // Exclui o produto do Banco
                produtos.setAll(produtoDAO.listarTodos()); // Atualizar a lista de produtos
                limparCampos(); // Limpa os campos de entrada
            }
        });

        Button botaoLimpar = new Button("Limpar");
        botaoLimpar.setOnAction(e -> limparCampos());

        tableView = new TableView<>();
        tableView.setItems(produtos); // Define a lista de produtos na tabela
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // Ajusta o tamanho das colunas
        List<TableColumn<Produto, ?>> columns = List.of(
            criarColuna("ID", "id"),
            criarColuna("Produto", "nome"),
            criarColuna("Quantidade", "quantidade"),
            criarColuna("Preço", "preco"),
            criarColuna("Status", "status")
        );

        tableView.getColumns().addAll(columns);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                nomeInput.setText(newSelection.getNome());
                quantidadeInput.setText(String.valueOf(newSelection.getQuantidade()));
                precoInput.setText(String.valueOf(newSelection.getPreco()));
                statusComboBox.setValue(newSelection.getStatus());
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(botaoAdicionar, botaoAtualizar, botaoExcluir, botaoLimpar); // Adicionando os botões no layout HBox

        vBox.getChildren().addAll(nomeProdutoHBox, quantidadeHBox, precoHBox, statusHBox, buttonBox, tableView); // Inserindo os layouts criados anteriormente dentro do layout principal VBox

        Scene scene = new Scene(vBox, 800, 800);
        //scene.getStylesheets().add("styles-produtos.css"); // Adiciona a folha de estilos
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // O método stop é automaticamente chamado quando a aplicação é encerrada
    @Override
    public void stop() {
        try {
            conexaoDB.close(); // Fecha a conexão com o banco de dados
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    // Limpa os campos de entrada do formulário
    // Este método é chamado após adicionar, atualizar ou excluir um produto
    private void limparCampos() {
        nomeInput.clear();
        quantidadeInput.clear();
        precoInput.clear();
        statusComboBox.setValue(null);
    }

    /** 
    Cria uma coluna para a TableView
    @param title O título da coluna que será exibido no cabeçalho 
    @param property A propriedade do objeto Produto que esta coluna deve exibir
    @return A coluna configurada para a TableView
    */
    private TableColumn<Produto, String> criarColuna(String title, String property) {
        TableColumn<Produto, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property)); // Define a propriedade da coluna
        return col;
    }

}
