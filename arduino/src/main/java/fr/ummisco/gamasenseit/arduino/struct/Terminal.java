package fr.ummisco.gamasenseit.arduino.struct;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Terminal {

    private final Board board;
    private boolean stopFlag;
    private Thread thread;

    public Terminal(final Board board) {
        this.board = board;
        this.thread = null;
        this.stopFlag = false;
    }

    public void listen(final Consumer<Character> callback) {
        this.stop();
        this.thread = new Thread(() -> this.run(callback));
        this.thread.start();
    }

    public synchronized void stop() {
        if (this.isAlive()) {
            this.stopFlag = true;
            try {
                this.thread.join();
            } catch (InterruptedException err) {
                throw new RuntimeException(err);
            }
            this.stopFlag = false;
        }
    }

    public boolean isAlive() {
        return this.thread != null && this.thread.isAlive();
    }

    private void run(final Consumer<Character> callback) {
        SerialPort port = board.getSerialPort();
        port.openPort();
        port.setBaudRate(9600);
        try {
            while (!this.stopFlag) {
                while (port.bytesAvailable() > 0 && !stopFlag) {
                    byte[] readBuffer = new byte[port.bytesAvailable()];
                    port.readBytes(readBuffer, readBuffer.length);
                    String morsel = new String(readBuffer, StandardCharsets.UTF_8);
                    for (char c : morsel.toCharArray())
                        callback.accept(c);
                }
            }
        } finally {
            if (port.isOpen())
                port.closePort();
        }
    }
}
