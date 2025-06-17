package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.EstacionamentoModel;

public class EstacionamentoModelTest {

    @Test
    public void testListaDeVagasInicialmenteVazia() {
        // Criando o objeto com dados fictícios
        EstacionamentoModel estacionamento = new EstacionamentoModel("Estacionamento Teste", "Rua Teste, 123", "12345-678");

        // Verificando que a lista de vagas está inicialmente vazia
        assertTrue(estacionamento.getVagas().isEmpty(), "A lista de vagas deve estar inicialmente vazia.");
    }

    @Test
    public void testCarregarVagasSemErro() {
        // Criando o objeto com dados fictícios
        EstacionamentoModel estacionamento = new EstacionamentoModel(1, "Estacionamento Teste", "Rua Teste, 123", "12345-678");

        // Chamando o método carregarVagas
        assertDoesNotThrow(estacionamento::carregarVagas, "O método carregarVagas não deve lançar exceções.");
    }
}
