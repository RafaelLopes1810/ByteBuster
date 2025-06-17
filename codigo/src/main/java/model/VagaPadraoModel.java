package model;

public class VagaPadraoModel extends VagaModel {

    public VagaPadraoModel(String id) {
        super(id);
    }

    @Override
    public double calcularPreco(int minutos) {
        return Math.min(getPrecoPorFacao() * (minutos / 15), getLimite());
    }

    @Override
    public String getTipo() {
        return "Padrão";
    }
}
