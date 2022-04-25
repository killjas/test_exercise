package example;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestConvert {
    String testString = "01000400A832925602000300047102" +
            "03000B008E8E8E2090AEACA0E8AAA004001D000B00070084EB" +
            "E0AEAAAEAB0C000200204E0D00020000020E000200409C";
    String testString1 = "0100000002000300047102" +
            "03000B008E8E8E2090AEACA0E8AAA004001D000B00070084EB" +
            "E0AEAAAEAB0C000200204E0D00020000020E000200409C";

    String testString2 = "0100000002000300047102" +
            "03000B008E8E8E2090AEACA0E8AAA0040019000B00070084EB" +
            "E0AEAAAEAB0C000200204E0D00020000020E000000";

    String testString3 = "01000400A832925602000300047102" +
            "03000B008E8E8E2090AEACA0E8AAA004003E000B00070084EB" +
            "E0AEAAAEAB0C000200204E0D00020000020E000200409C0D0003000292010C00020010270B000A008EA4A8AD84A2A092E0A80E000200004B";

    @Test
    public void testConvertToJson() throws IOException {

        byte[] byteArray = new byte[testString.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(testString.substring(index, index + 2), 16);
            byteArray[i] = (byte) j;
        }
        JSONObject jsonObject = new JSONObject();
        InputStream fileInputStream = new ByteArrayInputStream(byteArray);
        boolean flag = true;
        while (flag) {
            flag = ConvertBinaryToJson.setTextToJson(fileInputStream, jsonObject);
        }
        Assert.assertEquals(jsonObject.toString(), "{\"dateTime\":\"2016-01-10T10:30:00\",\"orderNumber\":160004,\"items\":[{\"quantity\":2,\"price\":20000,\"name\":\"Дырокол\",\"sum\":40000}],\"customerName\":\"ООО Ромашка\"}");
    }

    @Test
    public void testConvertToJson1() throws IOException {

        byte[] byteArray = new byte[testString1.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(testString1.substring(index, index + 2), 16);
            byteArray[i] = (byte) j;
        }
        JSONObject jsonObject = new JSONObject();
        InputStream fileInputStream = new ByteArrayInputStream(byteArray);
        boolean flag = true;
        while (flag) {
            flag = ConvertBinaryToJson.setTextToJson(fileInputStream, jsonObject);
        }
        Assert.assertEquals(jsonObject.toString(), "{\"orderNumber\":160004,\"items\":[{\"quantity\":2,\"price\":20000,\"name\":\"Дырокол\",\"sum\":40000}],\"customerName\":\"ООО Ромашка\"}");
    }

    @Test
    public void testConvertToJson2() throws IOException {

        byte[] byteArray = new byte[testString2.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(testString2.substring(index, index + 2), 16);
            byteArray[i] = (byte) j;
        }
        JSONObject jsonObject = new JSONObject();
        InputStream fileInputStream = new ByteArrayInputStream(byteArray);
        boolean flag = true;
        while (flag) {
            flag = ConvertBinaryToJson.setTextToJson(fileInputStream, jsonObject);
        }
        Assert.assertEquals(jsonObject.toString(), "{\"orderNumber\":160004,\"items\":[{\"quantity\":2,\"price\":20000,\"name\":\"Дырокол\"}],\"customerName\":\"ООО Ромашка\"}");
    }

    @Test
    public void testConvertToJson3() throws IOException {

        byte[] byteArray = new byte[testString3.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(testString3.substring(index, index + 2), 16);
            byteArray[i] = (byte) j;
        }
        JSONObject jsonObject = new JSONObject();
        InputStream fileInputStream = new ByteArrayInputStream(byteArray);
        boolean flag = true;
        while (flag) {
            flag = ConvertBinaryToJson.setTextToJson(fileInputStream, jsonObject);
        }
        Assert.assertEquals(jsonObject.toString(), "{\"dateTime\":\"2016-01-10T10:30:00\",\"orderNumber\":160004,\"items\":[{\"quantity\":2,\"price\":20000,\"name\":\"Дырокол\",\"sum\":40000},{\"quantity\":1.92,\"price\":10000,\"name\":\"ОдинДваТри\",\"sum\":19200}],\"customerName\":\"ООО Ромашка\"}");
    }
}
