package com.bernardo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.beans.cadastros.AgendamentoBean;
import com.bernardo.beans.cadastros.ServicoBean;
import com.bernardo.entidades.EnderecoCliente;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@FacesConverter("enderecoClienteConverter")
public class EnderecoClienteConverter implements Converter<EnderecoCliente> {

	@Override
	public String getAsString(FacesContext context, UIComponent component, EnderecoCliente endereco) {
		if (endereco == null || endereco.getEndIdEndereco() == null) {
			return "";
		}
		return endereco.getEndIdEndereco().toString();
	}

	@Override
	public EnderecoCliente getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isBlank()) {
			return null;
		}

		Integer id = Integer.valueOf(value);

		ServicoBean servicoBean = context.getApplication().evaluateExpressionGet(context, "#{servicoBean}",
				ServicoBean.class);

		if (servicoBean != null && servicoBean.getCrudObj() != null && servicoBean.getCrudObj().getSerVeiculo() != null
				&& servicoBean.getCrudObj().getSerVeiculo().getVeiCliente() != null) {

			return servicoBean.getCrudObj().getSerVeiculo().getVeiCliente().getCliEnderecos().stream()
					.filter(e -> e.getEndIdEndereco().equals(id)).findFirst().orElse(null);
		}

		AgendamentoBean agendamentoBean = context.getApplication().evaluateExpressionGet(context, "#{agendamentoBean}",
				AgendamentoBean.class);

		if (agendamentoBean != null && agendamentoBean.getCrudObj() != null
				&& agendamentoBean.getCrudObj().getAgVeiculo() != null
				&& agendamentoBean.getCrudObj().getAgVeiculo().getVeiCliente() != null) {

			return agendamentoBean.getCrudObj().getAgVeiculo().getVeiCliente().getCliEnderecos().stream()
					.filter(e -> e.getEndIdEndereco().equals(id)).findFirst().orElse(null);
		}

		return null;
	}
}
