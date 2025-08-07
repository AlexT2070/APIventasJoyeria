package com.example.demo.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.dto.IvaReportDTO;
import com.example.demo.dto.VentaRequestDTO;
import com.example.demo.dto.VentaResponseDTO;
import com.example.demo.services.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
	private final VentaService ventaService;
	
	@Autowired
	public VentaController(VentaService ventaService) {
		this.ventaService = ventaService;
	}
	
	@PostMapping
	public ResponseEntity<VentaResponseDTO> crearVenta(@RequestBody VentaRequestDTO ventaRequestDTO){
		try {
			VentaResponseDTO nuevaVenta = ventaService.crearVenta(ventaRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
		}catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@GetMapping("/iva_total")
	public ResponseEntity<IvaReportDTO> getIvaTotal(@RequestBody IvaReportDTO requestDto){
		BigDecimal ivaTotal = ventaService.getTotalIvaForSales();
		IvaReportDTO report = new IvaReportDTO();
		return ResponseEntity.ok(report);
	}
	
	@GetMapping("/iva_total_fecha")
	public ResponseEntity<IvaReportDTO> getIvaTotalByDateRange(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		BigDecimal ivaTotal = ventaService.getTotalIvaForSalesByDateRange(startDate, endDate);
		IvaReportDTO report = new IvaReportDTO();
		return ResponseEntity.ok(report);
	}
	
	@GetMapping("/reporte_detallado")
	public ResponseEntity<List<VentaResponseDTO>> getDetailedSalesReporrt(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime starterDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<VentaResponseDTO> ventas = ventaService.getDetailedSalesReport(starterDate, endDate);
		return ResponseEntity.ok(ventas);
	}
	
	@GetMapping("/venta_por_cliente{idCliente}")
	public ResponseEntity<List<VentaResponseDTO>> getDetailedSalesCliente(@PathVariable Integer idCliente ){
		List<VentaResponseDTO> ventas = ventaService.getDetailedSalesCliente(idCliente);
		
		if(ventas.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(ventas);
	}
}
