package kz.bikecology.services.reportBuilder;

public enum ReportType {
    STATISTICAL("Статистический отчёт"),
    IEC("Отчет ПЭК"),
    EXPLANATORY_NOTES("Пояснительная записка");

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
