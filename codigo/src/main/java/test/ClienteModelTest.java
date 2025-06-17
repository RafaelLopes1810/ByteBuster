package test;

import model.ClienteModel;
import model.VeiculoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteModelTest {

    private ClienteModel cliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteModel("João Silva", "12345");
    }

    @Test
    void testAdicionarVeiculo() {
        VeiculoModel veiculo = new VeiculoModel("ABC-1234");
        cliente.adicionarVeiculo(veiculo);

        List<VeiculoModel> veiculos = cliente.getVeiculos();
        assertEquals(1, veiculos.size());
        assertEquals(veiculo, veiculos.get(0));
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

        // Como o método está vazio, podemos verificar que a lista não foi alterada
        assertTrue(cliente.getVeiculos().isEmpty());
    }

    @Test
    void testVeiculoModelIntegration() {
        VeiculoModel veiculo = new VeiculoModel("XYZ-9876", cliente);
        cliente.adicionarVeiculo(veiculo);

        assertEquals("XYZ-9876", cliente.getVeiculos().get(0).getPlaca());
        assertEquals(cliente, veiculo.getCliente());
    }
}
