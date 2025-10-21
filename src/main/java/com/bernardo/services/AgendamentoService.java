package com.bernardo.services;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import com.bernardo.entidades.Agendamento;
import com.bernardo.utils.FiltrosPesquisa;
import com.bernardo.utils.StringUtil;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Stateless
@Named
public class AgendamentoService extends BaseService<Agendamento> {

	private static final long serialVersionUID = 1L;

	@Override
	protected List<FiltrosPesquisa> getFiltros(Map<String, Object> filtros) {
		List<FiltrosPesquisa> filtrosPesquisa = new ArrayList<>();
		add(filtrosPesquisa, "a.agIdAgendamento = '?agIdAgendamento'", "agIdAgendamento", filtros.get("agIdAgendamento"));
		return filtrosPesquisa;
	}

	public List<Agendamento> filtrar(Map<String, Object> filtros) {
		String sql = "SELECT a FROM Agendamento a ";
		sql = adicionarFiltros(sql, getFiltros(filtros));
		Query query = customEntityManager.getEntityManager().createQuery(sql);

		Set<Agendamento> agendamentoList = new HashSet<>();
		agendamentoList.addAll(query.getResultList());
		return new ArrayList<>(agendamentoList);
	}

	public List<Agendamento> getAgendamentoPorId(String idAgendamento) {
		String sql = "SELECT * FROM AGENDAMENTO_SERVICO a " + "WHERE a.AG_ID_AGENDAMENTO = '" + idAgendamento + "'";
		return customEntityManager.executeNativeQuery(Agendamento.class, sql);
	}
	
	public List<Date> buscarHorariosOcupadosPorData(Date dataSelecionada) {
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT a.AG_HORA ");
	    sql.append("FROM AGENDAMENTO_SERVICO a ");
	    sql.append("WHERE a.AG_DATA = ").append(StringUtil.toMySQLDateTime(dataSelecionada));
	    sql.append(" AND a.AG_STATUS <> 'X' "); // ignora cancelados
	    sql.append(" ORDER BY a.AG_HORA ");

	    List<Object> resultados = customEntityManager.executeNativeQuery(sql.toString());

	    List<Date> horarios = new ArrayList<>();
	    for (Object obj : resultados) {
	        if (obj != null) {
	            if (obj instanceof Time) {
	                horarios.add(new Date(((Time) obj).getTime()));
	            } else if (obj instanceof Date) {
	                horarios.add((Date) obj);
	            }
	        }
	    }
	    return horarios;
	}


}
