package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup;

import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct.Systems;

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
        this.path = Systems.userHome() + File.separator + ".arduino-cli/";
    }

    public Resolver getResolver() {
        return this.resolver;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String install() throws IOException {
        String url = resolver.url();
        byte[] dataInfo = url.getBytes(StandardCharsets.UTF_8);
        Path info = Paths.get(getPath(), "version");

        if (!Files.exists(info) || !Arrays.equals(Files.readAllBytes(info), dataInfo)) {
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
            System.out.println("Extracted CLI to " + cli);

            Files.write(Paths.get(getPath() + "/version"), url.getBytes(StandardCharsets.UTF_8));
        }

        return Paths.get(getPath(), resolver.binaryName()).toString();
    }


    public String getPath() {
        return path;
    }
}
