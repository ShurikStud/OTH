package com.omegaauto.shurik.mobilesklad.container;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;

public class NumberListLast {

    private static final int COUNT_NUMBERS = 8;
    private String currentNumber;
    private List<String> numberList;
    static NumberListLast instance;

    private NumberListLast() {
        numberList = new ArrayList<String>();
        numberList.add("");
        numberList.add("");
        numberList.add("");
        numberList.add("");
        numberList.add("");
        numberList.add("");
        numberList.add("");
        numberList.add("");
        currentNumber = "";
    }

    static public NumberListLast getInstance(){
        if (instance == null){
            instance = new NumberListLast();
        }
        return instance;
    }

    private int findByNumber(String number){
        if (number == null && number.isEmpty()){
            return -1;
        }
        return numberList.indexOf(number);
    }

    private void addNumber(String number){
        numberList.remove(number);
        numberList.add(0, number);
    }

    public String getNumber(int index){
        if (index < numberList.size()){
            return numberList.get(index);
        } else {
            return "";
        }
    }

    public int size(){
        return numberList.size();
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(String currentNumber) {

        if (!currentNumber.isEmpty()){
            this.currentNumber = currentNumber;
            addNumber(this.currentNumber);
        }
        if (numberList.size() > COUNT_NUMBERS){
            numberList.remove(COUNT_NUMBERS);
        }


        /*if (!this.currentNumber.isEmpty()){
            addNumber(this.currentNumber);
        }
        if (numberList.size() > COUNT_NUMBERS){
            numberList.remove(COUNT_NUMBERS);
        }
        this.currentNumber = currentNumber;*/
    }
}
