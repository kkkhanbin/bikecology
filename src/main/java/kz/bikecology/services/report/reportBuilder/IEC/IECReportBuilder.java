package kz.bikecology.services.report.reportBuilder.IEC;

import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.source.Source;
import kz.bikecology.services.report.ReportContext;
import kz.bikecology.services.report.reportBuilder.ReportBuilder;
import kz.bikecology.services.report.reportBuilder.tax.TaxReportBuilder;
import kz.bikecology.utils.excel.ExcelHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class IECReportBuilder extends ReportBuilder {

    private final String REPORT_TEMPLATE_PATH = "src/main/resources/reportTemplates/IEC/mainTemplate.xlsx";

    @Override
    public void build(OutputStream out, ReportContext ctx) throws IOException {
        // main report file
        FileInputStream fis = new FileInputStream(REPORT_TEMPLATE_PATH);
        Workbook workbook = WorkbookFactory.create(fis);

        // loading data
        loadTitlePage(workbook, ctx);
        loadGeneralFacilityDataSheet(workbook, ctx);
        loadAccreditedTestingLaboratory(workbook, ctx);
        loadSources(workbook, ctx);
        loadActualReleases(workbook, ctx);
        loadReleaseCalculations(workbook, ctx);
        loadAirMonitoring(workbook, ctx);

        workbook.write(out);
        out.close();
    }

    /**
     * Title
     */
    public void loadTitlePage(Workbook wb, ReportContext ctx) {
        Sheet sheet = wb.getSheetAt(0);

        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellValue(cell.getStringCellValue().replace("{year}", String.valueOf(ctx.getYear())).replace("{quarter}", String.valueOf(ctx.getQuarter())));
            }
        }
    }

    /**
     * 1.1.
     */
    public void loadGeneralFacilityDataSheet(Workbook wb, ReportContext ctx) {
        Sheet sheet = wb.getSheetAt(1);

        int rowStartIndex = 4;
        for (int i = 0; i < ctx.getFacilities().size(); i++) {
            Facility f = ctx.getFacilities().get(i);
            Row row = sheet.createRow(rowStartIndex + i);

            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(f.getName());
            row.createCell(2).setCellValue(f.getCATO());
            row.createCell(3).setCellValue(f.getAddress());
            row.createCell(4).setCellValue(f.getBIN());
            row.createCell(5).setCellValue(f.getCCEA());
            row.createCell(6).setCellValue(f.getProductionProcessSpecification());
            row.createCell(8).setCellValue(f.getObjectCategory());
        }
    }

    /**
     * 2.1.
     */
    public void loadAccreditedTestingLaboratory(Workbook wb, ReportContext ctx) {
        Sheet sheet = wb.getSheetAt(5);

        if (ctx.getQuarter() != 4) {
            sheet.createRow(4);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 3));

            sheet.getRow(4).createCell(0).setCellValue("Инструментальные замеры предусмотрены в 4 квартале 2025 года");
            return;
        }

        // TODO load
    }

    /**
     * 2.2.
     */
    public void loadSources(Workbook wb, ReportContext ctx) {
        Sheet sheet = wb.getSheetAt(6);

        int rowStartIndex = 5;
        for (int i = 0; i < ctx.getFacilities().size(); i++) {
            Facility f = ctx.getFacilities().get(i);
            Row row = sheet.createRow(rowStartIndex + i);

            List<Source> sources = sourceDAO.getByFacilityId(f.getId());

            row.createCell(0).setCellValue(f.getAddress());
            row.createCell(1).setCellValue(sources.size());
            row.createCell(2).setCellValue(Source.getNumberOfOrganized(sources));
            row.createCell(3).setCellValue(Source.getNumberOfNonOrganized(sources));

            // TODO equipped cleaning facilities
        }
    }

    /**
     * 2.3.
     */
    public void loadActualReleases(Workbook wb, ReportContext ctx) throws IOException {
        Sheet sheet = wb.getSheetAt(7);

        int startFacilityIndex;
        int rowIndex = 4;
        for (Facility f : ctx.getFacilities()) {
            Sheet taxReport = new TaxReportBuilder().getFacilityWorkbook(f, ctx.getQuarter(), ctx.getYear()).getSheetAt(0);

            // column 1
            sheet.createRow(rowIndex).createCell(0).setCellValue(f.getAddress());

            startFacilityIndex = rowIndex;
            int i = 2;
            while (taxReport.getRow(i).getCell(0) != null) {
                if (taxReport.getRow(i).getCell(0).getStringCellValue().isEmpty()) { break; }

                Row rowFrom = taxReport.getRow(i);
                Row rowTo;
                if (sheet.getRow(rowIndex) != null) { rowTo = sheet.getRow(rowIndex); } else { rowTo = sheet.createRow(rowIndex); }

                rowTo.createCell(2).setCellValue(rowFrom.getCell(0).getStringCellValue());
                rowTo.createCell(3).setCellValue(rowFrom.getCell(1).getStringCellValue());
                rowTo.createCell(4).setCellValue(rowFrom.getCell(3).getStringCellValue());
                rowTo.createCell(5).setCellValue(rowFrom.getCell(4).getNumericCellValue());
                rowTo.createCell(6).setCellValue(rowFrom.getCell(5).getNumericCellValue());
                rowTo.createCell(8).setCellValue(rowFrom.getCell(7).getNumericCellValue());

                i++;
                rowIndex++;
            }

            // adding a top border for a facility
            for (int j = 0; j < 13; j++) {
                Cell cell = sheet.getRow(startFacilityIndex).getCell(j);
                if (cell != null) {
                    cell.setCellStyle(ExcelHandler.STYLE.addTopBorder(wb.createCellStyle()));
                } else {
                    sheet.getRow(startFacilityIndex).createCell(j).setCellStyle(ExcelHandler.STYLE.addTopBorder(wb.createCellStyle()));
                }
            }

            // merging facility's name cells
            sheet.addMergedRegion(new CellRangeAddress(startFacilityIndex, rowIndex - 1, 0, 0));
            Cell facilityNameCell = sheet.getRow(startFacilityIndex).getCell(0);
            facilityNameCell.setCellStyle(ExcelHandler.STYLE.addCenterAlignment(facilityNameCell.getCellStyle()));
        }
    }

    /**
     * 2.4.
     */
    public void loadReleaseCalculations(Workbook wb, ReportContext ctx) throws IOException {
        Sheet sheet = wb.getSheetAt(8);

        int startFacilityIndex;
        int rowIndex = 4;
        for (Facility f : ctx.getFacilities()) {
            Sheet taxReport = new TaxReportBuilder().getFacilityWorkbook(f, ctx.getQuarter(), ctx.getYear()).getSheetAt(0);

            // column 1
            sheet.createRow(rowIndex).createCell(0).setCellValue(f.getAddress());

            startFacilityIndex = rowIndex;
            int i = 2;
            while (taxReport.getRow(i).getCell(0) != null) {
                if (taxReport.getRow(i).getCell(0).getStringCellValue().isEmpty()) { break; }
                                
                Row rowFrom = taxReport.getRow(i);
                Row rowTo;
                if (sheet.getRow(rowIndex) != null) { rowTo = sheet.getRow(rowIndex); } else { rowTo = sheet.createRow(rowIndex); }

                rowTo.createCell(2).setCellValue(rowFrom.getCell(1).getStringCellValue());
                rowTo.createCell(3).setCellValue(rowFrom.getCell(0).getStringCellValue());
                rowTo.createCell(4).setCellValue(rowFrom.getCell(3).getStringCellValue());
                rowTo.createCell(5).setCellValue(rowFrom.getCell(4).getNumericCellValue());
                rowTo.createCell(6).setCellValue(rowFrom.getCell(5).getNumericCellValue());
                rowTo.createCell(7).setCellValue(rowFrom.getCell(6).getNumericCellValue());
                rowTo.createCell(8).setCellValue(rowFrom.getCell(7).getNumericCellValue());

                float remains = (float) rowFrom.getCell(11).getNumericCellValue();
                if (remains < 0) { rowTo.createCell(13).setCellValue(-remains); } else { rowTo.createCell(13).setCellValue("-"); }

                i++;
                rowIndex++;
            }

            // adding a top border for a facility
            for (int j = 0; j < 14; j++) {
                Cell cell = sheet.getRow(startFacilityIndex).getCell(j);
                if (cell != null) {
                    cell.setCellStyle(ExcelHandler.STYLE.addTopBorder(wb.createCellStyle()));
                } else {
                    sheet.getRow(startFacilityIndex).createCell(j).setCellStyle(ExcelHandler.STYLE.addTopBorder(wb.createCellStyle()));
                }
            }

            // merging facility's name cells
            sheet.addMergedRegion(new CellRangeAddress(startFacilityIndex, rowIndex - 1, 0, 0));
            Cell facilityNameCell = sheet.getRow(startFacilityIndex).getCell(0);
            facilityNameCell.setCellStyle(ExcelHandler.STYLE.addCenterAlignment(facilityNameCell.getCellStyle()));
        }
    }

    /**
     * 2.6.
     */
    public void loadAirMonitoring(Workbook wb, ReportContext ctx) {
        Sheet sheet = wb.getSheetAt(9);

        if (ctx.getQuarter() != 4) {
            sheet.createRow(4);
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 3));

            sheet.getRow(4).createCell(0).setCellValue("Инструментальные замеры предусмотрены в 4 квартале 2025 года");
            return;
        }

        // TODO load
    }

    @Override
    protected HashMap<String, String> getSourceData(int facilityId, int quarter, int year) {
        return null;
    }
}
