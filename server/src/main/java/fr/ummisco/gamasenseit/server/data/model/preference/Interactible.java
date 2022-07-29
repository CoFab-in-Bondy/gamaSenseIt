package fr.ummisco.gamasenseit.server.data.model.preference;


import java.util.Date;
import java.util.Set;

public abstract class Interactible {

    public abstract Set<? extends InteractBase> getInteracts();

    public Date getDateInteractWithUser(long userId) {
        return getInteracts()
                .stream()
                .filter(interact -> interact.getUserId() == userId)
                .findAny()
                .map(InteractBase::getDate)
                .orElse(new Date(0L));
    }
}
