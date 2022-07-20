package ummisco.gamaSenseIt.springServer.data.model.sensor;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Consumer;

public enum DataFormat implements DataFormatSerializer {

    LONG(){
        @Override
        public Object bytesToObject(byte[] bytes) {
            return ByteBufferHelper.wrapByteBuffer(bytes, ByteBuffer::getLong);
        }

        @Override
        public byte[] stringToBytes(String string) {
            return ByteBuffer.allocate(Long.BYTES).putLong(Long.parseLong(string)).array();
        }
    },
    DOUBLE(){
        @Override
        public Object bytesToObject(byte[] bytes) {
            return ByteBufferHelper.wrapByteBuffer(bytes, ByteBuffer::getDouble);
        }

        @Override
        public byte[] stringToBytes(String string) {
            return ByteBuffer.allocate(Double.BYTES).putDouble("NAN".equalsIgnoreCase(string.strip()) ? Double.NaN : Double.parseDouble(string)).array();
        }
    },
    STRING() {
        @Override
        public Object bytesToObject(byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }

        @Override
        public byte[] stringToBytes(String string) {
            return string.getBytes(StandardCharsets.UTF_8);
        }
    },
    DATE() {
        @Override
        public Object bytesToObject(byte[] bytes) {
            Long timestamp = (Long) LONG.bytesToObject(bytes);
            if (timestamp == null) return null;
            return new Date(timestamp * 1000);
        }

        @Override
        public byte[] stringToBytes(String string) {
            return LONG.stringToBytes(string);
        }
    };

    public void switchValues(
            Object object,
            Consumer<Long> longCase,
            Consumer<Double> doubleCase,
            Consumer<String> stringCase,
            Consumer<Date> dateCase
    ) {
        switch (this) {
            case LONG -> longCase.accept(object == null ? null : (Long)object);
            case DOUBLE -> doubleCase.accept(object == null ? null : (Double)object);
            case STRING -> stringCase.accept(object == null ? null : (String)object);
            case DATE -> dateCase.accept(object == null ? null : (Date)object);
        }
    }

    public void switchValuesWithNullCase(
            Object object,
            Consumer<Long> longCase,
            Consumer<Double> doubleCase,
            Consumer<String> stringCase,
            Consumer<Date> dateCase,
            Runnable nullCase
    ) {
        if (object == null) {
            nullCase.run();
        } else {
            switch (this) {
                case LONG -> longCase.accept((Long)object);
                case DOUBLE -> doubleCase.accept((Double)object);
                case STRING -> stringCase.accept((String)object);
                case DATE -> dateCase.accept((Date)object);
            }
        }
    }
}
