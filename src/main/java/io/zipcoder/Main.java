package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;


public class Main {



    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        ItemParser ip = new ItemParser();
        Main main = new Main();
        String output = (new Main()).readRawDataToString();
        ArrayList<String> test = ip.parseRawDataIntoStringArray(output);
        System.out.println(ip.parseStringIntoItem(test.get(24).toString()));
        //System.out.println(ip.parseRawDataIntoStringArray(main.readRawDataToString()));

        // TODO: parse the data in output into items, and display to console.
    }
}
