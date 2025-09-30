package com.bernardo.services;

import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.Usuarios;
import com.bernardo.utils.FiltrosPesquisa;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Stateless
@Named
public class ResponsavelService extends BaseService<Responsavel> {

    @Override
    protected List<FiltrosPesquisa> getFiltros(Map<String, Object> filtros) {
        List<FiltrosPesquisa> filtrosPesquisa = new ArrayList<>();
        add(filtrosPesquisa, "r.resCpf = '?resCpf'", "resCpf", filtros.get("resCpf"));
        add(filtrosPesquisa, "r.resIdResponsavel = '?resIdResponsavel'", "resIdResponsavel", filtros.get("resIdResponsavel"));
        return filtrosPesquisa;
    }

    public List<Responsavel> filtrar(Map<String, Object> filtros) {
        String sql = "SELECT r FROM Responsavel r ";
        sql = adicionarFiltros(sql, getFiltros(filtros));
        Query query = customEntityManager.getEntityManager().createQuery(sql);

        Set<Responsavel> responsavelList = new HashSet<>();
        responsavelList.addAll(query.getResultList());
        return new ArrayList<>(responsavelList);
    }

    public List<Responsavel> getPessoaPorCpf(String cpf) {
       String sql = "SELECT * FROM RESPONSAVEL r "
                + "WHERE r.RES_CPF = '" + cpf + "'";
        return customEntityManager.executeNativeQuery(Responsavel.class, sql);
    }
}
