package com.bernardo.services;

import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
import com.bernardo.utils.FiltrosPesquisa;
import com.bernardo.utils.ServicoResumoAux;

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
    public List<ServicoResumoAux> buscarServicosPorTipoNoMes() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ts.TS_NOME AS tipoServico, COUNT(s.SER_ID_SERVICO) AS total ");
        sql.append("FROM servico s ");
        sql.append("JOIN tipo_servico ts ON ts.TS_ID_TIPO_SERVICO = s.SER_ID_TIPO_SERVICO ");
        sql.append("WHERE MONTH(s.SER_DATA) = MONTH(CURDATE()) ");
        sql.append("AND YEAR(s.SER_DATA) = YEAR(CURDATE()) ");
        sql.append("GROUP BY ts.TS_NOME ");
        sql.append("ORDER BY total DESC");

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = customEntityManager.executeNativeQueryArray(sql.toString());

        List<ServicoResumoAux> lista = new ArrayList<>();
        for (Object[] row : resultList) {
            lista.add(new ServicoResumoAux(
                (String) row[0],
                ((Number) row[1]).longValue()
            ));
        }

        return lista;
    }

}
