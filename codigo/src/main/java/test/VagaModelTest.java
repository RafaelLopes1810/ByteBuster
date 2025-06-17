package test;

import model.VagaModel;
import model.VeiculoModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VagaModelTest {

    @Test
    public void testGettersSetters() {
        // Usando classe abstrata simulada para testar métodos concretos
        VagaModel vaga = new VagaModel("V1") {
            @Override
            public double calcularPreco(int minutos) {
                return 0; // Apenas para evitar erros de compilação
            }

            @Override
            public String getTipo() {
                return "Teste"; // Apenas para evitar erros de compilação
            }
        };

        // Testando getters
        assertEquals("V1", vaga.getId());
        assertFalse(vaga.isOcupada());

        // Testando setters
        vaga.setOcupada(true);
        assertTrue(vaga.isOcupada());

        vaga.setIdEstacionamento(123);
        assertEquals(123, vaga.getIdEstacionamento());
    }

    @Test
    public void testOcuparLiberarVaga() {
        // Usando classe abstrata simulada para testar métodos concretos
        VagaModel vaga = new VagaModel("V2") {
            @Override
            public double calcularPreco(int minutos) {
                return 0; // Apenas para evitar erros de compilação
            }

            @Override
            public String getTipo() {
                return "Teste"; // Apenas para evitar erros de compilação
            }
        };

        // Estado inicial
        assertFalse(vaga.isOcupada());
        assertNull(vaga.getVeiculo());

        // Criando um veículo fictício
        VeiculoModel veiculo = new VeiculoModel("ABC-1234");
        vaga.ocuparVaga(veiculo);

        // Verificando estado após ocupar a vaga
        assertTrue(vaga.isOcupada());
        assertEquals(veiculo, vaga.getVeiculo());

        // Liberando a vaga
        vaga.liberarVaga();

        // Verificando estado após liberar a vaga
        assertFalse(vaga.isOcupada());
        assertNull(vaga.getVeiculo());
    }

    @Test
    public void testStaticMethods() {
        // Testando os métodos estáticos
        assertEquals(4.0, VagaModel.getPrecoPorFacao(), 0.01);
        assertEquals(50.0, VagaModel.getLimite(), 0.01);
    }
}
