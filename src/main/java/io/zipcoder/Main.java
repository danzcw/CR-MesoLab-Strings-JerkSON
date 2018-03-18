package io.zipcoder;

import org.apache.commons.io.IOUtils;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Map;


public class Main {



    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{

        ItemParser ip = new ItemParser();

        String output = (new Main()).readRawDataToString();

        System.out.println(ip.buildOutput());



            }
        }


        // TODO: parse the data in output into items, and display to console.


