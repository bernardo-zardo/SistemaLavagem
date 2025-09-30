package com.bernardo.filters;

import com.bernardo.beans.ResponsavelLogadoBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/restrito/*") // tudo que estiver em /restrito será protegido
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nada a inicializar
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
            // usuário logado, continua para a página
            chain.doFilter(request, response);
        } else {
            // redireciona para a página de login
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }

    @Override
    public void destroy() {
        // nada a destruir
    }
}
