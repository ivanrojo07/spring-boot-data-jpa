package com.ivanrojo.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivanrojo.springboot.app.models.dao.IClienteDao;
import com.ivanrojo.springboot.app.models.dao.IClienteQueryDAO;
import com.ivanrojo.springboot.app.models.dao.IFacturaDao;
import com.ivanrojo.springboot.app.models.dao.IProductoDao;
import com.ivanrojo.springboot.app.models.entity.Cliente;
import com.ivanrojo.springboot.app.models.entity.Factura;
import com.ivanrojo.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService{
	
	@Autowired
	private IClienteDao clienteDao;
	@Autowired
	private IClienteQueryDAO clienteDao2;
	@Autowired
	private IProductoDao productoDao;
	@Autowired
	private IFacturaDao facturaDao;

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

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return (List<Producto>) productoDao.findByNombre(term);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombreLikeIgnoreCase(String term){
		return (List<Producto>) productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Factura findFacturaById(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true )
	public Factura fetchFacturaById(Long id) {
		return facturaDao.fetchByIdWithClienteWithLineaFacturaWithProducto(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchClienteById(Long id) {
		return clienteDao2.fetchByIdWithFacturas(id);
	}

}
