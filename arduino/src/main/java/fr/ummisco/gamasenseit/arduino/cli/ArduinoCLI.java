package fr.ummisco.gamasenseit.arduino.cli;

import fr.ummisco.gamasenseit.arduino.exception.ArduinoException;
import fr.ummisco.gamasenseit.arduino.exception.ArduinoJSONArrayException;
import fr.ummisco.gamasenseit.arduino.exception.ArduinoJSONObjectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArduinoCLI {

    private final String path;

    public ArduinoCLI(String path) {
        this.path = path;
    }

    private String run(List<String> args) throws ArduinoException, IOException {
        for (String arg : args) if (arg == null) throw new NullPointerException("Null value in args");
        ArrayList<String> listArgs = new ArrayList<>();
        listArgs.add(path);
        listArgs.addAll(args);
        listArgs.add("--format");
        listArgs.add("json");

        System.out.println("Run command : " + listArgs);
        Process process = new ProcessBuilder(listArgs)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .directory(Paths.get(path).getParent().toFile())
                .start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String out = builder.toString();

        try {
            process.waitFor();
        } catch (InterruptedException err) {
            throw new IOException("Process was interrupted");
        }

        if (process.exitValue() == 0) {
            System.out.println(out);
            System.out.println("Success");
            return out;
        } else {
            System.out.println(out);
            throw new ArduinoException("Exit code " + process.exitValue(), out);
        }
    }

    private ArrayList<String> list(String... args) {
        return new ArrayList<>(Arrays.asList(args));
    }

    private JSONArray runAsJSONArray(List<String> args) throws IOException, ArduinoJSONArrayException {
        try {
            return new JSONArray(run(args));
        } catch (JSONException err) {
            throw new IOException("Input stream is not an JSONArray");
        } catch (ArduinoException err) {
            throw new ArduinoJSONArrayException(err.getMessage(), err.output());
        }
    }

    private JSONObject runAsJSONObject(List<String> args) throws IOException, ArduinoJSONObjectException {
        try {
            return new JSONObject(run(args));
        } catch (JSONException err) {
            throw new IOException("Input stream is not an runAsJSONObject");
        } catch (ArduinoException err) {
            throw new ArduinoJSONObjectException(err.getMessage(), err.output());
        }
    }


    public JSONArray coreList() throws IOException, ArduinoJSONArrayException {
        return runAsJSONArray(list("core", "list"));
    }

    public JSONObject compile(String arduino, String source, String build, String libraries, Properties buildExtraFlags) throws IOException, ArduinoJSONObjectException {
        ArrayList<String> args = new ArrayList<>();
        args.add("compile");
        if (arduino != null) {
            args.add("--fqbn");
            args.add(arduino);
        }
        if (libraries != null) {
            args.add("--libraries");
            args.add(libraries);
        }
        if (build != null) {
            args.add("--output-dir");
            args.add(build);
        }
        if (buildExtraFlags != null && !buildExtraFlags.empty()) {
            args.add("--build-property");
            args.add(buildExtraFlags.asString());
        }
        args.add("--clean");
        args.add(Objects.requireNonNull(source));
        return runAsJSONObject(args);
    }

    public void upload(String port, String fqbn, String file) throws IOException, ArduinoException {
        run(list("upload", "--port", port, "--fqbn", fqbn, "--input-file", file));
    }

    public void coreUpdateIndex() throws IOException, ArduinoException {
        run(list("core", "update-index"));
    }

    public void coreInstall(String arduino) throws IOException, ArduinoException {
        run(list("core", "install", arduino));
    }

    public JSONArray boardList() throws IOException, ArduinoJSONArrayException {
        return runAsJSONArray(list("board", "list"));
    }

    public void libInstall(String lib) throws IOException, ArduinoException {
        run(list("lib", "install", lib));
    }

    public void sketchNew(String sketch) throws IOException, ArduinoException {
        run(list("sketch", "new", sketch));
    }
}
