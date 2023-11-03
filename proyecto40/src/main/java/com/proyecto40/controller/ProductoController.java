package com.proyecto40.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto40.entity.Producto;
import com.proyecto40.repository.ProductoRepository;

//@Controller
@RestController
@RequestMapping("proyecto40")
public class ProductoController {
	public static final String VISTA_PRODUCTOS = "productos";
	public static final String VISTA_AGREGAR = "agregar";
	public static final String VISTA_EDITAR = "editar";
	@Autowired
	ProductoRepository pr;

	// método tipo texto string tipo Api Rest
	@GetMapping("listar")
	public List<Producto> listarProductos() {
		return this.pr.findAll();
	}

	// método para mostrar productos html usando ModelAndView
	@GetMapping("productos")
	public ModelAndView listarProductosModel() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("productos", this.pr.findAll());
		mv.setViewName(VISTA_PRODUCTOS);
		return mv;
	}

	// método para eliminar un producto tipo Api Rest
	@DeleteMapping("elimina/{id}")
	public void elimina(@PathVariable(value = "id") Integer id) {
		this.pr.deleteById(id);
	}

	// método para eliminar un producto desde html
	@GetMapping("eliminar/{id}")
	public String eliminarProducto(@PathVariable(value = "id") Integer id,RedirectAttributes redi) {
		this.pr.deleteById(id);
		redi.addFlashAttribute("mensaje","Producto eliminado del sistema");
		return "redirect:../" + VISTA_PRODUCTOS;
	}

	// método para mostrar html editar
	/*
	 * @GetMapping("editar/{id}") public String editar() { return VISTA_EDITAR; }
	 */
	// método para editar productos html ( muestra la info con ModelAndView)

	@GetMapping("editar/{id}")
	public ModelAndView editar(@PathVariable("id") Integer id, ModelAndView mv) {
		mv.addObject("productos", this.pr.findById(id).get());
		mv.setViewName(VISTA_EDITAR);
		return mv;
	}
	// método para implementar editar el producto tipo api rest

	@PutMapping("editarImplApi/{id}")
	public void editarImplApi(@PathVariable Integer id, @RequestBody Producto producto) {
		Producto prod = this.pr.findById(id).orElseThrow();
		prod.setNombre(producto.getNombre());
		prod.setPrecio(producto.getPrecio());
		prod.setCodigo(producto.getCodigo());
		System.out.println(producto);
		this.pr.save(prod);

	}

	// método para implementar editar el producto del html

	@GetMapping("editar/editarImpl/{id}")
	public String editarImpl(@PathVariable Integer id, @ModelAttribute("producto") Producto producto,
			RedirectAttributes redi) {
		Producto prod = this.pr.findById(id).orElseThrow();
		prod.setNombre(producto.getNombre());
		prod.setPrecio(producto.getPrecio());
		prod.setCodigo(producto.getCodigo());
		this.pr.save(prod);
		redi.addFlashAttribute("mensaje","editado correctamente");		
		// System.out.println(producto);
		return "redirect:../../" + VISTA_PRODUCTOS;
	}

	// método para mostrar html agregar

//		@GetMapping("agregar")
//		public String agregar() {
//			return VISTA_AGREGAR;
//		}

	// método para implementar agregar el producto tipo Api Rest

	@PostMapping("agrega")
	public Producto agrega(@RequestBody Producto producto) {
		System.out.println(producto);
		return this.pr.save(producto);
	}

	// método para mostrar form html agregar un producto

	@GetMapping("agregar")
	public String agregar(Model mo) {
		Producto prod = new Producto();
		mo.addAttribute("producto", prod);
		return VISTA_AGREGAR;
	}

	// método para implementar agregar el producto con html

	@PostMapping("agregarImpl")
	public String agregarImpl(@ModelAttribute("producto") Producto producto,RedirectAttributes redi) {
		//System.out.println("LLEGUEEE");
		//System.out.println(producto);
		try {
			this.pr.save(producto);			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		redi.addFlashAttribute("mensaje","Producto agregado correctamente");
		return "redirect:../proyecto40/" + VISTA_PRODUCTOS;
		
	}

}
