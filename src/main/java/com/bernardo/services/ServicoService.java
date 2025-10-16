package com.bernardo.services;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.Servico;
import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
import com.bernardo.utils.FiltrosPesquisa;
import com.bernardo.utils.StringUtil;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
		String sql = "SELECT * FROM SERVICO s " + "WHERE s.SER_ID_SERVICO = '" + idServico + "'";
		return customEntityManager.executeNativeQuery(Servico.class, sql);
	}

	public List<Servico> consultarServicosFiltrados(String filtroEntrega, Date filtroDataIni, Date filtroDataFim,
			List<TipoServico> filtroTiposServico, List<Veiculo> filtroVeiculos, List<Responsavel> filtroResponsaveis) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s.* FROM SERVICO s ");

		sql.append(" WHERE s.SER_DATA >= ").append(StringUtil.toMySQLDateTime(filtroDataIni));
		sql.append(" AND s.SER_DATA <= ").append(StringUtil.toMySQLDateTime(filtroDataFim));

		if (filtroEntrega != null && !filtroEntrega.equals("A")) {
			if (filtroEntrega.equals("C")) {
				sql.append(" AND s.SER_PRECO_ENTREGA IS NOT NULL ");
			} else if (filtroEntrega.equals("S")) {
				sql.append(" AND s.SER_PRECO_ENTREGA IS NULL ");
			}
		}

		if (filtroTiposServico != null && !filtroTiposServico.isEmpty()) {
			String ids = filtroTiposServico.stream().map(ts -> ts.getTsIdTipoServico().toString())
					.collect(Collectors.joining(","));
			sql.append(" AND s.SER_ID_TIPO_SERVICO IN (" + ids + ")");
		}

		if (filtroVeiculos != null && !filtroVeiculos.isEmpty()) {
			String ids = filtroVeiculos.stream().map(v -> v.getVeiIdVeiculo().toString())
					.collect(Collectors.joining(","));
			sql.append(" AND s.SER_ID_VEICULO IN (" + ids + ")");
		}

		if (filtroResponsaveis != null && !filtroResponsaveis.isEmpty()) {
			String ids = filtroResponsaveis.stream().map(r -> r.getResIdResponsavel().toString())
					.collect(Collectors.joining(","));
			sql.append(" AND s.SER_ID_RESPONSAVEL IN (" + ids + ")");
		}

		sql.append(" ORDER BY s.SER_DATA DESC ");

		return customEntityManager.executeNativeQuery(Servico.class, sql.toString());
	}

}
