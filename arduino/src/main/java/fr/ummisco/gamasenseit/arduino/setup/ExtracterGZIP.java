package fr.ummisco.gamasenseit.arduino.setup;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ExtracterGZIP implements Extracter {
    @Override
    public String extract(String archive, String dest, String name) throws IOException {
        Objects.requireNonNull(name, "Name must me not null");
        File src = new File(archive);
        File tmpDest = new File(dest + File.separator + "tmp");
        File destination = new File(dest + File.separator + name);
        Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
        archiver.extract(src, tmpDest);
        Files.deleteIfExists(Paths.get(archive));
        boolean moved = tmpDest.toPath().resolve(name).toFile().renameTo(destination);
        if (!moved) throw new IOException("Can't move " + name);
        FileHelper.deleteDirectory(tmpDest);
        return destination.toString();
    }
}
