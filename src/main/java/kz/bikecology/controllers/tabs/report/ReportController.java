package kz.bikecology.controllers.tabs.report;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import kz.bikecology.controllers.BaseController;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.record.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

public class ReportController extends BaseController {
    private static final Logger log = LogManager.getLogger(ReportController.class);
    Preferences prefs = Preferences.userNodeForPackage(getClass());
    String defaultDownloadDir = System.getProperty("user.home") + File.separator + "Downloads";

    @FXML private Button downloadReportBtn;

    // filters
    @FXML private HBox placeForFacilityFilter;
    @FXML private HBox placeForQuarterFilter;
    @FXML private HBox placeForYearFilter;

    CheckComboBox<Facility> facilityFilter = new CheckComboBox<>();
    CheckComboBox<Integer> quarterFilter = new CheckComboBox<>();
    CheckComboBox<Integer> yearFilter = new CheckComboBox<>();

    // available reports
    @FXML private TableView<Record> availableReportsTable;

    @FXML private TableColumn<Record, Facility> facilityCol;
    @FXML private TableColumn<Record, Integer> quarterCol;
    @FXML private TableColumn<Record, Integer> yearCol;

    @FXML
    public void initialize() {
        loadFilters();

        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));
        quarterCol.setCellValueFactory(new PropertyValueFactory<>("quarter"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    public void loadFilters() {
        Runnable onSelectionChanged = this::updateReportsPane;

        for (Facility facility : facilityDAO.getAll()) {
            facilityFilter.getItems().addAll(facility);
        }
        facilityFilter.setPrefWidth(10000);
        facilityFilter.getStyleClass().addAll("combo-box");
        facilityFilter.getCheckModel().getCheckedItems().addListener((ListChangeListener<Facility>) _ -> {
            onSelectionChanged.run();
        });
        placeForFacilityFilter.getChildren().setAll(facilityFilter);

        quarterFilter.getItems().addAll(1, 2, 3, 4);
        quarterFilter.setPrefWidth(10000);
        quarterFilter.getStyleClass().addAll("combo-box");
        quarterFilter.getCheckModel().getCheckedItems().addListener((ListChangeListener<Integer>) _ -> {
            onSelectionChanged.run();
        });
        placeForQuarterFilter.getChildren().setAll(quarterFilter);

        for (int i = 2020; i <= Year.now().getValue(); i++) {
            yearFilter.getItems().addAll(i);
        }
        yearFilter.setPrefWidth(10000);
        yearFilter.getStyleClass().addAll("combo-box");
        yearFilter.getCheckModel().getCheckedItems().addListener((ListChangeListener<Integer>) _ -> {
            onSelectionChanged.run();
        });
        placeForYearFilter.getChildren().setAll(yearFilter);
    }

    public void updateReportsPane() {
        List<Integer> facilityIds = facilityFilter.getCheckModel().getCheckedItems().stream().map(Facility::getId).toList();
        List<Integer> quarters = new ArrayList<>(quarterFilter.getCheckModel().getCheckedItems());
        List<Integer> years = new ArrayList<>(yearFilter.getCheckModel().getCheckedItems());

        availableReportsTable.setItems(FXCollections.observableArrayList()); // clear table
        if (facilityIds.isEmpty() || quarters.isEmpty() || years.isEmpty()) {
            return;
        }

        List<Record> allRecords = recordDAO.getByFacilityQuarterAndYear(facilityIds, quarters, years);

        // stage 1 - getting rid of reports without enough allRecords
        HashMap<List<Integer>, List<Integer>> matchingFuels = new HashMap<>();
        for (Record record : allRecords) {
            List<Integer> key = List.of(record.getFacility().getId(), record.getQuarter(), record.getYear());
            if (!matchingFuels.containsKey(key)) {
                matchingFuels.put(key, List.of(record.getFuel().getId()));
            } else {
                List<Integer> addedFuel = new ArrayList<>(List.of(record.getFuel().getId()));
                addedFuel.addAll(matchingFuels.get(key));
                matchingFuels.put(key, addedFuel);
            }
        }

        // stage 2 - structuring data
        List<Record> filteredRecords = new ArrayList<>(List.of());
        for (List<Integer> key : matchingFuels.keySet()) {
            if (facilityFuelsDAO.getByFacilityId(key.getFirst()).size() == matchingFuels.get(key).size()) {
                filteredRecords.add(recordDAO.getByFacilityQuarterAndYear(key.getFirst(), key.get(1), key.getLast()).getFirst());
            }
        }

        availableReportsTable.setItems(FXCollections.observableArrayList(filteredRecords));
        availableReportsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

        File file = fileChooser.showSaveDialog(downloadReportBtn.getScene().getWindow());

        prefs.put("lastDir", file.getParentFile().getAbsolutePath());

        reportBuilder.build(file.getAbsolutePath(), 2, 2025); // TODO delete mock
    }
}
