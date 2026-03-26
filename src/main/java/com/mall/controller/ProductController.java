package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.service.CategoryService;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String productPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories().getData());
        return "product/list";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<List<Product>> getProductList(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) Long categoryId,
                                                @RequestParam(required = false) Integer status) {
        return productService.getProductList(name, categoryId, status);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseBody
    public Result<String> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping
    @ResponseBody
    public Result<String> updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @PutMapping("/{id}/status")
    @ResponseBody
    public Result<String> updateProductStatus(@PathVariable Long id, @RequestParam Integer status) {
        return productService.updateProductStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return productService.uploadImage(file);
    }
}