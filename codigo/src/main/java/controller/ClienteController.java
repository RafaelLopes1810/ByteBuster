package controller;

import dao.ClienteDAO;
import exceptions.ClienteDAOException;
import model.ClienteModel;
import model.VoucherModel;
import model.VeiculoModel;

import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public ClienteModel adicionarCliente(String nome) throws ClienteDAOException {
        ClienteModel cliente = new ClienteModel(nome, null); // O ID será gerado pelo DAO
        clienteDAO.salvarCliente(cliente);
        return cliente;
    }

    public void adicionarVeiculoAoCliente(String idCliente, String placa) throws ClienteDAOException {
        VeiculoModel veiculo = new VeiculoModel(placa);
        clienteDAO.adicionarVeiculoAoCliente(idCliente, veiculo);
    }

    public List<ClienteModel> listarTodosClientes() throws ClienteDAOException {
        return clienteDAO.listarTodos();
    }


    public ClienteModel buscarClientePorId(String id) throws ClienteDAOException {
        return clienteDAO.buscarPorId(id);
    }

    public ClienteModel buscarClientePorPlaca(String placa) throws ClienteDAOException {
        return clienteDAO.buscarClientePorPlaca(placa);
    }

    public List<VoucherModel> listarTicketsDoCliente(String idCliente) throws ClienteDAOException {
        return clienteDAO.listarTicketsDoCliente(idCliente);
    }

    public List<ClienteModel> listarRankingClientes(int mes, int ano) throws ClienteDAOException {
        return clienteDAO.obterRankingClientes(mes, ano);
    }

    public List<VoucherModel> filtrarTickets(String idCliente, String entrada, String saida) throws ClienteDAOException {
        return clienteDAO.filtrarTicketsSimples(idCliente, entrada, saida);
    }

    public List<ClienteModel> buscarClientes(String termo) throws ClienteDAOException {
        return clienteDAO.buscarPorNomeOuPlaca(termo);
    }


}
