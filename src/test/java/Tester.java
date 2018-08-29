import com.tweakbit.controller.DataParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class Tester {
    public Tester() {
    }

    @Test
    public void testGetTimeFromString(){
        DataParser dataParser = new DataParser();
        TreeMap<String, String[]> expectedList = createListWillExpectedDateTexts();

        for(Map.Entry<String,String[]> map: expectedList.entrySet()){
            String actualTextDate = map.getKey();
            String[] expected = map.getValue();
            Assert.assertArrayEquals("test parse of date", expected, dataParser.getTime(actualTextDate));
        }


    }

    private TreeMap<String, String[]> createListWillExpectedDateTexts() {
        TreeMap<String, String[]> list = new TreeMap<>();

        String[] expected = new String[4];
        expected[0] = "GMT";
        expected[1] = "+0800";
        expected[2] = String.valueOf (14*60*60 + 32*60 + 9.0);
        expected[3] = "1534836729";

        String[] expected1 = new String[4];
        expected1[0] = "Server";
        expected1[1] = "null";
        expected1[2] = String.valueOf (10*60*60 + 29*60 + 40.0);
        expected1[3] = "1534822180";

        String[] expected2 = new String[4];
        expected2[0] = "EDT";
        expected2[1] = "null";
        expected2[2] = String.valueOf (19*60*60 + 8*60 + 42.0);
        expected2[3] = "1534853322";

        list.put("Tue Aug 21 2018 14:32:09 GMT+0800", expected);
        list.put("Tuesday August 21 2018 10:29:40 Server", expected1);
        list.put("Tue Aug 21 2018 19:08:42 EDT", expected2);

        return list;
    }
}
