package com.example.demo.controllers;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.dto.PagoRequestDTO;
import com.example.demo.dto.PagoResponseDTO;
import com.example.demo.models.Pago;
import com.example.demo.models.Venta;
import com.example.demo.repositories.PagoRepository;
import com.example.demo.repositories.VentaRepository;
import com.example.demo.services.PagoService;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
	private final PagoService pagoService;
	private final PagoRepository pagoRepository;
	private final VentaRepository ventaRepository;
	
	public PagoController(PagoService pagoService, PagoRepository pagoRepository, VentaRepository ventaRepository) {
		this.pagoService = pagoService;
		this.pagoRepository = pagoRepository;
		this.ventaRepository = ventaRepository;
	}
	
	@GetMapping("/pagoPorCliente/{idCliente}")
	public ResponseEntity<List<PagoResponseDTO>> getPagosPorCliente(@PathVariable Integer idCliente){
		List<PagoResponseDTO> pagos = pagoService.getPagosporCliente(idCliente);
		if(pagos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(pagos);
	}
	
	@GetMapping("/readAllpagos")
	public ResponseEntity<List<PagoResponseDTO>> getAllPagos(){
		List<PagoResponseDTO> pagos = pagoService.getAllPagos();
		return ResponseEntity.ok(pagos);
	}
	

	@PutMapping("/update/{idPago}")
	public ResponseEntity<PagoResponseDTO> updatePago(@PathVariable Integer idPago, @RequestBody PagoRequestDTO pagoRequestDTO){
		try {
			PagoResponseDTO pagoActualizado = pagoService.updatePago(idPago, pagoRequestDTO);
			return ResponseEntity.ok(pagoActualizado);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	
	
	@PostMapping("/createPago")
	public ResponseEntity<?> crearPago(@Valid @RequestBody PagoRequestDTO pagoRequestDTO){
		try {
			Pago pago = pagoService.RegisterPago(pagoRequestDTO);
				return ResponseEntity.status(HttpStatus.CREATED).body(pago);
		}catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(Map.of(
	                "error", e.getMessage(),
	                "timestamp", LocalDateTime.now()
	            ));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(Map.of(
		            "error", "Error interno del servidor",
		            "message", e.getMessage(),
		            "timestamp", LocalDateTime.now()
		        ));		
			}
	}
	
	
	@DeleteMapping("/deletePago/{id}")
	public ResponseEntity<Void> deletePago(@PathVariable Integer id){
	if(pagoRepository.existsById(id)) {
		pagoRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}else {
		return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/pagosxls")
	public ResponseEntity<byte[]> exportarPagosExcel(){
		try(Workbook worbook = new XSSFWorkbook()){
			Sheet hoja = worbook.createSheet("Pagos");
			
			
			Row encabezado = hoja.createRow(0);
			encabezado.createCell(0).setCellValue("ID");
			encabezado.createCell(1).setCellValue("Cliente");
			encabezado.createCell(2).setCellValue("Monto");
			encabezado.createCell(3).setCellValue("MetodoPago");
			
			List<Pago> pagos = pagoRepository.findAll();
			int rowNum = 1;
			for(Pago pago: pagos) {
				Row fila = hoja.createRow(rowNum++);
				fila.createCell(0).setCellValue(pago.getIdPago());
				fila.createCell(1).setCellValue(pago.getCliente().getNombreCliente()+ " " + pago.getCliente().getApellidos());
				fila.createCell(2).setCellValue(pago.getMonto().toString());
				fila.createCell(3).setCellValue(pago.getMetodoPago().toString());
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			worbook.write(outputStream);
			
			byte[] excelBytes = outputStream.toByteArray();
			
			return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pagos.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);
			
		}catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(("Error al generar el Excel: "+ e.getMessage()).getBytes());
		}
	}
	
	@GetMapping("/pdfPagos")
	public ResponseEntity<byte[]> exportarPagospdf(){
		try {
			List<Pago> pagos = pagoRepository.findAll();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			Document document = new Document();
			PdfWriter.getInstance(document, out);
			document.open();
			
			Font titulofont = FontFactory.getFont(FontFactory.TIMES_BOLD,12, Color.black);
			Paragraph titulo = new Paragraph("Lista de pagos", titulofont);
			titulo.setAlignment(Element.ALIGN_CENTER);
			document.add(titulo);
			document.add(new Paragraph(""));
			
			PdfPTable tabla = new PdfPTable(4);
			tabla.setWidthPercentage(100);
			tabla.setWidths(new float[] {2f, 4f, 2f, 2f});
			
			Stream.of("ID", "Cliente", "Monto", "MetodoPago").forEach(t ->{
				PdfPCell celda = new PdfPCell(new Phrase(t));
				celda.setBackgroundColor(new Color(100,180,255));
				celda.setHorizontalAlignment(Element.ALIGN_CENTER);
				tabla.addCell(celda);
			});
			
			for (Pago p: pagos) {
				tabla.addCell(String.valueOf(p.getIdPago()));
				tabla.addCell(String.valueOf(p.getCliente().getNombreCliente()+ " "+ p.getCliente().getApellidos()));
				tabla.addCell("$" + p.getMonto());
				tabla.addCell(String.valueOf(p.getMetodoPago()));
			}
			
			document.add(tabla);
			document.close();
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= pagos.pdf")
					.contentType(MediaType.APPLICATION_PDF)
					.body(out.toByteArray());
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
	}
	
	@GetMapping("venta/{idVenta}/estadoPagos")
	public ResponseEntity<Map<String, Object>> getEstadoPagos(@PathVariable Integer idVenta){
		Venta venta = ventaRepository.findById(idVenta)
				.orElseThrow(() -> new RuntimeException("Venta no encontrada"));
		
		List<Pago> pagos = pagoRepository.findByVentaIdVenta(idVenta);
		
		BigDecimal saldoPendiente = venta.getTotalVenta().subtract(venta.getTotalPagado());
		
		Map<String, Object> response = new HashMap<>();
		response.put("totalVenta", venta.getTotalVenta());
		response.put("totalPagado", venta.getTotalPagado());
		response.put("SaldoPendiente", saldoPendiente);
		response.put("estado", venta.getEstado());
		response.put("pagos", pagos.stream().map(this::convertToPagoDTO).collect(Collectors.toList()));
		
		return ResponseEntity.ok(response);
	}
	
	private Map<String, Object> convertToPagoDTO(Pago pago){
		Map<String, Object> dto = new HashMap<>();
		dto.put("idPago", pago.getIdPago());
		dto.put("monto", pago.getMonto());
		dto.put("metodoPago", pago.getMetodoPago());
		dto.put("fecha", pago.getFecha());
		return dto;
	}
	
	
	
}
