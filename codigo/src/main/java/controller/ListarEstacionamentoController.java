package controller;

import dao.EstacionamentoDAO;
import exceptions.EstacionamentoDAOException;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListarEstacionamentoController {

    private EstacionamentoDAO estacionamentoDAO;

    public ListarEstacionamentoController() {
        this.estacionamentoDAO = new EstacionamentoDAO();
    }

    public List<EstacionamentoModel> listarEstacionamentos() throws EstacionamentoDAOException {
        return estacionamentoDAO.listarEstacionamentos();
    }

    public boolean adicionarEstacionamento(String nome, String endereco, String telefone, int colunas, int vagasPorColuna) throws EstacionamentoDAOException {
        EstacionamentoModel estacionamento = new EstacionamentoModel(nome, endereco, telefone);

        int idEstacionamento = estacionamentoDAO.salvarEstacionamento(estacionamento);
        estacionamento.setId(idEstacionamento);

        if (idEstacionamento > 0) {
            gerarVagas(estacionamento, colunas, vagasPorColuna);
            return true;
        }
        return false;
    }

    private void gerarVagas(EstacionamentoModel estacionamento, int colunas, int vagasPorColuna) throws EstacionamentoDAOException {
        List<VagaModel> vagas = new ArrayList<>();
        int totalVagas = colunas * vagasPorColuna;

        // Define a proporção de vagas por tipo
        int vagasPCD = totalVagas / 5;  // 20% para PCD
        int vagasIdoso = totalVagas / 5; // 20% para Idoso
        int vagasVIP = totalVagas / 10;  // 10% para VIP
        int vagasPadrao = totalVagas - (vagasPCD + vagasIdoso + vagasVIP); // Restante para Padrão

        int vagasCriadasPCD = 0;
        int vagasCriadasIdoso = 0;
        int vagasCriadasVIP = 0;

        for (int c = 0; c < colunas; c++) {
            char letraColuna = (char) ('A' + c);
            for (int v = 1; v <= vagasPorColuna; v++) {
                String idVaga = letraColuna + String.format("%02d", v);

                VagaModel vaga;
                if (vagasCriadasPCD < vagasPCD) {
                    vaga = new VagaPCDModel(idVaga);
                    vagasCriadasPCD++;
                } else if (vagasCriadasIdoso < vagasIdoso) {
                    vaga = new VagaIdosoModel(idVaga);
                    vagasCriadasIdoso++;
                } else if (vagasCriadasVIP < vagasVIP) {
                    vaga = new VagaVIPModel(idVaga);
                    vagasCriadasVIP++;
                } else {
                    vaga = new VagaPadraoModel(idVaga);
                }

                vaga.setOcupada(false);
                vaga.setIdEstacionamento(estacionamento.getId());
                vagas.add(vaga);
            }
        }

        estacionamentoDAO.salvarVagas(vagas);
    }


    public boolean removerEstacionamento(int id) throws EstacionamentoDAOException {
        return estacionamentoDAO.removerEstacionamentoPorId(id);
    }

    public EstacionamentoModel buscarEstacionamentoPorId(int id) throws EstacionamentoDAOException {
        return estacionamentoDAO.buscarEstacionamentoPorId(id);
    }

    public List<Object[]> obterRankingEstacionamentos() {
        return EstacionamentoDAO.buscarRankingEstacionamentos();
    }

    public List<Map<String, Object>> getRankingUtilizacao() {
        return estacionamentoDAO.listarRankingUtilizacao();
    }

}
