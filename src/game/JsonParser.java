package game;
import org.json.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonParser
{
    private String parsedFile;

    public JsonParser(String path)
    {
        try
        {
            this.parsedFile = readFile(path, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public int getMutants(String field)
    {
        int number = 0;
        try
        {
            JSONObject json = new JSONObject(this.parsedFile);
            number = json.getInt(field);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return number;
    }

}
