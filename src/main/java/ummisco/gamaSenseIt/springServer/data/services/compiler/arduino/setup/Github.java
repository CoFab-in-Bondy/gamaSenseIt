package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Github {

    private String projectOwner;
    private String projectName;
    private String commit;
    private String path;

    public Github(String projectOwner, String projectName, String commit, String path) {
        this.projectOwner = projectOwner;
        this.projectName = projectName;
        this.commit = commit;
        this.path = path;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String url() {
        return "https://github.com/" + getProjectOwner() + "/" + getProjectName() +  "/archive/" + getCommit() + ".zip";
        // return "https://github.com/" + getProjectOwner() + "/" + getProjectName() + "/archive/refs/heads/master.zip";
    }


    public String download() throws IOException {
        String url = url();
        Path version = Paths.get(getPath(), "version");
        byte[] dataInfo = url.getBytes(StandardCharsets.UTF_8);
        if (!Files.exists(version) || !Arrays.equals(Files.readAllBytes(version), dataInfo)) {
            String dest = Paths.get(getPath(), getProjectName() + "-" + getCommit()).toString();
            String destZip = dest + ".zip";
            System.out.println("Download Github project to " + destZip);
            FileHelper.deleteDirectory(new File(destZip));

            new File(getPath()).mkdirs();
            FileHelper.downloadFileTo(url, destZip);

            System.out.println("End download");
            URI from = URI.create("jar:" + Paths.get(destZip).toUri());
            System.out.println(from);
            System.out.println(dest);
            FileHelper.deleteDirectory(new File(dest));
            FileHelper.extractSubDir(from, Paths.get(dest));
            FileHelper.deleteDirectory(new File(destZip));
            System.out.println("End extract");

            try {
                FileHelper.deleteDirectory(Paths.get(getPath(), getProjectName()).toFile());
                Files.move(Paths.get(dest + "/" + getProjectName() + "-" + getCommit()), Paths.get(getPath() + "/" + getProjectName()));
                FileHelper.deleteDirectory(new File(dest));
            } catch (FileAlreadyExistsException ex) {
                System.out.println("Target file already exists");
            } catch (IOException ex) {
                System.out.format("I/O error: %s%n", ex);
            }
            Files.write(Paths.get(getPath(), "version"), url.getBytes(StandardCharsets.UTF_8));
        }
        return Paths.get(getPath(), getProjectName()).toString();
    }
}
