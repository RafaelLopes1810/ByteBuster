package view;

import controller.AdicionarEstacionamentoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdicionarEstacionamentoView extends JFrame {
    private JTextField txtNome, txtEndereco, txtTelefone, txtColunas, txtVagasPorColuna;
    private JButton btnSalvar, btnCancelar;
    private AdicionarEstacionamentoController controller;
    private JPanel panel1;

    public AdicionarEstacionamentoView(AdicionarEstacionamentoController adicionarController) {
        controller = adicionarController;

        setTitle("Adicionar Estacionamento");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(20);
        panel.add(txtNome, gbc);

        // Endereço
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        txtEndereco = new JTextField(20);
        panel.add(txtEndereco, gbc);

        // Telefone
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        txtTelefone = new JTextField(15);
        panel.add(txtTelefone, gbc);

        // Colunas
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Colunas:"), gbc);
        gbc.gridx = 1;
        txtColunas = new JTextField(5);
        panel.add(txtColunas, gbc);

        // Vagas por Coluna
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Vagas por Coluna:"), gbc);
        gbc.gridx = 1;
        txtVagasPorColuna = new JTextField(5);
        panel.add(txtVagasPorColuna, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarEstacionamento);
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void salvarEstacionamento(ActionEvent e) {
        String nome = txtNome.getText();
        String endereco = txtEndereco.getText();
        String telefone = txtTelefone.getText();
        int colunas;
        int vagasPorColuna;

        try {
            colunas = Integer.parseInt(txtColunas.getText());
            vagasPorColuna = Integer.parseInt(txtVagasPorColuna.getText());

            if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
            }

            boolean sucesso = controller.adicionarEstacionamento(nome, endereco, telefone, colunas, vagasPorColuna);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Estacionamento adicionado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar estacionamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Colunas e Vagas por Coluna devem ser números.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Captura EstacionamentoDAOException e outras
            JOptionPane.showMessageDialog(this, "Erro ao salvar estacionamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
