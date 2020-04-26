package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.splitit.model.Action;
import com.example.splitit.model.Debt;

import java.util.List;

@Dao
public interface ActionDao {

    @Query("Select * from Action")
    List<Action> getAllActions();

    @Query("Select * from Action where action_id = :id limit 1")
    Action getActionById(long id);

    @Insert
    void insertAction(Action action);

    @Delete
    void deleteAction(Action action);
}
