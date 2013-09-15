import java.util.Scanner;
import java.io.*;

public class Main
{
    //debug
    static boolean debug = false;
    static boolean superdebug = false;
    
    //mandatory
    static Scanner keyboard = new Scanner(System.in);
    
    public static void main (String[] args)
    {
        debug(2);
        if (Input.guiActive() == 0)
        {
            Input.getInfo();
        }
        Output.end();
        while (true)
        {
            System.out.println(".osu file successfully created!\n");
            Input.tryAgain(0);
        }
    }
    
    public static void debug(int level)
    {
        if (level >= 1)
        {
            debug = true;
            Input.debug = true;
            Output.debug = true;
            Parser.debug = true;
            //TempoSpeeder.debug = true;
            if (level >= 2)
            {
                superdebug = true;
                Input.superdebug = true;
                Output.superdebug = true;
                Parser.superdebug = true;
                //TempoSpeeder.superdebug = true;
            }
        }
        return;
    }
}