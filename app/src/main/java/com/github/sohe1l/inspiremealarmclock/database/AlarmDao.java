package com.github.sohe1l.inspiremealarmclock.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.github.sohe1l.inspiremealarmclock.model.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM alarm")
    LiveData<List<Alarm>> getAll();


    @Query("SELECT * FROM alarm")
    List<Alarm> getAllAsList();

    @Insert
    void insert(Alarm alarm);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("SELECT * FROM alarm where id = :id")
    LiveData<Alarm> getMovie(int id);



}
