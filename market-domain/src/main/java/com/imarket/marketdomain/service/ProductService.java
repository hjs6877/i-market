package com.imarket.marketdomain.service;

import com.imarket.marketdomain.domain.Product;
import com.imarket.marketdomain.exception.ProductNotFoundException;
import com.imarket.marketdomain.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Page<Product> searchProduct(String productName,
                                       String description,
                                       int page,
                                       int size) {
        return productRepository.searchProduct(productName, description,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
}
