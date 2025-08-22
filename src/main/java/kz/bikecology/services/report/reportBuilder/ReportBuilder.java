package kz.bikecology.services.report.reportBuilder;

import kz.bikecology.data.dao.facility.FacilityDAO;
import kz.bikecology.data.dao.facilityFuels.FacilityFuelsDAO;
import kz.bikecology.data.dao.limit.LimitDAO;
import kz.bikecology.data.dao.pollutant.PollutantDAO;
import kz.bikecology.data.dao.record.RecordDAO;
import kz.bikecology.data.dao.source.SourceDAO;
import kz.bikecology.services.report.ReportContext;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public abstract class ReportBuilder {
    protected final FacilityDAO facilityDAO;
    protected final RecordDAO recordDAO;
    protected final FacilityFuelsDAO facilityFuelsDAO;
    protected final PollutantDAO pollutantDAO;
    protected final LimitDAO limitDAO;
    protected final SourceDAO sourceDAO;

    public ReportBuilder() {
        this.facilityDAO = new FacilityDAO();
        this.recordDAO = new RecordDAO();
        this.facilityFuelsDAO = new FacilityFuelsDAO();
        this.pollutantDAO = new PollutantDAO();
        this.limitDAO = new LimitDAO();
        this.sourceDAO = new SourceDAO();
    }

    public abstract void build(OutputStream out, ReportContext ctx) throws IOException;

    public void loadSourceData(Workbook workbook, HashMap<String, String> sourceData) {
        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (
                            cell.getCellType().equals(CellType.STRING) &&
                                    cell.getStringCellValue().startsWith("{") &&
                                    cell.getStringCellValue().endsWith("}")
                    ) {
                        String key = cell.getStringCellValue().substring(1, cell.getStringCellValue().length() - 1);

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

    protected abstract HashMap<String, String> getSourceData(int facilityId, int quarter, int year);
}
