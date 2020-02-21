package com.ivanrojo.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.ivanrojo.springboot.app.models.entity.Cliente;

@Repository("ClienteDaoImpl")
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> findAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	public void save(Cliente cliente) {
		
		em.persist(cliente);
	}

	@Override
	public Cliente find(Long id) {
		Cliente cliente = em.find(Cliente.class,id);
		return cliente;
	}

	@Override
	public void update(Cliente cliente, Long id) {
		cliente.setId(id);
		em.merge(cliente);
	}

	@Override
	public void delete(Cliente cliente) {
		em.remove(cliente);
	}

}
