package test;

import model.VoucherModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class VoucherModelTest {

    @Test
    public void testTicketModelWithNullValues() {
        // Dados de entrada com valores nulos em campos opcionais
        int idTicket = 2;
        int idEstacionamento = 101;
        String idVaga = null;
        int idCliente = 501;
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 5, 9, 30);
        LocalDateTime saida = null;
        BigDecimal custo = null;
        String placa = "XYZ-5678";

        // Criação do objeto TicketModel
        VoucherModel ticket = new VoucherModel(idTicket, idEstacionamento, idVaga, idCliente, entrada, saida, custo, placa);

        // Validações para campos não nulos
        assertEquals(idTicket, ticket.getIdTicket());
        assertEquals(idEstacionamento, ticket.getIdEstacionamento());
        assertEquals(idCliente, ticket.getIdCliente());
        assertEquals(entrada, ticket.getEntrada());
        assertEquals(placa, ticket.getPlaca());

        // Validações para campos nulos
        assertNull(ticket.getIdVaga());
        assertNull(ticket.getSaida());
        assertNull(ticket.getCusto());
    }

    @Test
    public void testTicketModelWithDifferentValues() {
        // Dados diferentes para verificar consistência
        int idTicket = 3;
        int idEstacionamento = 102;
        String idVaga = "B5";
        int idCliente = 502;
        LocalDateTime entrada = LocalDateTime.of(2024, 12, 10, 8, 15);
        LocalDateTime saida = LocalDateTime.of(2024, 12, 10, 11, 45);
        BigDecimal custo = new BigDecimal("15.00");
        String placa = "LMN-9101";

        // Criação do objeto TicketModel
        VoucherModel ticket = new VoucherModel(idTicket, idEstacionamento, idVaga, idCliente, entrada, saida, custo, placa);

        // Validações
        assertEquals(idTicket, ticket.getIdTicket());
        assertEquals(idEstacionamento, ticket.getIdEstacionamento());
        assertEquals(idVaga, ticket.getIdVaga());
        assertEquals(idCliente, ticket.getIdCliente());
        assertEquals(entrada, ticket.getEntrada());
        assertEquals(saida, ticket.getSaida());
        assertEquals(custo, ticket.getCusto());
        assertEquals(placa, ticket.getPlaca());
    }
}
