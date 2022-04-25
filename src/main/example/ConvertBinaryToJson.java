package example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConvertBinaryToJson {
    public static void main(String[] args) throws IOException {
        String binFile = args[0] + ".bin";
        String jsonFile = args[1] + ".json";
        InputStream fileInputStream = new FileInputStream(binFile);
        JSONObject json = new JSONObject();
        boolean flag = true;
        while (flag) {
            flag = setTextToJson(fileInputStream, json);
        }
        fileInputStream.close();
        File file = new File(jsonFile);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json.toString());
        fileWriter.close();

    }

    static Map<String, String> map = Map.of("0001", "dateTime", "0002", "orderNumber",
            "0003", "customerName", "0004", "items", "000B", "name",
            "000C", "price", "000D", "quantity",
            "000E", "sum");

    public static boolean setTextToJson(InputStream fileInputStream, JSONObject jsonObject) throws IOException {
        String tag = toNormalString(reverse(fileInputStream.readNBytes(2)));
        String lenght = toNormalString(reverse(fileInputStream.readNBytes(2)));
        tag = map.get(tag);
        long parseLenght = Long.parseLong(lenght, 16);
        if (tag.equals("items") && parseLenght == 0) {
            return false;
        }
        if (parseLenght == 0) {
            return true;
        }
        switch (tag) {
            case "dateTime": {
                String value = toNormalString(reverse(fileInputStream.readNBytes((int) parseLenght)));
                Date date = new Date(Long.parseLong(value, 16) * 1000L);
                SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                myDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                jsonObject.put(tag, myDate.format(date));
                break;
            }
            case "orderNumber": {
                String value = toNormalString(reverse(fileInputStream.readNBytes((int) parseLenght)));
                jsonObject.put(tag, Long.parseLong(value, 16));
                break;
            }
            case "customerName": {
                String value = toNormalString(fileInputStream.readNBytes((int) parseLenght));
                jsonObject.put(tag, new String(HexFormat.of().parseHex(value), Charset.forName("CP866")));
                break;
            }
            case "items": {
                JSONArray jsonItems = new JSONArray();
                while (fileInputStream.available() > 0) {
                    JSONObject items = new JSONObject();
                    while (setJsonItem(fileInputStream, items)) ;
                    jsonItems.put(items);
                }
                jsonObject.put("items", jsonItems);
                return false;
            }
        }
        return true;
    }

    private static boolean setJsonItem(InputStream fileInputStream, JSONObject jsonObject) throws IOException {
        String tag = toNormalString(reverse(fileInputStream.readNBytes(2)));
        String lenght = toNormalString(reverse(fileInputStream.readNBytes(2)));
        tag = map.get(tag);
        long parseLenght = Long.parseLong(lenght, 16);
        if (tag.equals("sum") && parseLenght == 0) {
            return false;
        }
        if (parseLenght == 0) {
            return true;
        }
        switch (tag) {
            case "name": {
                String value = toNormalString(fileInputStream.readNBytes((int) parseLenght));
                jsonObject.put(tag, new String(HexFormat.of().parseHex(value), Charset.forName("CP866")));
                break;
            }
            case "price": {
                String value = toNormalString(reverse(fileInputStream.readNBytes((int) parseLenght)));
                jsonObject.put(tag, Long.parseLong(value, 16));
                break;
            }
            case "quantity": {
                String value = toNormalString(reverse(fileInputStream.readNBytes((int) parseLenght)));
                long count = Long.parseLong(value.substring(value.length() - 2), 16);
                value = value.substring(0, value.length() - 2);
                jsonObject.put(tag, Double.parseDouble(value) / Math.pow(10, count));
                break;
            }
            case "sum": {
                String value = toNormalString(reverse(fileInputStream.readNBytes((int) parseLenght)));
                jsonObject.put(tag, Long.parseLong(value, 16));
                return false;
            }
        }
        return true;

    }

    private static byte[] reverse(byte[] array) {
        if (array == null) {
            return null;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static String toNormalString(byte[] data) {
        String[] strings = new String[data.length];
        String string = "";

        for (int i = 0; i < data.length; i++) {
            strings[i] = (String.format("%02X", data[i]));
        }

        for (int i = 0; i < strings.length; i++) {
            string = string.concat(strings[i]);
        }

        return string;
    }
}
