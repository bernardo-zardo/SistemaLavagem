package com.bernardo.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bernardo.beans.cadastros.VeiculoBean;
import com.bernardo.entidades.Cliente;

/**
*
* @author Bernardo Zardo Mergen
*/
@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter<Cliente> {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Cliente cliente) {
        if (cliente == null || cliente.getCliIdCliente() == null) {
            return "";
        }
        return cliente.getCliIdCliente().toString();
    }

    @Override
    public Cliente getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Integer id = Integer.valueOf(value);

        VeiculoBean bean = context.getApplication()
                .evaluateExpressionGet(context, "#{veiculoBean}", VeiculoBean.class);

        return bean.getClientes()
                   .stream()
                   .filter(c -> c.getCliIdCliente().equals(id))
                   .findFirst()
                   .orElse(null);
    }
}
