package com.ivanrojo.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
	
	private String UPLOADS_FOLDER = "uploads";
	
	private final Logger log = LoggerFactory.getLogger(getClass());
//	Tercera forma de subir imagen
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
	
	@RequestMapping(value="listar",method=RequestMethod.GET)
	public String index(@RequestParam(name="page",defaultValue="0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("listar", clientes);
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
	public String store(@Valid Cliente cliente, BindingResult result,Model model,@RequestParam("image") MultipartFile image, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("edit", false);
			model.addAttribute("titulo", "Formulario de Cliente");
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
				flash.addFlashAttribute("info", "La foto de perfil se ha subido correctamente: '"+uniqueFilename+"'.");
				
				cliente.setFoto(uniqueFilename);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente Creado con éxito.");
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/cliente/{id}",method=RequestMethod.GET)
	public String show(@PathVariable Long id,Model model, RedirectAttributes flash) {
		Cliente cliente = clienteService.fetchClienteById(id);//clienteService.find(id);
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
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
	public String update(@Valid Cliente cliente, BindingResult result,@PathVariable Long id,@RequestParam("image") MultipartFile image, Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("edit", true);
			model.addAttribute("titulo", "Información de: ".concat(cliente.getNombre().concat(" ".concat(cliente.getApellido()))));
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
				flash.addFlashAttribute("info", "La foto de perfil se ha subido correctamente: '"+uniqueFilename+"'.");
				
				cliente.setFoto(uniqueFilename);
				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		clienteService.update(cliente,id);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente se ha actualizado con éxito.");
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.POST)
	public String destroy(@PathVariable Long id, Model model, RedirectAttributes flash) {
//		Cliente cliente = clienteService.find(id);
//		clienteService.delete(cliente);
		Cliente cliente = clienteService.find(id);
//		Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
//		File archivo = rootPath.toFile();
//		if (archivo.exists() && archivo.canRead()) {
		if (fileService.delete(cliente.getFoto())) {
			flash.addFlashAttribute("warning", "Foto "+cliente.getFoto()+" eliminada con éxito");	
		}
//		}
		clienteService.deleteById(id);
		flash.addFlashAttribute("success", "Cliente Eliminado con éxito.");
		return "redirect:/listar";
	}
	
}
