package com.ivanrojo.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.ivanrojo.springboot.app.models.entity.Cliente;

public interface IClienteQueryDAO extends CrudRepository<Cliente, Long>{

	
	
}
