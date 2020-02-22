package com.ivanrojo.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ivanrojo.springboot.app.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public void save(Cliente cliente);
	
	public Cliente find(Long id);
	
	public void update(Cliente cliente, Long id);
	
	public void delete(Cliente cliente);
	
	public void deleteById(Long id);
}
