package Reader;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import Exception.InputTypeException;

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
    private long size;
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

    public boolean extractJSONFiles() {

        source=folderPath+inputFileBaseName+input_counter+".json";
        File f=new File(source);
        ArrayList<String> key= new ArrayList<>();

        /* If the File doesn't even exist return false */
        if(!f.exists()) {
            return false;
        }

        while(f.exists()) {
            try (FileReader reader = new FileReader(source)) {
                Object obj = jsonParser.parse(reader);
                JSONObject jsonob= (JSONObject) obj;
                Object obarr[]=jsonob.keySet().toArray();
                key.add((String) obarr[0]);
                JSONList.add(jsonob);
            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            catch(IOException e) {
                e.printStackTrace();
                return false;
            }
            catch(ParseException e) {
                e.printStackTrace();
                return false;
            }
            input_counter++;
            source=folderPath+inputFileBaseName+input_counter+".json";
            f = new File(source);
        }

        if(similerKeys(key)) {
            for (JSONObject temp : JSONList) {
                JSONArray array = ((JSONArray) temp.get(key.get(0)));
                for (Object i : array) {
                    JSONObject ob = (JSONObject) i;
                    values.add(ob);
                }
            }
            jsonobject.put(key.get(0), values);
            System.out.println(jsonobject);
        }
        else{
            try{
                throw new InputTypeException("Cannot merge because different objects in file");
            }
            catch(Exception e){
                System.out.println(e);
            }
        }

        //write
        File file= new File(folderPath+outputFileBaseName+output_counter+".json");
        try(BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            if (file.length() < size) {
                writer.write(jsonobject.toJSONString());
                size-=file.length();
                writer.flush();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean similerKeys(ArrayList<String> key){
        Set<String> set= new HashSet<>();
        set.addAll(key);
        if(set.size()==1){
            return true;
        }
        return false;
    }
}
