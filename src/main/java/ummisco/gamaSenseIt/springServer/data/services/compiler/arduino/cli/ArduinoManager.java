package ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.cli;

import org.json.JSONArray;
import org.json.JSONException;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct.Arduino;
import ummisco.gamaSenseIt.springServer.data.services.compiler.arduino.struct.Board;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class ArduinoManager {
    private final ArduinoCLI cli;
    private final HashSet<String> coresInstalled;

    public ArduinoManager(ArduinoCLI cli) throws IOException, ArduinoException {
        this.cli = cli;
        this.coresInstalled = new HashSet<>();
        this.cli.coreUpdateIndex();
        JSONArray cores = this.cli.coreList();
        for (int i = 0; i < cores.length(); i++) {
            coresInstalled.add(cores.getJSONObject(i).getString("id"));
        }
        this.cli.libInstall("SD");
    }

    public List<Board> boards() throws IOException, ArduinoJSONArrayException {
        ArrayList<Board> boards = new ArrayList<>();
        JSONArray jsonBoards = cli.boardList();
        for (int i = 0; i < jsonBoards.length(); i++) {
            try {
                boards.add(Board.fromJSON(jsonBoards.getJSONObject(i)));
            } catch (JSONException err) {
                System.err.println(err.getMessage());
            }
        }
        return boards;
    }

    public Board firstBoardForArduino(Arduino arduino) throws IOException, ArduinoException {
        return boards()
                .stream()
                .filter(b -> arduino.equals(b.getArduino()))
                .findAny()
                .orElseThrow(()->new IOException("No board connected"));
    }

    public Board firstBoard() throws IOException, ArduinoException {
        return boards()
                .stream()
                .findAny()
                .orElseThrow(()->new IOException("No board connected"));
    }

    private void lazyCoreInstall(String core) throws IOException, ArduinoException {
        if (coresInstalled.contains(core))
            return;
        this.cli.coreInstall(core);
        coresInstalled.add(core);
    }

    public String compile(Arduino arduino, String sketch, String build, String libraries, Properties properties) throws IOException, ArduinoException {
        lazyCoreInstall(arduino.getCore());
        try {
            this.cli.compile(arduino.getFQBN(), sketch, build, libraries, properties);
        } catch (ArduinoJSONObjectException err) {
            throw new ArduinoJSONObjectException(err.jsonObject().getString("compiler_err"), err.output());
        }
        String name = new File(sketch).getName();
        Path dest = Paths.get(build, name + ".gmst");
        System.out.println("Name of sketch " + name);
        for (String ext : Arrays.asList("eep", "elf", "with_bootloader.bin", "with_bootloader.hex")) {
            System.out.println("Deleting " + Paths.get(build, name + "." + ext));
            Files.deleteIfExists(Paths.get(build, name + ".ino." + ext));
        }
        Files.move(
                Paths.get(build, name + ".ino.hex"),
                dest,
                StandardCopyOption.REPLACE_EXISTING
        );
        return dest.toString();
    }

    public void upload(Board board, String build) throws IOException, ArduinoException {
        Path dir = Files.createTempDirectory(Paths.get("gamsenseit-" + new File(build).getName()).toString());
        Path from = Paths.get(dir.toString(), "abstract.hex");
        try {
            Files.copy(
                    Paths.get(build),
                    from,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES
            );
            this.cli.upload(board.getAddress(), board.getArduino().getFQBN(), from.toString());
        } finally {
            Files.deleteIfExists(from);
            Files.deleteIfExists(dir);
        }
    }
}
