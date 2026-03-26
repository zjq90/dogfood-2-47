package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.access-path}")
    private String accessPath;
    
    private static final String PRODUCT_CACHE_KEY = "product:cache:";
    private static final String PRODUCT_LIST_CACHE_KEY = "product:list:";
    
    @Override
    public Result<PageResult<Product>> getProductList(String name, Long categoryId, Integer status, 
                                                      Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectList(name, categoryId, status);
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        
        PageResult<Product> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList(), 
                                                          pageNum, pageSize);
        return Result.success(pageResult);
    }
    
    @Override
    public Result<Product> getProductById(Long id) {
        String cacheKey = PRODUCT_CACHE_KEY + id;
        Product product = (Product) redisTemplate.opsForValue().get(cacheKey);
        
        if (product == null) {
            product = productMapper.selectById(id);
            if (product != null) {
                redisTemplate.opsForValue().set(cacheKey, product, 30, TimeUnit.MINUTES);
            }
        }
        
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }
    
    @Override
    public Result<Void> addProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            return Result.error("商品名称不能为空");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            return Result.error("商品价格不能为空或负数");
        }
        if (product.getCategoryId() == null) {
            return Result.error("商品分类不能为空");
        }
        
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getSales() == null) {
            product.setSales(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(Product.STATUS_ON);
        }
        
        productMapper.insert(product);
        clearProductListCache();
        
        return Result.success("添加成功", null);
    }
    
    @Override
    public Result<Void> updateProduct(Product product) {
        if (product.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        
        Product existProduct = productMapper.selectById(product.getId());
        if (existProduct == null) {
            return Result.error("商品不存在");
        }
        
        productMapper.update(product);
        
        redisTemplate.delete(PRODUCT_CACHE_KEY + product.getId());
        clearProductListCache();
        
        return Result.success("更新成功", null);
    }
    
    @Override
    public Result<Void> updateProductStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return Result.error("参数错误");
        }
        
        productMapper.updateStatus(id, status);
        
        redisTemplate.delete(PRODUCT_CACHE_KEY + id);
        clearProductListCache();
        
        return Result.success("操作成功", null);
    }
    
    @Override
    public Result<Void> deleteProduct(Long id) {
        productMapper.deleteById(id);
        
        redisTemplate.delete(PRODUCT_CACHE_KEY + id);
        clearProductListCache();
        
        return Result.success("删除成功", null);
    }
    
    @Override
    public Result<String> uploadImage(MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        
        if (!".jpg".equals(suffix) && !".jpeg".equals(suffix) && !".png".equals(suffix) && !".gif".equals(suffix)) {
            return Result.error("只支持jpg、jpeg、png、gif格式的图片");
        }
        
        if (file.getSize() > 10 * 1024 * 1024) {
            return Result.error("图片大小不能超过10MB");
        }
        
        String newFilename = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        
        File destDir = new File(uploadPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        
        File destFile = new File(destDir, newFilename);
        
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
        
        String accessUrl = accessPath + newFilename;
        return Result.success("上传成功", accessUrl);
    }
    
    private void clearProductListCache() {
        redisTemplate.delete(PRODUCT_LIST_CACHE_KEY);
    }
}
