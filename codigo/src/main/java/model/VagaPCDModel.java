package model;

public class VagaPCDModel extends VagaModel {
 
    protected static double desconto = 0.87;

    public VagaPCDModel(String id) {
        super(id);
    }

    @Override
    public String getTipo() {
        return "PCD";
    }

    @Override
    public double calcularPreco(int minutos) {
        return Math.min((minutos / 15) * precoPorFacao * desconto, limite);
    }
}
