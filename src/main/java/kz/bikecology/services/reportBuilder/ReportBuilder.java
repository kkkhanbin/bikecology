package kz.bikecology.services.reportBuilder;

import kz.bikecology.data.dao.fuel.FuelDAO;
import kz.bikecology.data.dao.record.RecordDAO;
import kz.bikecology.data.models.fuel.Fuel;
import kz.bikecology.data.models.record.Record;
import kz.bikecology.utils.excel.ExcelService;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.List;

public class ReportBuilder {
    private final String REPORT_TEMPLATE_PATH = "src/main/resources/reportTemplates/template.xlsx";

    RecordDAO recordDAO = new RecordDAO();
    FuelDAO fuelDAO = new FuelDAO();

    public void build(String path, int quarter, int year) throws IOException {
        FileInputStream fis = new FileInputStream(REPORT_TEMPLATE_PATH);
        Workbook workbook = WorkbookFactory.create(fis);

        HashMap<String, Float> sourceData = new HashMap<>();
        List<Record> records = recordDAO.getByQuarterAndYear(quarter, year);
        List<Fuel> fuels = fuelDAO.getAll();

        if (!(records.size() == fuels.size())) {
            throw new NoSuchObjectException(String.format("""
Данных для составления отчета за %d квартал %d года не хватает""", quarter, year));
        }

        for (Record record : recordDAO.getByQuarterAndYear(quarter, year)) {
            sourceData.put(record.getFuel().getName(), record.getAmount());
        }

        loadSourceData(workbook, sourceData);

        ExcelService.updateFormulas(workbook);

        // Saving report file into downloads
        String home = System.getProperty("user.home");
        String downloadsPath = home + File.separator + "Downloads";
        String filename = String.format("Отчёт Кокпекты за %d квартал %d года.xlsx", quarter, year);
        FileOutputStream fos = new FileOutputStream(path);

        workbook.write(fos);
    }

    public void loadSourceData(Workbook workbook, HashMap<String, Float> sourceData) {
        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (
                            cell.getCellType().equals(CellType.STRING) &&
                                    cell.getStringCellValue().startsWith("{ ") &&
                                    cell.getStringCellValue().endsWith(" }")
                    ) {
                        String key = cell.getStringCellValue().substring(2, cell.getStringCellValue().length() - 2);

                        if (!sourceData.containsKey(key)) {
                            throw new IllegalArgumentException(String.format("""
Переменная "%s" в ячейке "%s" на странице "%s" не была найдена.""", key, cell.getAddress(), sheet.getSheetName()));
                        }

                        cell.setCellValue(sourceData.get(key));
                    }
                }
            }
        }
    }
}
