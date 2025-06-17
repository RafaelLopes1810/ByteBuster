package view;

import controller.ListarEstacionamentoController;
import exceptions.ClienteDAOException;
import exceptions.EstacionamentoDAOException;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private JButton mostrarEstacionamentosButton;
    private JButton buscarClienteVeiculoButton;
    private JButton rankingFaturamentoButton;

    private ListarEstacionamentoController controller;

    public MainView() throws EstacionamentoDAOException {
        this.controller = new ListarEstacionamentoController();

        setTitle("Sistema de Estacionamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Faz a tela ocupar toda a área disponível
        setLocationRelativeTo(null);

        inicializarComponentes();
        adicionarEventos();
    }

    private void inicializarComponentes() {
        // Definir o layout principal da tela
        setLayout(new BorderLayout());

        // Painel para o nome do grupo no topo
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);  // Fundo cinza claro
        JLabel groupLabel = new JLabel("ByteBusters");
        groupLabel.setFont(new Font("Arial", Font.BOLD, 36));  // Fonte grande e negrito
        groupLabel.setForeground(Color.BLACK);
        topPanel.add(groupLabel);
        add(topPanel, BorderLayout.NORTH);  // Adiciona o painel no topo

        // Painel principal para os botões, com GridBagLayout para centralizar
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);  // Fundo cinza claro
        add(panel, BorderLayout.CENTER);  // Adiciona o painel no centro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Espaçamento entre os botões
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        mostrarEstacionamentosButton = criarBotao("Mostrar Estacionamentos");
        buscarClienteVeiculoButton = criarBotao("Buscar Cliente/Veículo");
        rankingFaturamentoButton = criarBotao("Faturamento Total");

        gbc.gridy = 0;
        panel.add(mostrarEstacionamentosButton, gbc);
        gbc.gridy = 1;
        panel.add(buscarClienteVeiculoButton, gbc);
        gbc.gridy = 2;
        panel.add(rankingFaturamentoButton, gbc);
    }

    // Método para criar botões com estilo consistente
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(300, 50));  // Tamanho fixo para todos os botões
        botao.setFont(new Font("Arial", Font.PLAIN, 16));
        botao.setBackground(Color.WHITE);  // Fundo branco
        botao.setBorder(BorderFactory.createLineBorder(Color.BLACK));  // Borda preta
        return botao;
    }

    private void adicionarEventos() {
        mostrarEstacionamentosButton.addActionListener(e -> {
            try {
                ListarEstacionamentosView tabelaView = new ListarEstacionamentosView(controller);
                tabelaView.setVisible(true);
            } catch (EstacionamentoDAOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao listar estacionamentos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarClienteVeiculoButton.addActionListener(e -> {
            try {
                abrirMenuCliente();
            } catch (ClienteDAOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao abrir menu de clientes/veículos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rankingFaturamentoButton.addActionListener(e -> {
            try {
                ListarEstacionamentosView.exibirRankingFaturamentoStatic(controller, this);
            } catch (EstacionamentoDAOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao exibir ranking de faturamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void abrirMenuCliente() throws ClienteDAOException {
        MenuCliente menuCliente = new MenuCliente();
        menuCliente.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MainView mainView = new MainView();
                mainView.setVisible(true);
            } catch (EstacionamentoDAOException e) {
                e.printStackTrace();
            }
        });
    }
}
