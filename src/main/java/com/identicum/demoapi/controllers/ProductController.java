package com.identicum.demoapi.controllers;

import java.net.URI;

import com.identicum.demoapi.models.Product;
import com.identicum.demoapi.repo.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController
{

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProductRepository productRepository;
	
	@GetMapping()
	public Iterable<Product> index()
	{
		log.debug("Searching products");
		return this.productRepository.findAll();
	}

	@PostMapping()
	public ResponseEntity<Product> create(@RequestBody Product product) {
		log.debug("Creating product {}" , product.getName());

		Product created = this.productRepository.save(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(created.getId())
				.toUri();

		return ResponseEntity.created(location).body(created);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable long id) {
		log.debug("Deleting product id {}", id );
		this.productRepository.deleteById(id);
	}
}
