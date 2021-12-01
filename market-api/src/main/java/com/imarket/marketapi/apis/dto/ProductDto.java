package com.imarket.marketapi.apis.dto;

import com.imarket.marketdomain.domain.Product;
import com.imarket.marketdomain.domain.ProductCategory;
import com.imarket.marketdomain.domain.Seller;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class ProductDto {
    private long productId;
    private long sellerId;
    private String productName;
    private String description;
    private long price;
    private boolean canPurchase;
    private long productCategoryId;

    @Data
    public static class Post {
        @Positive
        private long sellerId;

        @Positive
        private long productCategoryId;

        @NotBlank
        private String productName;

        @Positive
        private long price;
        private String description;
    }

    @Data
    public static class Search {
        private String productName;
        private String description;
    }

    public Product toProduct(ProductDto.Post productPostDto) {
        Seller seller = new Seller();
        seller.setId(productPostDto.getSellerId());
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(productPostDto.getProductCategoryId());
        Product product = new Product();

        product.setSeller(seller);
        product.setProductCategory(productCategory);
        product.setProductName(productPostDto.getProductName());
        product.setDescription(productPostDto.getDescription());
        product.setPrice(productPostDto.getPrice());

        return product;
    }

    public ProductDto toProductDto(Product product) {
        this.setProductId(product.getId());
        this.setSellerId(product.getSeller().getId());
        this.setProductCategoryId(product.getProductCategory().getId());
        this.setProductName(product.getProductName());
        this.setDescription(product.getDescription());
        this.setPrice(product.getPrice());
        this.setCanPurchase(product.isCanPurchase());
        return this;
    }
}
