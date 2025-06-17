package test;

import model.VagaVIPModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VagaVIPModelTest {

    @Test
    public void testCalcularPrecoVagaVIP() {
        VagaVIPModel vagaVIP = new VagaVIPModel("VIP1");

        // Cenário 1: Minutos dentro do limite
        int minutos = 60; // 4 frações de 15 minutos
        double taxa = 1.2; // Valor esperado para o teste
        double expectedPreco = 4 * VagaVIPModel.getPrecoPorFacao() * taxa;
        assertEquals(expectedPreco, vagaVIP.calcularPreco(minutos), 0.01);

        // Cenário 2: Minutos suficientes para exceder o limite
        minutos = 600; // Muitas frações
        double limite = VagaVIPModel.getLimite();
        assertEquals(limite, vagaVIP.calcularPreco(minutos), 0.01);
    }

    @Test
    public void testGetTipoVagaVIP() {
        VagaVIPModel vagaVIP = new VagaVIPModel("VIP2");
        assertEquals("VIP", vagaVIP.getTipo());
    }
}
