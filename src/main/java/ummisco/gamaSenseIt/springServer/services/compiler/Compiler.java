package ummisco.gamaSenseIt.springServer.services.compiler;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class Compiler {

    private static final Logger logger = LoggerFactory.getLogger(Compiler.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Value("${gamaSenseIt.make}")
    private String make;

    static void copyRecursive(String dest) throws IOException {
        String dir = "/compiler/";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + dir + "**");
        for (Resource resource : resources) {
            if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
                URL url = resource.getURL();
                String urlString = url.toExternalForm();
                String targetName = urlString.substring(urlString.indexOf(dir) + dir.length());
                File destination = new File(dest, targetName);
                Files.copy(url.openStream(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public byte[] getBinary(Sensor sensor) throws IOException {
        return compileSources(sensor);
    }

    private byte[] compileSources(Sensor sensor) throws IOException {
        logger.info("Start compilation for " + sensor);

        var dir = Files.createTempDirectory("compiler-");
        logger.info("Working in " + dir);
        copyRecursive(dir.toString());

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
            logger.info("Directory deleted");
        }
    }

}
