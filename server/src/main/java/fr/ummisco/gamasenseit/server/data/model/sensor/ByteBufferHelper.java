package fr.ummisco.gamasenseit.server.data.model.sensor;

import java.nio.ByteBuffer;
import java.util.function.Function;

public class ByteBufferHelper {

    public static <T> T wrapByteBuffer(byte[] data, Function<ByteBuffer, T> callback) {
        if (data == null) return null;
        ByteBuffer buffer = ByteBuffer.wrap(data);
        T obj = callback.apply(buffer);
        if (buffer.hasRemaining()) {
            return null;
        }
        return obj;
    }
}
