package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.entity.Product;
import com.mall.service.CategoryService;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/list")
    public String listPage(Model model) {
        Result<List<Category>> categoryResult = categoryService.getAllCategories();
        model.addAttribute("categories", categoryResult.getData());
        return "product/list";
    }
    
    @GetMapping("/data")
    @ResponseBody
    public Result<PageResult<Product>> getProductList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return productService.getProductList(name, categoryId, status, pageNum, pageSize);
    }
    
    @GetMapping("/add")
    public String addPage(Model model) {
        Result<List<Category>> categoryResult = categoryService.getAllCategories();
        model.addAttribute("categories", categoryResult.getData());
        return "product/add";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public Result<Void> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Result<Product> result = productService.getProductById(id);
        Result<List<Category>> categoryResult = categoryService.getAllCategories();
        
        if (result.isSuccess()) {
            model.addAttribute("product", result.getData());
        }
        model.addAttribute("categories", categoryResult.getData());
        return "product/edit";
    }
    
    @PostMapping("/update")
    @ResponseBody
    public Result<Void> updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }
    
    @PostMapping("/updateStatus")
    @ResponseBody
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return productService.updateProductStatus(id, status);
    }
    
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
    
    @PostMapping("/upload")
    @ResponseBody
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return productService.uploadImage(file, request);
    }
    
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Result<Product> getProductDetail(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
