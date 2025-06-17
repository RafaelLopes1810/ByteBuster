package test;

import model.VagaIdosoModel;
import model.VeiculoModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VagaIdosoModelTest {

    @Test
    public void testCalcularPreco() {
        // Criando uma instância de VagaIdosoModel
        VagaIdosoModel vagaIdoso = new VagaIdosoModel("V1");

        // Cenário 1: Teste com minutos suficientes para gerar um preço abaixo do limite
        int minutos1 = 30; // 2 frações de 15 minutos
        double expectedPreco1 = (2 * VagaIdosoModel.getPrecoPorFacao() * 0.85); // Aplicando desconto
        assertEquals(expectedPreco1, vagaIdoso.calcularPreco(minutos1), 0.01);

        // Cenário 2: Teste com minutos que geram um preço acima do limite
        int minutos2 = 300; // Muitas frações, excedendo o limite
        double expectedPreco2 = VagaIdosoModel.getLimite(); // Deve respeitar o limite
        assertEquals(expectedPreco2, vagaIdoso.calcularPreco(minutos2), 0.01);

        // Cenário 3: Teste com minutos zero (custo deve ser zero)
        int minutos3 = 0;
        double expectedPreco3 = 0.0;
        assertEquals(expectedPreco3, vagaIdoso.calcularPreco(minutos3), 0.01);
    }

    @Test
    public void testGetTipo() {
        // Criando uma instância de VagaIdosoModel
        VagaIdosoModel vagaIdoso = new VagaIdosoModel("V2");

        // Validando o tipo da vaga
        assertEquals("Idoso", vagaIdoso.getTipo());
    }

    @Test
    public void testOcuparLiberarVaga() {
        // Criando uma instância de VagaIdosoModel
        VagaIdosoModel vagaIdoso = new VagaIdosoModel("V3");

        // Verificando estado inicial
        assertFalse(vagaIdoso.isOcupada());
        assertNull(vagaIdoso.getVeiculo());

        // Criando um veículo fictício para ocupar a vaga
        VeiculoModel veiculo = new VeiculoModel("ABC-1234");
        vagaIdoso.ocuparVaga(veiculo);

        // Verificando estado após ocupar a vaga
        assertTrue(vagaIdoso.isOcupada());
        assertEquals(veiculo, vagaIdoso.getVeiculo());

        // Liberando a vaga
        vagaIdoso.liberarVaga();

        // Verificando estado após liberar a vaga
        assertFalse(vagaIdoso.isOcupada());
        assertNull(vagaIdoso.getVeiculo());
    }
}
