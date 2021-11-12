package ummisco.gamaSenseIt.springServer.services.formatter;

import java.io.*;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service("CSVResponse")
public class FormatterCSV extends Formatter {

    public FormatterCSV() {
        super("csv");
    }

    @Override
    public <T> ResponseEntity<Resource> build(Iterable<T> list, String filename) throws Exception{
        var writer = new StringWriter();
        var beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        beanToCsv.write(list.iterator());
        var in = new ByteArrayResource(writer.toString().getBytes());
        return new ResponseEntity<>(in, header(new MediaType("text", "csv"), filename), HttpStatus.OK);
    }
}
