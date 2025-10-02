package com.bernardo.services;

import com.bernardo.entidades.Veiculo;
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
public class VeiculoService extends BaseService<Veiculo> {

    @Override
    protected List<FiltrosPesquisa> getFiltros(Map<String, Object> filtros) {
        List<FiltrosPesquisa> filtrosPesquisa = new ArrayList<>();
        add(filtrosPesquisa, "v.veiIdVeiculo = '?veiIdVeiculo'", "veiIdVeiculo", filtros.get("veiIdVeiculo"));
        add(filtrosPesquisa, "v.veiPlaca = '?veiPlaca'", "veiPlaca", filtros.get("veiPlaca"));
        return filtrosPesquisa;
    }

    public List<Veiculo> filtrar(Map<String, Object> filtros) {
        String sql = "SELECT v FROM Veiculo v ";
        sql = adicionarFiltros(sql, getFiltros(filtros));
        Query query = customEntityManager.getEntityManager().createQuery(sql);

        Set<Veiculo> veiculoList = new HashSet<>();
        veiculoList.addAll(query.getResultList());
        return new ArrayList<>(veiculoList);
    }

    public List<Veiculo> getVeiculoPorId(String idVeiculo) {
       String sql = "SELECT * FROM VEICULO v "
                + "WHERE v.VEI_ID_VEICULO = '" + idVeiculo + "'";
        return customEntityManager.executeNativeQuery(Veiculo.class, sql);
    }
}
