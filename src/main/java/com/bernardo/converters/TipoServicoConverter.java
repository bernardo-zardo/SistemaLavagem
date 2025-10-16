package com.bernardo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.beans.cadastros.ServicoBean;
import com.bernardo.entidades.TipoServico;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@FacesConverter(forClass = TipoServico.class)
public class TipoServicoConverter implements Converter<TipoServico> {

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

        ServicoBean bean = context.getApplication()
                .evaluateExpressionGet(context, "#{servicoBean}", ServicoBean.class);

        return bean.getTiposServico()
                   .stream()
                   .filter(t -> t.getTsIdTipoServico().equals(id))
                   .findFirst()
                   .orElse(null);
    }
}
