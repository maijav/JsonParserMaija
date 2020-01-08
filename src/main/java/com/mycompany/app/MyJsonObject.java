package com.mycompany.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class MyJsonObject{
    Map<String, Object> content;
    int printLevel = 1;
    public MyJsonObject(){
        content = new HashMap<>();
    }

    public void add (String key, Object value){
        content.put(key, value);
    }

    public Object getValue(String key){
        return content.get(key);
    }

    public boolean hasKey(String key){
        if(content.containsKey(key)){
            return true;
        }else{
            return false;
        }
    }

    public Set<String> getKeys(){
        return content.keySet();
    }

    public int getSize(){
        return content.size();
    }

}