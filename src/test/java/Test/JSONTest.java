package Test;

import Reader.JSONReaderWriter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTest {

    JSONReaderWriter jsonreader;
    String filePath;
    String iBase;
    String oBase;
    Integer size;


    @Before
    public void setUp() throws Exception {
        filePath="/home/hemanshu/Desktop/";
        iBase="data";
        oBase="merge";
        size= 2000;
        jsonreader = new JSONReaderWriter(filePath,iBase,oBase,size);
    }

    @Test
    public void extractJSONFiles_test() {
        assertTrue(jsonreader.extractJSONFiles());
    }
}