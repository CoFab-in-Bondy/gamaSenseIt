package fr.ummisco.gamasenseit.app;

import javafx.application.Application;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

/**
 * Class where is configured the settings and resources of the application.
 */
public class App extends Application {

    /**
     * Main function for run application in static context.
     *
     * @param args Arguments from commandline.
     */
    public static void main(String[] args) {
        System.out.println("Start running");
        launch(args);
    }

    /**
     * Set icon to a stage object.
     *
     * @param stage Stage to set icon.
     */
    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResource("/img/logo.png"), "No logo found").toString()));
    }

    /**
     * Set a stylesheet to a scene object.
     *
     * @param scene Scene to set stylesheet.
     */
    public static void setSceneStylesheet(Scene scene) {
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("/styles.css")).toExternalForm());
    }


    /**
     * Creates an alert with the given contentText, ButtonTypes, and AlertType
     * (refer to the {@link Alert.AlertType} documentation for clarification over which
     * one is most appropriate). This alert has application icon.
     *
     * @param alertType   the alert type
     * @param title       header and title of alert
     * @param contentText the content text
     * @param buttons     the button types
     */
    public static Alert alert(
            @NamedArg("alertType") Alert.AlertType alertType,
            String title,
            @NamedArg("contentText") String contentText,
            @NamedArg("buttonTypes") ButtonType... buttons) {
        Alert alert = new Alert(alertType, contentText, buttons);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.setTitle(title);
        alert.setHeaderText(title);
        App.setStageIcon(stage);
        return alert;
    }

    public static Alert error(String title, String contentText, ButtonType... buttons) {
        return App.alert(Alert.AlertType.ERROR, title, contentText, buttons);
    }

    public static Alert warning(String title, String contentText, ButtonType... buttons) {
        return App.alert(Alert.AlertType.WARNING, title, contentText, buttons);
    }

    public static Alert info(String title, String contentText, ButtonType... buttons) {
        return App.alert(Alert.AlertType.INFORMATION, title, contentText, buttons);
    }

    public static Alert confirmation(String title, String contentText, ButtonType... buttons) {
        return App.alert(Alert.AlertType.CONFIRMATION, title, contentText, buttons);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws java.lang.Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getClassLoader().getResource("view.fxml");
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Controller controller = loader.getController();
        controller.setArgs(getParameters());
        primaryStage.setScene(scene);
        primaryStage.setTitle("GamaSenseIt Arduino");
        setStageIcon(primaryStage);
        setSceneStylesheet(scene);
        primaryStage.centerOnScreen();
        primaryStage.toFront();
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}