package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.setup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resolver {

    private static final String DEFAULT_PROJECT_OWNER = "arduino";
    private static final String DEFAULT_PROJECT_NAME = "arduino-cli";
    private static final String DEFAULT_VERSION = "0.24.0";
    private static final String DEFAULT_ARCH = "64bit";

    private String os;
    private String arch;
    private String version;
    private String projectOwner;
    private String projectName;

    public Resolver() {

    }

    public void setOS(String os) {
        this.os = os;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public String getProjectOwner() {
        if (this.projectOwner == null)
            this.projectOwner = DEFAULT_PROJECT_OWNER;
        return this.projectOwner;
    }

    public String getProjectName() {
        if (this.projectName == null)
            this.projectName = DEFAULT_PROJECT_NAME;
        return this.projectName;
    }

    public String getOS() {
        if (os == null)
            setDefaultOS();
        return os;
    }

    private void setDefaultOS() {
        this.os = System.getProperty("os.name");
        if (os.startsWith("Linux"))
            this.os = "Linux";
        else if (os.startsWith("macOS"))
            this.os = "macOS";
        else if (os.startsWith("MINGW") || os.startsWith("MSYS") || os.startsWith("Windows"))
            this.os = "Windows";
    }

    public String getArch() {
        if (arch == null)
            setDefaultArch();
        return arch;
    }

    private void setDefaultArch() {
        this.arch = System.getProperty("os.arch").toLowerCase();
        if (arch.startsWith("armv5"))
            this.arch = "armv5";
        else if (arch.startsWith("armv6"))
            this.arch = "armv6";
        else if (arch.startsWith("armv7"))
            this.arch = "armv7";
        else {
            switch (arch) {
                case "aarch64":
                case "arm64":
                    this.arch = "ARM64";
                    return;
                case "x86":
                case "i686":
                case "i386":
                    this.arch = "32bit";
                    return;
                case "x86_64":
                    this.arch = "64bit";
                    return;
                default:
                    this.arch = DEFAULT_ARCH;
            }
        }
    }

    public String getVersion() {
        if (version == null)
            setDefaultVersion();
        return version;
    }

    private void setDefaultVersion() {
        String url = "https://github.com/" + getProjectOwner() + "/" + getProjectName() + "/releases/latest";
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ignored) {
            this.version = DEFAULT_VERSION;
            return;
        }
        Element titleElement = doc.selectFirst("title");
        if (titleElement == null) {
            this.version = DEFAULT_VERSION;
            return;
        }
        String title = titleElement.text();
        Pattern pattern = Pattern.compile("^Release ([0-9][A-Za-z0-9|\\\\.-]*) · " + Pattern.quote(getProjectOwner()) + "/" + Pattern.quote(getProjectName()) + " · GitHub$");
        Matcher matcher = pattern.matcher(title);
        this.version = matcher.find()? DEFAULT_VERSION : matcher.group(1);
    }

    public String extension() {
        return isWindows() ? ".zip" : ".tar.gz";
    }

    public String name() {
        return getProjectName() + "_" + getVersion() + "_" + getOS() + "_" + getArch() + extension();
    }

    public String url() {
        return "https://downloads.arduino.cc/" + getProjectName() + "/" + name();
    }

    public String alternativeUrl() throws IOException {
        return "https://github.com/" + getProjectOwner() + "/" + getProjectName() + "/releases/download/" + getVersion() + "/" + name();
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String binaryName() {
        return getProjectName() + (isWindows() ? ".exe" : "");
    }

    public boolean isWindows() {
        return "windows".equalsIgnoreCase(getOS());
    }

    public Extracter extracter() {
        return isWindows() ? new ExtracterZIP() : new ExtracterGZIP();
    }
}
