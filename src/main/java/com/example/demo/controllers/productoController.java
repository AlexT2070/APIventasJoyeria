package com.example.demo.controllers;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Producto;
import com.example.demo.repositories.ProductoRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api/producto")
public class productoController {

	@Autowired
	private ProductoRepository productoRepository;
	
	@GetMapping("/readAllProductos")
	public List<Producto> getAllProductos(){
		return productoRepository.findAll();
	}
	
	@GetMapping("/readProductos/{id}")
	public ResponseEntity<Producto> getProductoById(@PathVariable Integer id){
		Optional<Producto> producto = productoRepository.findById(id);
		return producto.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("/createProductos")
	public ResponseEntity<?> createProducto(@RequestBody Producto producto){
		producto.setIdProducto(null);
		Producto nuevoProducto = productoRepository.save(producto);
		return ResponseEntity.ok(nuevoProducto);
	}
	
	@PutMapping("/updateProducto/{id}")
	public ResponseEntity<Producto> updateUsuario(@PathVariable Integer id, @RequestBody Producto productoDetails){
		Optional<Producto> producto = productoRepository.findById(id);
		if(producto.isPresent()) {
		Producto updateProducto = producto.get();
		updateProducto.setNombreProducto(productoDetails.getNombreProducto());
		updateProducto.setDescripcion(productoDetails.getDescripcion());
		updateProducto.setPrecio(productoDetails.getPrecio());
		updateProducto.setActivo(productoDetails.getActivo());
		updateProducto.setStock(productoDetails.getStock());
		updateProducto.setIva(productoDetails.getIva());
		return ResponseEntity.ok(productoRepository.save(updateProducto));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/deleteProducto/{idProducto}")
	public ResponseEntity<Void> deleteProducto(@PathVariable Integer idProducto){
		if(productoRepository.existsById(idProducto)) {
			productoRepository.deleteById(idProducto);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/inventarioPdf")
	public ResponseEntity<byte[]> exportarProductosPdf(){
		try {
			List<Producto> productos = productoRepository.findAll();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			document.open();
			
			Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.black);
			Paragraph titutlo = new Paragraph("Lista de Productos", tituloFont);
			titutlo.setAlignment(Element.ALIGN_CENTER);
			document.add(titutlo);
			document.add(new Paragraph(""));
			
			
			PdfPTable tabla =new PdfPTable(7);
			tabla.setWidthPercentage(100);
			tabla.setWidths(new float[] {1f,3f,4f,2f,2f,2f,2f});
			
			String[] encabezados = {"ID", "Nombre","Descripcion", "Precio", "Stock", "Iva ","Activo"};
			for(String encabezado : encabezados) {
				PdfPCell celda = new PdfPCell(new Phrase(encabezado));
				celda.setBackgroundColor(new Color(100,180,255));
				celda.setHorizontalAlignment(Element.ALIGN_CENTER);
				tabla.addCell(celda);
			};
			
			for(Producto p: productos) {
				tabla.addCell(String.valueOf(p.getIdProducto()));
				tabla.addCell(String.valueOf(p.getNombreProducto()));
				tabla.addCell(String.valueOf(p.getDescripcion()));
				tabla.addCell("$" + p.getPrecio().toString());
				tabla.addCell(String.valueOf(p.getStock()));
				tabla.addCell(String.valueOf(p.getIva()+"%"));
				tabla.addCell(String.valueOf(p.getActivo()? "Si" : "No"));
			}
			if(document.getPageNumber() >1) {
				document.newPage();
			}
			document.add(tabla);
			document.close();
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.pdf")
					.contentType(MediaType.APPLICATION_PDF)
					.body(out.toByteArray());
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error al generar PDF: " + e.getMessage()).getBytes());
		}
	}
	
	
}