package com.ivanrojo.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ivanrojo.springboot.app.models.entity.Cliente;
import com.ivanrojo.springboot.app.models.service.IClienteService;
import com.ivanrojo.springboot.app.util.paginator.PageRender;

@Controller
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@RequestMapping(value="listar",method=RequestMethod.GET)
	public String index(@RequestParam(name="page",defaultValue="0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("listar", clientes);
		System.out.println(clientes.getTotalPages());
		model.addAttribute("titulo", "Listado de Clientes.");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
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
	public String store(@Valid Cliente cliente, BindingResult result,Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("edit", false);
			model.addAttribute("titulo", "Formulario de Cliente");
			return "cliente/form";
		}
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente Creado con éxito.");
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/cliente/{id}",method=RequestMethod.GET)
	public String show(@PathVariable Long id,Model model) {
		Cliente cliente = clienteService.find(id);
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Información de: ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
		return "cliente/show";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String edit(@PathVariable Long id, Model model, RedirectAttributes flash) {
		try {
			Cliente cliente = clienteService.find(id);
			model.addAttribute("edit", true);
			model.addAttribute("cliente", cliente);
			model.addAttribute("titulo", "Editar a : ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
//			flash.addFlashAttribute(attributeName, attributeValue)
			return "cliente/form";
		}
		catch (NullPointerException e) {
			flash.addFlashAttribute("error", "El cliente no existe.");
			return "redirect:/listar";
		}
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public String update(@Valid Cliente cliente, BindingResult result,@PathVariable Long id, Model model) {
		System.out.print(cliente.getId());
		if(result.hasErrors()) {
			model.addAttribute("edit", true);
			model.addAttribute("titulo", "Información de: ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
			return "cliente/form";
		}
		clienteService.update(cliente,id);
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public String destroy(@PathVariable Long id, Model model, RedirectAttributes flash) {
//		Cliente cliente = clienteService.find(id);
//		clienteService.delete(cliente);
		clienteService.deleteById(id);
		flash.addFlashAttribute("success", "Cliente Eliminado con éxito.");
		return "redirect:/listar";
	}
	
}
