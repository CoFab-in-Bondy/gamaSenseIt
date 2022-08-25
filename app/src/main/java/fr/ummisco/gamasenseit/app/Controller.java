package fr.ummisco.gamasenseit.app;

import fr.ummisco.gamasenseit.arduino.cli.ArduinoCLI;
import fr.ummisco.gamasenseit.arduino.cli.ArduinoManager;
import fr.ummisco.gamasenseit.arduino.exception.ArduinoException;
import fr.ummisco.gamasenseit.arduino.exception.BoardNotFoundException;
import fr.ummisco.gamasenseit.arduino.setup.FileHelper;
import fr.ummisco.gamasenseit.arduino.setup.Installer;
import fr.ummisco.gamasenseit.arduino.struct.Board;
import fr.ummisco.gamasenseit.arduino.struct.Terminal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Application.Parameters parameters;

    @FXML
    private TextField key;
    @FXML
    private VBox anchor;
    @FXML
    private TextArea textArea;

    private ArduinoManager manager;
    private Installer installer;
    private Terminal terminal;

    public Scene getScene() {
        return anchor.getScene();
    }

    public Stage getStage() {
        return (Stage) getScene().getWindow();
    }

    public void setArgs(Application.Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            getStage().setOnCloseRequest(ev -> {
                if (this.terminal != null)
                    this.terminal.stop();
            });
        });
        waitUntil(System.currentTimeMillis() + 500, this::onInit);
    }

    public void waitUntil(final long time, final Runnable func) {
        Platform.runLater(() -> {
            if (time < System.currentTimeMillis())
                func.run();
            else
                waitUntil(time, func);
        });
    }


    public void processInstallArduino() {
        installer = new Installer();
        try {
            installer.getResolver().setVersion(AppProperties.arduinoVersion());
            String arduinoPath = installer.check();
            if (arduinoPath == null) {
                Alert alertInstall = App.warning(
                        "Avertissement installation",
                        "L'installation d'Arduino CLI est requise\nVoulez-vous l'installer ?",
                        ButtonType.YES,
                        ButtonType.CLOSE
                );
                alertInstall.showAndWait();
                if (alertInstall.getResult() != ButtonType.YES) {
                    ((Stage) anchor.getScene().getWindow()).close();
                }
                arduinoPath = installer.install();
                ArduinoCLI cli = new ArduinoCLI(arduinoPath);
                cli.libInstall("SD");
                manager = new ArduinoManager(cli);
                Alert infoSetupDone = App.info(
                        "Information",
                        "Installation terminée !",
                        ButtonType.OK
                );
                infoSetupDone.showAndWait();
            } else {
                manager = new ArduinoManager(new ArduinoCLI(arduinoPath));
            }
        } catch (IOException | ArduinoException e) {
            e.printStackTrace();
        }
    }

    public void processInstallSetup() {
        Setup setup = new Setup();
        try {
            if (!setup.checkInstall()) {
                Alert alert = App.warning(
                        "Avertissement installation",
                        "L'application nécéssite une installation priviliégiée\n"
                                + "Voulez-vous lancer l'installation en tant qu'Administrateur ?",
                        ButtonType.YES,
                        ButtonType.CANCEL
                );
                alert.showAndWait();
                if (alert.getResult() != ButtonType.YES)
                    return;
                setup.setupInstall();
                Alert infoSetupDone = App.info(
                        "Information",
                        "Installation terminé !",
                        ButtonType.YES
                );
                infoSetupDone.showAndWait();
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processArguments() {
        // use argument
        List<String> params = parameters.getRaw();
        if (!params.isEmpty()) {
            try {
                String base = params.get(0);
                if (base.startsWith(AppProperties.gamaSenseItLauncher() + ":")) {
                    String key = base.substring(AppProperties.gamaSenseItLauncher().length() + 1);
                    downloadAndUploadFile(key);
                } else {
                    importFile(base);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onInit() {
        processInstallSetup();
        processInstallArduino();
        processArguments();
    }

    public void onImport() throws IOException, ArduinoException {
        // Choose file
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GamaSenseIt files (*.gmst, *.hex)", "*.gmst", "*.hex"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file == null)
            return;
        String source = file.getAbsolutePath();
        importFile(source);
    }

    private void importFile(String source) throws ArduinoException, IOException {
        Board board;
        if (this.terminal != null) this.terminal.stop();
        textArea.clear();

        while (true) {
            try {
                board = manager.firstBoard();
                textArea.appendText("Uploading to " + board.getAddress() + " ...\n");
                break;
            } catch (BoardNotFoundException err) {
                Alert error = App.error(
                        "Erreur de detection",
                        "Pas de carte connecté",
                        new ButtonType("Réessayer", ButtonBar.ButtonData.YES),
                        ButtonType.CANCEL
                );
                Optional<ButtonType> bt = error.showAndWait();
                if (bt.map(b -> b.getButtonData() != ButtonBar.ButtonData.YES).orElse(true)) {
                    return;
                }
            }
        }

        Alert alert = App.confirmation(
                "Confirmation de téléversement",
                "Source :\n" + source + "\nDestination :\n" + board.getAddress() + " (" + board.getArduino().getFQBN() + ")",
                ButtonType.YES,
                ButtonType.CANCEL
        );
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
            textArea.appendText("Upload aborted\n");
            return;
        }

        try {
            manager.upload(board, source);
        } catch (ArduinoException err) {
            App.error("Erreur de téléversement", "Le fichier n'est pas un executable valide", ButtonType.OK).showAndWait();
            return;
        }
        this.terminal = board.getTerminal();
        textArea.appendText("Listening " + board.getAddress() + "\n");
        terminal.listen(c -> Platform.runLater(() -> textArea.appendText(c.toString())));
        Alert alterDone = App.info("Information", "Importation terminée !", ButtonType.OK);
        alterDone.show();
    }

    private void downloadFile(final String key, final String dest) throws Exception {
        String query = AppProperties.baseUrl() + "/public/binary/download?token=" + key;
        System.out.println("Request to " + query);
        try {
            FileHelper.downloadFileTo(query, dest);
        } catch (SSLHandshakeException e) {
            Alert alert = App.warning(
                    "Avertissement de sécurité",
                    "La connection n'est pas sécurisée\nVoulez-vous continuer malgré le risque ?",
                    ButtonType.YES,
                    ButtonType.CANCEL
            );
            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES)
                throw e;

            FileHelper.withDisabledSSL(() -> {
                FileHelper.downloadFileTo(query, dest);
            });
        } catch (IOException err) {
            Alert alert = App.error(
                    "Erreur de clé invalide",
                    "La clé n'est pas valide ou à déjà été utilisé",
                    ButtonType.YES
            );
            alert.show();
        }
    }

    private void downloadAndUploadFile(String key) throws Exception {
        Path dir = Files.createTempDirectory("gsmtdownload");
        String downloadpath = dir.resolve("binary.gmst").toString();
        downloadFile(key, downloadpath);
        importFile(downloadpath);
        Files.deleteIfExists(Paths.get(downloadpath));
    }

    public void onTeleverse() {
        String token = key.getText();
        key.clear();
        if (token == null || token.isEmpty()) {
            Alert alert = App.error(
                    "Erreur",
                    "Vous devez spécifier une clé de téléchargement",
                    ButtonType.YES
            );
            alert.show();
            return;
        }
        Platform.runLater(() -> {
            try {
                downloadAndUploadFile(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
