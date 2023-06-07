package ru.mrnightfury.queuemanager.repository.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "favourite")
public class FavouriteEntity {
    @PrimaryKey(autoGenerate = true)
    Integer id;

    @ColumnInfo(name = "queueId")
    String queueId;

    public FavouriteEntity(String queueId) {
        this.queueId = queueId;
    }

    public Integer getId() {
        return id;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
