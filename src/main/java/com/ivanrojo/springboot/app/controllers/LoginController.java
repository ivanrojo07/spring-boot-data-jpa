package com.ivanrojo.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error",required=false) String error,Model model,Principal principal, RedirectAttributes flash) {
//		SI YA INICIO SESIÓN
		if (principal != null) {
			flash.addFlashAttribute("info", "Ya has iniciado sesión anteriormente.");
			return "redirect:/";
		}
		if (error != null) {
			model.addAttribute("error", "Error en el inicio de sesión: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo.");
		}
		return "login";
	}
}
