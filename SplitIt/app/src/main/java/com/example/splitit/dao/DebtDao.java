package com.example.splitit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.splitit.model.Debt;

import java.util.List;

@Dao
public interface DebtDao {

    @Query("Select * from Debt")
    List<Debt> getAllDebts();

    @Query("Select * from Debt where debtId = :id limit 1")
    Debt getDebtById(long id);

    @Insert
    void insertDebt(Debt debt);

    @Delete
    void deleteDebt(Debt debt);
}
