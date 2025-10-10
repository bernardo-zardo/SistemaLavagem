package com.bernardo.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.beans.cadastros.ServicoBean;
import com.bernardo.entidades.Veiculo;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@FacesConverter(forClass = Veiculo.class)
public class VeiculoConverter implements Converter<Veiculo> {

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

        ServicoBean bean = context.getApplication()
                .evaluateExpressionGet(context, "#{servicoBean}", ServicoBean.class);

        return bean.getVeiculos()
                   .stream()
                   .filter(v -> v.getVeiIdVeiculo().equals(id))
                   .findFirst()
                   .orElse(null);
    }
}
