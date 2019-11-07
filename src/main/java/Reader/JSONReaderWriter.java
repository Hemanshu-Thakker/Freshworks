package Reader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReaderWriter {

    private JSONParser jsonParser;
    private JSONObject jsonobject;
    private List<JSONObject> JSONList;
    private List<JSONObject> values;
    private String folderPath;
    private String inputFileBaseName;
    private String outputFileBaseName;
    private Integer size;
    private int input_counter;
    private int output_counter;
    private String source;

    public JSONReaderWriter(String folderPath, String inputFileBaseName, String outputFileBaseName, Integer size){
        this.folderPath=folderPath;
        this.inputFileBaseName=inputFileBaseName;
        this.outputFileBaseName=outputFileBaseName;
        this.size=size;
        jsonParser= new JSONParser();
        jsonobject = new JSONObject();
        JSONList= new ArrayList<>();
        values = new ArrayList<>();
        input_counter=1;
        output_counter=1;
        source="";
    }

    public boolean extractJSONFiles(){
        source=folderPath+inputFileBaseName+input_counter+".json";
        File f=new File(source);
        String key="";
        /*
        If the File doesn't even exist return false.
         */
        if(!f.exists()){
            return false;
        }

        while(f.exists()){
            try (FileReader reader = new FileReader(source)) {
                Object obj = jsonParser.parse(reader);
                JSONObject jsonob= (JSONObject) obj;
//                Map<String, JSONObject> map = (Map<String,JSONObject>)jsonob.getMap();

                JSONList.add(jsonob);
//                System.out.println(obj);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            input_counter++;
            source=folderPath+inputFileBaseName+input_counter+".json";
            f = new File(source);
        }
//        printJSONList();

        for(JSONObject temp : JSONList){
            JSONArray array=((JSONArray) temp.get("strikers"));
            for(Object i:array){
                JSONObject ob= (JSONObject)i;
                values.add(ob);
            }
        }
        jsonobject.put("striker",values);
        System.out.println(jsonobject);

        //write
        try (FileWriter file = new FileWriter(folderPath+outputFileBaseName+output_counter+".json")) {
            file.write(jsonobject.toJSONString());
            file.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

//    public boolean mergeJSONFiles(){
//        if(JSONList.isEmpty()){
//            System.out.println("***");
//            return false;
//        }
//
//        return true;
//    }

    public void printJSONList(){
        for(JSONObject obj:JSONList){
            System.out.println(obj);
            System.out.println();
        }
    }
}
