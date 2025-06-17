package dao;

import exceptions.EstacionamentoDAOException;
import model.*;
import conectionBD.BancoDados;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstacionamentoDAO {

    public int salvarEstacionamento(EstacionamentoModel estacionamento) throws EstacionamentoDAOException {
        String sql = "INSERT INTO estacionamento (nome, endereco, telefone) VALUES (?, ?, ?) RETURNING id_estacionamento";

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, estacionamento.getNome());
            stmt.setString(2, estacionamento.getEndereco());
            stmt.setString(3, estacionamento.getTelefone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_estacionamento"); // Retorna o ID gerado
                }
            }
        } catch (SQLException e) {
            throw new EstacionamentoDAOException("Erro ao salvar estacionamento", e);
        }
        return 0; // Retorne 0 ou lance uma exceção se falhar
    }

    public List<EstacionamentoModel> listarEstacionamentos() throws EstacionamentoDAOException {
        List<EstacionamentoModel> estacionamentos = new ArrayList<>();
        String sql = "SELECT id_estacionamento, nome, endereco, telefone FROM estacionamento";

        try (Connection connection = BancoDados.getInstancia().getConexao();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_estacionamento");
                String nome = rs.getString("nome");
                String endereco = rs.getString("endereco");
                String telefone = rs.getString("telefone");

                EstacionamentoModel estacionamento = new EstacionamentoModel(id, nome, endereco, telefone);
                estacionamentos.add(estacionamento);
            }

        } catch (SQLException e) {
            throw new EstacionamentoDAOException("Erro ao listar estacionamentos", e);
        }

        return estacionamentos;
    }

    public boolean removerEstacionamentoPorId(int id) throws EstacionamentoDAOException {
        String sqlVagas = "DELETE FROM vaga WHERE id_estacionamento = ?"; // Remove dependências
        String sqlEstacionamento = "DELETE FROM estacionamento WHERE id_estacionamento = ?";
        boolean removed = false;

        try (Connection connection = BancoDados.getInstancia().getConexao()) {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtVagas = connection.prepareStatement(sqlVagas);
                    PreparedStatement stmtEstacionamento = connection.prepareStatement(sqlEstacionamento)) {

                // Remove as vagas relacionadas ao estacionamento
                stmtVagas.setInt(1, id);
                stmtVagas.executeUpdate();

                // Remove o estacionamento
                stmtEstacionamento.setInt(1, id);
                int rowsDeleted = stmtEstacionamento.executeUpdate();
                removed = rowsDeleted > 0;

                if (removed) {
                    System.out.println("Estacionamento com ID " + id + " removido com sucesso.");
                }

                // Confirma a transação
                connection.commit();

            } catch (SQLException e) {
                // Em caso de erro, desfaz a transação
                connection.rollback();
                System.err.println("Erro ao remover estacionamento e suas dependências: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new EstacionamentoDAOException("Erro ao remover estacionamento", e);
        }

        return removed;
    }

    public void salvarVagas(List<VagaModel> vagas) throws EstacionamentoDAOException {
        String sql = "INSERT INTO vaga (id_vaga, ocupada, id_estacionamento, tipo) VALUES (?, ?, ?, ?)";

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (VagaModel vaga : vagas) {
                stmt.setString(1, vaga.getId());
                stmt.setBoolean(2, vaga.isOcupada());
                stmt.setInt(3, vaga.getIdEstacionamento());
                stmt.setString(4, vaga.getTipo());
                stmt.addBatch();

                // Log para verificar cada vaga que está sendo salva
                System.out.printf("Salvando vaga: ID=%s, Ocupada=%b, Estacionamento=%d, Tipo=%s%n",
                        vaga.getId(), vaga.isOcupada(), vaga.getIdEstacionamento(), vaga.getTipo());
            }

            int[] resultados = stmt.executeBatch();
            System.out.printf("Batch executado. Total de registros salvos: %d%n", resultados.length);
        } catch (SQLException e) {
            throw new EstacionamentoDAOException("Erro ao salvar vagas", e);
        }
    }

    public EstacionamentoModel buscarEstacionamentoPorId(int id) throws EstacionamentoDAOException {
        String sql = "SELECT id_estacionamento, nome, endereco, telefone FROM estacionamento WHERE id_estacionamento = ?";
        EstacionamentoModel estacionamento = null;

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String endereco = rs.getString("endereco");
                    String telefone = rs.getString("telefone");

                    estacionamento = new EstacionamentoModel(id, nome, endereco, telefone);
                }
            }

        } catch (SQLException e) {
            throw new EstacionamentoDAOException("Erro ao buscar estacionamento por ID", e);
        }

        return estacionamento;
    }

    public List<VagaModel> listarVagasPorEstacionamento(int idEstacionamento) {
        List<VagaModel> vagas = new ArrayList<>();
        String sql = "SELECT id_vaga, tipo, ocupada FROM vaga WHERE id_estacionamento = ?";

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEstacionamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String idVaga = rs.getString("id_vaga");
                    String tipo = rs.getString("tipo");
                    boolean ocupada = rs.getBoolean("ocupada");

                    VagaModel vaga;
                    switch (tipo) {
                        case "PCD":
                            vaga = new VagaPCDModel(idVaga);
                            break;
                        case "VIP":
                            vaga = new VagaVIPModel(idVaga);
                            break;
                        case "Idoso":
                            vaga = new VagaIdosoModel(idVaga);
                            break;
                        default:
                            vaga = new VagaPadraoModel(idVaga);
                    }

                    vaga.setOcupada(ocupada);
                    vaga.setIdEstacionamento(idEstacionamento);
                    vagas.add(vaga);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar vagas por estacionamento: " + e.getMessage());
        }

        return vagas;
    }

    public static List<Object[]> buscarRankingEstacionamentos() {
        List<Object[]> ranking = new ArrayList<>();
        String sql = "SELECT e.nome, COALESCE(SUM(t.custo), 0) AS total_faturado " +
                "FROM estacionamento e " +
                "LEFT JOIN ticket t ON e.id_estacionamento = t.id_estacionamento " +
                "GROUP BY e.nome " +
                "ORDER BY total_faturado DESC";

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                double totalFaturado = rs.getDouble("total_faturado");
                ranking.add(new Object[] { nome, totalFaturado });
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ranking de estacionamentos: " + e.getMessage());
        }

        return ranking;
    }

    public List<Map<String, Object>> listarRankingUtilizacao() {
        String sql = """
                    SELECT
                        e.id_estacionamento AS idEstacionamento,
                        e.nome AS nomeEstacionamento,
                        COALESCE(COUNT(t.id_ticket), 0) AS totalUtilizacoes
                    FROM estacionamento e
                    LEFT JOIN ticket t ON e.id_estacionamento = t.id_estacionamento
                    GROUP BY e.id_estacionamento, e.nome
                    ORDER BY totalUtilizacoes DESC
                """;

        List<Map<String, Object>> ranking = new ArrayList<>();

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("idEstacionamento", rs.getInt("idEstacionamento"));
                row.put("nomeEstacionamento", rs.getString("nomeEstacionamento"));
                row.put("totalUtilizacoes", rs.getInt("totalUtilizacoes"));
                ranking.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar ranking de utilização: " + e.getMessage());
        }

        return ranking;
    }

}
