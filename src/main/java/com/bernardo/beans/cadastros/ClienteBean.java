package com.bernardo.beans.cadastros;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.EnderecoCliente;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ClienteService;
import com.bernardo.utils.CEPUtil;
import com.bernardo.utils.GeocodingUtil;
import com.bernardo.utils.JsfUtil;
import com.bernardo.utils.LocalizacaoJson;
import com.bernardo.utils.StringUtil;

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
    
    private EnderecoCliente novoEndereco = new EnderecoCliente();
    private boolean editandoEndereco = false;
    
    @PostConstruct
    public void montaRegistros() {
    	clientes = clienteService.filtrar(new HashMap<>());
    }
    
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
                JsfUtil.warn("Cliente já cadastrado com o CPF: " + clientesExistentes.get(0).getCliNome());
                return;
            }
        }
        clientes = clienteService.filtrar(new HashMap<>());
        criaObj();
    }
    
    @Override
    public void deletar() {
    	clienteService.deletar(crudObj);
    	criaObj();
        JsfUtil.info("Cliente excluído com sucesso!");
        clientes = clienteService.filtrar(new HashMap<>());
    }

    public void selecionarCliente(Cliente cliente) {
        this.crudObj = cliente;
        this.alterando = true;
        JsfUtil.info("Cliente selecionado.");
    }
    
    public void excluirCliente(Cliente cliente) {
        this.crudObj = cliente;
        clienteService.deletar(cliente);
        criaObj();
        JsfUtil.info("Cliente excluído com sucesso!");
        clientes = clienteService.filtrar(new HashMap<>());
    }
    
    public void excluirClienteSelecionado() {
        clienteService.deletar(crudObj);
        JsfUtil.info("Cliente excluido com sucesso!");
        criaObj();
    }
    
    public void salvarEndereco() {
        String enderecoCompleto = novoEndereco.getEndRua() + ", " + novoEndereco.getEndCidade() + ", " + novoEndereco.getEndUf() + ", Brasil";
        double[] coords = GeocodingUtil.buscarLatLong(enderecoCompleto);
        if (coords != null) {
            novoEndereco.setEndLatitude(coords[0]);
            novoEndereco.setEndLongitude(coords[1]);
        }

        if (!editandoEndereco) {
        	novoEndereco.setEndCep(StringUtil.getOnlyNumbers(novoEndereco.getEndCep()));
            crudObj.adicionarEndereco(novoEndereco);
            JsfUtil.info("Endereço adicionado à lista.");
        } else {
        	JsfUtil.info("Endereço editado.");
        }
        
        this.novoEndereco = new EnderecoCliente();
    }
    
    public void preencherEnderecoPorCep() {
        if (novoEndereco.getEndCep() != null && !novoEndereco.getEndCep().isEmpty()) {
            LocalizacaoJson local = CEPUtil.buscarLocalizacao(novoEndereco.getEndCep().replaceAll("\\D", ""));
            if (local != null) {
            	novoEndereco.setEndRua(local.getLogradouro());
            	novoEndereco.setEndBairro(local.getBairro());
            	novoEndereco.setEndCidade(local.getLocalidade());
            	novoEndereco.setEndUf(local.getUf());
            }
        }
    }
    
    public void novoEndereco() {
        this.novoEndereco = new EnderecoCliente();
        this.editandoEndereco = false;
    }

    public void editarEndereco(EnderecoCliente end) {
        this.novoEndereco = end;
        this.editandoEndereco = true;
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
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public EnderecoCliente getNovoEndereco() {
		return novoEndereco;
	}

	public void setNovoEndereco(EnderecoCliente novoEndereco) {
		this.novoEndereco = novoEndereco;
	}

	public boolean isEditandoEndereco() {
		return editandoEndereco;
	}

	public void setEditandoEndereco(boolean editandoEndereco) {
		this.editandoEndereco = editandoEndereco;
	}
}
