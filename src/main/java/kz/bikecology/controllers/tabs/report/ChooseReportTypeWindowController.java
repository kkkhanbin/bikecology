package kz.bikecology.controllers.tabs.report;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kz.bikecology.controllers.BaseController;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.services.reportBuilder.ReportType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class ChooseReportTypeWindowController extends BaseController {
    private static final Logger log = LogManager.getLogger(ReportController.class);
    Preferences prefs = Preferences.userNodeForPackage(getClass());
    String defaultDownloadDir = System.getProperty("user.home") + File.separator + "Downloads";

    private List<Facility> facilities;
    private int quarter;
    private int year;

    @FXML ChoiceBox<String> reportTypeChoiceBox;

    @FXML Label quarterText;
    @FXML Label yearText;
    @FXML Label facilityText;

    @FXML
    public void initialize() {
        reportTypeChoiceBox.getItems().addAll(Arrays.stream(ReportType.values()).map(ReportType::toString).toList());
        reportTypeChoiceBox.setValue(ReportType.STATISTICAL.toString());
    }

    public void loadReportDataText() {
        quarterText.setText(String.format("Квартал: %d", quarter));
        yearText.setText(String.format("Год: %d", year));
        facilityText.setText(String.format("Выбрано производственных объектов: %d", facilities.size()));
    }

    @FXML
    public void downloadReport(ActionEvent event) throws IOException {
        int quarter = 2;
        int year = 2025;

        String filename = String.format("Отчёт Кокпекты за %d квартал %d года.xlsx", quarter, year);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчёт в...");
        fileChooser.setInitialFileName(filename);
        fileChooser.setInitialDirectory(new File(prefs.get("lastDir", defaultDownloadDir)));

        File file = fileChooser.showSaveDialog(reportTypeChoiceBox.getScene().getWindow());

        try {
            prefs.put("lastDir", file.getParentFile().getAbsolutePath());
        } catch (NullPointerException ignored) {}

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

//        reportBuilder.build(file.getAbsolutePath(), 2, 2025); // TODO delete mock
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
