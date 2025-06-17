package dao;

import model.VoucherModel;
import conectionBD.BancoDados;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    public double calcularArrecadacaoTotal(int idEstacionamento) {
        String sql = "SELECT COALESCE(SUM(custo), 0) AS total FROM ticket WHERE id_estacionamento = ?";
        double totalArrecadado = 0.0;

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEstacionamento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalArrecadado = rs.getDouble("total");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular a arrecadação total: " + e.getMessage());
        }

        return totalArrecadado;
    }

    public double calcularValorMedioUtilizacao(int idEstacionamento) {
        String sql = "SELECT COALESCE(AVG(custo), 0) AS media FROM ticket WHERE id_estacionamento = ?";
        double valorMedio = 0.0;

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEstacionamento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    valorMedio = rs.getDouble("media");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular o valor médio de utilização: " + e.getMessage());
        }

        return valorMedio;
    }

    public double calcularArrecadacaoMensal(int idEstacionamento, int mes, int ano) {
        String sql = "SELECT COALESCE(SUM(custo), 0) AS total " +
                "FROM ticket " +
                "WHERE id_estacionamento = ? " +
                "AND EXTRACT(MONTH FROM entrada) = ? " +
                "AND EXTRACT(YEAR FROM entrada) = ?";
        double totalArrecadado = 0.0;

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEstacionamento);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalArrecadado = rs.getDouble("total");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular a arrecadação mensal: " + e.getMessage());
        }

        return totalArrecadado;
    }

    public boolean salvarTicket(VoucherModel ticket) {
        String sql = "INSERT INTO ticket (id_estacionamento, id_vaga, id_cliente, entrada, custo, placa) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, ticket.getIdEstacionamento());
            stmt.setString(2, ticket.getIdVaga());
            stmt.setInt(3, ticket.getIdCliente());

            // Truncar o LocalDateTime para minutos antes de salvar
            LocalDateTime entradaTruncada = ticket.getEntrada().withSecond(0).withNano(0);
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(entradaTruncada));

            stmt.setBigDecimal(5, ticket.getCusto());
            stmt.setString(6, ticket.getPlaca());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o ticket no banco de dados: " + e.getMessage());
            return false;
        }
    }

    public List<VoucherModel> buscarTicketsAtivos(int idEstacionamento) {
        String sql = "SELECT * FROM ticket WHERE id_estacionamento = ? AND saida IS NULL";
        List<VoucherModel> tickets = new ArrayList<>();

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEstacionamento);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new VoucherModel(
                            rs.getInt("id_ticket"),
                            rs.getInt("id_estacionamento"),
                            rs.getString("id_vaga"),
                            rs.getInt("id_cliente"),
                            rs.getTimestamp("entrada").toLocalDateTime(),
                            null, // Saída é null para tickets ativos
                            rs.getBigDecimal("custo"),
                            rs.getString("placa")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tickets ativos: " + e.getMessage());
        }

        return tickets;
    }

    public String buscarVeiculoPorVaga(String idVaga, int idEstacionamento) {
        String sql = "SELECT placa FROM ticket WHERE id_vaga = ? AND id_estacionamento = ? AND saida IS NULL";
        String placa = null;

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, idVaga);
            stmt.setInt(2, idEstacionamento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    placa = rs.getString("placa");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar veículo por vaga: " + e.getMessage());
        }

        return placa;
    }

    public boolean registrarSaida(String idVaga, int idEstacionamento, double preco) {
        String sql = "UPDATE ticket SET saida = ?, custo = ? WHERE id_vaga = ? AND id_estacionamento = ? AND saida IS NULL";

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Truncar o horário atual para minutos
            LocalDateTime saidaTruncada = LocalDateTime.now().withSecond(0).withNano(0);

            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(saidaTruncada));
            stmt.setBigDecimal(2, BigDecimal.valueOf(preco));
            stmt.setString(3, idVaga);
            stmt.setInt(4, idEstacionamento);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar saída: " + e.getMessage());
            return false;
        }
    }

    // Buscar todos os tickets fechados (com saída registrada)
    public List<VoucherModel> buscarTicketsFechados(int idEstacionamento) {
        List<VoucherModel> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE id_estacionamento = ? AND saida IS NOT NULL";

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idEstacionamento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VoucherModel ticket = new VoucherModel(
                        rs.getInt("id_ticket"),
                        rs.getInt("id_estacionamento"),
                        rs.getString("id_vaga"),
                        rs.getInt("id_cliente"),
                        rs.getTimestamp("entrada").toLocalDateTime(),
                        rs.getTimestamp("saida").toLocalDateTime(), // Saída registrada
                        rs.getBigDecimal("custo"),
                        rs.getString("placa")
                );
                tickets.add(ticket);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar tickets fechados: " + e.getMessage());
        }

        return tickets;
    }

    public VoucherModel buscarTicketPorVaga(String idVaga, int idEstacionamento) {
        String sql = "SELECT * FROM ticket WHERE id_vaga = ? AND id_estacionamento = ? AND saida IS NULL";

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, idVaga);
            stmt.setInt(2, idEstacionamento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new VoucherModel(
                            rs.getInt("id_ticket"),
                            rs.getInt("id_estacionamento"),
                            rs.getString("id_vaga"),
                            rs.getInt("id_cliente"),
                            rs.getTimestamp("entrada").toLocalDateTime(),
                            null, // Sem saída ainda
                            rs.getBigDecimal("custo"),
                            rs.getString("placa")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar ticket por vaga: " + e.getMessage());
        }

        return null;
    }

    public boolean temTicketAberto(String placa) {
        String sql = "SELECT COUNT(*) AS total FROM ticket WHERE placa = ? AND saida IS NULL";

        try (Connection connection = BancoDados.getInstancia().getConexao();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0; // Retorna true se existir pelo menos 1 ticket aberto
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar ticket aberto: " + e.getMessage());
        }

        return false;
    }


}

