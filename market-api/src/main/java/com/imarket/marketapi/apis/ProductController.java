package com.imarket.marketapi.apis;

import com.imarket.marketapi.apis.dto.ProductDto;
import com.imarket.marketapi.apis.response.SingleResponse;
import com.imarket.marketdomain.domain.Product;
import com.imarket.marketdomain.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/${api.version}/products", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ProductController {
    private ProductService productService;
    private ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SingleResponse<ProductDto>> postProduct(@Valid @RequestBody ProductDto.Post body) {
        ProductDto productDto = new ProductDto();
        Product product = productService.saveProduct(productDto.toProduct(body));
        return ResponseEntity.ok(new SingleResponse<>(HttpStatus.OK, modelMapper.map(product, ProductDto.class)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{product-id}")
    public ResponseEntity<SingleResponse<ProductDto>> getProduct(@PathVariable("product-id") long productId) {
        ProductDto productDto = new ProductDto();
        Product product = productService.findProductById(productId);
        return ResponseEntity.ok(new SingleResponse<>(HttpStatus.OK, productDto.toProductDto(product)));
    }
}
