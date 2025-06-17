package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.ClienteModel;
import model.VeiculoModel;

public class VeiculoModelTest {

    private VeiculoModel veiculo;
    private ClienteModel cliente;

    @BeforeEach
    public void setUp() {
        cliente = new ClienteModel("Maria", "001");
        veiculo = new VeiculoModel("XYZ-1234");
    }

    // Teste 1: Verificar métodos getPlaca e setPlaca
    @Test
    public void testGetSetPlaca() {
        assertEquals("XYZ-1234", veiculo.getPlaca(), "A placa inicial deveria ser 'XYZ-1234'");
        veiculo.setPlaca("ABC-5678");
        assertEquals("ABC-5678", veiculo.getPlaca(), "A placa deveria ser 'ABC-5678' após a configuração");
    }

    // Teste 2: Verificar métodos setCliente e getCliente
    @Test
    public void testSetGetCliente() {
        veiculo.setCliente(cliente);
        assertEquals(cliente, veiculo.getCliente(), "O cliente associado ao veículo deveria ser 'Maria'");
        assertEquals("Maria", veiculo.getCliente().getNome(), "O nome do cliente deveria ser 'Maria'");
    }
}
