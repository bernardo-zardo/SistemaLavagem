package com.bernardo.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class CustomEntityManager implements Serializable {

    @PersistenceContext(name = "gestao_clinica")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public <T> T salvar(T obj) {
        return entityManager.merge(obj);
    }

    public boolean deletar(Object obj) {
        if (!entityManager.contains(obj)) {
            obj = entityManager.merge(obj);
        }

        entityManager.remove(obj);
        return true;
    }

    public void executeNativeUpdate(String query) {
        Query q = entityManager.createNativeQuery(query);
        q.executeUpdate();
    }

    public List<Object> executeNativeQuery(String query) {
        Query q = entityManager.createNativeQuery(query);
        return q.getResultList();
    }

    public List executeNativeQuery(Class<?> classe, String query) {
        Query q = entityManager.createNativeQuery(query, classe);
        return q.getResultList();
    }
}
