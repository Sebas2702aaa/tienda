package com.tienda.Desarrollo.repository;

import com.tienda.Desarrollo.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
    public List<Producto> findbyActivoTrue();

    public List<Producto> findByActivoTrue();
    
}
