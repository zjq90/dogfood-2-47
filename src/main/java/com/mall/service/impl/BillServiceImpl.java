package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.mapper.BillMapper;
import com.mall.service.BillService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillMapper billMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "bill:";
    private static final long REDIS_EXPIRE_TIME = 30;

    @Override
    public Result<Bill> getBillById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        Bill bill = (Bill) redisTemplate.opsForValue().get(redisKey);
        if (bill == null) {
            bill = billMapper.selectById(id);
            if (bill != null) {
                redisTemplate.opsForValue().set(redisKey, bill, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(bill);
    }

    @Override
    public Result<Bill> getBillByBillNo(String billNo) {
        Bill bill = billMapper.selectByBillNo(billNo);
        return Result.success(bill);
    }

    @Override
    public Result<List<Bill>> getBillList(String billNo, String orderNo, Integer billType, Integer billStatus) {
        List<Bill> billList = billMapper.selectList(billNo, orderNo, billType, billStatus);
        return Result.success(billList);
    }

    @Override
    public Result<String> addBill(Bill bill) {
        int result = billMapper.insert(bill);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<String> updateBill(Bill bill) {
        int result = billMapper.update(bill);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + bill.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public Result<String> deleteBill(Long id) {
        int result = billMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public void exportBill(HttpServletResponse response, String billNo, String orderNo, Integer billType, Integer billStatus) {
        List<Bill> billList = billMapper.selectList(billNo, orderNo, billType, billStatus);
        String[] headers = {"账单编号", "订单编号", "用户名称", "账单金额", "账单类型", "账单状态", "备注", "创建时间"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("账单列表");
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            for (int i = 0; i < billList.size(); i++) {
                Bill bill = billList.get(i);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(bill.getBillNo());
                dataRow.createCell(1).setCellValue(bill.getOrderNo());
                dataRow.createCell(2).setCellValue(bill.getUsername());
                dataRow.createCell(3).setCellValue(bill.getAmount().toString());
                dataRow.createCell(4).setCellValue(bill.getBillType() == 1 ? "收入" : "支出");
                dataRow.createCell(5).setCellValue(bill.getBillStatus() == 1 ? "已确认" : "待确认");
                dataRow.createCell(6).setCellValue(bill.getRemark() == null ? "" : bill.getRemark());
                dataRow.createCell(7).setCellValue(sdf.format(bill.getCreateTime()));
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("账单列表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}