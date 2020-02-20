package com.ivanrojo.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ivanrojo.springboot.app.models.dao.IClienteDao;
import com.ivanrojo.springboot.app.models.entity.Cliente;

@Controller
public class ClienteController {
	
	@Autowired
	@Qualifier("ClienteDaoImpl")
	private IClienteDao clienteDao;
	
	@RequestMapping(value="listar",method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("titulo", "Listado de Clientes.");
		model.addAttribute("clientes", clienteDao.findAll());
		return "cliente/listar";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String create(Map<String,Object> model) {
		Cliente cliente = new Cliente();
		model.put("edit", false);
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "cliente/form";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String store(@Valid Cliente cliente, BindingResult result,Model model) {
		if(result.hasErrors()) {
			model.addAttribute("edit", false);
			model.addAttribute("titulo", "Formulario de Cliente");
			return "cliente/form";
		}
		clienteDao.save(cliente);
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/cliente/{id}",method=RequestMethod.GET)
	public String show(@PathVariable Long id,Model model) {
		Cliente cliente = clienteDao.find(id);
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Información de: ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
		return "cliente/show";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String edit(@PathVariable Long id, Model model) {
		Cliente cliente = clienteDao.find(id);
		model.addAttribute("edit", true);
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar a : ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
		return "cliente/form";
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public String update(@Valid Cliente cliente, BindingResult result,@PathVariable Long id, Model model) {
		System.out.print(cliente.getId());
		if(result.hasErrors()) {
			model.addAttribute("edit", true);
			model.addAttribute("titulo", "Información de: ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
			return "cliente/form";
		}
		clienteDao.update(cliente,id);
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public String destroy(@PathVariable Long id, Model model) {
		Cliente cliente = clienteDao.find(id);
		clienteDao.delete(cliente);
		return "redirect:/listar";
	}
	
}
