package com.bernardo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.consultas.ConsultaAgendamentoBean;
import com.bernardo.consultas.ConsultaServicoBean;
import com.bernardo.entidades.TipoServico;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@FacesConverter("tipoServicoConsultaConverter")
public class TipoServicoConsultaConverter implements Converter<TipoServico> {

	@Override
	public String getAsString(FacesContext context, UIComponent component, TipoServico tipoServico) {
		if (tipoServico == null || tipoServico.getTsIdTipoServico() == null) {
			return "";
		}
		return tipoServico.getTsIdTipoServico().toString();
	}

	@Override
	public TipoServico getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isBlank()) {
			return null;
		}

		Integer id = Integer.valueOf(value);

		ConsultaAgendamentoBean agendamentoBean = context.getApplication().evaluateExpressionGet(context,
				"#{consultaAgendamentoBean}", ConsultaAgendamentoBean.class);

		if (agendamentoBean != null && agendamentoBean.getTiposServico() != null) {
			TipoServico encontrado = agendamentoBean.getTiposServico().stream()
					.filter(t -> t.getTsIdTipoServico().equals(id)).findFirst().orElse(null);

			if (encontrado != null) {
				return encontrado;
			}
		}

		ConsultaServicoBean servicoBean = context.getApplication().evaluateExpressionGet(context,
				"#{consultaServicoBean}", ConsultaServicoBean.class);

		if (servicoBean != null && servicoBean.getTiposServico() != null) {
			return servicoBean.getTiposServico().stream().filter(t -> t.getTsIdTipoServico().equals(id)).findFirst()
					.orElse(null);
		}

		return null;
	}
}
