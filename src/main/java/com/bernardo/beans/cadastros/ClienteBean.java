package com.bernardo.beans.cadastros;

import com.bernardo.beans.BuscaBean;
import com.bernardo.entidades.Cliente;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ClienteService;
import com.bernardo.utils.JsfUtil;
import com.bernardo.utils.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
*
* @author Bernardo Zardo Mergen
*/
@Named
@ViewScoped
public class ClienteBean extends BaseCrud<Cliente> implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteService clienteService;

    private boolean alterando;
    private List<Cliente> clientes;

    @Override
    public void criaObj() {
        crudObj = new Cliente();
        alterando = false;
    }

    @Override
    public void salvar() {
        if (!StringUtil.isCPFValido(crudObj.getCliCpf())) {
            JsfUtil.error("CPF inválido");
            return;
        }

        crudObj.setCliCpf(StringUtil.getOnlyNumbers(crudObj.getCliCpf()));
        crudObj.setCliTelefone(StringUtil.getOnlyNumbers(crudObj.getCliTelefone()));

        if (alterando) {
            clienteService.salvar(crudObj);
            JsfUtil.info("Cliente atualizado com sucesso!");
        } else {
            Map<String, Object> filtros = new HashMap<>();
            filtros.put("cliCpf", crudObj.getCliCpf());
            List<Cliente> clientesExistentes = clienteService.filtrar(filtros);

            if (clientesExistentes.isEmpty()) {
                clienteService.salvar(crudObj);
                JsfUtil.info("Cliente salvo com sucesso!");
            } else {
                crudObj = clientesExistentes.get(0);
                alterando = true;
                JsfUtil.warn("Cliente já existe, carregado para edição");
            }
        }
        getClientes();
        criaObj();
    }
    
    @Override
    public void deletar() {
    	clienteService.deletar(crudObj);
    	getClientes();
    }

    public void buscarCliente() {
        String cpf = StringUtil.getOnlyNumbers(crudObj.getCliCpf());
        Map<String, Object> filtros = new HashMap<>();
        filtros.put("cliCpf", cpf);
        List<Cliente> clientes = clienteService.filtrar(filtros);

        if (!clientes.isEmpty()) {
            crudObj = clientes.get(0);
            alterando = true;
            crudObj.setCliCpf(StringUtil.getCpfFormatado(crudObj.getCliCpf()));
        } else {
            JsfUtil.warn("Cliente não encontrado");
        }
    }

    @Override
    public void setObjetoCrudPesquisa() {
        Cliente cliente = BuscaBean.getResultadoPesquisa(Cliente.class);
        if (cliente != null) {
            crudObj = cliente;
            alterando = true;
            crudObj.setCliCpf(StringUtil.getCpfFormatado(crudObj.getCliCpf()));
        }
    }
    
    public void selecionarCliente(Cliente cliente) {
        this.crudObj = cliente;
        this.alterando = true;
    }
    
    public void excluirCliente(Cliente cliente) {
        this.crudObj = cliente;
        clienteService.deletar(cliente);
        JsfUtil.info("Cliente excluido com sucesso!");
        criaObj();
    }
    
    public void excluirClienteSelecionado() {
        clienteService.deletar(crudObj);
        JsfUtil.info("Cliente excluido com sucesso!");
        criaObj();
    }

    public Cliente getCrudObj() {
        return crudObj;
    }

    public boolean isAlterando() {
        return alterando;
    }

    public void setAlterando(boolean alterando) {
        this.alterando = alterando;
    }
    
    public List<Cliente> getClientes() {
        return clienteService.filtrar(new HashMap<>());
    }
}
