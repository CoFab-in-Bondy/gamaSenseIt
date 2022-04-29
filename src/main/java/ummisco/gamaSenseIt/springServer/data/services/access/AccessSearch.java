package ummisco.gamaSenseIt.springServer.data.services.access;

import org.springframework.util.comparator.Comparators;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.preference.Interactible;
import ummisco.gamaSenseIt.springServer.data.model.sensor.Sensor;
import ummisco.gamaSenseIt.springServer.data.model.user.AccessUserPrivilege;
import ummisco.gamaSenseIt.springServer.data.model.user.User;

import java.util.*;
import java.util.function.Function;

public class AccessSearch {

    private final ArrayList<Node> json;

    public AccessSearch() {
        super();
        json = new ArrayList<>();
    }

    public boolean put(User user, boolean present, AccessUserPrivilege privilege) {
        return json.add(new Node(){{
            put("user", user);
            put("present", present);
            put("privilege", privilege);
        }});
    }

    public boolean put(Sensor sensor, boolean present) {
        return json.add(new Node(){{
            put("sensor", sensor);
            put("present", present);
        }});
    }

    public static <R> R computeNode(Node node, Function<User, R> userCase, Function<Sensor, R> sensorCase) {
        if (node.get("user") instanceof User)
            return userCase.apply((User) node.get("user"));
        if (node.get("sensor") instanceof Sensor)
            return sensorCase.apply((Sensor) node.get("sensor"));
        return null;
    }

    private static Date getDate(long userId, Node userOrSensor) {
        Object obj = userOrSensor.get("user");
        if (obj == null)
            obj = userOrSensor.get("sensor");
        return ((Interactible)obj).getDateInteractWithUser(userId);
    }

    private static String getName(Node userOrSensor) {
        return computeNode(userOrSensor, u->u.getFirstname()+ ' ' +u.getLastname(), Sensor::getName);
    }

    public List<Node> toNodeForUser(User currentUser) {
        Comparator<Node> cmp = Comparator.comparing(o-> getDate(currentUser.getId(), o));
        json.sort(cmp.reversed().thenComparing(AccessSearch::getName));
        return json;
    }
}
