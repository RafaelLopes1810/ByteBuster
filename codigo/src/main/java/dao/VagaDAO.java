package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exceptions.VagaDAOException;
import conectionBD.BancoDados;

public class VagaDAO {

    public static boolean atualizarStatusVaga(String idVaga, boolean ocupada, int idEstacionamento)
            throws VagaDAOException {
        String sql = "UPDATE vaga SET ocupada = ? WHERE id_vaga = ? AND id_estacionamento = ?";

        try (Connection connection = BancoDados.getInstancia().getConexao();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBoolean(1, ocupada);
            stmt.setString(2, idVaga);
            stmt.setInt(3, idEstacionamento);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new VagaDAOException("Erro ao atualizar o status da vaga", e);
        }
    }
}