package kz.bikecology.controllers.tabs.works;

import jakarta.persistence.NoResultException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Pair;
import kz.bikecology.controllers.BaseController;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.facilityFuels.FacilityFuels;
import kz.bikecology.data.models.fuel.Fuel;
import kz.bikecology.data.models.record.Record;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class WorksController extends BaseController {
    private List<Pair<Fuel, String>> fuelFields = new ArrayList<>();

    @FXML private Label searchResultText;

    @FXML private ChoiceBox<Facility> facilityChoiceBox;
    @FXML private ChoiceBox<Integer> quarterChoiceBox;
    @FXML private ChoiceBox<Integer> yearChoiceBox;

    @FXML private VBox fuelsBox;

    @FXML
    public void initialize() {
        loadFilters();
    }

    @FXML
    public void updateFuelsPane() {
        if (facilityChoiceBox.getValue() == null) { return; }

        // search result
        if (recordDAO.getByFacilityQuarterAndYear(
                facilityChoiceBox.getValue().getId(), quarterChoiceBox.getValue(), yearChoiceBox.getValue()).isEmpty()) {
            searchResultText.setText("Сохраненных данных для выбранного периода не найдено.");
        } else {
            searchResultText.setText("Обнаружены сохраненные данные для выбранного периода.");
        }

        fuelsBox.getChildren().setAll(); // clear previous output

        fuelFields.clear();
        for (FacilityFuels facilityFuels : facilityFuelsDAO.getByFacilityId(facilityChoiceBox.getValue().getId())) {
            fuelsBox.getChildren().addAll(getFuelInputBox(facilityFuels.getFuel(), facilityFuels.getFacility()));
        }
    }

    @FXML
    public void saveChanges() {
        Facility facility = facilityChoiceBox.getValue();

        boolean changed = false;
        for (Pair<Fuel, String> input : fuelFields) {
            Fuel fuel = input.getKey();
            TextField amountField = (TextField) fuelsBox.lookup("#" + input.getValue());
            amountField.getId();

            if (amountField.getText().isEmpty()) { continue; }

            recordDAO.merge(facility.getId(), fuel.getId(), quarterChoiceBox.getValue(), yearChoiceBox.getValue(), Float.parseFloat(amountField.getText()));
            changed = true;
        }

        //  update content if any changes were made, otherwise do not do anything
        if (changed) {
            updateFuelsPane();
            searchResultText.setText("Данные успешно сохранены.");
        }
    }

    public HBox getFuelInputBox(Fuel fuel, Facility facility) {
        // container
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 5));
        hBox.setAlignment(Pos.CENTER_LEFT);

        // fuel - display name
        Label fuelName = new Label(fuel.getDisplayName());
        fuelName.setFont(Font.font("Arial", 13));
        fuelName.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        fuelName.setPrefSize(300, 30);
        fuelName.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // fuel - amount field
        TextField amountField = new TextField();
        String amountFieldId = String.format("amountField%d", fuel.getId());
        amountField.setId(amountFieldId);
        amountField.setOnInputMethodTextChanged(inputMethodEvent -> {
            fuelName.setText(((TextField) inputMethodEvent.getSource()).getText());
        });

        amountField.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        amountField.setPrefSize(150, 30);
        amountField.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        amountField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*(\\.\\d*)?")) {
                amountField.setText(oldText);
            }
        });

        // fuel - search result
        Label savedAmount;
        try {
            Record savedRecord = recordDAO.get(facility.getId(), fuel.getId(), quarterChoiceBox.getValue(), yearChoiceBox.getValue());
            savedAmount = new Label(String.format("Сохраненное значение: %s.", savedRecord.getAmount().toString()));
            amountField.setText(savedRecord.getAmount().toString());
        } catch (NoResultException ignored) {
            savedAmount = new Label("Сохраненные данные отсутствуют.");
            savedAmount.setTextFill(Paint.valueOf("gray"));
        }
        savedAmount.setFont(Font.font("Arial", 13));
        savedAmount.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        savedAmount.setPrefSize(350, 30);
        savedAmount.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        savedAmount.setPadding(new Insets(0, 0, 0, 5));

        hBox.getChildren().addAll(fuelName, amountField, savedAmount);
        fuelFields.addLast(new Pair<>(fuel, amountFieldId));

        return hBox;
    }

    public void loadFilters() {
        for (Facility facility : facilityDAO.getAll()) {
            facilityChoiceBox.getItems().addAll(facility);
        }
        facilityChoiceBox.setOnHidden(_ -> { updateFuelsPane(); });

        quarterChoiceBox.getItems().addAll(1, 2, 3, 4);
        quarterChoiceBox.setValue(getCurrentQuarter());
        quarterChoiceBox.setOnHidden(event -> { updateFuelsPane(); });


        for (int i = 2020; i <= Year.now().getValue(); i++) {
            yearChoiceBox.getItems().addAll(i);
        }
        yearChoiceBox.setValue(getCurrentYear());
        yearChoiceBox.setOnHidden(event -> { updateFuelsPane(); });
    }

    public int getCurrentQuarter() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        return (month - 1) / 3 + 1;
    }

    public int getCurrentYear() {
        return Year.now().getValue();
    }
}
