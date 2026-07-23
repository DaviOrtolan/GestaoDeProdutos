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

            // Exemplo de consulta por ID (consultando produto com ID 1)
            Produto produtoConsultado = produtoDAO.consultarPorId(1);
            if (produtoConsultado != null) {
                System.out.println("Produto encontrado: " + produtoConsultado.getNome());
            } else {
                System.out.println("Produto não encontrado!");
            }
        } catch (Exception e) {
            System.err.println("Erro geral: " + e.getMessage());
        }
    }

    // Método para listar os produtos
    private static void mostrarProdutos(ProdutoDAO produtoDAO) {
        List<Produto> todosProdutos = produtoDAO.listarTodos();
        if (todosProdutos.isEmpty()) {
            System.out.println("Nenhum produto encontrado!");
        } else {
            System.out.println("Lista de produtos: ");
            for (Produto p : todosProdutos) {
                System.out.println(p.getId() + ": " + p.getNome() + ": " + p.getPreco() );
            }
        }
    }
}
