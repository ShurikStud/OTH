package com.omegaauto.shurik.mobilesklad.container;

import com.omegaauto.shurik.mobilesklad.ObservablesContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.OnSettingsContainerRedoListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//public final class ContainerPropertiesSettings implements ObservablesContainerPropertiesSettings {
public final class ContainerPropertiesSettings {
    // класс настроек порядка и видимости реквизитов КОНТЕЙНЕРа (Container)
    // Элемент в позиции 0 содержит последний номер контейнера, который искали.

    private static final int PROPERTIES_VERSION = 11;
    int current_version;

    //private List<OnSettingsContainerRedoListener> observers; // слушатели

    private List<Property> properties; // все возможные свойства контейнера
    private int separator; // номер в списке, меньше которго - видимые свойства, больше или равно - невидимые
    static ContainerPropertiesSettings instance;


    private ContainerPropertiesSettings(){

    }

    static public ContainerPropertiesSettings getInstance() {

        if (instance == null){
            instance = new ContainerPropertiesSettings();
            instance.setCurrentVersion();
            //instance.observers = new LinkedList<>();
        }

        return instance;
    }

    public void setProperties(ContainerPropertiesSettings input){

        if (input.current_version != current_version) {
            initDefault();
            return;
        }

        if (properties == null) {
            properties = new ArrayList<Property>();
        } else {
            properties.clear();
        }

        for (Property property: input.getProperties()) {
            properties.add(property);
        }

        separator = input.getSeparator();

    }

    public void initDefault(){

        if (properties == null) {
            properties = new ArrayList<Property>();
        } else {
            properties.clear();
        }

        properties.add(new Property("Driver_name", "ФИО водителя", null));
        properties.add(new Property("Vehicle_name", "№ транспортного средства", null));
        properties.add(new Property("ZayavkaTEP_highway_date", "дата Заявки ТЭП (магистральной)", null));
        properties.add(new Property("ZayavkaTEP_highway_number", "№ Заявки ТЭП (магистральной)", null));
        properties.add(new Property("ZayavkaTEP_number", "№ Заявки ТЭП", null));
        properties.add(new Property("Route_name", "Маршрут", null));
        properties.add(new Property("Trip_number", "№ рейса", null));
        properties.add(new Property("Nn", "№ п/п", "/"));
        properties.add(new Property("SEPARATOR", "НЕ ОТОБРАЖАЮТСЯ", null));
        properties.add(new Property("Partner_address", "Адрес Клиента", null));
        properties.add(new Property("Partner_name", "Клиент", null));
        properties.add(new Property("Partner_phone", "тел. Клиента", null));
        properties.add(new Property("Invoice_numbers", "№№ накладных", null));
        properties.add(new Property("Type_pack", "Тип упаковки", null));
        properties.add(new Property("Weight", "Вес, кг.", "/"));
        properties.add(new Property("Amount_goods", "Количество грузов", "/"));
        properties.add(new Property("Number", "№ тарного места", "из"));
        properties.add(new Property("Volume", "Объем тарного места, л.", "/"));
        //separator = properties.size();
        separator = 8;
        setCurrentVersion();
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<Property> getVisibleProperties(){
        List<Property> visibleProperties = new ArrayList<Property>();

        for (int i = 0; i < separator; i++){
            visibleProperties.add(properties.get(i));
        }
        return visibleProperties;
    }

    public int getSeparator(){
        return separator;
    }

    public int getSize(){
        return properties.size();
    }

    public Property getProperty(int index){
        return properties.get(index);
    }

    public int getIndex(Property property){
        return properties.indexOf(property);
    }

    public void movePropertyTo(int index, Property element){
        int currentIndex = properties.indexOf(element);
        if (currentIndex < separator && index >= separator){
            --separator;
        } else if (currentIndex > separator && index <= separator){
            ++separator;
        }
        properties.remove(element);
        properties.add(index, element);

    }

    public class Property{
        String name;
        String description;
        String delimiter;

        public Property(String name, String description, String delimiter) {
            this.name = name;
            this.description = description;
            this.delimiter = delimiter;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }
    }

    private void setCurrentVersion(){
        current_version = PROPERTIES_VERSION;
    }

 /*   @Override
    public void setOnSettingsContainerRedoListener(OnSettingsContainerRedoListener observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(OnSettingsContainerRedoListener observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (OnSettingsContainerRedoListener observer : observers)
            observer.onSettingsContainerRedo();
    }*/
}
