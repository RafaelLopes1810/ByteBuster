package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VoucherModel {
    private int idTicket;
    private int idEstacionamento;
    private String idVaga;
    private int idCliente;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private BigDecimal custo;
    private String placa;


    public VoucherModel(int idTicket, int idEstacionamento, String idVaga, int idCliente,
                        LocalDateTime entrada, LocalDateTime saida, BigDecimal custo, String placa) {
        this.idTicket = idTicket;
        this.idEstacionamento = idEstacionamento;
        this.idVaga = idVaga;
        this.idCliente = idCliente;
        this.entrada = entrada;
        this.saida = saida;
        this.custo = custo;
        this.placa = placa;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public int getIdEstacionamento() {
        return idEstacionamento;
    }

    public String getIdVaga() {
        return idVaga;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public BigDecimal getCusto() {
        return custo;
    }
    public String getPlaca() {
        return placa;
    }


    @Override
    public String toString() {
        return "TicketModel{" +
                "idTicket=" + idTicket +
                ", idEstacionamento=" + idEstacionamento +
                ", idVaga='" + idVaga + '\'' +
                ", idCliente=" + idCliente +
                ", entrada=" + entrada +
                ", saida=" + saida +
                ", custo=" + custo +
                ", placa=" + placa +

                '}';
    }
}
