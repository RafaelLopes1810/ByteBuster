package test;

import model.VagaPadraoModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VagaPadraoModelTest {

    @Test
    public void testCalcularPrecoVagaPadrao() {
        VagaPadraoModel vagaPadrao = new VagaPadraoModel("P1");

        // Cenário 1: Minutos dentro do limite
        int minutos = 30; // 2 frações de 15 minutos
        double expectedPreco = 2 * VagaPadraoModel.getPrecoPorFacao();
        assertEquals(expectedPreco, vagaPadrao.calcularPreco(minutos), 0.01);

        // Cenário 2: Minutos suficientes para exceder o limite
        minutos = 300; // Muitas frações
        expectedPreco = VagaPadraoModel.getLimite();
        assertEquals(expectedPreco, vagaPadrao.calcularPreco(minutos), 0.01);
    }

    @Test
    public void testGetTipoVagaPadrao() {
        VagaPadraoModel vagaPadrao = new VagaPadraoModel("P2");
        assertEquals("Padrão", vagaPadrao.getTipo());
    }
}
