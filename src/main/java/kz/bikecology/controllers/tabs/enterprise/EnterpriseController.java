package kz.bikecology.controllers.tabs.enterprise;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kz.bikecology.data.dao.facility.FacilityDAO;
import kz.bikecology.data.models.facility.Facility;

public class EnterpriseController {
    FacilityDAO facilityDAO = new FacilityDAO();

    @FXML private TableView<Facility> facilitiesTable;

    @FXML private TableColumn<Facility, Integer> idCol;
    @FXML private TableColumn<Facility, String> nameCol;
    @FXML private TableColumn<Facility, String> CATOCol;
    @FXML private TableColumn<Facility, String> addressCol;
    @FXML private TableColumn<Facility, String> BINCol;
    @FXML private TableColumn<Facility, String> CCEACol;
    @FXML private TableColumn<Facility, String> productionProcessSpecificationCol;
    @FXML private TableColumn<Facility, String> objectCategoryCol;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        CATOCol.setCellValueFactory(new PropertyValueFactory<>("CATO"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        BINCol.setCellValueFactory(new PropertyValueFactory<>("BIN"));
        CCEACol.setCellValueFactory(new PropertyValueFactory<>("CCEA"));
        productionProcessSpecificationCol.setCellValueFactory(new PropertyValueFactory<>("productionProcessSpecification"));
        objectCategoryCol.setCellValueFactory(new PropertyValueFactory<>("objectCategory"));

        facilitiesTable.setItems(FXCollections.observableArrayList(facilityDAO.getAll()));

    }
}
