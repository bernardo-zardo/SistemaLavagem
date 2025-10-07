package com.bernardo.beans.cadastros;

import com.bernardo.beans.BuscaBean;
import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.Veiculo;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ClienteService;
import com.bernardo.services.VeiculoService;
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
public class VeiculoBean extends BaseCrud<Veiculo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private VeiculoService veiculoService;
    @EJB
    private ClienteService clienteService;

    private boolean alterando;
    private List<Veiculo> veiculos;
    private List<Cliente> clientes;
    
    @PostConstruct
    public void montaRegistros() {
    	veiculos = veiculoService.filtrar(new HashMap<>());
    	clientes = clienteService.filtrar(new HashMap<>());
    }
    
    @Override
    public void criaObj() {
        crudObj = new Veiculo();
        alterando = false;
    }

    @Override
    public void salvar() {
        if (alterando) {
        	veiculoService.salvar(crudObj);
            JsfUtil.info("Veículo atualizado com sucesso!");
        } else {
            Map<String, Object> filtros = new HashMap<>();
            filtros.put("veiPlaca", crudObj.getVeiPlaca());
            List<Veiculo> veiculosExistentes = veiculoService.filtrar(filtros);

            if (veiculosExistentes.isEmpty()) {
                veiculoService.salvar(crudObj);
                JsfUtil.info("Veículo salvo com sucesso!");
            } else {
                JsfUtil.warn("Veículo já cadastrado com a placa: " + veiculosExistentes.get(0).getVeiPlaca());
                return;
            }
        }
        veiculos = veiculoService.filtrar(new HashMap<>());
        criaObj();
    }
    
    @Override
    public void deletar() {
    	veiculoService.deletar(crudObj);
    	criaObj();
    	JsfUtil.info("Veículo excluído com sucesso!");
    	veiculos = veiculoService.filtrar(new HashMap<>());
    }
    
    public void selecionarVeiculo(Veiculo veiculo) {
        this.crudObj = veiculo;
        this.alterando = true;
    }
    
    public void excluirVeiculo(Veiculo veiculo) {
        this.crudObj = veiculo;
        veiculoService.deletar(veiculo);
        criaObj();
        JsfUtil.info("Veículo excluído com sucesso!");
        veiculos = veiculoService.filtrar(new HashMap<>());
    }
    
    public Veiculo getCrudObj() {
        return crudObj;
    }

    public boolean isAlterando() {
        return alterando;
    }

    public void setAlterando(boolean alterando) {
        this.alterando = alterando;
    }

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
}
