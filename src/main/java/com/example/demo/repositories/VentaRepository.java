package com.example.demo.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import com.example.demo.models.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta , Integer> {
    List<Venta> findByFechaBetween(LocalDateTime startDate, LocalDateTime endDate);

    
    @Query("SELECT SUM(v.totalIva) FROM Venta v WHERE v.fecha BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalIvaByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    
    @Query("SELECT SUM(v.totalIva) FROM Venta v") 
    BigDecimal sumTotalIvaAllSales();
    
    
    List<Venta> findByClienteIdCliente(Integer idCliente);
    
   
    
  
}