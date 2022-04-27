package ummisco.gamaSenseIt.springServer.data.model.preference;


import java.util.Date;

public interface InteractBase {
    Date getDate();
    void setDate(Date date);
    Long getUserId();
    void setUserId(Long userId);
}
