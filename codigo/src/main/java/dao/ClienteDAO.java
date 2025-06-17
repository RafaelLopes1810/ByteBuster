package dao;

import exceptions.ClienteDAOException;
import model.ClienteModel;
import model.VoucherModel;
import model.VeiculoModel;
import conectionBD.BancoDados;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void salvarCliente(ClienteModel cliente) throws ClienteDAOException {
        String sql = "INSERT INTO cliente (nome) VALUES (?) RETURNING id_cliente";

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente.setId(String.valueOf(rs.getInt("id_cliente")));
            } else {
                throw new ClienteDAOException("Falha ao obter ID do cliente após inserção.");
            }
            System.out.println("Cliente salvo com ID: " + cliente.getId());
        } catch (SQLException e) {
            throw new ClienteDAOException("Erro ao salvar cliente", e);
        }
    }

    public boolean existeIdCliente(String idCliente) throws ClienteDAOException {
        String sql = "SELECT 1 FROM cliente WHERE id_cliente = ?";
        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idCliente));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException | NumberFormatException e) {
            throw new ClienteDAOException("Erro ao verificar existência do cliente", e);
        }
    }

    public void adicionarVeiculoAoCliente(String idCliente, VeiculoModel veiculo) throws ClienteDAOException {
        if (veiculoJaExiste(veiculo.getPlaca())) {
            System.out.println("Veículo com a placa " + veiculo.getPlaca() + " já existe!");
            return;
        }

        String sql = "INSERT INTO veiculo (placa, id_cliente) VALUES (?, ?)";

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, Integer.parseInt(idCliente));
            stmt.executeUpdate();
            System.out.println("Veículo adicionado ao cliente com ID: " + idCliente);
        } catch (SQLException | NumberFormatException e) {
            throw new ClienteDAOException("Erro ao adicionar veículo ao cliente", e);
        }
    }

    public boolean veiculoJaExiste(String placa) throws ClienteDAOException {
        String sql = "SELECT 1 FROM veiculo WHERE placa = ?";
        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new ClienteDAOException("Erro ao verificar existência do veículo", e);
        }
    }

    public List<ClienteModel> listarTodos() throws ClienteDAOException {
        String sqlClientes = "SELECT * FROM cliente";
        String sqlVeiculos = "SELECT * FROM veiculo WHERE id_cliente = ?";
        List<ClienteModel> clientes = new ArrayList();

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmtClientes = conn.prepareStatement(sqlClientes);
                ResultSet rsClientes = stmtClientes.executeQuery()) {
            while (rsClientes.next()) {
                String id = String.valueOf(rsClientes.getInt("id_cliente"));
                String nome = rsClientes.getString("nome");
                ClienteModel cliente = new ClienteModel(nome, id);
                try (PreparedStatement stmtVeiculos = conn.prepareStatement(sqlVeiculos)) {
                    stmtVeiculos.setInt(1, Integer.parseInt(id));
                    ResultSet rsVeiculos = stmtVeiculos.executeQuery();
                    while (rsVeiculos.next()) {
                        String placa = rsVeiculos.getString("placa");
                        cliente.adicionarVeiculo(new VeiculoModel(placa));
                    }
                } catch (NumberFormatException e) {
                    throw new ClienteDAOException("ID de cliente inválido: " + id, e);
                }
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new ClienteDAOException("Erro ao listar clientes", e);
        }
        return clientes;
    }

    public ClienteModel buscarPorId(String id) throws ClienteDAOException {
        String sqlCliente = "SELECT * FROM cliente WHERE id_cliente = ?";
        String sqlVeiculos = "SELECT * FROM veiculo WHERE id_cliente = ?";
        ClienteModel cliente = null;

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
            stmtCliente.setInt(1, Integer.parseInt(id));
            ResultSet rsCliente = stmtCliente.executeQuery();

            if (rsCliente.next()) {
                String nome = rsCliente.getString("nome");
                cliente = new ClienteModel(nome, id);

                try (PreparedStatement stmtVeiculos = conn.prepareStatement(sqlVeiculos)) {
                    stmtVeiculos.setInt(1, Integer.parseInt(id));
                    ResultSet rsVeiculos = stmtVeiculos.executeQuery();
                    while (rsVeiculos.next()) {
                        String placa = rsVeiculos.getString("placa");
                        cliente.adicionarVeiculo(new VeiculoModel(placa));
                    }
                } catch (NumberFormatException e) {
                    throw new ClienteDAOException("ID de cliente inválido: " + id, e);
                }
                System.out.println("Cliente encontrado com ID: " + id);
            } else {
                System.out.println("Nenhum cliente encontrado com ID: " + id);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ClienteDAOException("Erro ao buscar cliente por ID", e);
        }
        return cliente;
    }

    public ClienteModel buscarClientePorPlaca(String placa) throws ClienteDAOException {
        String sql = "SELECT c.* FROM cliente c JOIN veiculo v ON c.id_cliente = v.id_cliente WHERE v.placa = ?";
        ClienteModel cliente = null;

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = String.valueOf(rs.getInt("id_cliente"));
                String nome = rs.getString("nome");
                cliente = new ClienteModel(nome, id);

                String sqlVeiculos = "SELECT * FROM veiculo WHERE id_cliente = ?";
                try (PreparedStatement stmtVeiculos = conn.prepareStatement(sqlVeiculos)) {
                    stmtVeiculos.setInt(1, Integer.parseInt(id));
                    ResultSet rsVeiculos = stmtVeiculos.executeQuery();
                    while (rsVeiculos.next()) {
                        String placaVeiculo = rsVeiculos.getString("placa");
                        cliente.adicionarVeiculo(new VeiculoModel(placaVeiculo));
                    }
                } catch (NumberFormatException e) {
                    throw new ClienteDAOException("ID de cliente inválido: " + id, e);
                }
                System.out.println("Cliente encontrado pela placa: " + placa);
            } else {
                System.out.println("Nenhum cliente encontrado para a placa: " + placa);
            }
        } catch (SQLException e) {
            throw new ClienteDAOException("Erro ao buscar cliente por placa", e);
        }
        return cliente;
    }

    public List<VoucherModel> listarTicketsDoCliente(String idCliente) throws ClienteDAOException {
        String sql = "SELECT * FROM ticket WHERE id_cliente = ?";
        List<VoucherModel> tickets = new ArrayList();

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idCliente));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                VoucherModel ticket = new VoucherModel(
                        rs.getInt("id_ticket"),
                        rs.getInt("id_estacionamento"),
                        rs.getString("id_vaga"),
                        rs.getInt("id_cliente"),
                        rs.getTimestamp("entrada").toLocalDateTime(),
                        rs.getTimestamp("saida") != null ? rs.getTimestamp("saida").toLocalDateTime() : null,
                        rs.getBigDecimal("custo"),
                        rs.getString("placa"));
                tickets.add(ticket);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ClienteDAOException("Erro ao buscar tickets do cliente", e);
        }
        return tickets;
    }

    public List<ClienteModel> obterRankingClientes(int mes, int ano) throws ClienteDAOException {
        String baseSql = """
                SELECT c.id_cliente, c.nome, SUM(t.custo) AS total_gasto
                FROM cliente c
                JOIN ticket t ON c.id_cliente = t.id_cliente
                """;

        StringBuilder sql = new StringBuilder(baseSql);

        if (mes > 0 || ano > 0) {
            sql.append(" WHERE ");
            if (mes > 0) {
                sql.append("EXTRACT(MONTH FROM t.entrada) = ? ");
            }
            if (mes > 0 && ano > 0) {
                sql.append("AND ");
            }
            if (ano > 0) {
                sql.append("EXTRACT(YEAR FROM t.entrada) = ? ");
            }
        }

        sql.append("""
                GROUP BY c.id_cliente, c.nome
                ORDER BY total_gasto DESC
                LIMIT 5
                """);

        List<ClienteModel> ranking = new ArrayList();

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (mes > 0) {
                stmt.setInt(paramIndex++, mes);
            }
            if (ano > 0) {
                stmt.setInt(paramIndex++, ano);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id_cliente"));
                String nome = rs.getString("nome");
                BigDecimal totalGasto = rs.getBigDecimal("total_gasto");
                ClienteModel cliente = new ClienteModel(nome, id);
                cliente.setTotalGasto(totalGasto);
                ranking.add(cliente);
            }
        } catch (SQLException e) {
            throw new ClienteDAOException("Erro ao obter ranking de clientes", e);
        }
        return ranking;
    }

    public List<VoucherModel> filtrarTicketsSimples(String idCliente, String entrada, String saida)
            throws ClienteDAOException {
        String baseSql = "SELECT * FROM ticket WHERE id_cliente = ?";
        StringBuilder sql = new StringBuilder(baseSql);

        if (!entrada.isEmpty()) {
            sql.append(" AND DATE(entrada) = ?");
        }
        if (!saida.isEmpty()) {
            sql.append(" AND DATE(saida) = ?");
        }

        List<VoucherModel> tickets = new ArrayList();

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            stmt.setInt(1, Integer.parseInt(idCliente));
            int paramIndex = 2;
            if (!entrada.isEmpty()) {
                stmt.setDate(paramIndex++, Date.valueOf(entrada));
            }
            if (!saida.isEmpty()) {
                stmt.setDate(paramIndex, Date.valueOf(saida));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                VoucherModel ticket = new VoucherModel(
                        rs.getInt("id_ticket"),
                        rs.getInt("id_estacionamento"),
                        rs.getString("id_vaga"),
                        rs.getInt("id_cliente"),
                        rs.getTimestamp("entrada").toLocalDateTime(),
                        rs.getTimestamp("saida") != null ? rs.getTimestamp("saida").toLocalDateTime() : null,
                        rs.getBigDecimal("custo"),
                        rs.getString("placa"));
                tickets.add(ticket);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ClienteDAOException("Erro ao filtrar tickets", e);
        }
        return tickets;
    }

    public List<ClienteModel> buscarPorNomeOuPlaca(String termo) throws ClienteDAOException {
        String sql = """
                SELECT DISTINCT c.id_cliente, c.nome, v.placa
                FROM cliente c
                LEFT JOIN veiculo v ON c.id_cliente = v.id_cliente
                WHERE LOWER(c.nome) LIKE ? OR LOWER(v.placa) LIKE ?
                """;

        List<ClienteModel> clientes = new ArrayList();

        try (Connection conn = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            String termoBusca = "%" + termo.toLowerCase() + "%";
            stmt.setString(1, termoBusca);
            stmt.setString(2, termoBusca);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = String.valueOf(rs.getInt("id_cliente"));
                    String nome = rs.getString("nome");
                    String placa = rs.getString("placa");
                    ClienteModel cliente = clientes.stream()
                            .filter(c -> c.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                    if (cliente == null) {
                        cliente = new ClienteModel(nome, id);
                        clientes.add(cliente);
                    }
                    if (placa != null) {
                        cliente.adicionarVeiculo(new VeiculoModel(placa));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ClienteDAOException("Erro ao buscar clientes por nome ou placa", e);
        }
        return clientes;
    }
}