package com.bernardo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.consultas.ConsultaServicoBean;
import com.bernardo.entidades.Veiculo;

@FacesConverter("veiculoConsultaConverter")
public class VeiculoConsultaConverter implements Converter<Veiculo> {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Veiculo veiculo) {
        if (veiculo == null || veiculo.getVeiIdVeiculo() == null) {
            return "";
        }
        return veiculo.getVeiIdVeiculo().toString();
    }

    @Override
    public Veiculo getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        Integer id = Integer.valueOf(value);
        ConsultaServicoBean bean = context.getApplication()
                .evaluateExpressionGet(context, "#{consultaServicoBean}", ConsultaServicoBean.class);

        return bean.getVeiculos().stream()
                .filter(v -> v.getVeiIdVeiculo().equals(id))
                .findFirst()
                .orElse(null);
    }
}
