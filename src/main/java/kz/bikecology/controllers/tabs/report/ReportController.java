package kz.bikecology.controllers.tabs.report;

import kz.bikecology.controllers.BaseController;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.record.Record;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportController extends BaseController {
    // filters
    @FXML ChoiceBox<Integer> quarterFilter;
    @FXML ChoiceBox<Integer> yearFilter;

    @FXML Label statusBar;

    // available reports
    @FXML private TableView<ReportReadyFacility> availableReportsTable;

    @FXML private TableColumn<ReportReadyFacility, Boolean> selectCol;
    @FXML private TableColumn<ReportReadyFacility, Facility> facilityCol;

    @FXML
    public void initialize() {
        loadFilters();
        loadAvailableReportsColumns();
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

        updateReportsPane();
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
        selectAllCheckBox.setSelected(true);
        selectCol.setGraphic(selectAllCheckBox);
        selectCol.setEditable(true);
        selectCol.setCellValueFactory(cellData -> {
            ReportReadyFacility facility = cellData.getValue();

            BooleanProperty selected = new SimpleBooleanProperty(facility.isSelected());

            selected.addListener((_, _, isNowSelected) -> {
                statusBar.setText("Производственные объекты, имеющие достаточно информации по проводимым работам для отчёта");
                facility.setSelected(isNowSelected);
            });

            return selected;
        });
        selectCol.setCellFactory(_ -> new CheckBoxTableCell<>());

        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));

        availableReportsTable.setPlaceholder(new Label("Доступных отчетов не найдено."));
        availableReportsTable.setEditable(true);
    }

    public void updateReportsPane() {
        availableReportsTable.setItems(FXCollections.observableArrayList()); // clearing table
        if (quarterFilter.getValue() == null || yearFilter.getValue() == null) { return; }

        int quarter = quarterFilter.getValue();
        int year = yearFilter.getValue();

        availableReportsTable.setItems(FXCollections.observableArrayList(getReportReadyFacilities(quarter, year)));
    }

    @FXML
    public void showChoosingReportTypeWindow(ActionEvent event) throws IOException {
        List<Facility> selectedFacilities = availableReportsTable.getItems().stream().filter(ReportReadyFacility::isSelected).map(ReportReadyFacility::getFacility).toList();

        if (selectedFacilities.isEmpty()) {
            statusBar.setText("Выберите минимум 1 производственный объект");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tabs/report/chooseReportTypeWindow.fxml"));
        Parent root = loader.load();

        ChooseReportTypeWindowController controller = loader.getController();
        controller.setFacilities(selectedFacilities);
        controller.setQuarter(quarterFilter.getValue());
        controller.setYear(yearFilter.getValue());
        controller.loadReportDataText();

        Stage stage = new Stage();
        stage.setTitle("Выбор типа отчёта");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        stage.setOnHidden(_ -> {  });
    }

    public List<ReportReadyFacility> getReportReadyFacilities(int quarter, int year) {
        List<Record> allRecords = recordDAO.getByQuarterAndYear(quarter, year);

        // stage 1 - getting rid of reports without enough allRecords
        HashMap<Integer, List<Integer>> matchingFuels = new HashMap<>();
        for (Record record : allRecords) {
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
        List<ReportReadyFacility> reportReadyFacilities = new ArrayList<>(List.of());
        for (Integer facilityId : matchingFuels.keySet()) {
            if (facilityFuelsDAO.getByFacilityId(facilityId).size() == matchingFuels.get(facilityId).size()) {
                ReportReadyFacility reportReadyFacility = new ReportReadyFacility(true, facilityDAO.getById(facilityId));
                reportReadyFacilities.add(reportReadyFacility);
            }
        }

        return reportReadyFacilities;
    }
}
