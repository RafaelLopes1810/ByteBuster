package model;

import dao.EstacionamentoDAO;

import java.util.ArrayList;
import java.util.List;

public class EstacionamentoModel {
    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private List<VagaModel> vagas;

    public EstacionamentoModel(String nome, String endereco, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.vagas = new ArrayList<>();
    }

    public EstacionamentoModel(int id, String nome, String endereco, String telefone) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.vagas = new ArrayList<>();
    }

    public void carregarVagas() {
        EstacionamentoDAO estacionamentoDAO = new EstacionamentoDAO();
        this.vagas = estacionamentoDAO.listarVagasPorEstacionamento(this.id);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public List<VagaModel> getVagas() {
        return vagas;
    }
}
