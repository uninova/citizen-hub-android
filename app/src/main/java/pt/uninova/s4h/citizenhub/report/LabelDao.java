package pt.uninova.s4h.citizenhub.report;

import androidx.room.Query;

import java.util.List;

public interface LabelDao {

    @Query("SELECT * FROM user")
    List<Integer> getAll();

}
