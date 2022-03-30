package ummisco.gamaSenseIt.springServer.services.export;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import ummisco.gamaSenseIt.springServer.data.model.sensor.ParameterMetadata;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class Export {

    protected final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final String ext;
    private final MediaType media;

    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    @Autowired
    private SecurityUtils securityUtils;


    public Export(@NotNull MediaType media, @NotNull String ext) {
        this.ext = ext;
        this.media = media;
    }

    private String buildFilename(
            @NotNull Sensor sensor,
            @NotNull String type,
            @Nullable ParameterMetadata parameterMetadata,
            @Nullable Date start,
            @Nullable Date end
    ) {
        return securityUtils.sanitizeFilename(
                (parameterMetadata == null ? "parameters" : parameterMetadata.getName())
                + "-"
                + sensor.getName()
                + (start != null || end != null
                ? "-"
                + (start == null ? "X" : dateFormat.format(start))
                + "-"
                + (end == null ? "X" : dateFormat.format(end))
                : "")
        ) + "." + type;
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
}
