package com.omegaauto.shurik.mobilesklad.container;

import java.util.ArrayList;
import java.util.List;

/////////////////////////////////////////////////

//НЕ ИСПОЛЬЗОВАТЬ

//////////////////////////////////////////////

public class ContainerListLast {

    private static final int COUNT_CONTAINERS = 8;
    private List<Container> containerList;
    static ContainerListLast instance;

    private ContainerListLast() {
        containerList = new ArrayList<Container>();
        addContainer(new Container());
    }

    static public ContainerListLast getInstance(){
        if (instance == null){
            instance = new ContainerListLast();
        }
        return instance;
    }

    private int findByNumber(String number){

        for (Container currentContainer: containerList) {
            if (currentContainer.getNumber().equals(number)){
                return containerList.indexOf(currentContainer);
            }
        }
        return -1;
    }

    private boolean isExist(String trip_number){
        for (Container currentContainer: containerList) {
            if (currentContainer.getTrip_number().equals(trip_number)){
                return true;
            }
        }
        return false;
    }

    private void deleteItemByNumber(String tripNumber){
        int containerPosition = findByNumber(tripNumber);
        if (containerPosition >= 0){
            containerList.remove(containerPosition);
        }
//        if (containerList.size() < 1){
//            addContainer(new Container());
//        }
    }

    private void insertFirst(Container newContainer){
        containerList.add(0, newContainer);
    }

    public void addContainer(Container newContainer){
        deleteItemByNumber(newContainer.getTrip_number());
        insertFirst(newContainer);
        if (containerList.size() > COUNT_CONTAINERS) {
            containerList.remove(COUNT_CONTAINERS);
        }
    }

    public Container getContainer(int index){
        if (index < containerList.size()){
            return containerList.get(index);
        } else {
            return null;
        }
    }

    public int size(){
        return containerList.size() - 1;
    }

}
