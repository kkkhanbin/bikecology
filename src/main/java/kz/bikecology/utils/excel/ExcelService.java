package kz.bikecology.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelService {
    public static void write() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MySheet");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello Excel!");

        FileOutputStream out = new FileOutputStream("output.xlsx");
        workbook.write(out);
        out.close();
        workbook.close();
    }

    public static void read() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Khanbin\\job\\БиК Эколоджи\\source\\расчеты Кокпекты.xlsx");
        Workbook workbook = WorkbookFactory.create(fis);



        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                    case NUMERIC -> System.out.print(cell.getNumericCellValue() + "\t");
                    case BOOLEAN -> System.out.print(cell.getBooleanCellValue() + "\t");
                    default -> System.out.print("?\t");
                }
            }
            System.out.println();
        }

        workbook.close();
        fis.close();
    }

    public static void updateFormulas(Workbook workbook) {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateAll();
    }
}
