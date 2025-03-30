package com.example.d308mobileapplication.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "excursions",
    foreignKeys = {
    @ForeignKey(
    entity = Vacation.class,
    parentColumns = {"vacationID"},
    childColumns = {"vacationID"},
    onDelete = ForeignKey.CASCADE)
    }
)
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionTitle;
    private String excursionDate;
    private int vacationID;

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public Excursion(int excursionID, String excursionTitle, String excursionDate, int vacationID) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationID= vacationID;
    }

    public int getVacationID() {
        return vacationID;
    }
    public void setVacationID(int vacationID){
        this.vacationID = vacationID;
    }
}
