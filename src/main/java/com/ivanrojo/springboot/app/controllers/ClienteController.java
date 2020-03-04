package com.ivanrojo.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ivanrojo.springboot.app.models.entity.Cliente;
import com.ivanrojo.springboot.app.models.service.IClienteService;
import com.ivanrojo.springboot.app.models.service.IUploadFileService;
import com.ivanrojo.springboot.app.util.paginator.PageRender;

@Controller
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService fileService;
	
	@Autowired
	private MessageSource messageSource;
	
	private String UPLOADS_FOLDER = "uploads";
	
	private final Logger log = LoggerFactory.getLogger(getClass());
//	Tercera forma de subir imagen
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
//		Path pathImage = Paths.get("uploads").resolve(filename).toAbsolutePath();
//		log.info("pathImage: "+pathImage);
//		Resource recurso = null;
//		try {
//			recurso = new UrlResource(pathImage.toUri());
//			if (!recurso.exists() && !recurso.isReadable()) {
//				throw new RuntimeException("Error: no se puede cargar la imagen: "+pathImage.toString());
//			}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Resource recurso = null;
		try {
			recurso = fileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+recurso.getFilename()+"\"").
				body(recurso);
	}
	
	@RequestMapping(value= {"listar",""},method=RequestMethod.GET)
	public String index(@RequestParam(name="page",defaultValue="0") int page, Model model, Authentication authentication, HttpServletRequest request, Locale locale) {
		
		if (authentication != null) {
			log.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			log.info("Utilizando de forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario "+auth.getName());
		}
		
		if (hasRole("ROLE_ADMIN")) {
			log.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		}
		else {
			log.info("Hola ".concat(auth.getName()).concat(" No tienes acceso!"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if (securityContext.isUserInRole("ADMIN")) {
			log.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" No tienes acceso!"));
		}
		
		if (request.isUserInRole("ROLE_ADMIN")) {
			log.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else {
			log.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" No tienes acceso!"));
		}
		
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("listar", clientes);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo",null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "cliente/listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String create(Map<String,Object> model, Locale locale) {
		Cliente cliente = new Cliente();
		model.put("edit", false);
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "cliente/form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String store(@Valid Cliente cliente, BindingResult result,Model model,@RequestParam("image") MultipartFile image, RedirectAttributes flash, SessionStatus status, Locale locale) {
		if(result.hasErrors()) {
			model.addAttribute("edit", false);
			model.addAttribute("titulo",  messageSource.getMessage("text.cliente.form.titulo", null, locale));
			return "cliente/form";
		}
		if (!image.isEmpty()) {
//			Path directorioRecursos = Paths.get("src//main//resources//static/upload");
//			String rootPath = directorioRecursos.toFile().getAbsolutePath();
//			String rootPath = "/var/www/uploads";
			String uniqueFilename = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
			Path rootAbsolutPath = rootPath.toAbsolutePath();
			log.info("rootPath: "+rootPath);
			log.info("rootAbsolutePath: "+rootAbsolutPath);
			try {
//				byte[] bytes = image.getBytes();
//				Path rutaCompleta = Paths.get(rootPath+"//"+image.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);
				Files.copy(image.getInputStream(), rootAbsolutPath);
				flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFilename + "'");
				
				cliente.setFoto(uniqueFilename);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String mensajeFlash = messageSource.getMessage("text.cliente.flash.crear.success", null, locale);
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/cliente/{id}",method=RequestMethod.GET)
	public String show(@PathVariable Long id,Model model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = clienteService.fetchClienteById(id);//clienteService.find(id);
		if(cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));
		return "cliente/show";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String edit(@PathVariable Long id, Model model, RedirectAttributes flash, Locale locale) {
		try {
			Cliente cliente = clienteService.find(id);
			model.addAttribute("edit", true);
			model.addAttribute("cliente", cliente);
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
//			flash.addFlashAttribute(attributeName, attributeValue)
			return "cliente/form";
		}
		catch (NullPointerException e) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public String update(@Valid Cliente cliente, BindingResult result,@PathVariable Long id,@RequestParam("image") MultipartFile image, Model model, RedirectAttributes flash, SessionStatus status, Locale locale) {
		if(result.hasErrors()) {
			model.addAttribute("edit", true);
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
			return "cliente/form";
		}
		if (!image.isEmpty()) {
			if(clienteService.find(id).getId() != null
					&& clienteService.find(id).getId() >0
					&& clienteService.find(id).getFoto() != null
					&& clienteService.find(id).getFoto().length() >0) {
				fileService.delete(clienteService.find(id).getFoto());
//				Path rootPath = Paths
//						.get(UPLOADS_FOLDER)
//						.resolve(clienteService.find(id).getFoto())
//						.toAbsolutePath();
//				log.info("borrado rootPath: "+rootPath);
//				File archivo = rootPath.toFile();
//				if (archivo.exists() && archivo.canRead()) {
//					if (archivo.delete()) {
//						flash.addFlashAttribute("warning", "La foto anterior '"+clienteService.find(id).getFoto()+"' ha sido eliminada");
//					}
//				}
				
			}
//			Path directorioRecursos = Paths.get("src//main//resources//static/upload");
//			String rootPath = directorioRecursos.toFile().getAbsolutePath();
//			String rootPath = "/var/www/uploads";
//			String uniqueFilename = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
//			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
//			Path rootAbsolutPath = rootPath.toAbsolutePath();
//			try {
//				byte[] bytes = image.getBytes();
//				Path rutaCompleta = Paths.get(rootPath+"//"+image.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);
//				Files.copy(image.getInputStream(), rootAbsolutPath);
			String uniqueFilename = null;
			try {
				uniqueFilename = fileService.copy(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
				flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFilename + "'");
				
				cliente.setFoto(uniqueFilename);
				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		clienteService.update(cliente,id);
		
		status.setComplete();
		String mensajeFlash = messageSource.getMessage("text.cliente.flash.editar.success", null, locale);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public String destroy(@PathVariable Long id, Model model, RedirectAttributes flash, Locale locale) {
//		Cliente cliente = clienteService.find(id);
//		clienteService.delete(cliente);
		Cliente cliente = clienteService.find(id);
//		Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
//		File archivo = rootPath.toFile();
//		if (archivo.exists() && archivo.canRead()) {
		if (fileService.delete(cliente.getFoto())) {
			String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
			flash.addFlashAttribute("warning", mensajeFotoEliminar);	
		}
//		}
		clienteService.deleteById(id);
		flash.addFlashAttribute("success",messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context == null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		
		if (auth == null) {
			return false;
			
		}
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
//		for (GrantedAuthority authority : authorities) {
//			if (role.equals(authority.getAuthority())) {
//				return true;
//			}
//		}
//		
//		return false;
	}
	
}
