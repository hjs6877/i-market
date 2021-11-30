package com.imarket.marketdomain.service;

import com.imarket.marketdomain.domain.Product;
import com.imarket.marketdomain.exception.ExceptionType;
import com.imarket.marketdomain.exception.ProductNotFoundException;
import com.imarket.marketdomain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product findProductById(long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.orElseThrow(() -> new ProductNotFoundException());
    }
}
