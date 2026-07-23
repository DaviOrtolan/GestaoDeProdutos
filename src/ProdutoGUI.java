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
    }
}
