package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.service.ProductService;
import com.mall.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @GetMapping("/list")
    public Result<List<Product>> list(Product product) {
        List<Product> list = productService.getList(product);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }
    
    @PostMapping("/save")
    public Result<String> save(@RequestBody Product product) {
        boolean result = productService.save(product);
        if (result) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }
    
    @PostMapping("/update")
    public Result<String> update(@RequestBody Product product) {
        boolean result = productService.update(product);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = FileUploadUtil.upload(file, uploadPath);
            return Result.success("上传成功", fileName);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        boolean result = productService.updateStatus(id, status);
        if (result) {
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }
    
    @PostMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean result = productService.deleteById(id);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
