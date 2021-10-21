package ummisco.gamaSenseIt.springServer.services.csvFormatter;

import java.util.List;

public interface IFormatter {
	public <T> String format(List<T> list);
}
