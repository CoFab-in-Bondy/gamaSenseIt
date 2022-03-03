package ummisco.gamaSenseIt.springServer.data.services.access;

import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.user.AccessUserPrivilege;
import ummisco.gamaSenseIt.springServer.data.model.user.User;

import java.util.*;

public class AccessSearch extends ArrayList<HashMap<String, Object>> {

    public AccessSearch() {
        super();
    }

    public boolean put(User user, boolean present, AccessUserPrivilege privilege) {
        return this.add(new HashMap<>(){{
            put("user", user);
            put("present", present);
            put("privilege", privilege);
        }});
    }

    public boolean put(Sensor sensor, boolean present) {
        return this.add(new HashMap<>(){{
            put("sensor", sensor);
            put("present", present);
        }});
    }
}
