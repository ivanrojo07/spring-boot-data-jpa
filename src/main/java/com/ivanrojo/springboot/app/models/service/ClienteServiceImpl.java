package com.ivanrojo.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivanrojo.springboot.app.models.dao.IClienteDao;
import com.ivanrojo.springboot.app.models.dao.IClienteQueryDAO;
import com.ivanrojo.springboot.app.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService{
	
	@Autowired
	private IClienteDao clienteDao;
	@Autowired
	private IClienteQueryDAO clienteDao2;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
//		return clienteDao.findAll();
		return (List<Cliente>) clienteDao2.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao2.save(cliente);
		
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente find(Long id) {
//		return clienteDao.find(id);
		return clienteDao2.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void update(Cliente cliente, Long id) {
		clienteDao.update(cliente, id);
		
	}

	@Override
	@Transactional
	public void delete(Cliente cliente) {
		clienteDao.delete(cliente);
		
	}
	
	@Override
	@Transactional
	public void deleteById(Long id) {
		clienteDao2.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return (Page<Cliente>) clienteDao2.findAll(pageable);
	}

}
