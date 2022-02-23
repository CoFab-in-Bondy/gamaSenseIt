package ummisco.gamaSenseIt.springServer.data.services.geo;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Service
public class GeoService {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private DatabaseReader dbReader;

    public GeoService() {
        try {
            var database = new ClassPathResource("GeoLite2-City.mmdb").getInputStream();
            this.dbReader = new DatabaseReader.Builder(database).fileMode(Reader.FileMode.MEMORY) .build();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't load GeoLite2-City.mmdb all return of geolocate gonna be empty");
        }
    }

    private InetAddress getSafeIP(HttpServletRequest request) throws UnknownHostException {
        return InetAddress.getByName(request.getRemoteAddr());
    }

    public InetAddress getIpFromRequest(HttpServletRequest request) throws UnknownHostException {
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                try {
                    return InetAddress.getByName(ipList.split(",")[0]);
                } catch (UnknownHostException ignored) {}
            }
        }
        return getSafeIP(request);
    }


    public Optional<CityResponse> geolocate(HttpServletRequest request) {
        InetAddress ip;
        try {
            ip = getIpFromRequest(request);
        } catch (UnknownHostException ignore) {
            try {
                ip = InetAddress.getLocalHost();
            } catch (UnknownHostException ignore2) {
                return Optional.empty();
            }
        }
        try {
            return Optional.of(dbReader.city(ip));
        } catch (IOException | GeoIp2Exception exception) {
            return Optional.empty();
        }
    }
}
