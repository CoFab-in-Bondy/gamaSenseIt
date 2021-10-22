package ummisco.gamaSenseIt.springServer.services.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service("JSONResponse")
public class FormatterJSON extends Formatter {

    public FormatterJSON() {
        super("json");
    }

    public <T> ResponseEntity<Resource> build(Iterable<T> list) throws Exception  {
        var out = new ByteArrayOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, list);
        var in = new ByteArrayResource(out.toByteArray());
        return new ResponseEntity<>(in, header(MediaType.APPLICATION_JSON), HttpStatus.OK);
    }
}