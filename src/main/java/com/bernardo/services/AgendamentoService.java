package com.bernardo.services;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import com.bernardo.entidades.Agendamento;
import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
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
		add(filtrosPesquisa, "a.agStatus <> 'X'", "agNaoCancelado", filtros.get("agNaoCancelado"));
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
	
	public List<Agendamento> consultarAgendamentosFiltrados(String filtroStatus, Date filtroDataIni, Date filtroDataFim,
			List<TipoServico> filtroTiposServico, List<Veiculo> filtroVeiculos) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.* FROM AGENDAMENTO_SERVICO a ");

		sql.append(" WHERE a.AG_DATA >= ").append(StringUtil.toMySQLDateTime(filtroDataIni));
		sql.append(" AND a.AG_DATA <= ").append(StringUtil.toMySQLDateTime(filtroDataFim));

		if (filtroStatus != null && !filtroStatus.equals("T")) {
			sql.append(" AND a.AG_STATUS = '").append(filtroStatus).append("' ");
		}

		if (filtroTiposServico != null && !filtroTiposServico.isEmpty()) {
			String ids = filtroTiposServico.stream().map(ts -> ts.getTsIdTipoServico().toString())
					.collect(Collectors.joining(","));
			sql.append(" AND a.AG_ID_TIPO_SERVICO IN (").append(ids).append(")");
		}

		if (filtroVeiculos != null && !filtroVeiculos.isEmpty()) {
			String ids = filtroVeiculos.stream().map(v -> v.getVeiIdVeiculo().toString())
					.collect(Collectors.joining(","));
			sql.append(" AND a.AG_ID_VEICULO IN (").append(ids).append(")");
		}

		sql.append(" ORDER BY a.AG_DATA DESC ");

		return customEntityManager.executeNativeQuery(Agendamento.class, sql.toString());
	}
}
