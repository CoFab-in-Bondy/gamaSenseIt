package fr.ummisco.gamasenseit.app;


import org.junit.jupiter.api.Test;
import fr.ummisco.gamasenseit.arduino.cli.ArduinoCLI;
import fr.ummisco.gamasenseit.arduino.exception.ArduinoException;
import fr.ummisco.gamasenseit.arduino.cli.ArduinoManager;
import fr.ummisco.gamasenseit.arduino.setup.Installer;

import java.io.IOException;


public class AppTest
{
    @Test
    public void testApp() throws IOException, ArduinoException {
        Installer installer = new Installer();
        String path = installer.checkAndInstall();
        ArduinoCLI cli =  new ArduinoCLI(path);
        ArduinoManager manager = new ArduinoManager(cli);
        manager.boards();
    }
}
