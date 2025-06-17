package view;

import controller.ClienteController;
import exceptions.ClienteDAOException;
import model.ClienteModel;
import model.VoucherModel;
import model.VeiculoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class MenuCliente extends JFrame {

    private JTextField barraDeBusca;
    private JButton btnAdicionarCliente;
    private JButton btnAdicionarVeiculo;
    private JList<String> listaClientes;
    private JButton historicoButton;
    private JButton rankingButton;
    private DefaultListModel<String> listaClientesModel;
    private ClienteController clienteController;
    private List<ClienteModel> clientesCadastrados;

    public MenuCliente() throws ClienteDAOException {
        clienteController = new ClienteController();

        setTitle("Menu de Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Painel de busca
        JPanel panelSuperior = new JPanel(new FlowLayout());
        barraDeBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");

        panelSuperior.add(new JLabel("Buscar (Nome/Placa):"));
        panelSuperior.add(barraDeBusca);
        panelSuperior.add(btnBuscar);

        add(panelSuperior, BorderLayout.NORTH);

        // Lista de clientes
        listaClientesModel = new DefaultListModel<>();
        listaClientes = new JList<>(listaClientesModel);
        JScrollPane scrollPane = new JScrollPane(listaClientes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões na parte inferior
        JPanel panelInferior = new JPanel(new FlowLayout());
        btnAdicionarCliente = new JButton("Adicionar Cliente");
        btnAdicionarVeiculo = new JButton("Adicionar Veículo");
        historicoButton = new JButton("Histórico");
        rankingButton = new JButton("Ranking");

        panelInferior.add(btnAdicionarCliente);
        panelInferior.add(btnAdicionarVeiculo);
        panelInferior.add(historicoButton);
        panelInferior.add(rankingButton);

        add(panelInferior, BorderLayout.SOUTH);

        atualizarListaClientes();

        // Configurar ações dos botões
        btnBuscar.addActionListener(e -> {
            try {
                buscarClientes();
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        btnAdicionarCliente.addActionListener(e -> {
            try {
                abrirAdicionarCliente();
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        btnAdicionarVeiculo.addActionListener(e -> {
            try {
                abrirAdicionarVeiculo();
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        historicoButton.addActionListener(e -> {
            try {
                exibirHistorico();
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        rankingButton.addActionListener(e -> {
            try {
                exibirRankingClientes();
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void atualizarListaClientes() throws ClienteDAOException {
        listaClientesModel.clear();
        clientesCadastrados = clienteController.listarTodosClientes();
        for (ClienteModel cliente : clientesCadastrados) {
            String clienteInfo = cliente.getId() + " - " + cliente.getNome();
            if (!cliente.getVeiculos().isEmpty()) {
                List<VeiculoModel> veiculos = cliente.getVeiculos();
                String placas = veiculos.stream()
                        .map(VeiculoModel::getPlaca)
                        .reduce((placa1, placa2) -> placa1 + ", " + placa2)
                        .orElse("");
                clienteInfo += " - Veículos: [" + placas + "]";
            }
            listaClientesModel.addElement(clienteInfo);
        }
    }

    private void abrirAdicionarCliente() throws ClienteDAOException {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do cliente (ou deixe em branco para Anônimo):", "Adicionar Cliente", JOptionPane.PLAIN_MESSAGE);
        if (nome == null) return; // Cancelado

        if (nome.trim().isEmpty()) {
            nome = "Anônimo";
        }

        ClienteModel cliente = clienteController.adicionarCliente(nome);
        atualizarListaClientes();
    }

    private void abrirAdicionarVeiculo() throws ClienteDAOException {
        int selectedIndex = listaClientes.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente na lista!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClienteModel clienteSelecionado = clientesCadastrados.get(selectedIndex);

        String placa = JOptionPane.showInputDialog(this, "Digite a placa do veículo:", "Adicionar Veículo", JOptionPane.PLAIN_MESSAGE);
        if (placa == null || placa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Placa inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        clienteController.adicionarVeiculoAoCliente(clienteSelecionado.getId(), placa);

        atualizarListaClientes();
    }

    private void exibirHistorico() throws ClienteDAOException {
        int selectedIndex = listaClientes.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente na lista!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClienteModel clienteSelecionado = clientesCadastrados.get(selectedIndex);

        List<VoucherModel> tickets = clienteController.listarTicketsDoCliente(clienteSelecionado.getId());

        if (tickets.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum histórico encontrado para este cliente.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            exibirDialogoHistorico(tickets, clienteSelecionado.getId());
        }
    }

    private void exibirDialogoHistorico(List<VoucherModel> tickets, String idCliente) {
        JDialog dialog = new JDialog(this, "Histórico de Tickets", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);

        String[] colunas = {"ID Ticket", "Estacionamento", "Vaga", "Entrada", "Saída", "Custo", "Placa"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);

        JTable tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Campos de texto para os filtros
        JTextField txtEntrada = new JTextField(10);
        JTextField txtSaida = new JTextField(10);

        // Botão para aplicar o filtro
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> {
            String entrada = txtEntrada.getText().trim();
            String saida = txtSaida.getText().trim();

            // Validar entradas
            if (!entrada.isEmpty() && !isDataValida(entrada)) {
                JOptionPane.showMessageDialog(dialog, "Data de entrada inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!saida.isEmpty() && !isDataValida(saida)) {
                JOptionPane.showMessageDialog(dialog, "Data de saída inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Delegar ao Controller a filtragem
            List<VoucherModel> ticketsFiltrados = List.of();
            try {
                ticketsFiltrados = clienteController.filtrarTickets(idCliente, entrada, saida);
            } catch (ClienteDAOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // Atualizar a tabela
            atualizarTabela(ticketsFiltrados, tableModel);
        });

        // Painel superior com os filtros
        JPanel panelFiltros = new JPanel(new GridLayout(1, 5, 5, 5));
        panelFiltros.add(new JLabel("Entrada:"));
        panelFiltros.add(txtEntrada);
        panelFiltros.add(new JLabel("Saída:"));
        panelFiltros.add(txtSaida);
        panelFiltros.add(btnFiltrar);

        dialog.add(panelFiltros, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        JPanel panelBotoes = new JPanel();
        panelBotoes.add(btnFechar);
        dialog.add(panelBotoes, BorderLayout.SOUTH);

        atualizarTabela(tickets, tableModel);

        dialog.setVisible(true);
    }

    // Validação de data
    private boolean isDataValida(String data) {
        try {
            LocalDate.parse(data); // Use LocalDate em vez de LocalDateTime
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // Atualização da tabela
    private void atualizarTabela(List<VoucherModel> tickets, DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpa a tabela

        // Formato para moeda brasileira (R$)
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (VoucherModel ticket : tickets) {
            tableModel.addRow(new Object[]{
                    ticket.getIdTicket(),
                    ticket.getIdEstacionamento(),
                    ticket.getIdVaga(),
                    ticket.getEntrada(),
                    ticket.getSaida(),
                    ticket.getCusto() != null ? currencyFormatter.format(ticket.getCusto()) : "N/A",
                    ticket.getPlaca()
            });
        }
    }


    private int comboMesParaNumero(String mes) {
        return switch (mes) {
            case "Janeiro" -> 1;
            case "Fevereiro" -> 2;
            case "Março" -> 3;
            case "Abril" -> 4;
            case "Maio" -> 5;
            case "Junho" -> 6;
            case "Julho" -> 7;
            case "Agosto" -> 8;
            case "Setembro" -> 9;
            case "Outubro" -> 10;
            case "Novembro" -> 11;
            case "Dezembro" -> 12;
            default -> -1; // "Todos"
        };
    }

    private void exibirRankingClientes() throws ClienteDAOException {
            JDialog dialog = new JDialog(this, "Ranking de Clientes", true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);

            String[] colunas = {"Posição", "Cliente", "Total Gasto"};
            DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);

            JTable tabela = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(tabela);

            JComboBox<String> comboMes = new JComboBox<>(new String[]{
                    "Todos", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
            });

            JTextField txtAno = new JTextField(4);

            JButton btnFiltrar = new JButton("Filtrar");
            btnFiltrar.addActionListener(e -> {
                String mesSelecionado = (String) comboMes.getSelectedItem();
                String anoSelecionado = txtAno.getText();

                if (anoSelecionado.isEmpty() && !mesSelecionado.equals("Todos")) {
                    JOptionPane.showMessageDialog(dialog, "Por favor, insira um ano para aplicar o filtro.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int mesNumero = mesSelecionado.equals("Todos") ? -1 : comboMesParaNumero(mesSelecionado);

                List<ClienteModel> ranking = List.of();
                try {
                    ranking = clienteController.listarRankingClientes(mesNumero, anoSelecionado.isEmpty() ? -1 : Integer.parseInt(anoSelecionado));
                } catch (NumberFormatException | ClienteDAOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                atualizarTabelaRanking(ranking, tableModel);
            });

            // Painel superior com os filtros
            JPanel panelFiltros = new JPanel();
            panelFiltros.add(new JLabel("Mês:"));
            panelFiltros.add(comboMes);
            panelFiltros.add(new JLabel("Ano:"));
            panelFiltros.add(txtAno);
            panelFiltros.add(btnFiltrar);

            dialog.add(panelFiltros, BorderLayout.NORTH);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton btnFechar = new JButton("Fechar");
            btnFechar.addActionListener(e -> dialog.dispose());
            JPanel panelBotoes = new JPanel();
            panelBotoes.add(btnFechar);
            dialog.add(panelBotoes, BorderLayout.SOUTH);

            // Carregar ranking inicial (sem filtro)
            List<ClienteModel> rankingInicial = clienteController.listarRankingClientes(-1, -1);
            atualizarTabelaRanking(rankingInicial, tableModel);

            dialog.setVisible(true);
        }

    private void atualizarTabelaRanking(List<ClienteModel> ranking, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        int posicao = 1;
        for (ClienteModel cliente : ranking) {
            tableModel.addRow(new Object[]{
                    posicao++,
                    cliente.getNome(),
                    currencyFormatter.format(cliente.getTotalGasto())
            });
        }
    }

    private void buscarClientes() throws ClienteDAOException {
        String termo = barraDeBusca.getText().trim();
        if (termo.isEmpty()) {
            atualizarListaClientes();
            return;
        }

        List<ClienteModel> clientesFiltrados = clienteController.buscarClientes(termo);
        listaClientesModel.clear();
        for (ClienteModel cliente : clientesFiltrados) {
            StringBuilder clienteInfo = new StringBuilder(cliente.getId() + " - " + cliente.getNome());

            // Adicionar veículos ao cliente
            if (!cliente.getVeiculos().isEmpty()) {
                clienteInfo.append(" - Veículos: ");
                cliente.getVeiculos().forEach(veiculo -> clienteInfo.append(veiculo.getPlaca()).append(", "));
                // Remove a última vírgula
                clienteInfo.setLength(clienteInfo.length() - 2);
            }

            listaClientesModel.addElement(clienteInfo.toString());
        }
    }


    public static void main(String[] args) throws ClienteDAOException {
        new MenuCliente();
    }

}
