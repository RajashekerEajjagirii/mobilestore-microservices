package com.raj.product_service.Service;

import com.raj.product_service.DTO.ProductRequest;
import com.raj.product_service.DTO.ProductResponse;
import com.raj.product_service.Model.Product;
import com.raj.product_service.Repository.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j // For printing logs using Lombok
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    public Product addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .processor(productRequest.getProcessor())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .model(productRequest.getModel())
                .brand(productRequest.getBrand())
                .storage(productRequest.getStorage())
                .color(productRequest.getColor())
                .build();


        System.out.println("Product was added");
        log.info("{} product added successfully", product.getModel());
        return productRepo.save(product);

    }

    public List<ProductResponse> getAllProducts() {

        List<Product> products= productRepo.findAll();
        return products.stream().map(this:: mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .processor(product.getProcessor())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .model(product.getModel())
                .storage(product.getStorage())
                .color(product.getColor())
                .build();

    }

    public List<ProductResponse> getAllProductsBasedOnFieldSorting(String field) {
        List<Product> products=productRepo.findAll(Sort.by(Sort.Direction.ASC,field));
        return products.stream().map(this:: mapToProductResponse).toList();
    }

    public List<ProductResponse> getAllProductsWithPagination(int offset, int pageSize) {
        List<Product> products=productRepo.findAll(PageRequest.of(offset,pageSize)).getContent();
        return products.stream().map(this:: mapToProductResponse).toList();
    }

    public List<ProductResponse> getAllProductsWithPaginationSorting(int offset, int pageSize, String field) {
        List<Product> products=productRepo.findAll(PageRequest.of(offset,pageSize,Sort.by(Sort.Direction.ASC,field))).getContent();
        return products.stream().map(this:: mapToProductResponse).toList();
    }
}
