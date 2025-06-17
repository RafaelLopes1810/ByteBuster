package model;

public class VeiculoModel {
    private String placa;
    private ClienteModel cliente; // Correção: Certifique-se de que o ClienteModel está importado corretamente

    public VeiculoModel(String placa) {
        this.placa = placa;
    }

    public VeiculoModel(String placa, ClienteModel cliente) {
        this.placa = placa;
        this.cliente = cliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return placa;
    }

    public void setProprietarios(ClienteModel cliente) {
    }
}
