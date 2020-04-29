package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.splitit.model.Group;

import java.util.List;

@Dao
public interface GroupDao {
    @Query("Select * from Groups")
    List<Group> getAllGroups();

    @Query("Select * from Groups where groupId = :id limit 1")
    Group getGroupById(long id);

    @Insert
    void insertGroup(Group group);

    @Delete
    void deleteGroup(Group group);
}
