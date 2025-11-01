package com.bernardo.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@FacesConverter("horaConverter")
public class HoraConverter implements Converter<Date> {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	  @Override
	    public Date getAsObject(FacesContext context, UIComponent component, String value) {
	        if (value == null || value.isEmpty()) {
	            return null;
	        }

	        try {
	            // Cria uma data base padrão (1970-01-01) e define apenas hora/minuto
	            Date parsedTime = sdf.parse(value);
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(parsedTime);
	            Calendar base = Calendar.getInstance();
	            base.clear();
	            base.set(1970, Calendar.JANUARY, 1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0);
	            return base.getTime();
	        } catch (ParseException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    @Override
	    public String getAsString(FacesContext context, UIComponent component, Date value) {
	        if (value == null) {
	            return "";
	        }
	        return sdf.format(value);
	    }
}
