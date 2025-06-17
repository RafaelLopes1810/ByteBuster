package test;

import model.VagaPCDModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VagaPCDModelTest {

    @Test
    public void testCalcularPrecoVagaPCD() {
        VagaPCDModel vagaPCD = new VagaPCDModel("PCD1");

        // Cenário 1: Minutos dentro do limite
        int minutos = 45; // 3 frações de 15 minutos
        double desconto = 0.87; // Usando o valor esperado para o teste
        double expectedPreco = 3 * VagaPCDModel.getPrecoPorFacao() * desconto;
        assertEquals(expectedPreco, vagaPCD.calcularPreco(minutos), 0.01);

        // Cenário 2: Minutos suficientes para exceder o limite
        minutos = 500; // Muitas frações
        double limite = VagaPCDModel.getLimite();
        assertEquals(limite, vagaPCD.calcularPreco(minutos), 0.01);
    }

    @Test
    public void testGetTipoVagaPCD() {
        VagaPCDModel vagaPCD = new VagaPCDModel("PCD2");
        assertEquals("PCD", vagaPCD.getTipo());
    }
}
