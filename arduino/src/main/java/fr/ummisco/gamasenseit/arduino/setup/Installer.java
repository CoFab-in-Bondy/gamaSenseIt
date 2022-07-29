package fr.ummisco.gamasenseit.arduino.setup;

import fr.ummisco.gamasenseit.arduino.struct.Systems;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Installer {

    private final Resolver resolver;
    private final String path;

    public Installer() {
        this.resolver = new Resolver();
        this.path = Systems.gamasenseitHome() + File.separator + "arduino/";
    }

    public Resolver getResolver() {
        return this.resolver;
    }

    /**
     * Check if arduino is intsalled
     *
     * @return PAth to install or null if not installed
     */
    public String check() {
        String url = resolver.url();
        byte[] dataInfo = url.getBytes(StandardCharsets.UTF_8);
        Path info = Paths.get(getPath(), "version");
        try {
            if (Files.exists(info) && Arrays.equals(Files.readAllBytes(info), dataInfo)) {
                return Paths.get(getPath(), resolver.binaryName()).toString();
            }
        } catch (IOException err) {
            return null;
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String install() throws IOException {
        String url = resolver.url();
        String name = resolver.name();
        String dest = Paths.get(getPath(), name).toString();
        new File(getPath()).mkdirs();
        System.out.println("Download " + url);
        System.out.println("Install it into " + dest);
        try {
            FileHelper.downloadFileTo(url, dest);
        } catch (IOException ignored) {
            url = resolver.alternativeUrl();
            System.out.println("Download failed retry with " + url);
            FileHelper.downloadFileTo(url, dest);
        }

        System.out.println("End download");
        String cli = resolver.extracter().extract(dest, getPath(), resolver.binaryName());
        new File(cli).setExecutable(true, true);
        System.out.println("Extracted CLI to " + cli);

        Files.write(Paths.get(getPath() + "/version"), url.getBytes(StandardCharsets.UTF_8));

        return Paths.get(getPath(), resolver.binaryName()).toString();
    }

    public String checkAndInstall() throws IOException {
        String path = check();
        return path != null ? check() : install();
    }


    public String getPath() {
        return path;
    }
}
