package test;

import model.ClienteModel;
import model.VeiculoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ClienteModelTest {

    private ClienteModel cliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteModel("João Silva", "12345");
    }

    @Test
    void testSetAndGetTotalGasto() {
        BigDecimal totalGasto = new BigDecimal("1500.75");
        cliente.setTotalGasto(totalGasto);

        assertEquals(totalGasto, cliente.getTotalGasto());
    }

    @Test
    void testGetAndSetNome() {
        assertEquals("João Silva", cliente.getNome());

        cliente.setNome("Maria Oliveira");
        assertEquals("Maria Oliveira", cliente.getNome());
    }

    @Test
    void testGetAndSetId() {
        assertEquals("12345", cliente.getId());

        cliente.setId("67890");
        assertEquals("67890", cliente.getId());
    }

    @Test
    void testAdicionarVeiculoAoCliente() {
        VeiculoModel veiculo = new VeiculoModel("XYZ-9876", cliente);
        cliente.adicionarVeiculoAoCliente(veiculo);

        // Verifica se a lista de veiculos do cliente não está vazia
        assertTrue(!cliente.getVeiculos().isEmpty());

        // Verifica retorno da lista de veiculos é igual ao adicionado
        assertEquals(veiculo, cliente.getVeiculo(0));
    }

    @Test
    void testContemVeiculoEmCliente() {
        VeiculoModel veiculo = new VeiculoModel("XYZ-9876", cliente);
        cliente.adicionarVeiculoAoCliente(veiculo);

        assertEquals("XYZ-9876", cliente.getVeiculo(0).getPlaca());
        assertEquals(cliente, veiculo.getCliente());
    }
}
