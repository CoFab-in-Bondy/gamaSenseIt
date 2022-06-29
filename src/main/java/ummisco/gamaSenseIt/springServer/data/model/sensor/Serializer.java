package ummisco.gamaSenseIt.springServer.data.model.sensor;

import com.sun.istack.NotNull;

public interface Serializer {
    @NotNull
    Object bytesToObject(byte[] bytes);

    @NotNull
    byte[] stringToBytes(String string);

    default String bytesToString(byte[] bytes) {
        var obj = bytesToObject(bytes);
        return obj == null ? "" : obj.toString();
    }

}