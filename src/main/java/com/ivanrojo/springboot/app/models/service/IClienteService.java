package com.ivanrojo.springboot.app.models.service;

import java.util.List;

import com.ivanrojo.springboot.app.models.entity.Cliente;

public interface IClienteService {

public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public Cliente find(Long id);
	
	public void update(Cliente cliente, Long id);
	
	public void delete(Cliente cliente);
}
