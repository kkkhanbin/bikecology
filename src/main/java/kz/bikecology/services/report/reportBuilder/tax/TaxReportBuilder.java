package kz.bikecology.services.report.reportBuilder.tax;

import jakarta.persistence.NoResultException;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.facilityFuels.FacilityFuels;
import kz.bikecology.data.models.limit.Limit;
import kz.bikecology.data.models.pollutant.Pollutant;
import kz.bikecology.data.models.record.Record;
import kz.bikecology.services.report.ReportContext;
import kz.bikecology.services.report.reportBuilder.ReportBuilder;
import kz.bikecology.utils.excel.ExcelHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxReportBuilder extends ReportBuilder {
    private final String TEMPLATES_DIR = "src/main/resources/reportTemplates/tax";
    private final Map<Integer, String> TEMPLATE_PATHS = new HashMap<>();

    public TaxReportBuilder() {
        super();

        for (Facility facility : facilityDAO.getAll()) {
            TEMPLATE_PATHS.put(facility.getId(), TEMPLATES_DIR + File.separator + facility.getAddress() + ".xlsx");
        }
    }

    @Override
    public void build(OutputStream out, ReportContext ctx) throws IOException {
        // main report file
        XSSFWorkbook reportWorkbook = new XSSFWorkbook();

        // for each selected facility
        for (Facility facility : ctx.getFacilities()) {

            // generate its worksheets from templates
            Workbook workbook = getFacilityWorkbook(facility, ctx.getQuarter(), ctx.getYear());

            // then add them to the main report file
            for (Sheet sheet : workbook) {
                Sheet newSheet = reportWorkbook.createSheet(facility.getAddress());
                ExcelHandler.copySheet(sheet, newSheet, workbook, reportWorkbook);
            }
        }

        reportWorkbook.write(out);
        out.close();
    }

    @Override
    protected HashMap<String, String> getSourceData(int facilityId, int quarter, int year) {
        HashMap<String, String> sourceData = new HashMap<>();

        List<Record> records = recordDAO.getByFacilityQuarterAndYear(facilityId, quarter, year);
        for (Record record : records) {
            sourceData.put(record.getFuel().getName(), record.getAmount().toString());
        }

        sourceData.put("quarter", String.valueOf(quarter));
        sourceData.put("year", String.valueOf(year));
        sourceData.put("MCI", "3932");

        return sourceData;
    }

    /**
     * Generating worksheets for a facility
     */
    public Workbook getFacilityWorkbook(Facility facility, int quarter, int year) throws IOException {
        if (!isEnoughRecords(facility.getId(), quarter, year)) {
            throw new NoSuchObjectException(String.format("""
Данных для составления отчета за %d квартал %d года не хватает""", quarter, year));
        }

        // getting template
        FileInputStream fis = new FileInputStream(TEMPLATE_PATHS.get(facility.getId()));
        Workbook workbook = WorkbookFactory.create(fis);

        // filling template with facility's data
        loadSourceData(workbook, getSourceData(facility.getId(), quarter, year));
        loadPollutantLimits(workbook, facility, quarter, year);

        ExcelHandler.updateFormulas(workbook);

        return workbook;
    }

    protected void loadPollutantLimits(Workbook workbook, Facility facility, int quarter, int year) throws IOException {
        List<Workbook> quarterWorkbooks = new ArrayList<>(List.of());
        for (int i = 1; i < quarter; i++) {
            quarterWorkbooks.add(getFacilityWorkbook(facility, i, year));
        }

        int sheetIndex = 0;
        for (Sheet sheet : workbook) {

            boolean readingStarted = false;

            for (Row row : sheet) {

                // processing each row
                Cell codeCell = row.getCell(2);
                if (codeCell != null && codeCell.getCellType().equals(CellType.STRING)) {
                    Pollutant pollutant;
                    try {
                        pollutant = pollutantDAO.getByCode(codeCell.getStringCellValue());
                    } catch (NoResultException _) { if (readingStarted) { break; } else { continue; } }

                    readingStarted = true;

                    Limit limit;
                    try {
                        limit = limitDAO.getByPollutantIdAndFacilityId(pollutant.getId(), facility.getId());
                    } catch (NoResultException _) { continue; }

                    row.getCell(5).setCellValue(limit.getValue());

                    // calculating actual release of previous quarters
                    Float remains = limit.getValue();
                    for (Workbook quarterWorkbook : quarterWorkbooks) {
                        remains -= (float) quarterWorkbook.getSheetAt(sheetIndex).getRow(row.getRowNum()).getCell(7).getNumericCellValue();
                    }

                    row.getCell(6).setCellValue(remains);
                }
            }

            sheetIndex++;
        }
    }

    protected boolean isEnoughRecords(int facilityId, int quarter, int year) {
        for (int i = 1; i <= quarter; i++) {
            List<Record> records = recordDAO.getByFacilityQuarterAndYear(facilityId, quarter, year);
            List<FacilityFuels> fuels = facilityFuelsDAO.getByFacilityId(facilityId);

            if (records.size() != fuels.size()) { return false; }
        }
        return true;
    }
}
