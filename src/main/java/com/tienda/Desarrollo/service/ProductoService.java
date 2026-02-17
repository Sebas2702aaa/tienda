package com.tienda.Desarrollo.service;

import com.tienda.Desarrollo.domain.Producto;
import com.tienda.Desarrollo.repository.ProductoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductoService {
    
    //Permite la creacion de una instancia unica de ProductoRepository, y la crea de forma automatica
    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;
    
    public ProductoService(ProductoRepository productoRepository, FirebaseStorageService firebaseStorageService){
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }
    
    //Se agregan a la clase, los metodos: get, save, delete
    @Transactional(readOnly = true)
    public List<Producto> getProducto(boolean activo) {
        if (activo) {
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto){
        return productoRepository.findById(idProducto);
    }
    
    @Transactional
    public void save(Producto producto, MultipartFile imagenFile){
        producto = productoRepository.save(producto);
        if (!imagenFile.isEmpty()) { //Si no esta vacio, pasaron una imagen
            try{
                String rutaImagen = firebaseStorageService.uploadImage(imagenFile, "producto", producto.getIdProducto());
                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);
            } catch (IOException e) {
                
            }
        }
    }
    
    @Transactional
    public void delete (Integer idProducto){
        //Verifica si la producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            //Lanza una excepcion para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("El producto con ID" + idProducto + " no existe.");
        }
        try{
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            //Lanza una nueva excepcion para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el producto. Tiene datos asociados.", e);
        }
    }
}
