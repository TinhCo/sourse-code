package com.tinhco.repositories;

import com.tinhco.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryId(Integer categoryId); // Đổi tên phương thức
}
