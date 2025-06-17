package model;

public abstract class VagaModel {
    private String id;
    private boolean ocupada;
    private VeiculoModel veiculo;
    private int idEstacionamento;

    protected static double precoPorFacao = 4.0;
    protected static double limite = 50.0;

    public VagaModel(String id) {
        this.id = id;
        this.ocupada = false;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public int getIdEstacionamento() {
        return idEstacionamento;
    }

    public void setIdEstacionamento(int idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
    }

    public VeiculoModel getVeiculo() {
        return veiculo;
    }

    // Métodos para ocupar e liberar vaga
    public void ocuparVaga(VeiculoModel veiculo) {
        this.veiculo = veiculo;
        this.ocupada = true;
    }

    public void liberarVaga() {
        this.veiculo = null;
        this.ocupada = false;
    }

    // Métodos abstratos
    public abstract double calcularPreco(int minutos);

    public abstract String getTipo();

    // Métodos estáticos
    public static double getPrecoPorFacao() {
        return precoPorFacao;
    }

    public static double getLimite() {
        return limite;
    }
}

