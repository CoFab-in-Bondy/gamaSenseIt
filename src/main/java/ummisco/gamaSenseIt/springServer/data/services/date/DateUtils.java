package ummisco.gamaSenseIt.springServer.data.services.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final TimeZone utc = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat prettyFormat = new SimpleDateFormat("EEEEE d MMMMM yyyy à H'h'mm");
    private static final SimpleDateFormat compactFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat javascriptFormat = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss Z");
    static {
        prettyFormat.setTimeZone(utc);
        compactFormat.setTimeZone(utc);
        javascriptFormat.setTimeZone(utc);
    }

    public static Date parseCompact(String string) {
        try {
            return compactFormat.parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatCompact(Date date) {
        return compactFormat.format(date);
    }

    public static String formatPretty(Date date) {
        return prettyFormat.format(date);
    }

    public static String formatJavascript(Date date) {
        return javascriptFormat.format(date);
    }

    public static Date parseJavascript(String string) throws ParseException {
        return javascriptFormat.parse(string);
    }
}
