package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

class MyJsonArray{
    List<MyJsonObject> list;
    int printLevel = 1;

    public MyJsonArray(){
        list = new ArrayList<>();
    }

    public void add(MyJsonObject object){
        list.add(object);
    }

    public int getLength(){
        return list.size();
    }

    public MyJsonObject get(int index){
        return list.get(index);
    }

}