package com.ivanrojo.springboot.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ivanrojo.springboot.app.models.entity.Cliente;
import com.ivanrojo.springboot.app.models.entity.Factura;
import com.ivanrojo.springboot.app.models.entity.LineaFactura;
import com.ivanrojo.springboot.app.models.entity.Producto;
import com.ivanrojo.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping("factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Secured("ROLE_ADMIN")
	@GetMapping("{clienteId}/create")
	public String crear(@PathVariable(value="clienteId") Long clienteId, Model model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = clienteService.find(clienteId);
		if (cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null,locale));
			return "redirect:/listar";
		}
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
		model.addAttribute("factura", factura);
		return "factura/form";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping(value="cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
//		return clienteService.findByNombre(term);
		return clienteService.findByNombreLikeIgnoreCase(term);
	}
	@Secured("ROLE_ADMIN")
	@PostMapping("/store")
	public String save(@Valid Factura factura,
			BindingResult result,
			Model model,
			@RequestParam(name="item_id[]",required=false) Long[] itemId,
			@RequestParam(name="cantidad[]",required=false) Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status, 
			Locale locale) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			return "factura/form";
		}
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			model.addAttribute("error", messageSource.getMessage("text.factura.flash.lineas.error", null, locale));
			return "factura/form";
		}
		for (int i = 0; i<itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			LineaFactura linea = new LineaFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addLineaFactura(linea);
			log.info("ID: "+itemId[i].toString()+", Cantidad: "+cantidad[i].toString());
		}
		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.crear.success", null, locale));
		return "redirect:/cliente/"+factura.getCliente().getId();
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/show/{id}")
	public String show(@PathVariable(value = "id") Long factura_id,
			Model model,
			RedirectAttributes flash,
			Locale locale) {
		Factura factura = clienteService.fetchFacturaById(factura_id);//clienteService.findFacturaById(factura_id);
		if (factura == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null , locale));
			return "redirect:/listar";
		}
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", String.format(messageSource.getMessage("text.factura.ver.titulo", null, locale), factura.getDescripcion()));
		return "factura/show";
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("delete/{id}")
	public String destroy(@PathVariable(value="id")Long factura_id,
			RedirectAttributes flash, Locale locale) {
		Factura factura = clienteService.findFacturaById(factura_id);
		if (factura != null) {
			clienteService.deleteFactura(factura_id);	
			flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
			return "redirect:/cliente/"+factura.getCliente().getId();
		}
		flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
		return "redirect:/listar";
	}
}
