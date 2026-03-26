package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.url:/uploads}")
    private String uploadUrl;

    private static final String REDIS_KEY_PREFIX = "product:";
    private static final long REDIS_EXPIRE_TIME = 30;

    @Override
    public Result<Product> getProductById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        Product product = (Product) redisTemplate.opsForValue().get(redisKey);
        if (product == null) {
            product = productMapper.selectById(id);
            if (product != null) {
                redisTemplate.opsForValue().set(redisKey, product, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(product);
    }

    @Override
    public Result<List<Product>> getProductList(String name, Long categoryId, Integer status) {
        List<Product> productList = productMapper.selectList(name, categoryId, status);
        return Result.success(productList);
    }

    @Override
    public Result<String> addProduct(Product product) {
        int result = productMapper.insert(product);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<String> updateProduct(Product product) {
        int result = productMapper.update(product);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + product.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result<String> updateProductStatus(Long id, Integer status) {
        int result = productMapper.updateStatus(id, status);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }

    @Override
    public Result<String> deleteProduct(Long id) {
        int result = productMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result<String> uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择上传文件");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }
        
        // 验证文件大小 (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            return Result.error("文件大小不能超过10MB");
        }
        
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + extension;
            
            // 创建上传目录（如果不存在）
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            String filePath = uploadPath + File.separator + fileName;
            File dest = new File(filePath);
            FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
            
            // 返回可访问的URL（相对于context-path的路径）
            String fileUrl = uploadUrl + "/" + fileName;
            return Result.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}