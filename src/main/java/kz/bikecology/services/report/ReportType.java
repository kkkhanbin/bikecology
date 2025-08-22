package kz.bikecology.services.report;

public enum ReportType {
    STATISTICAL("Статистический отчёт"),
    IEC("Отчет ПЭК"),
    TAX("Налоговый отчёт");

    private final String label;

    ReportType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }
}
