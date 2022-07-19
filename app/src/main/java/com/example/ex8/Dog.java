package com.example.ex8;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Dog {
    String name;
    String ownerName;
    String details;
    String walkEvery;
    Date walkDate;

    public Dog(String name, String ownerName, String walkDate, String details, String walkEvery){
        this.name = name;
        this.ownerName= ownerName;
        this.details = details;
        this.walkEvery = walkEvery;
        String date[] = walkDate.split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);;
        this.walkDate = new Date(year, month, day);
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

    public Date getWalkDate() {
        return walkDate;
    }

    public void setWalkDate(Date walkDate) {
        this.walkDate = walkDate;
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
                Objects.equals(walkDate, dog.walkDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ownerName, details, walkEvery, walkDate);
    }

    public boolean checkWalkNeeded() {
        Calendar cl = Calendar.getInstance();
        Date currentTime = new Date(cl.getWeekYear(), cl.get(Calendar.MONTH) + 1, cl.get(Calendar.DAY_OF_MONTH));
        long difference = TimeUnit.DAYS.convert(currentTime.getTime() - getWalkDate().getTime(), TimeUnit.MILLISECONDS);
        if(returnWaterEveryAsInt() <= difference){
            return true;
        }
        return false;
    }

    public int returnWaterEveryAsInt(){
        switch (getWalkEvery()){
//            case "Day":
//                return 1;
//            case "2 Days":
//                return 2;
//            case "5 Days":
//                return 5;
//            case "Week":
//                return 7;
//            case "2 Weeks":
//                return 14;
//            case "Month":
//                return 30;
            //{"8 hours", "12 hours", "1 Day", "2 Day", "3 Day", "4 Day"};
            case "8 hours":
                return 8;
            case "12 hours":
                return 12;
            case "1 Day":
                return 1;
            case "2 Day":
                return 2;
            case "3 Day":
                return 3;
            case "4 Day":
                return 4;

        }
        return 0;
    }

    @Override
    public String toString() {
        String str = "";
        str = "Name: " + name + "\n"  + "Owner`s name:" + "\n" + ownerName + "Details: " + details + "\n" + "Walk Every: " + walkEvery + "\n" + "Last walk: " + walkDate.getDate()+"/"+ walkDate.getMonth()+"/"+ walkDate.getYear();
        return str;
    }

}
