package fr.ummisco.gamasenseit.server.data.model.user;

public class AccessDTO {
    private String name;
    private AccessPrivilege privilege;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(AccessPrivilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "AccessDTO{" +
                "name='" + name + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}
