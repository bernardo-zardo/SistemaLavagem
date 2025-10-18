package com.bernardo.beans.cadastros;

import com.bernardo.beans.BuscaBean;
import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.Veiculo;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ClienteService;
import com.bernardo.utils.CEPUtil;
import com.bernardo.utils.GeocodingUtil;
import com.bernardo.utils.JsfUtil;
import com.bernardo.utils.LocalizacaoJson;
import com.bernardo.utils.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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
        crudObj.setCliCep(StringUtil.getOnlyNumbers(crudObj.getCliCep()));
        
        String enderecoCompleto = crudObj.getCliRua() + ", " + crudObj.getCliCidade() + ", " + crudObj.getCliUf() + ", Brasil";

        double[] coords = GeocodingUtil.buscarLatLong(enderecoCompleto);
        if (coords != null) {
        	crudObj.setCliLatitude(coords[0]);
        	crudObj.setCliLongitude(coords[1]);
        }
        
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
    
    public void preencherEnderecoPorCep() {
        if (crudObj.getCliCep() != null && !crudObj.getCliCep().isEmpty()) {
            LocalizacaoJson local = CEPUtil.buscarLocalizacao(crudObj.getCliCep().replaceAll("\\D", ""));
            if (local != null) {
                crudObj.setCliRua(local.getLogradouro());
                crudObj.setCliBairro(local.getBairro());
                crudObj.setCliCidade(local.getLocalidade());
                crudObj.setCliUf(local.getUf());
            }
        }
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
}
