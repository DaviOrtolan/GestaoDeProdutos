import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private final Connection CONEXAO_DB;

    // Contrutor que inicializa a conexão com o Banco de Dados
    public ProdutoDAO(Connection conexao) {
        this.CONEXAO_DB = conexao;
    }

    // Método para inserir um novo produto no Banco
    public void inserir(Produto produto) {
        String sql = "INSERT INTO produtos (nome_produto, quantidade, preco, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
           System.err.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    // Método para excluir todos os produtos do Banco de Dados
    public void excluirTodos() {
        String sql = "DELETE FROM produtos"; 
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir todos os produtos: " + e.getMessage());
        }
    }
}
