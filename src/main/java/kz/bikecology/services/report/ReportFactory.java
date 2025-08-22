package kz.bikecology.services.report;

import kz.bikecology.services.report.reportBuilder.IEC.IECReportBuilder;
import kz.bikecology.services.report.reportBuilder.ReportBuilder;
import kz.bikecology.services.report.reportBuilder.tax.TaxReportBuilder;
import kz.bikecology.services.report.reportBuilder.statistical.StatisticalReportBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ReportFactory {
    private static final Map<ReportType, Supplier<ReportBuilder>> reportBuilders = new HashMap<>();

    static {
        reportBuilders.put(ReportType.STATISTICAL, StatisticalReportBuilder::new);
        reportBuilders.put(ReportType.IEC, IECReportBuilder::new);
        reportBuilders.put(ReportType.TAX, TaxReportBuilder::new);
    }

    public static ReportBuilder getBuilder(ReportType type) {
        return reportBuilders.get(type).get();
    }
}
