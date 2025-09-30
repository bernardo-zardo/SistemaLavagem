package com.bernardo.filters;

import com.bernardo.beans.ResponsavelLogadoBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
*
* @author Bernardo Zardo Mergen
*/
@WebFilter("/restrito/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean logado = false;

        if (session != null) {
            ResponsavelLogadoBean responsavelBean = 
                (ResponsavelLogadoBean) session.getAttribute("responsavelLogadoBean");
            if (responsavelBean != null && responsavelBean.getResponsavelLogado() != null) {
                logado = true;
            }
        }

        if (logado) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }

    @Override
    public void destroy() {
    }
}
