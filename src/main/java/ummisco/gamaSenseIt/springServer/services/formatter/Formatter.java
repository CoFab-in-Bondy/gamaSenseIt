package ummisco.gamaSenseIt.springServer.services.formatter;

import org.springframework.core.io.Resource;
import org.springframework.http.*;

public abstract class Formatter {

	private final String name;

	public Formatter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract <T> ResponseEntity<Resource> build(Iterable<T> list, String filename) throws Exception;

	public <T> ResponseEntity<Resource> format(Iterable<T> list, String filename) {
		try {
			return this.build(list, filename);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static HttpHeaders header(MediaType type, String filename) {
		var header = new HttpHeaders();
		header.setContentType(type);
		header.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
		return header;
	}
}
