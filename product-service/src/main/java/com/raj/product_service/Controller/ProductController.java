package com.raj.product_service.Controller;

import com.raj.product_service.DTO.ProductRequest;
import com.raj.product_service.DTO.ProductResponse;
import com.raj.product_service.Model.Product;
import com.raj.product_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    //Add Product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest) {

        try{
           Product ap=productService.addProduct(productRequest);
           return ResponseEntity.status(HttpStatus.CREATED).body(ap);
       }catch(Exception e){
         e.printStackTrace();
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }

    //Fetching All Products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
       try{
           List<ProductResponse> products=productService.getAllProducts();
           return ResponseEntity.status(HttpStatus.OK).body(products);
       }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }

    //Fetching All Products based on Sorting
    @GetMapping("/{field}")
    public ResponseEntity<List<ProductResponse>> getAllProductsBasedOnSorting(@PathVariable String field) {
        try{
            List<ProductResponse> products=productService.getAllProductsBasedOnFieldSorting(field);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //Fetching All Products based on Pagination
    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<List<ProductResponse>> getAllProductsWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        try{
            List<ProductResponse> products=productService.getAllProductsWithPagination(offset,pageSize);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //Fetching All Products based on Sorting with Pagination
    @GetMapping("/pagination/{offset}/{pageSize}/{field}")
    public ResponseEntity<List<ProductResponse>> getAllProductsBySortingPagination(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
        try{
            List<ProductResponse> products=productService.getAllProductsWithPaginationSorting(offset,pageSize,field);
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
