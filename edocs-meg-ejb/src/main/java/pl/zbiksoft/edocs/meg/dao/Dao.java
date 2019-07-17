/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zbiksoft.edocs.meg.dao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author ZbikKomp
 */
@Stateless
public class Dao {

    //@PersistenceContext(unitName="EDOCS_DB")
    protected EntityManager em;
    
    @PostConstruct
    public void init() {
        em = Persistence.createEntityManagerFactory("EDOCS_MAIN").createEntityManager();
    }
    
    private final Logger logger = Logger.getLogger(getClass().getName());
    
    public <T extends Serializable> void save(T entity)   {
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(entity);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

    public <T extends Serializable> void saveOrUpdate(T entity)  {
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(entity);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
    
    public <T extends Serializable> void delete(T entity)  {
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(entity);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
    
    public Object tryToFindSingleResult(Query query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
