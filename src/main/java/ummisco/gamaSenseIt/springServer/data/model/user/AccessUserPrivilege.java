package ummisco.gamaSenseIt.springServer.data.model.user;

public enum AccessUserPrivilege {
    MANAGE, // peut ajouter des capteurs et utilisateur mais pas les modifiers
    VIEW  // peut lire les capteurs
}
