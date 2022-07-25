package com.example.finalproject;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Dog {
    String name;
    String ownerName;
    String details;
    String walkEvery;
    Date nextWalkDate;

    public Dog(String name, String ownerName, String nextWalkDate, String details, String walkEvery){
        this.name = name;
        this.ownerName= ownerName;
        this.details = details;
        this.walkEvery = walkEvery;
        String date[] = nextWalkDate.split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);;
        this.nextWalkDate = new Date(year, month, day);
    }

    public Dog() {
        // TODO Auto-generated constructor stub
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        this.ownerName = ownerName;
    }

    public String getWalkEvery() {
        return walkEvery;
    }

    public void setWalkEvery(String walkEvery) {
        this.walkEvery = walkEvery;
    }

    public Date getNextWalkDate() {
        return nextWalkDate;
    }

    public void setNextWalkDate(Date nextWalkDate) {
        this.nextWalkDate = nextWalkDate;
    }

    public void setDetails(String details){
        this.details = details;
    }
    public String getDetails(){
        return this.details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return Objects.equals(name, dog.name) &&
                Objects.equals(ownerName, dog.ownerName) &&
                Objects.equals(details, dog.details) &&
                Objects.equals(walkEvery, dog.walkEvery) &&
                Objects.equals(nextWalkDate, dog.nextWalkDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ownerName, details, walkEvery, nextWalkDate);
    }

    /*
    currentTime == is the time in the clock right now.
    difference == is the time in days (convert from MILLISECONDS) of the days which passed since the time we has to take the dog to a walk.

    If difference equal or big then the WalkEvery filed so we need to take the dog to a walk but we didn't done it on time
    --> soo.. send a SMS to user(even if the app off) && mark this dog row in light orange.
     */
    public boolean checkWalkNeeded() {
        Calendar cl = Calendar.getInstance();
        Date currentTime = new Date(cl.getWeekYear(), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));
        long difference = TimeUnit.DAYS.convert(currentTime.getTime() - getNextWalkDate().getTime(), TimeUnit.MILLISECONDS);
        if(returnWalkEveryAsInt() <= difference){
            return true;
        }
        return false;
    }

    public int returnWalkEveryAsInt(){
        switch (getWalkEvery()){
            case "1 Day":
                return 1;
            case "2 Days":
                return 2;
            case "3 Days":
                return 3;
            case "4 Days":
                return 4;
            case "5 Days":
                return 5;
            case "6 Days":
                return 6;
            case "Week":
                return 7;
        }
        return 0;
    }

    @Override
    public String toString() {
        String str = "";
        return str = "Name: " + name + "\n"  + "Owner`s name:" + ownerName + "\n" + "Details: " + details + "\n" + "Walk Every: " + walkEvery + "\n" + "Next walk: " + nextWalkDate.getDate()+"/"+ nextWalkDate.getMonth()+"/"+ nextWalkDate.getYear();
    }
  
}
