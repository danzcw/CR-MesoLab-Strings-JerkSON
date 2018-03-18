package io.zipcoder;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.NaN;

public class ItemParser {

    private Matcher matcher;
    private Pattern pattern;
    public int count = 0;
    Main main = new Main();
    private Map<String, ArrayList<Item>> groceryList = new HashMap<String, ArrayList<Item>>();

    public ArrayList<String> parseRawDataIntoStringArray(String rawData) {
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException {
        String name = changeAny0(rawItem);
        Double price = findPrice(rawItem);
        String type = findType(rawItem);
        String expiration = findExpirationDate(rawItem);

        if (changeAny0(rawItem) == null || findPrice(rawItem) == 0.0) {
            throw new ItemParseException();
        }

        return new Item(name, price, type, expiration);
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem) {
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString) {
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    private String findName(String text) {
        pattern = Pattern.compile("(?<=([Nn][Aa][Mm][Ee][^A-Za-z])).*?(?=[^A-Za-z0])");
        matcher = pattern.matcher(text);

        if (matcher.find()) {
            String group = matcher.group();
            if (group.length() > 0) {
                return group.substring(0, 1).toUpperCase() + group.substring(1).toLowerCase();
            } else {
                return null;
            }
        }
        return null;
    }

    private String changeAny0(String text) throws ItemParseException {
        try {
            pattern = Pattern.compile("[0]");
            matcher = pattern.matcher(findName(text));
            String noZeros = matcher.replaceAll("o");
            return noZeros;
        } catch (Exception e) {
            throw new ItemParseException();
        }
    }

    private Double findPrice(String text) throws ItemParseException{

        pattern = Pattern.compile("(?<=([Pp][Rr][Ii][Cc][Ee][^A-Za-z])).*?(?=[^0-9.])");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group());
            } catch (Exception e) {
                throw new ItemParseException();
            }
        } else {
            return 0.0;
        }
    }

    private String findType(String text) {
        pattern = Pattern.compile("(?<=([Tt][Yy][Pp][Ee][^A-Za-z])).*?(?=[^A-Za-z0])");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }

    }

    private String findExpirationDate(String text) {
        pattern = Pattern.compile("(?<=([Ee][Xx][Pp][Ii][Rr][Aa][Tt][Ii][Oo][Nn][^A-Za-z]))(.)+([^#])");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }

    }

    public Map<String, ArrayList<Item>> getMap() throws Exception {
        Main main = new Main();

        ArrayList<String> listOfItems = parseRawDataIntoStringArray(main.readRawDataToString());
        for (String anItem : listOfItems) {

            try {
                Item newItem = parseStringIntoItem(anItem);
                if (!groceryList.containsKey(newItem.getName())) {
                    ArrayList<Item> myItem = new ArrayList<Item>();
                    myItem.add(newItem);
                    groceryList.put(newItem.getName(), myItem);
                } else {
                    groceryList.get(newItem.getName()).add(newItem);
                }

            } catch (ItemParseException e) {
                count++;
            }
        }
        return groceryList;

    }
    public ArrayList<Double> uniquePrices(Map.Entry<String, ArrayList<Item>> item) {
        ArrayList<Double> uniquePrice = new ArrayList<Double>();
        for (int i = 0; i < item.getValue().size() ; i++) {
            if(!uniquePrice.contains(item.getValue().get(i).getPrice())) {
                uniquePrice.add(item.getValue().get(i).getPrice());
            }
        }
        return uniquePrice;
    }

    public int countPrices(ArrayList<Item> item, Double price) {
        int count =0;
        for (int i = 0; i <item.size() ; i++) {
            if(item.get(i).getPrice().equals(price)) {
                count++;
            }
        } return count;
    }

    public int getKeyCount(String key) {

        ArrayList<Item> numberOfItems = groceryList.get(key);
        return numberOfItems.size();
    }

    public String buildOutput() throws Exception {

        groceryList = getMap();
        StringBuilder displayOutput = new StringBuilder();
        for(Map.Entry<String, ArrayList<Item>> item : groceryList.entrySet()) {
            displayOutput.append("\n" + String.format("%-5s%10s%15s%2d%5s",
                    "name:", item.getKey(), "seen: ", item.getValue().size()," times" ));
            displayOutput.append("\n" + String.format("%15s%3s%5s", "===============", "\t\t\t", "===============") + "\n");

            ArrayList<Double> uniquePricesToName = uniquePrices(item);

            for (int i = 0; i < uniquePricesToName.size() ; i++) {
                displayOutput.append(String.format("%-11s%.2f%15s%2d%5s", "Price:", uniquePricesToName.get(i),
                        "seen: ", countPrices(item.getValue(), uniquePricesToName.get(i)), " times") + "\n");
                displayOutput.append(String.format("%15s%3s%5s", "---------------", "\t\t\t", "---------------") + "\n");
            }
          }
          displayOutput.append("\n" + String.format("%-20s%10s%2d%5s", "Errors", "seen: ", count, " times"));

          return displayOutput.toString();
        }
}