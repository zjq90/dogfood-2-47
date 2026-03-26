package com.mall.service.impl;

import com.mall.entity.Bill;
import com.mall.mapper.BillMapper;
import com.mall.service.BillService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    
    @Autowired
    private BillMapper billMapper;
    
    @Override
    public Bill getById(Long id) {
        return billMapper.selectById(id);
    }
    
    @Override
    public List<Bill> getList(Bill bill) {
        return billMapper.selectList(bill);
    }
    
    @Override
    public boolean save(Bill bill) {
        return billMapper.insert(bill) > 0;
    }
    
    @Override
    public boolean update(Bill bill) {
        return billMapper.update(bill) > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        return billMapper.deleteById(id) > 0;
    }
    
    @Override
    public void exportBills(String startTime, String endTime, HttpServletResponse response) {
        List<Bill> bills = billMapper.selectExportList(startTime, endTime);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("账单列表");
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            String[] headers = {"账单编号", "订单编号", "用户名", "金额", "类型", "支付方式", "状态", "备注", "创建时间"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            
            for (int i = 0; i < bills.size(); i++) {
                Bill bill = bills.get(i);
                Row row = sheet.createRow(i + 1);
                
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(bill.getBillNo());
                cell0.setCellStyle(dataStyle);
                
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(bill.getOrderNo() != null ? bill.getOrderNo() : "-");
                cell1.setCellStyle(dataStyle);
                
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(bill.getUserName() != null ? bill.getUserName() : "-");
                cell2.setCellStyle(dataStyle);
                
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(bill.getAmount() != null ? bill.getAmount().toString() : "0");
                cell3.setCellStyle(dataStyle);
                
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(bill.getType() == 1 ? "收入" : "支出");
                cell4.setCellStyle(dataStyle);
                
                Cell cell5 = row.createCell(5);
                String paymentMethod = "";
                if ("alipay".equals(bill.getPaymentMethod())) {
                    paymentMethod = "支付宝";
                } else if ("wechat".equals(bill.getPaymentMethod())) {
                    paymentMethod = "微信";
                } else if ("bank".equals(bill.getPaymentMethod())) {
                    paymentMethod = "银行卡";
                }
                cell5.setCellValue(paymentMethod);
                cell5.setCellStyle(dataStyle);
                
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(bill.getStatus() == 1 ? "已完成" : "待处理");
                cell6.setCellStyle(dataStyle);
                
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(bill.getRemark() != null ? bill.getRemark() : "-");
                cell7.setCellStyle(dataStyle);
                
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(bill.getCreateTime() != null ? sdf.format(bill.getCreateTime()) : "-");
                cell8.setCellStyle(dataStyle);
            }
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("账单列表.xlsx", "UTF-8"));
            
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("导出账单失败", e);
        }
    }
}
