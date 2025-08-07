package com.example.demo.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DetalleVentaRequestDTO;
import com.example.demo.dto.VentaRequestDTO;
import com.example.demo.dto.VentaResponseDTO;
import com.example.demo.models.Cliente;
import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.models.Venta.Estado;
import com.example.demo.models.Venta.Metodo_pago;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.ProductoRepository;
import com.example.demo.repositories.VentaRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {
	private final VentaRepository ventaRepository;
	private final ProductoRepository productoRepository;
	private final ClienteRepository clienteRepository;
	
	@Autowired
	public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository, ClienteRepository clienteRepository) {
		this.ventaRepository = ventaRepository;
		this.productoRepository = productoRepository;
		this.clienteRepository = clienteRepository;
}

	@Transactional
	public VentaResponseDTO crearVenta(VentaRequestDTO ventaRequestDTO) {
		Cliente cliente = clienteRepository.findById(ventaRequestDTO.getIdCliente())
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " +ventaRequestDTO.getIdCliente()));
		
		Venta venta = new Venta();
		venta.setCliente(cliente);
		venta.setFecha(LocalDateTime.now());
		venta.setEstado(Estado.valueOf(ventaRequestDTO.getEstado()));
		venta.setMetodo_pago(Metodo_pago.valueOf(ventaRequestDTO.getMetodoPago()));
		
		BigDecimal totalVentaCalculado = BigDecimal.ZERO;
		BigDecimal totalIvaCalculado = BigDecimal.ZERO;
		
		for(DetalleVentaRequestDTO detalleRequest : ventaRequestDTO.getDetalles()) {
			Producto producto = productoRepository.findById(detalleRequest.getIdProducto())
					.orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleRequest.getIdProducto()));
				
			if(producto.getStock() < detalleRequest.getCantidad()) {
				throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
			}
			
			BigDecimal precioUnitario = producto.getPrecio();
			BigDecimal ivaPorcentaje = producto.getIva();
			
			BigDecimal subtotalLinea = precioUnitario.multiply(BigDecimal.valueOf(detalleRequest.getCantidad()))
					.setScale(2, RoundingMode.HALF_UP);
			
			BigDecimal ivaMontoLinea = BigDecimal.ZERO;
			if(ivaPorcentaje.compareTo(BigDecimal.ZERO) > 0) {
				ivaMontoLinea = subtotalLinea.multiply(ivaPorcentaje.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
						.setScale(2, RoundingMode.HALF_UP);
			}
		
		
		
		BigDecimal totalLinea = subtotalLinea.add(ivaMontoLinea).setScale(2, RoundingMode.HALF_UP);
		
		DetalleVenta detalle = new DetalleVenta();
		detalle.setProducto(producto);
		detalle.setCantidad(detalleRequest.getCantidad());
		detalle.setPrecioUnitatario(precioUnitario);
		detalle.setIvaPorcentaje(ivaPorcentaje);
		detalle.setSubtotalLinea(subtotalLinea);
		detalle.setIvaMontoLinea(ivaMontoLinea);
		detalle.setTotalLinea(totalLinea);
		
		venta.addDetalle(detalle);
		
		totalVentaCalculado = totalVentaCalculado.add(totalLinea);
		totalIvaCalculado = totalIvaCalculado.add(ivaMontoLinea);
		
		producto.setStock(producto.getStock() - detalleRequest.getCantidad());
		productoRepository.save(producto);
		}
		
		venta.setTotalVenta(totalVentaCalculado.setScale(2, RoundingMode.HALF_UP));
		venta.setTotalIva(totalIvaCalculado.setScale(2, RoundingMode.HALF_UP));
		
		Venta ventaGuardada = ventaRepository.save(venta);
		
		return convertToVentaResponseDTO(ventaGuardada);
	}
	
	@Transactional(readOnly = true)
	public BigDecimal getTotalIvaForSales() {
		BigDecimal totalIva = ventaRepository.sumTotalIvaAllSales();
		return totalIva != null ? totalIva.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}
	
	@Transactional(readOnly = true)
	public BigDecimal getTotalIvaForSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		BigDecimal totalIva = ventaRepository.sumTotalIvaByDateRange(startDate, endDate);
		return totalIva != null ? totalIva.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}
	
	@Transactional(readOnly = true)
	public List<VentaResponseDTO> getDetailedSalesReport(LocalDateTime startDate, LocalDateTime endDate){
		List<Venta> ventas = ventaRepository.findByFechaBetween(startDate, endDate);
		return ventas.stream()
				.map(this::convertToVentaResponseDTO)
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<VentaResponseDTO> getDetailedSalesCliente(Integer idCliente){
		List<Venta> ventas = ventaRepository.findByClienteIdCliente(idCliente);
		return ventas.stream()
				.map(this::convertToVentaResponseDTO)
				.collect(Collectors.toList());
	}
	
	
	private VentaResponseDTO convertToVentaResponseDTO(Venta venta) {
		VentaResponseDTO dto = new VentaResponseDTO();
		dto.setIdVenta(venta.getIdVenta());
		dto.setNombreCliente(venta.getCliente().getNombreCliente() + " " +venta.getCliente().getApellidos());
		dto.setFecha(venta.getFecha());
		dto.setTotalVenta(venta.getTotalVenta());
		dto.setTotalIva(venta.getTotalIva());
		dto.setEstado(venta.getEstado().name());
		dto.setMetodoPago(venta.getMetodo_pago().name());
		dto.setDetalles(venta.getDetalles().stream()
				.map(detalle -> {
					VentaResponseDTO.DetalleVentaResponseDTO detalleDTO = new VentaResponseDTO.DetalleVentaResponseDTO();
					detalleDTO.setNombreProducto(detalle.getProducto().getNombreProducto());
					detalleDTO.setCantidad(detalle.getCantidad());
					detalleDTO.setPrecioUnitario(detalle.getPrecioUnitatario());
					detalleDTO.setIvaPorcentaje(detalle.getIvaPorcentaje());
					detalleDTO.setIvaMontoLinea(detalle.getIvaMontoLinea());
					detalleDTO.setTotalLinea(detalle.getTotalLinea());
					return detalleDTO;
				})
				.collect(Collectors.toList()));
		return dto;
		
					
	}
}
