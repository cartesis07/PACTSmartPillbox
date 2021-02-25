package com.example.application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PillsWrapper {

    public PillsWrapper(int wheelNumber) {
        this.wheelNumber = wheelNumber;
        this.isExtended = false; //Don't ask
        this.listeDesPrises = new ArrayList<Prise>();

        // Partie déboguage TODO : à virer
        this.pillName = "Default";
        this.numberOfPillsInWheel = 20;
        this.daysUntilEnd = 34;

    }

//TODO : instanciation (ou non si juste modification) avec le bouton "Ajouter une prise" de la page 2.
    // permet de ne considérer que le cas de modification dans la 3e page en utilisant les getters et setters.

    //INFORMATIONS MEDICAMENT =================================================================
    private String pillName;
    private int numberOfPillsInWheel;
    private int wheelNumber;
    private SimpleDateFormat endDate;
    private int daysUntilEnd, daysUntilRefill;
    private ArrayList<Prise> listeDesPrises;

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getNumberOfPillsInWheel() {
        return numberOfPillsInWheel;
    }

    public void setNumberOfPillsInWheel(int numberOfPillsInWheel) {
        this.numberOfPillsInWheel = numberOfPillsInWheel;
        calculateDaysUntilRefill();
    }

    public int getWheelNumber() {
        return wheelNumber;
    }

    public SimpleDateFormat getEndDate() {
        return endDate;
    }

    public void setEndDate(SimpleDateFormat endDate) {
        this.endDate = endDate;
        //TODO :set daysUntilEnd
    }

    public int getDaysUntilEnd() {
        return daysUntilEnd;
    }

    public int getDaysUntilRefill() {
        return daysUntilRefill;
    }

    public int getNumberOfPrises() {
        return listeDesPrises.size();
    }

    public String getPrisesText() {
        String result = "" ;
        for (int i=0; i<getNumberOfPrises(); i++) {
            Prise prise = listeDesPrises.get(i);
            result += prise.getHour() + " | " + prise.getNumberOfPills() + " médicaments \n" ;
        }
        return result;
    }

    public ArrayList<String> getPillsText() {

        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < getNumberOfPrises(); i++) {
            Prise prise = listeDesPrises.get(i);

            String priseText= "add" + getPillName() + prise.getHour() + prise.getNumberOfPills()  ;
            result.add(priseText);
        }

        return result;
    }

    public void addPrise(String time, int numberOfPills) {
        listeDesPrises.add(new Prise(time, numberOfPills));
        calculateDaysUntilRefill();
    }

    public void removePrise(int position) {
        listeDesPrises.remove(position);
        calculateDaysUntilRefill();
    }

    public void modifPrise(int position, String hour, int numberOfPills) { // XXX : does this work ?
        Prise prise = listeDesPrises.get(position);
        prise.hour = hour;
        prise.numberOfPills = numberOfPills;
        calculateDaysUntilRefill();
    }

    public class Prise{

        public String hour;
        public int numberOfPills;

        public Prise(String heure, int numberOfPills) {
            this.hour = heure;
            this.numberOfPills = numberOfPills;
        }
        public String getHour(){
            return hour;
        }
        public int getNumberOfPills(){
            return numberOfPills;
        }
    }

    private void calculateDaysUntilRefill() {
        int numberOfPillsByDay = 0;
        for (int i=0; i<getNumberOfPrises(); i++) {
            numberOfPillsByDay += listeDesPrises.get(i).getNumberOfPills();
        }
        if (numberOfPillsByDay != 0) daysUntilRefill = numberOfPillsInWheel/numberOfPillsByDay;
        else daysUntilRefill = 666;
    }


    //RECYCLER VIEW ===========================================================================
    public boolean isExtended;

    public void toggleExtension() {
        if (isExtended) {
            isExtended = false;
        } else {
            isExtended = true;
        }
    }

}
