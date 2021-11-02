package ummisco.gamaSenseIt.springServer.data.model;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.Date;

@Entity
public class Parameter implements IConvertible<DisplayableParameter> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long parameterId;
    @Lob
    private byte[] data;
    private Date captureDate;
    @ManyToOne
    private Sensor sensor;
    @ManyToOne
    private ParameterMetadata parameterMetadata;

    public Parameter() {
    }

    private Parameter(Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        super();
        this.sensor = sensor;
        this.captureDate = captureDate;
        this.parameterMetadata = parameterMetadata;
    }

    public Parameter(double data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        this(captureDate, parameterMetadata, sensor);
        this.data = ByteBuffer.allocate(Double.BYTES).putDouble(data).array();
    }

    public Parameter(long data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        this(captureDate, parameterMetadata, sensor);
        this.data = ByteBuffer.allocate(Integer.BYTES).putLong(data).array();
    }

    public Parameter(String data, Date captureDate, ParameterMetadata parameterMetadata, Sensor sensor) {
        this(captureDate, parameterMetadata, sensor);
        this.data = data.getBytes();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Object getDataObject() {
        return parameterMetadata.getDataFormat().convertToObject(data);
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

    public ParameterMetadata getParameterMetadata() {
        return parameterMetadata;
    }

    public void setParameterMetadata(ParameterMetadata metadata) {
        this.parameterMetadata = metadata;
    }

    public long getParameterId() {
        return parameterId;
    }

    public void setParameterId(long parameterId) {
        this.parameterId = parameterId;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public DisplayableParameter convert() {
        return new DisplayableParameter(this);
    }

    /*
     * void setBlob() throws SerialException, SQLException { SerialBlob sb = new
     * SerialBlob((new String("tioti")).getBytes()); }
     *
     * void loadData() { Blob blob = rs.getBlob(cloumnName[i]); byte[] bdata =
     * blob.getBytes(1, (int) blob.length()); String s = new String(bdata); }
     */
}
