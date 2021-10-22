package ummisco.gamaSenseIt.springServer.services.formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FormattedResponseFactory")
public class FormattedResponseFactory {

    // TODO make a map
    @Autowired
    private List<Formatter> formats;

    @Autowired
    private FormatterJSON formatDefault;

    public Formatter resolve(String name) {
        if (name != null)
            for (var format : formats)
                if (format.getName().equals(name))
                    return format;
        return formatDefault;
    }

    public <T> ResponseEntity<Resource> format(String name, Iterable<T> list) {
        return resolve(name).format(list);
    }
}
