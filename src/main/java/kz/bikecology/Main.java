package kz.bikecology;

import kz.bikecology.data.dao.feeRate.FeeRateDAO;
import kz.bikecology.data.dao.record.RecordDAO;
import kz.bikecology.data.dao.source.SourceDAO;
import kz.bikecology.services.reportBuilder.ReportBuilder;
import kz.bikecology.utils.excel.ExcelService;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        ReportBuilder reportBuilder = new ReportBuilder();
//        reportBuilder.build(2, 2025);
    }
}
