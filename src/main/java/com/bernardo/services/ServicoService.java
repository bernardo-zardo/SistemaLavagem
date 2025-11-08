package com.bernardo.services;

import java.math.BigDecimal;
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

import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.Servico;
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

	public List<Object[]> consultarTotaisUltimos12Meses() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("    YEAR(s.SER_DATA) AS ano, ");
		sql.append("    MONTH(s.SER_DATA) AS mes, ");
		sql.append("    COUNT(*) AS total_servicos, ");
		sql.append("    SUM(s.SER_PRECO_TOTAL) AS total_ganhos ");
		sql.append("FROM SERVICO s ");
		sql.append("WHERE s.SER_DATA >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) ");
		sql.append("GROUP BY YEAR(s.SER_DATA), MONTH(s.SER_DATA) ");
		sql.append("ORDER BY ano, mes;");

		return customEntityManager.executeNativeQueryArray(sql.toString());
	}

	public List<Object[]> buscarFaturamentoDiarioMesAtual() {
		String sql = """
			    WITH RECURSIVE ultimos_dias AS (
			        SELECT DATE_SUB(CURDATE(), INTERVAL 14 DAY) AS dia
			        UNION ALL
			        SELECT DATE_ADD(dia, INTERVAL 1 DAY)
			        FROM ultimos_dias
			        WHERE dia < CURDATE()
			    )
			    SELECT
			        DATE_FORMAT(d.dia, '%d/%m') AS DIA,
			        COALESCE(SUM(s.SER_PRECO_TOTAL), 0) AS FATURAMENTO
			    FROM ultimos_dias d
			    LEFT JOIN SERVICO s ON DATE(s.SER_DATA) = d.dia
			    GROUP BY d.dia
			    ORDER BY d.dia
			    """;

	    return customEntityManager.executeNativeQueryArray(sql);
	}

	public BigDecimal consultarFaturamentoMesAtual() {
		String sql = """
				    SELECT COALESCE(SUM(s.SER_PRECO_TOTAL), 0) AS FATURAMENTO
				    FROM SERVICO s
				    WHERE EXTRACT(MONTH FROM s.SER_DATA) = EXTRACT(MONTH FROM CURRENT_DATE)
				      AND EXTRACT(YEAR FROM s.SER_DATA) = EXTRACT(YEAR FROM CURRENT_DATE)
				""";

		List<Object[]> resultado = customEntityManager.executeNativeQueryArray(sql);

		if (resultado != null && !resultado.isEmpty()) {
			Object valor = resultado.get(0);
			if (valor instanceof Number) {
				return BigDecimal.valueOf(((Number) valor).doubleValue());
			} else if (valor instanceof Object[] arr && arr.length > 0 && arr[0] instanceof Number) {
				return BigDecimal.valueOf(((Number) arr[0]).doubleValue());
			}
		}

		return BigDecimal.ZERO;
	}

	public BigDecimal consultarFaturamentoMesAnterior() {
		String sql = """
				    SELECT COALESCE(SUM(s.SER_PRECO_TOTAL), 0) AS FATURAMENTO
				    FROM SERVICO s
				    WHERE EXTRACT(MONTH FROM s.SER_DATA) = EXTRACT(MONTH FROM DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH))
				      AND EXTRACT(YEAR FROM s.SER_DATA) = EXTRACT(YEAR FROM DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH))
				""";

		List<Object[]> resultado = customEntityManager.executeNativeQueryArray(sql);

		if (resultado != null && !resultado.isEmpty()) {
			Object valor = resultado.get(0);
			if (valor instanceof Number) {
				return BigDecimal.valueOf(((Number) valor).doubleValue());
			} else if (valor instanceof Object[] arr && arr.length > 0 && arr[0] instanceof Number) {
				return BigDecimal.valueOf(((Number) arr[0]).doubleValue());
			}
		}

		return BigDecimal.ZERO;
	}
	
	public Long contarServicosMesAtual() {
	    String sql = """
	        SELECT COUNT(*) 
	        FROM SERVICO s
	        WHERE MONTH(s.SER_DATA) = MONTH(CURDATE())
	          AND YEAR(s.SER_DATA) = YEAR(CURDATE())
	    """;
	    List<Object> result = customEntityManager.executeNativeQuery(sql);
	    return result.isEmpty() ? 0L : ((Number) result.get(0)).longValue();
	}
}
