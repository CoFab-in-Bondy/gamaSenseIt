package fr.ummisco.gamasenseit.app;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import fr.ummisco.gamasenseit.arduino.setup.Resolver;
import fr.ummisco.gamasenseit.arduino.struct.Systems;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;

public class Setup {

    private String externalIconPath;
    private String javaPath;
    private String jarPath;

    private Path getHome() {
        Path home = Paths.get(Systems.gamasenseitHome(), "app");
        home.toFile().mkdirs();
        return home;
    }

    private String getExternalIconPath() throws IOException {
        if (externalIconPath == null) {
            InputStream inIco = Objects.requireNonNull(Setup.class.getResourceAsStream(Constantes.ICON_PATH));
            Path ico = getHome().resolve(Constantes.ICON_NAME);
            Files.copy(inIco, ico, StandardCopyOption.REPLACE_EXISTING);
            externalIconPath = ico.toString();
        }
        return externalIconPath;
    }

    private String getJavaPath() {
        if (javaPath == null) {
            javaPath = new File(new File(new File(System.getProperty("java.home")), "bin"), Constantes.JAVA_EXE).toString();
        }
        return javaPath;
    }

    private String getJarPath() throws URISyntaxException {
        if (jarPath == null) {
            jarPath = new File(Setup.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
        }
        return jarPath;
    }

    public void setupWindowInstall() throws IOException, InterruptedException, URISyntaxException {
        InputStream inBat = Objects.requireNonNull(Setup.class.getResourceAsStream(Constantes.SCRIPT_PATH + "/" + Constantes.GAMASENSEIT_NAME_WINDOWS));
        Path bat = getHome().resolve(Constantes.GAMASENSEIT_NAME_WINDOWS);
        Files.copy(inBat, bat, StandardCopyOption.REPLACE_EXISTING);

        ArrayList<String> listArgs = new ArrayList<>();
        listArgs.add(bat.toString());
        listArgs.add(getJavaPath());
        listArgs.add(getJarPath());
        listArgs.add(getExternalIconPath());

        Process p = new ProcessBuilder(listArgs).inheritIO().start();
        p.waitFor();
        Thread.sleep(1000);
    }

    public boolean checkWindowsInstall() throws URISyntaxException, IOException {
        try {
            for (String key : new String[]{Constantes.GAMASENSEIT_FILE, Constantes.GAMASENSEIT_LAUNCHER}) {
                String regCmd = Advapi32Util.registryGetExpandableStringValue(WinReg.HKEY_CLASSES_ROOT, key + "\\Shell\\Open\\Command", null);
                if (!regCmd.contains(getJarPath()) || !regCmd.contains(getJavaPath()))
                    return false;
                String regIcon = Advapi32Util.registryGetStringValue(WinReg.HKEY_CLASSES_ROOT, key + "\\DefaultIcon", null);
                if (regIcon == null || !regIcon.equals(getExternalIconPath()))
                    return false;
            }
        } catch (Win32Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkInstall() {
        if (!new Resolver().isWindows())
            return true; // TODO Not supported
        try {
            return checkWindowsInstall();
        } catch (URISyntaxException | IOException err) {
            err.printStackTrace();
            return false;
        }
    }

    public void setupInstall() throws URISyntaxException, IOException, InterruptedException {
        if (!new Resolver().isWindows())
            return; // TODO Not supported
        setupWindowInstall();
    }
}
