package com.dcj.meeting.util;

import com.dcj.meeting.pojo.entity.Room;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 董成杰
 * @date 2020/5/18 23:15
 */
public class RoomExcelUtil {

    /**
     * 从excel文件中读取会议室信息
     *
     * @param path excel文件路径
     * @return 会议室列表
     */
    public static List<Room> getRoomsFromExcel(String path) {
        List<Room> rooms = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            //工作表
            Workbook workbook = null;
            try {
                workbook = WorkbookFactory.create(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取第一个工作簿
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet != null) {
                // 行数
                int rowNumbers = sheet.getLastRowNum();
                // 读数据，第二行开始读取
                for (int row = 1; row <= rowNumbers; row++) {
                    Row r = sheet.getRow(row);
                    if (r == null) {
                        continue;
                    }
                    Room room = new Room();
                    if (r.getCell(0) == null || r.getCell(0).getStringCellValue().equals("")) {
                        continue;//会议室地点为空
                    }
                    room.setPlace(r.getCell(0).getStringCellValue());
                    if (r.getCell(1) == null || r.getCell(1).toString().equals("")) {
                        continue;//会议室可容纳人数为空
                    }
                    room.setSize((int) r.getCell(1).getNumericCellValue());
                    if (r.getCell(2) == null || r.getCell(2).toString().equals("")) {
                        room.setAvailable(1);//开放状态不填默认为开放
                    } else {
                        room.setAvailable((int) r.getCell(2).getNumericCellValue());
                    }
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }

    /**
     * 将会议室列表写入excel文件
     *
     * @param in    excel输入路径
     * @param out   excel输出路径
     * @param rooms 会议室列表
     * @return 成功写入条数
     */
    public static int writeRoomsToExcel(String in, String out, List<Room> rooms) {
        int rows = rooms.size();
        int count = 0;//成功写出条数
        Workbook workbook = null;
        //获得工作表
        try (FileInputStream fis = new FileInputStream(new File(in))) {
            workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获得工作簿
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet != null) {
            //清空除第一行外的数据
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                if (sheet.getRow(i) != null) {
                    sheet.removeRow(sheet.getRow(i));
                }
            }
            //写入数据
            try {
                for (int row = 0; row < rows; row++) {
                    Row r = sheet.createRow(row + 1);
                    Room room = rooms.get(row);
                    r.createCell(0).setCellValue(room.getPlace());
                    r.createCell(1).setCellValue(room.getSize());
                    r.createCell(2).setCellValue(room.getAvailable());
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //写出Excel
            try (FileOutputStream fos = new FileOutputStream(new File(out))) {
                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
}
