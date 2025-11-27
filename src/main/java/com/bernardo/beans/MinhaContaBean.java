package com.bernardo.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.EnderecoCliente;
import com.bernardo.entidades.Veiculo;
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
public class MinhaContaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioLogadoBean usuarioLogado;
	@EJB
	private ClienteService clienteService;

	private boolean alterando;

	private EnderecoCliente novoEndereco = new EnderecoCliente();
	private boolean editandoEndereco = false;
	
	private Veiculo novoVeiculo = new Veiculo();
	private boolean editandoVeiculo = false;
	
	private Cliente clienteLogado = new Cliente();
	private String novaSenha;

	private List<String> listaMarcas;
	
	@PostConstruct
	public void montaRegistros() {
		clienteLogado = usuarioLogado.getClienteLogado();
		
		listaMarcas = Arrays.asList(
				// Populares
				"Chevrolet", "Volkswagen", "Fiat", "Ford", "Toyota", "Honda", "Renault", "Hyundai", "Peugeot",
				"Citroën", "Nissan", "Jeep", "Mitsubishi", "Kia", "Chery", "JAC Motors", "Great Wall", "BYD",
				"GWM Haval",

				// Premium e esportivas
				"BMW", "Mercedes-Benz", "Audi", "Volvo", "Lexus", "Mini", "Porsche", "Land Rover", "Jaguar", "Tesla",

				// Outras conhecidas no Brasil
				"Suzuki", "Subaru", "RAM", "Dodge", "Chrysler", "Alfa Romeo", "Maserati", "Ferrari", "Lamborghini",

				// Antigas ou menos comuns
				"Troller", "Mahindra", "Effa", "Lifan", "Geely", "Hafei", "Agrale", "Seat", "Saab",

				// Caminhonetes e utilitários
				"Iveco", "Scania", "MAN", "Volare");
	}

	public void salvar() {
		if (!StringUtil.isCPFValido(clienteLogado.getCliCpf())) {
			JsfUtil.error("CPF inválido");
			return;
		}
		clienteLogado.setCliCpf(StringUtil.getOnlyNumbers(clienteLogado.getCliCpf()));
		clienteLogado.setCliTelefone(StringUtil.getOnlyNumbers(clienteLogado.getCliTelefone()));
		
		if (novaSenha != null && !novaSenha.isEmpty()) {
		    clienteLogado.setCliSenha(StringUtil.gerarHashSHA256(novaSenha));
		}
		
		clienteService.salvar(clienteLogado);
		JsfUtil.info("Cadastro atualizado com sucesso!");
	}

	public void salvarEndereco() {
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndDescricao())) {
			JsfUtil.error("O campo Descrição é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndCep())) {
			JsfUtil.error("O campo CEP é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndRua())) {
			JsfUtil.error("O campo Rua é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndNumero())) {
			JsfUtil.error("O campo Número é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndBairro())) {
			JsfUtil.error("O campo Bairro é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndCidade())) {
			JsfUtil.error("O campo Cidade é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoEndereco.getEndUf())) {
			JsfUtil.error("O campo UF é obrigatório.");
			return;
		}

		String enderecoCompleto = novoEndereco.getEndRua() + ", " + novoEndereco.getEndNumero() + ", "
				+ novoEndereco.getEndBairro() + ", " + novoEndereco.getEndCidade() + ", " + novoEndereco.getEndUf()
				+ ", Brasil";

		double[] coords = GeocodingUtil.buscarLatLong(enderecoCompleto);
		if (coords != null) {
			novoEndereco.setEndLatitude(coords[0]);
			novoEndereco.setEndLongitude(coords[1]);
		}

		if (!editandoEndereco) {
			clienteLogado.adicionarEndereco(novoEndereco);
			JsfUtil.info("Endereço adicionado à lista.");
		} else {
			JsfUtil.info("Endereço editado.");
		}

		novoEndereco.setEndCep(StringUtil.getOnlyNumbers(novoEndereco.getEndCep()));
		this.novoEndereco = new EnderecoCliente();
		JsfUtil.pfHideDialog("dlgEndereco");
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
	
	public void salvarVeiculo() {
		if (StringUtil.isNullOrEmpty(novoVeiculo.getVeiMarca())) {
			JsfUtil.error("O campo Marca é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoVeiculo.getVeiModelo())) {
			JsfUtil.error("O campo Modelo é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoVeiculo.getVeiCor())) {
			JsfUtil.error("O campo Cor é obrigatório.");
			return;
		}
		if (StringUtil.isNullOrEmpty(novoVeiculo.getVeiPlaca())) {
			JsfUtil.error("O campo Placa é obrigatório.");
			return;
		}

		if (!editandoVeiculo) {
			clienteLogado.adicionarVeiculo(novoVeiculo);
			JsfUtil.info("Veículo adicionado à lista.");
		} else {
			JsfUtil.info("Veículo editado.");
		}

		this.novoVeiculo = new Veiculo();
		JsfUtil.pfHideDialog("dlgVeiculo");
	}

	public void novoEndereco() {
		this.novoEndereco = new EnderecoCliente();
		this.editandoEndereco = false;
	}

	public void editarEndereco(EnderecoCliente end) {
		this.novoEndereco = end;
		this.editandoEndereco = true;
	}
	
	public void novoVeiculo() {
		this.novoVeiculo = new Veiculo();
		this.editandoVeiculo = false;
	}

	public void editarVeiculo(Veiculo vei) {
		this.novoVeiculo = vei;
		this.editandoVeiculo = true;
	}

	public boolean isAlterando() {
		return alterando;
	}

	public void setAlterando(boolean alterando) {
		this.alterando = alterando;
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
	
	public Veiculo getNovoVeiculo() {
		return novoVeiculo;
	}

	public void setNovoVeiculo(Veiculo novoVeiculo) {
		this.novoVeiculo = novoVeiculo;
	}

	public boolean isEditandoVeiculo() {
		return editandoVeiculo;
	}

	public void setEditandoVeiculo(boolean editandoVeiculo) {
		this.editandoVeiculo = editandoVeiculo;
	}

	public Cliente getClienteLogado() {
		return clienteLogado;
	}

	public void setClienteLogado(Cliente clienteLogado) {
		this.clienteLogado = clienteLogado;
	}

	public List<String> getListaMarcas() {
		return listaMarcas;
	}

	public void setListaMarcas(List<String> listaMarcas) {
		this.listaMarcas = listaMarcas;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
}
