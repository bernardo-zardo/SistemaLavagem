package com.bernardo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.consultas.ConsultaServicoBean;
import com.bernardo.entidades.Responsavel;

@FacesConverter("responsavelConsultaConverter")
public class ResponsavelConsultaConverter implements Converter<Responsavel> {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Responsavel responsavel) {
        if (responsavel == null || responsavel.getResIdResponsavel() == null) {
            return "";
        }
        return responsavel.getResIdResponsavel().toString();
    }

    @Override
    public Responsavel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        Integer id = Integer.valueOf(value);
        ConsultaServicoBean bean = context.getApplication()
                .evaluateExpressionGet(context, "#{consultaServicoBean}", ConsultaServicoBean.class);

        return bean.getResponsaveis().stream()
                .filter(r -> r.getResIdResponsavel().equals(id))
                .findFirst()
                .orElse(null);
    }
}
