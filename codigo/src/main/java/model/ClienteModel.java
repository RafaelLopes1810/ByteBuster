package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClienteModel {
    private String nome;
    private String id;
    private List<VeiculoModel> veiculos;
    private BigDecimal totalGasto;

    public ClienteModel(String nome, String id) {
        this.nome = nome;
        this.id = id;
        this.veiculos = new ArrayList<>();
    }

    public ClienteModel() {
        this.veiculos = new ArrayList<>();
    }

    public void adicionarVeiculo(VeiculoModel veiculo) {
        veiculos.add(veiculo);
    }

    public List<VeiculoModel> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<VeiculoModel> veiculos) {
        this.veiculos = veiculos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(BigDecimal totalGasto) {
        this.totalGasto = totalGasto;
    }

    public VeiculoModel getVeiculo(int index){
        return veiculos.get(index);
    }

    public void adicionarVeiculoAoCliente(VeiculoModel veiculo) {
        this.veiculos.add(veiculo);
    }
}
