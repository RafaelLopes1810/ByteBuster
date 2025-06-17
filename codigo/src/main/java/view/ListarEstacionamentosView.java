package view;

import controller.ListarEstacionamentoController;
import controller.AdicionarEstacionamentoController;
import controller.ClienteController;
import exceptions.ClienteDAOException;
import exceptions.EstacionamentoDAOException;
import model.EstacionamentoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class ListarEstacionamentosView extends JFrame {

    private JPanel panel;
    private JTable tabelaEstacionamentos;
    private DefaultTableModel tabelaModelo;

    private ListarEstacionamentoController controller;

    public ListarEstacionamentosView(ListarEstacionamentoController controller) throws EstacionamentoDAOException {
        this.controller = controller;

        setTitle("Lista de Estacionamentos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        inicializarComponentes();
        carregarEstacionamentosNaTabela();
    }

    private void inicializarComponentes() {
        panel = new JPanel(new BorderLayout());
        setContentPane(panel);

        tabelaModelo = new DefaultTableModel(new Object[] { "ID", "Nome", "Endereço", "Telefone" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEstacionamentos = new JTable(tabelaModelo);
        tabelaEstacionamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaEstacionamentos.setComponentPopupMenu(criarMenuContexto());

        // Adicionar mouse listener para mostrar o menu de contexto
        tabelaEstacionamentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = tabelaEstacionamentos.rowAtPoint(e.getPoint());
                tabelaEstacionamentos.getSelectionModel().setSelectionInterval(row, row);
                if (e.isPopupTrigger()) {
                    mostrarMenuContexto(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenuContexto(e);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaEstacionamentos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botão para adicionar estacionamento
        JButton adicionarButton = criarBotao("Adicionar Estacionamento");
        adicionarButton.addActionListener(e -> adicionarEstacionamento());

        JPanel topoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topoPanel.add(adicionarButton);
        panel.add(topoPanel, BorderLayout.NORTH);

        // Texto de instrução
        JLabel textoInstrucao = new JLabel("Clique com o botão direito para ver ou remover um estacionamento.");
        textoInstrucao.setHorizontalAlignment(SwingConstants.CENTER);
        textoInstrucao.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(textoInstrucao, BorderLayout.SOUTH);
    }

    // Método para criar botões com estilo consistente
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(200, 40));
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        return botao;
    }

    private void carregarEstacionamentosNaTabela() throws EstacionamentoDAOException {
        tabelaModelo.setRowCount(0);
        List<EstacionamentoModel> estacionamentos = controller.listarEstacionamentos();

        for (EstacionamentoModel estacionamento : estacionamentos) {
            tabelaModelo.addRow(new Object[]{
                    estacionamento.getId(),
                    estacionamento.getNome(),
                    estacionamento.getEndereco(),
                    estacionamento.getTelefone()
            });
        }
    }

    private void adicionarEstacionamento() {
        AdicionarEstacionamentoController adicionarController = new AdicionarEstacionamentoController();
        AdicionarEstacionamentoView adicionarView = new AdicionarEstacionamentoView(adicionarController);
        adicionarView.setVisible(true);

        // Atualiza a tabela ao fechar a tela de adicionar
        adicionarView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    carregarEstacionamentosNaTabela();
                } catch (EstacionamentoDAOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JPopupMenu criarMenuContexto() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem verItem = new JMenuItem("Ver Estacionamento");
        verItem.addActionListener(e -> {
            try {
                gerenciarEstacionamentoSelecionado();
            } catch (EstacionamentoDAOException | ClienteDAOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gerenciar estacionamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem removerItem = new JMenuItem("Remover Estacionamento");
        removerItem.addActionListener(e -> {
            try {
                removerEstacionamento();
            } catch (EstacionamentoDAOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao remover estacionamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        menu.add(verItem);
        menu.add(removerItem);

        return menu;
    }

    private void mostrarMenuContexto(MouseEvent e) {
        if (tabelaEstacionamentos.getSelectedRow() != -1) {
            tabelaEstacionamentos.getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void removerEstacionamento() throws EstacionamentoDAOException {
        int linhaSelecionada = tabelaEstacionamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um estacionamento.");
            return;
        }

        int idEstacionamento = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o estacionamento selecionado?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = controller.removerEstacionamento(idEstacionamento);

            if (sucesso) {
                carregarEstacionamentosNaTabela();
                JOptionPane.showMessageDialog(this, "Estacionamento removido com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover estacionamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void gerenciarEstacionamentoSelecionado() throws EstacionamentoDAOException, ClienteDAOException {
        int linhaSelecionada = tabelaEstacionamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um estacionamento.");
            return;
        }

        int idEstacionamento = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
        EstacionamentoModel estacionamento = controller.buscarEstacionamentoPorId(idEstacionamento);

        if (estacionamento != null) {
            GerenciarView gerenciarView = new GerenciarView(estacionamento);
            gerenciarView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Estacionamento não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos estáticos para exibir rankings a partir do MainView
    public static void exibirRankingFaturamentoStatic(ListarEstacionamentoController controller, JFrame parent) throws EstacionamentoDAOException {
        // Cria uma nova janela para exibir o ranking
        JFrame rankingFrame = new JFrame("Ranking de Estacionamentos - Faturamento");
        rankingFrame.setSize(600, 400);
        rankingFrame.setLocationRelativeTo(parent);

        // Configuração do layout e painel
        JPanel panel = new JPanel(new BorderLayout());
        rankingFrame.add(panel);

        // Tabela para exibir o ranking
        String[] colunas = { "Posição", "Nome", "Total Faturado" };
        DefaultTableModel rankingModel = new DefaultTableModel(colunas, 0);
        JTable tabelaRanking = new JTable(rankingModel);
        panel.add(new JScrollPane(tabelaRanking), BorderLayout.CENTER);

        // Chama o método no controller para buscar o ranking
        List<Object[]> ranking = controller.obterRankingEstacionamentos();

        // Adiciona os dados do ranking na tabela
        for (int i = 0; i < ranking.size(); i++) {
            Object[] linha = ranking.get(i);
            rankingModel.addRow(new Object[] { i + 1, linha[0], String.format("R$ %.2f", linha[1]) });
        }

        // Botão para fechar a janela
        JButton fecharButton = new JButton("Fechar");
        fecharButton.addActionListener(e -> rankingFrame.dispose());
        panel.add(fecharButton, BorderLayout.SOUTH);

        rankingFrame.setVisible(true);
    }

    public static void exibirRankingUtilizacaoStatic(ListarEstacionamentoController controller, JFrame parent) throws EstacionamentoDAOException {
        List<Map<String, Object>> ranking = controller.getRankingUtilizacao();

        if (ranking.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "Nenhuma utilização registrada.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Criar dados para exibição na tabela
        String[] colunas = { "ID", "Estacionamento", "Utilizações" };
        String[][] dados = new String[ranking.size()][3];

        for (int i = 0; i < ranking.size(); i++) {
            Map<String, Object> row = ranking.get(i);
            dados[i][0] = String.valueOf(row.get("idEstacionamento"));
            dados[i][1] = String.valueOf(row.get("nomeEstacionamento"));
            dados[i][2] = String.valueOf(row.get("totalUtilizacoes"));
        }

        // Exibir tabela em nova janela
        JTable tabelaRanking = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabelaRanking);

        JFrame rankingFrame = new JFrame("Ranking de Utilização");
        rankingFrame.setSize(600, 400);
        rankingFrame.setLocationRelativeTo(parent);
        rankingFrame.add(scrollPane, BorderLayout.CENTER);

        // Botão para fechar a janela
        JButton fecharButton = new JButton("Fechar");
        fecharButton.addActionListener(e -> rankingFrame.dispose());
        rankingFrame.add(fecharButton, BorderLayout.SOUTH);

        rankingFrame.setVisible(true);
    }
}
