package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer>{
	
	List<Pago> findByClienteIdCliente(Integer IdCliente);
	 List<Pago> findByVentaIdVenta(Integer idVenta);
}
