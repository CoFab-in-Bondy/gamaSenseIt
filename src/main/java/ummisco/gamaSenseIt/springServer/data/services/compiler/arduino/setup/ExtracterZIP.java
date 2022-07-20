package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtracterZIP implements Extracter {
    @Override
    public String extract(String archive, String dest, String name) throws IOException {
        Objects.requireNonNull(name, "name must me not null");
        String filePath = dest + File.separator + name;
        try (ZipFile zipFile = new ZipFile(archive)) {
            ZipEntry zipEntry = zipFile.getEntry(name);
            try (
                    InputStream zipIn = zipFile.getInputStream(zipEntry);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))
            ) {
                byte[] bytesIn = new byte[2048];
                int read = 0;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
            }
        }
        Files.deleteIfExists(Paths.get(archive));
        return filePath;
    }
}
