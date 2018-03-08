package com.example.asus.oap;

public class Message {
    String name;
    String massage;
    String key;

public Message(){

}

    public Message(String name, String massage, String key) {
        this.name = name;
        this.massage = massage;
        this.key= key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {

        return name;
    }

    public String getMassage() {
        return massage;
    }

    public String getKey() {
        return key;
    }
}
