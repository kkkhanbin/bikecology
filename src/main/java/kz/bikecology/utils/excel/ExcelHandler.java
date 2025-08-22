package kz.bikecology.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelHandler {
    public static void updateFormulas(Workbook workbook) {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateAll();
    }

    public static void copySheet(Sheet srcSheet, Sheet destSheet, Workbook srcWb, Workbook destWb) {
        for (int i = srcSheet.getFirstRowNum(); i <= srcSheet.getLastRowNum(); i++) {
            Row srcRow = srcSheet.getRow(i);
            if (srcRow == null) continue;

            Row destRow = destSheet.createRow(i);
            destRow.setHeight(srcRow.getHeight());

            for (int j = srcRow.getFirstCellNum(); j < srcRow.getLastCellNum(); j++) {
                Cell srcCell = srcRow.getCell(j);
                if (srcCell == null) continue;

                Cell destCell = destRow.createCell(j);
                copyCell(srcCell, destCell, srcWb, destWb);
            }
        }

        // Copy merged regions
        for (int i = 0; i < srcSheet.getNumMergedRegions(); i++) {
            destSheet.addMergedRegion(srcSheet.getMergedRegion(i));
        }

        // Copy column widths
        for (int i = 0; i <= srcSheet.getRow(0).getLastCellNum(); i++) {
            destSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
        }
    }

    private static void copyCell(Cell srcCell, Cell destCell, Workbook srcWb, Workbook destWb) {
        CellStyle newStyle = destWb.createCellStyle();
        newStyle.cloneStyleFrom(srcCell.getCellStyle());
        destCell.setCellStyle(newStyle);

        switch (srcCell.getCellType()) {
            case STRING -> destCell.setCellValue(srcCell.getStringCellValue());
            case NUMERIC -> destCell.setCellValue(srcCell.getNumericCellValue());
            case BOOLEAN -> destCell.setCellValue(srcCell.getBooleanCellValue());
            case FORMULA -> destCell.setCellFormula(srcCell.getCellFormula());
            case BLANK -> destCell.setBlank();
            default -> {}
        }
    }

    public static class STYLE {
        public static CellStyle addTopBorder(CellStyle style) {
            style.setBorderTop(BorderStyle.THIN);

            return style;
        }

        public static CellStyle addCenterAlignment(CellStyle style) {
            style.setWrapText(true);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            return style;
        }
    }
}
