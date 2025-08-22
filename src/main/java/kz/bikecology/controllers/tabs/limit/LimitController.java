package kz.bikecology.controllers.tabs.limit;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import kz.bikecology.controllers.BaseController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.prefs.Preferences;

public class LimitController extends BaseController {
    private static final Logger log = LogManager.getLogger(LimitController.class);
    Preferences prefs = Preferences.userNodeForPackage(getClass());
    String defaultDownloadDir = System.getProperty("user.home") + File.separator + "Downloads";

    @FXML VBox limitsBox;

    @FXML
    public void initialize() {}
}
