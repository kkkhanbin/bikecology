package kz.bikecology.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainController extends BaseController {

    @FXML private StackPane contentPane;

    @FXML private VBox sidebar;

    @FXML private Button btnEnterprise;
    @FXML private Button btnWorks;
    @FXML private Button btnReport;

    @FXML private BorderPane enterpriseTab;
    @FXML private BorderPane worksTab;
    @FXML private BorderPane reportTab;

    @FXML
    public void initialize() throws IOException {
        worksTab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/tabs/works.fxml")));
        reportTab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/tabs/report.fxml")));
        enterpriseTab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/tabs/enterprise.fxml")));

        btnEnterprise.getStyleClass().add("selected");
        contentPane.getChildren().setAll(enterpriseTab);
    }

    @FXML
    public void toggleSidebar() {
        if (sidebar.isVisible()) {
            sidebar.setVisible(false);
            sidebar.setPrefWidth(0);
            sidebar.setMinWidth(0);
        } else {
            sidebar.setVisible(true);
            sidebar.setPrefWidth(0);
            sidebar.setMinWidth(Region.USE_COMPUTED_SIZE);
        }
    }

    @FXML
    public void setWorksContent(ActionEvent event) {
        setSelectedSidebarBtn((Button) event.getSource());
        contentPane.getChildren().setAll(worksTab);
    }

    @FXML
    public void setReportContent(ActionEvent event) {
        setSelectedSidebarBtn((Button) event.getSource());
        contentPane.getChildren().setAll(reportTab);
    }

    @FXML
    public void setEnterpriseContent(ActionEvent event) {
        setSelectedSidebarBtn((Button) event.getSource());
        contentPane.getChildren().setAll(enterpriseTab);
    }

    protected void setSelectedSidebarBtn(Button clicked) {
        getSidebarBtns().forEach(btn -> btn.getStyleClass().remove("selected"));
        clicked.getStyleClass().add("selected");
    }

    protected List<Button> getSidebarBtns() {
        List<Button> sidebarBtns = List.of(btnReport, btnWorks, btnEnterprise);
        return sidebarBtns;
    }
}
