package com.mycompany.app;

import java.util.ArrayList;
import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Maija Visala
 */

public class JsonParser{

    /**
     * Parse any file that contains JSON string to a MyJsonObject.
     * 
     * @param file the file that contais JSON in string format.
     * @return MyJsonObject that has been parsed from the file content.
     */
    public static MyJsonObject jsonFileToMyJsonObject(File file){
        String checkable = readJsonFileToString(file);
        MyJsonObject jsonObject = new MyJsonObject();

        jsonObject = parseSingleJsonObject(checkable);

        return jsonObject;
    }

    /**
     * Parse any JSON string to a MyJsonObject.
     * 
     * @param JSON in string format.
     * @return MyJsonObject that has been parsed from the JSON string.
     */
    public static MyJsonObject StringToMyJsonObject(String jsonString){
        MyJsonObject jsonObject = new MyJsonObject();

        jsonObject = parseSingleJsonObject(jsonString);

        return jsonObject;
    }

    /**
     * Read a file that contains JSON string.
     * 
     * @param file the file that contais JSON in string format.
     * @return the contents of the file in one string variable.
     */
    public static String readJsonFileToString(File file){
        ArrayList<String> rowsArray = new ArrayList<>();
        String jSonRows = "";

        try {
            Files.lines(file.toPath()).forEach(row -> rowsArray.add(row.trim()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        for(String i: rowsArray){
            jSonRows += i;
        }

        return jSonRows;
    }

    /**
    * Parse one key-value-pair from jSon string to MyJsonObject. 
    * 
    * @param jSonObjectString JSON object in string format.
    * @return MyJsonObject that has been parsed from the JSON string.
    */
    public static MyJsonObject parseSingleJsonObject(String jSonObjectString){
        MyJsonObject jsonObject = new MyJsonObject();

        String tempString = jSonObjectString.trim();        //tempString is the string that is yet to be checked
        String key = "";
        if(tempString.startsWith("{")){
            tempString = tempString.substring(1).trim();
            while(tempString.length() > 1){
                if(!tempString.startsWith("\"")){
                    System.out.println("Error at:" + tempString.substring(0, Math.min(30, tempString.length()-1)));
                    return jsonObject;
                }
                key = tempString.substring(1, tempString.substring(1).indexOf("\"") + 1);
                //Removing key and quotation marks from tempString
                tempString = tempString.substring(key.length() + 2).trim();

                if(!tempString.startsWith(":")){
                    System.out.println("Error at:" + tempString.substring(0, Math.min(30, tempString.length()-1)));
                    return jsonObject;
                }
                //Removing colon
                tempString = tempString.substring(1).trim();

                if(tempString.charAt(0) == '{'){                //if value is another jSon object
                    String jsonString = findSubstringFromString(tempString, '{');
                    MyJsonObject innerObject = parseSingleJsonObject(jsonString);
                    jsonObject.add(key, innerObject);
                    tempString = tempString.substring(jsonString.length());
                }else if(tempString.charAt(0) == '['){          //if value is json array
                    MyJsonArray array = new MyJsonArray();
                    String jsonString = findSubstringFromString(tempString, '[').trim();
                    array = parseJsonArray(jsonString);
                    jsonObject.add(key, array);
                    tempString = tempString.substring(jsonString.length());
                }else if(tempString.charAt(0) == '"'){          //if value is string
                    String value = tempString.substring(1, tempString.substring(1).indexOf("\"") + 1);
                    //Removing value and quotation marks from string
                    tempString = tempString.substring(value.length() + 2).trim();
                    jsonObject.add(key, value);
                }else{                                      //if value is number, null or boolean
                    int index = tempString.indexOf("}");
                    if(tempString.contains(",") && tempString.indexOf(",") < index){
                        index = tempString.indexOf(",");
                    }
                    String value = tempString.substring(0, index);
                    if(value.equals("null")){
                        jsonObject.add(key, null);
                    }else if(value.equals("true")){
                        jsonObject.add(key, true);
                    }else if(value.equals("false")){
                        jsonObject.add(key, false);
                    }else{
                        int valueInt = 0;
                        try{
                            valueInt = Integer.parseInt(value);
                        }catch (NumberFormatException e) {}
                        jsonObject.add(key, valueInt);
                    }
                    tempString = tempString.substring(value.length()).trim();
                }

                tempString = tempString.trim();
                if(tempString.charAt(0) == ','){
                    tempString = tempString.substring(1).trim();
                }
            }
        }

        tempString = "";
        return jsonObject;
    }

    /**
    * Find a logical piece of JSON string (the whole object or array).
    *
    * JSON string can contain multiple nested JSON objects or JSON arrays. This method keeps track of "levels"
    * of nested objects and returns a logical piece of JSON string.
    * 
    * @param jsonString a piece of JSON string, where the object or array is looked for.
    * @param startChar either '{' or '['. Defines whether it is a JSON object or a JSON array that is looked for 
    * @return a string that contains one logical piece of the JSON string (the whole object or array).
    */
    public static String findSubstringFromString(String jsonString, char startChar){
        char endChar = '}';
        if (startChar == '['){
            endChar = ']';
        }
        String temp = jsonString.substring(1);
        //Check how many JSON objects or JSON arrays there are inside one another
        int howManyBrackets = 1;
        int index = 0;
        
        //Check what is the index of the last ending bracket ('}' or ']')
        while(howManyBrackets > 0 && index <= temp.length()){
            if(temp.charAt(index) == startChar){
                howManyBrackets ++;
            }else if(temp.charAt(index) == endChar){
                howManyBrackets--;
            }
            index++;
        }
        return jsonString.substring(0, index + 1);
    }

    /**
    * Parse one JSON array. Find JSON objects from inside of the array, parse them
    * and add to a new MyJsonArray.
    * 
    * @param jSonObjectString a piece of JSON string that contains the JSON array.
    * @return MyJsonArray object that contains parsed MyJsonObjects.
    */
    public static MyJsonArray parseJsonArray(String jsonString){
        MyJsonArray returnable = new MyJsonArray();

        String tempString = jsonString.trim();
        // String key = "";
        if(tempString.startsWith("[")){
            tempString = tempString.substring(1).trim();
            if(tempString.startsWith("]")){

            }else if(!tempString.startsWith("{")){
                System.out.println("Error at:" + tempString.substring(0, Math.min(30, tempString.length()-1)));
            }else{
                while(tempString.length() > 1){
                    String innerJsonString = findSubstringFromString(tempString, '{');
                    MyJsonObject innerObject = parseSingleJsonObject(innerJsonString);
                    returnable.add(innerObject);                
                    tempString = tempString.substring(innerJsonString.length());

                    tempString = tempString.trim();
                    if(tempString.charAt(0) == ','){
                        tempString = tempString.substring(1).trim();
                    }
                }
            }
        }
        tempString = "";        
        return returnable;
    }

    public String parseMyJsonObjectToString(MyJsonObject obj, int level){
        String spaces = "";
        int counter = obj.getSize();
        for (int i = 0; i < level; i++){
            spaces += "  ";
        }
        String returnable = "{\n";
        for (String key: obj.getKeys()){
            //Handling key (string)
            returnable += spaces + "\"" + key + "\": ";
            //Handling value
            Object value = obj.getValue(key);
            if(value instanceof String){
                String stringValue = value.toString();
                returnable += "\"" + stringValue + "\"";
            }else if(value instanceof Integer){
                int intValue = Integer.parseInt(value.toString());
                returnable += intValue;
            }else if(value instanceof MyJsonObject){
                MyJsonObject innerObject = (MyJsonObject) value;
                returnable += parseMyJsonObjectToString(innerObject, level + 1);
            }else if(value instanceof MyJsonArray){
                MyJsonArray array = (MyJsonArray) value;
                int arrayCounter = array.getLength();
                boolean empty = false;
                if(arrayCounter > 0){
                    returnable += "[\n";
                }else{
                    empty = true;
                    returnable += "[";
                }
                for(int i = 0; i < array.getLength(); i++){
                    MyJsonObject jsonObject = array.get(i);
                    returnable += spaces + "  ";
                    returnable += parseMyJsonObjectToString(jsonObject, level + 2);
                    arrayCounter--;
                    if(arrayCounter > 0){
                        returnable += ",\n";
                    }
                }
                if(!empty){
                    returnable += "\n" + spaces + "]";
                }else{
                    returnable += "]";
                }
                
            }else if (value == null){
                returnable += "null";
            }else{
                returnable += value.toString();
            }

            if(counter > 1){
                returnable += ",\n";
            }else{
                returnable += "\n" + spaces.substring(2) + "}";
            }

            counter --;
            
        }

        return returnable;
    }

}