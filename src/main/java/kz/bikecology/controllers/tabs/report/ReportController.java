package kz.bikecology.controllers.tabs.report;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import kz.bikecology.controllers.BaseController;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.record.Record;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import kz.bikecology.services.report.ReportContext;
import kz.bikecology.services.report.ReportFactory;
import kz.bikecology.services.report.ReportType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

public class ReportController extends BaseController {
    private static final Logger log = LogManager.getLogger(ReportController.class);
    Preferences prefs = Preferences.userNodeForPackage(getClass());
    String defaultDownloadDir = System.getProperty("user.home") + File.separator + "Downloads";

    // filters
    @FXML ChoiceBox<Integer> quarterFilter;
    @FXML ChoiceBox<Integer> yearFilter;
    @FXML ChoiceBox<ReportType> reportTypeFilter;

    // labels
    @FXML Label statusBar;
    @FXML Label chosenFacilitiesAmountLabel;

    // available reports
    @FXML private TableView<ReportReadyFacility> availableReportsTable;

    @FXML private TableColumn<ReportReadyFacility, Boolean> selectCol;
    @FXML private TableColumn<ReportReadyFacility, Facility> facilityCol;

    @FXML
    public void initialize() {
        loadFilters();
        loadAvailableReportsColumns();

        updateReportsPane();
    }

    public void loadFilters() {
        quarterFilter.setOnHidden(_ -> { updateReportsPane(); });
        quarterFilter.getItems().addAll(1, 2, 3, 4);
        quarterFilter.setValue(getCurrentQuarter());

        yearFilter.setOnHidden(_ -> { updateReportsPane(); });
        for (int i = 2020; i <= Year.now().getValue(); i++) {
            yearFilter.getItems().addAll(i);
        }
        yearFilter.setValue(getCurrentYear());

        reportTypeFilter.setOnHidden(_ -> { updateReportsPane(); });
        reportTypeFilter.getItems().addAll(ReportType.values());
        reportTypeFilter.setValue(ReportType.STATISTICAL);
    }

    public void loadAvailableReportsColumns() {
        // Boolean checkbox configuration
        CheckBox selectAllCheckBox = new CheckBox();
        selectAllCheckBox.setOnAction(_ -> {
            for (ReportReadyFacility facility : availableReportsTable.getItems()) {
                facility.setSelected(selectAllCheckBox.isSelected());
            }

            availableReportsTable.refresh();
        });

        selectCol.setGraphic(selectAllCheckBox);
        selectCol.setEditable(true);
        selectCol.setCellValueFactory(cellData -> {
            ReportReadyFacility facility = cellData.getValue();

            facility.selectedProperty().addListener((_, _, isNowSelected) -> {
                facility.setSelected(isNowSelected);
                updateLabels();
            });

            if (getSelectedFacilities().size() == availableReportsTable.getItems().size()) { selectAllCheckBox.setSelected(true); }
            if (getSelectedFacilities().isEmpty()) { selectAllCheckBox.setSelected(false); };

            return facility.selectedProperty();
        });
        selectCol.setCellFactory(_ -> new CheckBoxTableCell<>());

        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));

        availableReportsTable.setPlaceholder(new Label("Доступных отчетов не найдено."));
        availableReportsTable.setEditable(true);

        selectAllCheckBox.setSelected(true);
    }

    @FXML
    public void updateReportsPane() {
        availableReportsTable.setItems(FXCollections.observableArrayList()); // clearing table
        if (quarterFilter.getValue() == null || yearFilter.getValue() == null) { return; }

        CheckBox selectAllCheckBox = (CheckBox) selectCol.getGraphic();
        selectAllCheckBox.setSelected(true);

        ReportType reportType = reportTypeFilter.getValue();
        int quarter = quarterFilter.getValue();
        int year = yearFilter.getValue();

        ObservableList<ReportReadyFacility> reports;

        quarterFilter.setDisable(reportType.equals(ReportType.STATISTICAL));
        if (reportType.equals(ReportType.IEC) || reportType.equals(ReportType.TAX)) {
            reports = FXCollections.observableArrayList(getQuarterReadyFacilities(quarter, year));
        } else {
            reports = FXCollections.observableArrayList(getYearReadyFacilities(year));
        }

        availableReportsTable.setItems(reports);
        updateLabels();
    }

    public void updateLabels() {
        statusBar.setText("Производственные объекты, имеющие достаточно информации по проводимым работам для отчёта");
        chosenFacilitiesAmountLabel.setText(String.format("Выбрано производственных объектов: %d.", getSelectedFacilities().size()));
    }

    @FXML
    public void downloadReport() throws IOException {
        String filename = String.format("%s за %d квартал %d года.xlsx", reportTypeFilter.getValue(), quarterFilter.getValue(), yearFilter.getValue());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчёт в...");
        fileChooser.setInitialFileName(filename);
        fileChooser.setInitialDirectory(new File(prefs.get("lastDir", defaultDownloadDir)));

        File file = fileChooser.showSaveDialog(reportTypeFilter.getScene().getWindow());

        prefs.put("lastDir", file.getParentFile().getAbsolutePath());

        // Building a report
        ReportContext ctx = new ReportContext(getSelectedFacilities(), quarterFilter.getValue(), yearFilter.getValue());
        ReportFactory.getBuilder(reportTypeFilter.getValue()).build(new FileOutputStream(file.getAbsolutePath()), ctx);
    }

    public List<Facility> getSelectedFacilities() {
        return availableReportsTable.getItems().stream().filter(ReportReadyFacility::isSelected).map(ReportReadyFacility::getFacility).toList();
    }

    /**
     * TEMP: returns all facilities that have all records for facility's fuels for each of the previous and including chosen quarter
     */
    public List<ReportReadyFacility> getQuarterReadyFacilities(int quarter, int year) {

        List<List<Integer>> quartersFacilities = new ArrayList<>(new ArrayList<>(List.of()));
        for (int i = 1; i <= quarter; i++) {
            List<Record> records = recordDAO.getByQuarterAndYear(i, year);

            // stage 1 - getting rid of reports without enough records
            HashMap<Integer, List<Integer>> matchingFuels = new HashMap<>();
            for (Record record : records) {
                Integer facilityId = record.getFacility().getId();
                if (!matchingFuels.containsKey(facilityId)) {
                    matchingFuels.put(facilityId, List.of(record.getFuel().getId()));
                } else {
                    List<Integer> addedFuel = new ArrayList<>(List.of(record.getFuel().getId()));
                    addedFuel.addAll(matchingFuels.get(facilityId));
                    matchingFuels.put(facilityId, addedFuel);
                }
            }

            // stage 2 - structuring data
            List<Integer> quarterFacility = new ArrayList<>(List.of());
            for (Integer facilityId : matchingFuels.keySet()) {
                if (facilityFuelsDAO.getByFacilityId(facilityId).size() == matchingFuels.get(facilityId).size()) {
                    ReportReadyFacility facility = new ReportReadyFacility(facilityDAO.getById(facilityId));
                    quarterFacility.add(facility.getFacility().getId());
                }
            }

            quartersFacilities.add(quarterFacility);
        }

        List<ReportReadyFacility> reportReadyFacilities = new ArrayList<>(List.of());
        boolean passed;

        for (Facility facility : facilityDAO.getAll()) {

            passed = true;
            for (List<Integer> filteredFacilities : quartersFacilities) {
                if (!filteredFacilities.contains(facility.getId())) { passed = false; break; }
            }

            if (passed) { reportReadyFacilities.add(new ReportReadyFacility(facility)); }
        }

        return reportReadyFacilities;
    }

    /**
     * TEMP: returns all facilities that have all records for facility's fuels for ALL quarters
     */
    public List<ReportReadyFacility> getYearReadyFacilities(int year) {
        HashMap<Integer, List<Boolean>> quarterStatus = new HashMap<>();

        for (int quarter = 1; quarter <= 4; quarter++) {
            List<Record> records = recordDAO.getByQuarterAndYear(quarter, year);

            HashMap<Integer, List<Integer>> matchingFuels = new HashMap<>();
            for (Facility facility : facilityDAO.getAll()) {
                matchingFuels.put(facility.getId(), List.of());
            }

            for (Record record : records) {
                Integer facilityId = record.getFacility().getId();

                List<Integer> addedFuel = new ArrayList<>(List.of(record.getFuel().getId()));
                addedFuel.addAll(matchingFuels.get(facilityId));
                matchingFuels.put(facilityId, addedFuel);
            }

            for (Integer facilityId : matchingFuels.keySet()) {
                boolean result = facilityFuelsDAO.getByFacilityId(facilityId).size() == matchingFuels.get(facilityId).size();

                if (quarterStatus.containsKey(facilityId)) {
                    List<Boolean> addedStatus = new ArrayList<>(List.of(result));
                    addedStatus.addAll(quarterStatus.get(facilityId));
                    quarterStatus.put(facilityId, addedStatus);
                } else {
                    quarterStatus.put(facilityId, List.of(result));
                }
            }
        }

        List<ReportReadyFacility> filtered = new ArrayList<>(List.of());
        for (int facilityId : quarterStatus.keySet()) {
            if (quarterStatus.get(facilityId).stream().allMatch(Predicate.isEqual(true))) {
                filtered.add(new ReportReadyFacility(facilityDAO.getById(facilityId)));
            }
        }

        return filtered;
    }
}
