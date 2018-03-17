package io.zipcoder;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.NaN;

public class ItemParser {

    private Matcher matcher;
    private Pattern pattern;

    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException{
            String name = changeAny0(rawItem);
            Double price = findPrice(rawItem);
            String type = findType(rawItem);
            String expiration =findExpirationDate(rawItem);

            return new Item(name,price,type,expiration);
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    private String findName(String text) {
        pattern = Pattern.compile("(?<=([Nn][Aa][Mm][Ee][^A-Za-z])).*?(?=[^A-Za-z0])");
        matcher = pattern.matcher(text);

        if (matcher.find()) {
               return matcher.group().substring(0, 1).toUpperCase() + matcher.group().substring(1).toLowerCase();
            } else {
            return null;
        }
    }
    private String changeAny0(String text) {
        pattern = Pattern.compile("[0]");
        matcher = pattern.matcher(findName(text));
        String noZeros = matcher.replaceAll("o");
        return noZeros;
    }

    private Double findPrice(String text){
        pattern = Pattern.compile("(?<=([Pp][Rr][Ii][Cc][Ee][^A-Za-z])).*?(?=[^0-9.])");
        matcher = pattern.matcher(text);
        if(matcher.find()) {
            return Double.parseDouble(matcher.group());
        } else {
            return null;

        }
    }

    private String findType(String text){
        pattern = Pattern.compile("(?<=([Tt][Yy][Pp][Ee][^A-Za-z])).*?(?=[^A-Za-z0])");
        matcher = pattern.matcher(text);
        if(matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }

    }

    private String findExpirationDate(String text){
        pattern = Pattern.compile("(?<=([Ee][Xx][Pp][Ii][Rr][Aa][Tt][Ii][Oo][Nn][^A-Za-z]))(.)+");
        matcher = pattern.matcher(text);
        if(matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }

    }

}
