package ummisco.gamaSenseIt.springServer.services.formatter;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import ummisco.gamaSenseIt.springServer.data.model.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.Sensor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class Export {

    protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final String ext;
    private final MediaType media;
    @Autowired
    protected NamedParameterJdbcTemplate jdbc;


    public Export(@NotNull MediaType media, @NotNull String ext) {
        this.ext = ext;
        this.media = media;
    }

    private static String buildFilename(
            @NotNull Sensor sensor,
            @NotNull String type,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    ) {
        return (parameterMetadata == null ? "parameters" : parameterMetadata.getName())
                + "-"
                + sensor.getName()
                + (start != null || end != null
                ? "-"
                + (start == null ? "X" : dateFormat.format(start))
                + "-"
                + (end == null ? "X" : dateFormat.format(end))
                : "")
                + "."
                + type;
    }

    public static byte[] zipBytes(String filename, byte[] input) throws IOException {
        var out = new ByteArrayOutputStream();
        var zipOut = new ZipOutputStream(out);
        var entry = new ZipEntry(filename);
        entry.setSize(input.length);
        zipOut.putNextEntry(entry);
        zipOut.write(input);
        zipOut.closeEntry();
        zipOut.close();
        return out.toByteArray();
    }

    protected <T> Stream<T> streamParametersByParameterMetadata(
            @NotNull Sensor sensor,
            @NotNull ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end,
            RowMapperPartial<T> rm
    ) {
        if (!Objects.equals(
                Objects.requireNonNull(parameterMetadata).getSensorMetadataId(),
                Objects.requireNonNull(sensor).getSensorMetadataId())
        ) {
            return Stream.empty();
        }
        return jdbc.queryForStream(
                """
                        SELECT p.capture_date, p.data FROM parameter p
                            WHERE p.sensor_id = :sensor_id
                                AND p.parameter_metadata_id = :parameter_metadata_id
                                AND (:start IS NULL OR p.capture_date >= :start)
                                AND (:end IS NULL OR p.capture_date <= :end)""",
                new HashMap<>() {{
                    put("sensor_id", sensor.getId());
                    put("parameter_metadata_id", parameterMetadata.getId());
                    put("start", start);
                    put("end", end);
                }},
                (rs, rpwNum) -> rm.apply(
                        rs.getDate(1),
                        parameterMetadata.getDataType().convertToString(rs.getBytes(2))
                )
        );
    }

    protected <T> Stream<T> streamParametersBySensor(
            @NotNull Sensor sensor,
            @Nullable Date start,
            @Nullable Date end,
            RowMapperComplete<T> rm
    ) {
        return jdbc.queryForStream(
                """
                        SELECT pmd.id, pmd.name, pmd.unit, pmd.data_type, p.capture_date, p.data FROM parameter p
                            JOIN parameter_metadata pmd ON (p.parameter_metadata_id = pmd.id)
                            WHERE p.sensor_id = :sensor_id
                                AND (:start IS NULL OR p.capture_date >= :start)
                                AND (:end IS NULL OR p.capture_date <= :end)
                            ORDER BY pmd.idx""",
                new HashMap<>() {{
                    put("sensor_id", Objects.requireNonNull(sensor).getId());
                    put("start", start);
                    put("end", end);
                }},
                (rs, rowNum) -> rm.apply(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(5),
                        ParameterMetadata.DataFormat.values()[rs.getInt(4)].convertToObject(rs.getBytes(6))
                )
        );
    }

    public String getExt() {
        return ext;
    }

    public MediaType getMedia() {
        return media;
    }

    protected abstract byte[] toBytes(
            @NotNull Sensor sensor,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    );

    public ResponseEntity<Resource> export(
            @NotNull Sensor sensor,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    ) {
        String filename = buildFilename(sensor, ext, parameterMetadata, start, end);
        var header = new HttpHeaders();
        var res = toBytes(sensor, parameterMetadata, start, end);
        try {
            res = zipBytes(filename, res);
            header.setContentType(new MediaType("application", "zip"));
            header.setContentDisposition(ContentDisposition.attachment().filename(filename + ".zip").build());
        } catch (IOException err) {
            err.printStackTrace();
            header.setContentType(media);
            header.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        }

        return new ResponseEntity<>(new ByteArrayResource(res), header, HttpStatus.OK);
    }

    interface RowMapperPartial<T> {
        T apply(Date captureDate, Object value);
    }

    interface RowMapperComplete<T> {
        T apply(long idParameterMetadata, String name, String unit, Date captureDate, Object value);
    }
}
