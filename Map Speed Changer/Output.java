import java.util.Scanner;
import java.io.*;

public class Output
{
    //debug
    static boolean debug = false;
    static boolean superdebug = false;
    
    //mandatory
    static PrintWriter out = null;
    static File file = new File("New " + Input.filenameN);
    
    public static void writeFile (String line)
    {
        try 
        {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        }
        catch (IOException e)
        {
            System.out.println("wtf");
        }
        out.write(line);
        out.write(13);
        out.write(10);
        out.flush();
    }
    
    public static void clearOldFile()
    {
        file.delete();
    }
    
    public static void rehook()
    {
        if (debug == true)
        {
            System.out.println("Rehooking...");
        }
        Input.osufile.close();
        if (Parser.betterName == true)
        {
            file = new File(Input.filename);
            if (debug == true)
            {
                System.out.println("Made a new file: " + Input.filename);
            }
        }
        Input.makeScanner();
    }
    
    public static void end()
    {
        if (out != null)
        {
            out.close();
        }
    }
}