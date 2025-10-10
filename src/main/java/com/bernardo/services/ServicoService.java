package com.bernardo.services;

import com.bernardo.entidades.Servico;
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
public class ServicoService extends BaseService<Servico> {

    private static final long serialVersionUID = 1L;

	@Override
    protected List<FiltrosPesquisa> getFiltros(Map<String, Object> filtros) {
        List<FiltrosPesquisa> filtrosPesquisa = new ArrayList<>();
        add(filtrosPesquisa, "s.serIdServico = '?serIdServico'", "serIdServico", filtros.get("serIdServico"));
        return filtrosPesquisa;
    }

    public List<Servico> filtrar(Map<String, Object> filtros) {
        String sql = "SELECT s FROM Servico s ";
        sql = adicionarFiltros(sql, getFiltros(filtros));
        Query query = customEntityManager.getEntityManager().createQuery(sql);

        Set<Servico> servicoList = new HashSet<>();
        servicoList.addAll(query.getResultList());
        return new ArrayList<>(servicoList);
    }

    public List<Servico> getServicoPorId(String idServico) {
       String sql = "SELECT * FROM SERVICO s "
                + "WHERE s.SER_ID_SERVICO = '" + idServico + "'";
        return customEntityManager.executeNativeQuery(Servico.class, sql);
    }
}
