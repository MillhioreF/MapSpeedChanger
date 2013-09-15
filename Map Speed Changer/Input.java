import java.util.Scanner;
import java.io.*;

public class Input
{
    //debug
    static boolean debug = false;
    static boolean superdebug = false;
    
    //options
    public static boolean askForAudio = false;
    public static boolean convertMP3 = false;
    public static boolean loop = false;
    public static boolean displayOldBPM = false;
    
    //mandatory
    static Scanner keyboard = new Scanner(System.in);
    public static File osu = null;
    static double speedpercent = 0;
    static boolean loser = false;
    static double speedQuotient = 0; // 200% would be 50, 50% would be 200)
    static double startSpeed = 0;
    static double endSpeed = 0;
    public static double newBPM = 0;
    public static double oldBPM = 0;
    public static boolean isBPM = false;
    public static double bpmValue = 0;
    public static String mp3name = "";
    public static String speedString = "";
    public static Scanner osufile = null; // initializes the scanner
    public static String filename = "";
    public static String filenameP = ""; // p = permanent
    public static String filenameN = "";
    public static String query = "";
    static Scanner settings = null;
    
    public static void reInitialize()
    {
        speedpercent = 0;
        loser = false;
        speedQuotient = 0; // 200% would be 50, 50% would be 200)
        startSpeed = 0;
        endSpeed = 0;
        newBPM = 0;
        oldBPM = 0;
        isBPM = false;
        bpmValue = 0;
        mp3name = "";
        String speedString = "";
        String filename = "";
        String filenameP = ""; // p = permanent
        String filenameN = "";
        query = "";
        getInfo();
    }
    
    public static int guiActive()
    {
        try
        {
            settings = new Scanner("MSC.ini");
        }
        catch (Exception e)
        {
            return 0;
        }
        if (debug == true)
        {
            System.out.println("Settings found!");
        }
        parseGuiSettings();
        return 1;
    }
    
    public static void parseGuiSettings()
    {
        settings.nextLine(); // throw away header ;)
        filename = settings.nextLine();
        filenameP = filename;
        makeScanner();
        speedString = settings.nextLine();
        settings.close();
        Output.clearTempFile();
        Parser.parseFile();
    }
    
    public static void makeScanner()
    {
        try 
        {
            osu = new File(filenameN);
            if (superdebug == true)
            {
                System.out.println("Attempting scanner creation for " + filenameN);
            }
            osufile = new Scanner(osu); // creates the file scanner
            if (debug == true)
            {
                System.out.println("Made a scanner for " + filenameN);
            }
        }
        catch (FileNotFoundException e) // needed in case of error
        {
            System.out.print ("Couldn't find the .osu file!\n"); // stops the program if no file is found
            tryAgain(1);
        }
        return;
    }
    
    public static void getInfo()
    {
        if (askForAudio == true)
        {
            System.out.print("Enter the name of the modified mp3: ");
            mp3name = keyboard.nextLine();
        }
        System.out.print ("Enter the name of the .osu file: ");
        filename = keyboard.nextLine();
        filenameP = filename;
        if (superdebug == true)
        {
            System.out.println("Received " + filename);
        }
        filename = filename.trim();
        if (superdebug == true)
        {
            System.out.println("Trimmed to " + filename);
        }
        if (filename.indexOf("[") != -1 && filename.indexOf ("]") != -1)
        {
            Parser.betterName = true;
        }
        if (filename.lastIndexOf(".") != filename.length() - 4 || filename.lastIndexOf(".") < 0)
        {
            filename = filename + ".osu";
        }
        if (superdebug == true)
        {
            System.out.println("Searching for " + filename);
        }
        filenameN = filename;
        makeScanner();
        System.out.print("Enter the desired speed of the song.\n" +
            "You can specify the percentage of the original speed (e.g. 150%)\n" +
            "Or you can specify the the desired new BPM (e.g. 180)\n\n" +
            "Input: ");
        speedString = keyboard.nextLine();
        processSpeedString();
        Output.clearOldFile();
        Parser.parseFile();
        return;
    }
    
    public static void didntFollowInstructions()
    {
        System.out.println("People who don't follow instructions make Millhiore sad. :(");
        tryAgain(1);
    }
    
    public static void processSpeedString()
    {
        if (speedString.indexOf("%") != -1)
        {
            speedString = speedString.replaceAll("%","");
            speedpercent = Double.valueOf(speedString);
            isBPM = false;
            Parser.getData();
        }
        try 
        {
            if (Double.valueOf(speedString) == 0)
            {
                System.out.println("Dude, no. You're making me divide by 0. Not cool.");
                System.exit(0);
            }
        }
        catch (NumberFormatException e)
        {
            didntFollowInstructions();
        }
        try 
        {
            if (Double.valueOf(speedString) < 0)
            {
                System.out.println("Dude, no. If you want a negative BPM, you can go hit yourself with a brick.");
                System.exit(0);
            }
        }
        catch (NumberFormatException e)
        {
            didntFollowInstructions();
        }
        if (speedString.compareTo(speedString.toUpperCase()) != speedString.compareTo(speedString.toLowerCase()))
        {
            didntFollowInstructions();
        }
        else if (speedString.length() != 0)
        {
            isBPM = true;
            newBPM = Double.valueOf(speedString);
            if (superdebug == true)
            {
                System.out.println("Time to get data!");
            }
            Parser.getData();
            oldBPM = (60000 / bpmValue);
            speedpercent = (newBPM / oldBPM * 100.0);
            if (debug == true)
            {
                System.out.println("speedpercent is " + speedpercent + ", old BPM is " + oldBPM);
            }
            if (displayOldBPM == true)
            {
                Parser.showConversion();
            }
            /*if (convertMP3 == true)
            {
                TempoSpeeder.importFile(mp3name);
            }*/
        }
    }
    
    public static void tryAgain(int failed)
    {
        if (failed == 1)
        {
            System.out.println("Convert failed!");
        }
        if (loop == true)
        {
            System.out.print("Would you like to convert another file? (Y/N) ");
            query = keyboard.nextLine();
            if (query.substring(0,1).toUpperCase().equals("Y"))
            {
                reInitialize();
            }
            else if (query.substring(0,1).toUpperCase().equals("N"))
            {
                System.out.println("Bai :D");
                System.exit(0);
            }
            else
            {
                System.out.println("Well if you're not going to follow instructions then I'm quitting anyway! :<");
                System.exit(0);
            }
        }
        else
        {
            if (debug == true)
            {
                System.out.println("Loop disabled; press any key to exit.");
                speedString = keyboard.nextLine();
            }
            System.exit(0);
        }
        return;
    }
}