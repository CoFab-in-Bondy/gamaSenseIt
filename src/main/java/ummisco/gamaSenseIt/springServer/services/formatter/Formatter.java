package ummisco.gamaSenseIt.springServer.services.formatter;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public abstract class Formatter {

	private final String name;

	public Formatter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract <T> ResponseEntity<Resource> build(Iterable<T> list) throws Exception;

	public <T> ResponseEntity<Resource> format(Iterable<T> list) {
		try {
			return this.build(list);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static HttpHeaders header(MediaType type) {
		var header = new HttpHeaders();
		header.setContentType(type);
		return header;
	}
}
