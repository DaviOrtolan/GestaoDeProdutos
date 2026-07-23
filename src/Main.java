import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try(Connection conexao = ConexaoDB.conectar()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(conexao);

            // Lista todos os produtos (deve estar vazio neste ponto)
            mostrarProdutos(produtoDAO);

            // Exemplo de inserção de produtos
            Produto novoProduto1 = new Produto("Notebook", 10, 1999.99, "Em estoque");
            Produto novoProduto2 = new Produto("Smartphone", 20, 1499.99, "Estoque Baixo");
            Produto novoProduto3 = new Produto("Tablet", 15, 799.99, "Estoque Baixo");

            produtoDAO.inserir(novoProduto1);
            produtoDAO.inserir(novoProduto2);
            produtoDAO.inserir(novoProduto3);

            // Lista todos os produtos novamente
            mostrarProdutos(produtoDAO);
        }
    }
}
