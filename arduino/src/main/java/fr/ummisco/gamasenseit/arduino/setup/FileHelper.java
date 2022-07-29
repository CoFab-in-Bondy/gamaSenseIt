package fr.ummisco.gamasenseit.arduino.setup;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public class FileHelper {

    public static void extractSubDir(URI zipFileUri, Path targetDir)
            throws IOException {
        FileSystem zipFs = FileSystems.newFileSystem(zipFileUri, new HashMap<>());
        Path pathInZip = zipFs.getPath(".");
        Files.walkFileTree(pathInZip, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                Path relativePathInZip = pathInZip.relativize(filePath);
                Path targetPath = targetDir.resolve(relativePathInZip.toString());
                Files.createDirectories(targetPath.getParent());
                Files.copy(filePath, targetPath);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void downloadFileTo(String url, String dest) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    public static void deleteDirectory(File directory) {
        if (!directory.exists()) return;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    public synchronized static void withDisabledSSL(ThrowingRunnable run) throws Exception {
        SSLSocketFactory sslSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // reset action
            run.runThrows();
        } catch (KeyManagementException | NoSuchAlgorithmException err) {
            throw new IllegalStateException("Can't disable SSL");
        } finally {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        }
    }

    @FunctionalInterface
    public interface ThrowingRunnable extends Runnable {

        @Override
        default void run() {
            try {
                runThrows();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        void runThrows() throws Exception;

    }
}
