package com.bernardo.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public class JsfUtil implements Serializable {

    private static final long serialVersionUID = 1L;
	private static PrimeFaces pf = PrimeFaces.current();

    public static Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    public static FacesContext getCurrentInstance() {
        return FacesContext.getCurrentInstance();
    }

    private static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static void info(String msg) {
        getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    public static void warn(String msg) {
        getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
    }

    public static void error(String msg) {
        getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public static boolean isPage(String page) {
        return getHttpServletRequest().getRequestURI().contains(page);
    }

    public static void redirect(String page) {
        try {
            getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pfShowDialog(String widgetVar) {
        pf.executeScript("PF('" + widgetVar + "').show();");
    }

    public static void pfHideDialog(String widgetVar) {
        pf.executeScript("PF('" + widgetVar + "').hide();");
    }

    public static String getParamFromUrl(String param) {
        return getHttpServletRequest().getParameter(param);
    }

    public static String getParam(String param) {
        return getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);
    }

    public static void pfUpdate(String id) {
        pf.ajax().update(id);
    }

    public static void primeFacesExecute(String summary) {
        pf.executeScript(summary);
    }

    public static boolean isPaginaPublica() {
        return getHttpServletRequest().getRequestURI().contains("publico");
    }

    public static void refresh() {
        redirect(getHttpServletRequest().getRequestURI());
    }

    public static void abrirDialog(String dialog) {
        abrirDialog(dialog, null);
    }

    public static void abrirDialog(String dialog, Map<String, List<String>> parametros) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("closeOnEscape", true);
        options.put("width", "97%");
        options.put("height", "90%");
        options.put("contentHeight", "100%");
        options.put("contentWidth", "100%");

        pf.dialog().openDynamic(dialog, options, parametros);
    }

    public static void executeJavaScript(String js) {
        pf.executeScript(js);
    }
}
