package com.mycompany.app;

import org.junit.Test;
import org.junit.Assert;
import com.mycompany.app.JsonParser;
import com.mycompany.app.MyJsonObject;
import com.mycompany.app.MyJsonArray;

/**
* Tests for class JsonParser
*/
public class JsonParserTest {
    
    JsonParser parser = new JsonParser();

    //JSON string for testing. Contains values of type string, integer,JSON object,
    //JSON array, boolean and null
    String testString1 = "{\"firstName\": \"John\",\"lastName\": \"Smith\",\"isAlive\": true}";

   @Test
   public void jSonObjectContainsValues() {
        MyJsonObject test1 = parser.StringToMyJsonObject(testString1);

        Assert.assertTrue("Object should contain key \"city\"", test1.hasKey("lastName"));
   }
}