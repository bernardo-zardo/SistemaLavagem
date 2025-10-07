package com.bernardo.services;

import com.bernardo.entidades.TipoServico;
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
public class TipoServicoService extends BaseService<TipoServico> {

    private static final long serialVersionUID = 1L;

	@Override
    protected List<FiltrosPesquisa> getFiltros(Map<String, Object> filtros) {
        List<FiltrosPesquisa> filtrosPesquisa = new ArrayList<>();
        add(filtrosPesquisa, "t.tsIdTipoServico = '?tsIdTipoServico'", "tsIdTipoServico", filtros.get("tsIdTipoServico"));
        return filtrosPesquisa;
    }

    public List<TipoServico> filtrar(Map<String, Object> filtros) {
        String sql = "SELECT t FROM TipoServico t ";
        sql = adicionarFiltros(sql, getFiltros(filtros));
        Query query = customEntityManager.getEntityManager().createQuery(sql);

        Set<TipoServico> tipoServicoList = new HashSet<>();
        tipoServicoList.addAll(query.getResultList());
        return new ArrayList<>(tipoServicoList);
    }

    public List<TipoServico> getTipoServicoPorId(String idTipoServico) {
       String sql = "SELECT * FROM TIPO_SERVICO t "
                + "WHERE t.TS_ID_TIPO_SERVICO = '" + idTipoServico + "'";
        return customEntityManager.executeNativeQuery(TipoServico.class, sql);
    }
}
