package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.Date;

@Entity
public class SensorData implements IConvertible<DisplayableData> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    private byte[] data;
    private Date captureDate;
    @ManyToOne
    private Sensor sensor;
    @ManyToOne
    private ParameterMetadata parameter;

    public SensorData() {
    }

    private SensorData(Date captureDate, ParameterMetadata metadata, Sensor s) {
        super();
        this.sensor = s;
        this.captureDate = captureDate;
        this.parameter = metadata;
    }

    public SensorData(double data, Date captureDate, ParameterMetadata metadata, Sensor s) {
        this(captureDate, metadata, s);
        this.data = ByteBuffer.allocate(Double.BYTES).putDouble(data).array();
    }

    public SensorData(long data, Date captureDate, ParameterMetadata metadata, Sensor s) {
        this(captureDate, metadata, s);
        this.data = ByteBuffer.allocate(Integer.BYTES).putLong(data).array();
    }

    public SensorData(String data, Date captureDate, ParameterMetadata metadata, Sensor s) {
        this(captureDate, metadata, s);
        this.data = data.getBytes();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Object getDataObject() {
        return parameter.getDataFormat().convertToObject(data);
    }

    public String toString() {
        return this.getDataObject().toString();
    }

    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }

    public ParameterMetadata getParameter() {
        return parameter;
    }

    public void setParameter(ParameterMetadata metadata) {
        this.parameter = metadata;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public DisplayableData convert() {
        return new DisplayableData(this);
    }

    /*
     * void setBlob() throws SerialException, SQLException { SerialBlob sb = new
     * SerialBlob((new String("tioti")).getBytes()); }
     *
     * void loadData() { Blob blob = rs.getBlob(cloumnName[i]); byte[] bdata =
     * blob.getBytes(1, (int) blob.length()); String s = new String(bdata); }
     */
}
