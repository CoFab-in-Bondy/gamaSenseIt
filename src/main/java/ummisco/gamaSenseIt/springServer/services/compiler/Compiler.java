package ummisco.gamaSenseIt.springServer.services.compiler;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class Compiler {

    private static final Logger logger = LoggerFactory.getLogger(Compiler.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Value("classpath:compiler")
    private Resource directory;

    @Value("${gamaSenseIt.make}")
    private String make;

    static void copyRecursive(File path, String dest) throws IOException {
        if (path.isFile()) {
            copyResource(path, dest);
        } else {
            for (var file : Objects.requireNonNull(path.listFiles())) {
                if (file.isFile() &&
                        (file.getName().endsWith(".c")
                                || file.getName().endsWith(".cpp")
                                || file.getName().endsWith(".h")
                                || "Makefile".equals(file.getName()))) {
                    copyResource(file, Paths.get(dest, file.getName()).toString());
                }
            }
        }
    }

    static void copyResource(File path, String dest) throws IOException {
        InputStream src = new FileInputStream(path);
        Files.copy(src, Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
    }

    public byte[] getBinary(Sensor sensor) throws IOException {
        return compileSources(sensor);
    }

    private byte[] compileSources(Sensor sensor) throws IOException {
        logger.info("Start compilation for " + sensor);

        var dir = Files.createTempDirectory("compiler-");
        copyRecursive(directory.getFile(), dir.toString());
        logger.info("Working in " + dir);

        var out = dir.resolve("sensor.exe");
        try {
            var command = List.of(make, "NAME=" + securityUtils.sanitizeFilename(sensor.getName()), "OUT=" + out);
            var pb = new ProcessBuilder(command)
                    .inheritIO()
                    .directory(dir.toFile());

            var process = pb.start();
            try {
                process.waitFor();
            } catch (InterruptedException err) {
                throw new IOException("Process was interrupted");
            }

            if (process.exitValue() == 0) {
                var bytes = Files.readAllBytes(out);
                logger.info("Binary (" + bytes.length + "o) is ready at " + out);
                return bytes;
            } else {
                logger.error("Compilation exit with error code " + process.exitValue());
                throw new IllegalStateException("Can't compile file");
            }
        } finally {
            FileUtils.deleteDirectory(dir.toFile());
            logger.trace("Directory deleted");
        }
    }

}
