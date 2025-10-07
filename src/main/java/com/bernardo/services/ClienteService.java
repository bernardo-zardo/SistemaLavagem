package com.bernardo.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import com.bernardo.entidades.Cliente;
import com.bernardo.utils.FiltrosPesquisa;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Stateless
@Named
public class ClienteService extends BaseService<Cliente> {

    private static final long serialVersionUID = 1L;

	@Override
    protected List<FiltrosPesquisa> getFiltros(Map<String, Object> filtros) {
        List<FiltrosPesquisa> filtrosPesquisa = new ArrayList<>();
        add(filtrosPesquisa, "c.cliCpf = '?cliCpf'", "cliCpf", filtros.get("cliCpf"));
        add(filtrosPesquisa, "c.cliIdCliente = '?cliIdCliente'", "cliIdCliente", filtros.get("cliIdCliente"));
        return filtrosPesquisa;
    }

    public List<Cliente> filtrar(Map<String, Object> filtros) {
        String sql = "SELECT c FROM Cliente c ";
        sql = adicionarFiltros(sql, getFiltros(filtros));
        Query query = customEntityManager.getEntityManager().createQuery(sql);

        Set<Cliente> clienteList = new HashSet<>();
        clienteList.addAll(query.getResultList());
        return new ArrayList<>(clienteList);
    }

    public List<Cliente> getPessoaPorCpf(String cpf) {
       String sql = "SELECT * FROM CLIENTE c "
                + "WHERE c.CLI_CPF = '" + cpf + "'";
        return customEntityManager.executeNativeQuery(Cliente.class, sql);
    }
}
