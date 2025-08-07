package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	
}
