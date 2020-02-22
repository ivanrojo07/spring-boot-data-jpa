package com.ivanrojo.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ivanrojo.springboot.app.models.entity.Cliente;

public interface IClienteQueryDAO extends PagingAndSortingRepository<Cliente, Long>{

}
