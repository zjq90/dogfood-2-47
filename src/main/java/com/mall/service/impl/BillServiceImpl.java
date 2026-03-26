package com.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.mapper.BillMapper;
import com.mall.service.BillService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    
    @Autowired
    private BillMapper billMapper;
    
    @Override
    public Result<PageResult<Bill>> getBillList(Integer type, String startTime, String endTime, 
                                                Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        
        PageHelper.startPage(pageNum, pageSize);
        List<Bill> list = billMapper.selectList(type, startTime, endTime);
        PageInfo<Bill> pageInfo = new PageInfo<>(list);
        
        PageResult<Bill> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList(), 
                                                       pageNum, pageSize);
        return Result.success(pageResult);
    }
    
    @Override
    public Result<Bill> getBillById(Long id) {
        Bill bill = billMapper.selectById(id);
        if (bill == null) {
            return Result.error("账单不存在");
        }
        return Result.success(bill);
    }
    
    @Override
    public Result<Void> addBill(Bill bill) {
        if (bill.getType() == null) {
            return Result.error("账单类型不能为空");
        }
        if (bill.getAmount() == null || bill.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("账单金额必须大于0");
        }
        if (bill.getTitle() == null || bill.getTitle().trim().isEmpty()) {
            return Result.error("账单标题不能为空");
        }
        
        if (bill.getStatus() == null) {
            bill.setStatus(Bill.STATUS_VALID);
        }
        
        billMapper.insert(bill);
        return Result.success("添加成功", null);
    }
    
    @Override
    public Result<Void> updateBill(Bill bill) {
        if (bill.getId() == null) {
            return Result.error("账单ID不能为空");
        }
        
        Bill existBill = billMapper.selectById(bill.getId());
        if (existBill == null) {
            return Result.error("账单不存在");
        }
        
        billMapper.update(bill);
        return Result.success("更新成功", null);
    }
    
    @Override
    public Result<Void> deleteBill(Long id) {
        billMapper.deleteById(id);
        return Result.success("删除成功", null);
    }
    
    @Override
    public void exportBills(Integer type, String startTime, String endTime, HttpServletResponse response) throws IOException {
        List<Bill> list = billMapper.selectList(type, startTime, endTime);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("账单明细");
        
        Row headerRow = sheet.createRow(0);
        String[] headers = {"账单编号", "类型", "金额", "标题", "描述", "关联订单", "状态", "创建时间"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        int rowNum = 1;
        for (Bill bill : list) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(bill.getBillNo());
            row.createCell(1).setCellValue(bill.getTypeText());
            
            Cell amountCell = row.createCell(2);
            amountCell.setCellValue(bill.getAmount().doubleValue());
            amountCell.setCellStyle(amountStyle);
            
            row.createCell(3).setCellValue(bill.getTitle());
            row.createCell(4).setCellValue(bill.getDescription());
            row.createCell(5).setCellValue(bill.getOrderNo());
            row.createCell(6).setCellValue(bill.getStatusText());
            row.createCell(7).setCellValue(bill.getCreateTime() != null ? bill.getCreateTime().format(formatter) : "");
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        String filename = "账单明细_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + 
                          URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()));
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
