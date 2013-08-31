import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;
import java.nio.channels.FileChannel;


public class Parser
{
    //debug
    static boolean debug = false;
    static boolean superdebug = false;
    
    //mandatory
    static boolean betterName = false;
    String coolFilename = "";
    static DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    static DecimalFormat df = new DecimalFormat("#.#############", dfs);
    static DecimalFormat dfTrunc = new DecimalFormat("#", dfs);
    
    public static void getData()
    {
        String currentLine = "";
        String timingLine = "";
        int startPlace = 0;
        boolean isUnicode = true;
        int endPlace = 0;
        String diffName = "";
        double bpm = 0;
        
        //Output.rehook();
        while (Input.osufile.hasNext())
        {
            isUnicode = false;
            currentLine = Input.osufile.nextLine();
            if (superdebug == true)
            {
                System.out.println("Current line is " + currentLine);
            }
            if (betterName == true && currentLine.indexOf("V") == 0 && currentLine.indexOf("n") == 6)
            {
                startPlace = currentLine.indexOf(":");
                diffName = currentLine.substring(startPlace + 1, currentLine.length());
                if (superdebug == true)
                {
                    System.out.println("diffName gotten: " + diffName);
                }
                if (Input.isBPM == false)
                {
                    diffName = diffName + " " + df.format(Input.speedpercent) + "% Speed";
                }
                else
                {
                    diffName = diffName + " " + df.format(Input.newBPM) + "BPM";
                }
                if (superdebug == true)
                {
                    System.out.println("diffName adjusted, now " + diffName);
                }
                Input.filename = Input.filename.substring(0, Input.filename.lastIndexOf("[") + 1) + diffName + Input.filename.substring(Input.filename.lastIndexOf("]"), Input.filename.length());
                if (debug == true)
                {
                    System.out.println("Filename is now " + Input.filename);
                }
            }
            if (currentLine.indexOf("[") == 0 && currentLine.indexOf("]") == 13)
            {
                timingLine = Input.osufile.nextLine();
                if (debug == true)
                {
                    System.out.println("Found first timing line!");
                }
                startPlace = timingLine.indexOf(",");
                endPlace = timingLine.indexOf(",", startPlace + 1);
                bpm = Double.valueOf(timingLine.substring(startPlace + 1, endPlace));
            }
        }
        if (debug == true)
        {
            System.out.println("Got BPM, returning " + bpm);
        }
        if (isUnicode == true)
        {
            System.out.println("This file contains unicode!\nI haven't figured out how to make Java not break with unicode yet,\nbut rest assured fixing this is my top priority.");
            System.exit(0); //comment out later to resume tests
            try
            {
                fixFile(Input.osu, new File("temp " + Input.filename));
                Input.filename = "temp " + Input.filename;
                if (debug == true)
                {
                    System.out.println("File fixed!");
                }
                isUnicode = false;
                Output.rehook();
                Parser.getData();
            }
            catch (IOException e)
            {
                if (debug == true)
                {
                    System.out.println("Fixing unicode file failed!");
                }
            }
        }
        Output.rehook();
        Input.bpmValue = bpm;
        return;
    }
    
    public static void parseFile()
    {
        boolean foundObjects = false;
        boolean timingFound = false;
        boolean breaksFound = false;
        String currentObject = "";
        String timingLine = "";
        int breakStart = 0;
        int breakEnd = 0;
        int currentPlace = 0;
        String sloser = "";
        int startPlace = 0;
        int endPlace = 0;
        int start2 = 0;
        int end2 = 0;
        double lastNum = 0.0;
        String lastNumS = "";
        String currentLine = "";
        double modNumS = 0.0;
        boolean isMania = false;
        String modString = "";
        String modStringS = "";
        double modNum = 0.0;
        
        System.out.println("\nWorking...");
        while (Input.osufile.hasNext())
        {
            currentLine = Input.osufile.nextLine();
            if (foundObjects != true)
            {
                sloser = currentLine;
                if (currentLine.indexOf("P") == 0 && currentLine.indexOf("T") == 7)
                {
                    modNum = Double.valueOf(currentLine.substring(13, currentLine.length()));
                    if (modNum > 0)
                    {
                        modNum = modNum / ((Input.speedpercent) / 100.0);
                        if (debug == true)
                        {
                            System.out.println("Positive previewtime!");
                        }
                    }
                    modString = dfTrunc.format(modNum);
                    Output.writeFile("PreviewTime: " + modString);
                }
                else if (currentLine.indexOf("M") == 0 && currentLine.indexOf("o") == 1 && currentLine.indexOf("3") != -1)
                {
                    isMania = true;
                    if (debug == true)
                    {
                        System.out.println("WOW IT'S MANIA");
                    }
                    Output.writeFile("Mode: 3");
                }
                else if (currentLine.indexOf("A") == 0 && currentLine.indexOf("F") == 5)
                {
                    if (Input.askForAudio == true)
                    {
                        Output.writeFile("AudioFilename: " + Input.mp3name);
                    }
                    else
                    {
                        Output.writeFile(sloser);
                    }
                } 
                else if (currentLine.indexOf("V") == 0 && currentLine.indexOf("n") == 6)
                {
                    if (Input.isBPM == false)
                    {
                        Output.writeFile(sloser + " " + df.format(Input.speedpercent) + "% Speed");
                    }
                    else
                    {
                        Output.writeFile(sloser + " " + df.format(Input.newBPM) + "BPM");
                    }
                }
                else if (currentLine.indexOf("/") == 0 && currentLine.indexOf("P") == 8)
                {
                    breaksFound = true;
                    if (debug == true)
                    {
                        System.out.println("Found breaks!");
                    }
                    Output.writeFile(sloser);
                }
                else if (currentLine.indexOf("[") == 0 && currentLine.indexOf("]") == 13)
                {
                    timingFound = true;
                    if (debug == true)
                    {
                        System.out.println("Found timing sections!");
                    }
                    Output.writeFile(sloser);
                }
                else if (currentLine.indexOf("[") == 0 && currentLine.indexOf("s") == 10)
                {
                    foundObjects = true;
                    if (debug == true)
                    {
                        System.out.println("Found hitobjects!");
                    }
                    Output.writeFile(sloser);
                }
                else if (breaksFound == true)
                {
                    if (currentLine.indexOf("/") == 0 && currentLine.indexOf("S") == 2)
                    {
                        breaksFound = false;
                        if (debug == true)
                        {
                            System.out.println("No more breaks...");
                        }
                        Output.writeFile(sloser);
                    }
                    else
                    {
                        breakStart = currentLine.indexOf(",");
                        breakEnd = currentLine.indexOf(",", breakStart + 1);
                        modNum = Double.valueOf(currentLine.substring(breakStart + 1, breakEnd));
                        if (superdebug == true)
                        {
                            System.out.println("modNum collected from break: " + modNum);
                        }
                        modNumS = Double.valueOf(currentLine.substring(breakEnd + 1, currentLine.length()));
                        if (superdebug == true)
                        {
                            System.out.println("modNum conversion math: " + modNum + " / ((" + Input.speedpercent + ") / " + 100.0 + ")");
                        }
                        modNum = modNum / ((Input.speedpercent) / 100.0);
                        if (superdebug == true)
                        {
                            System.out.println("modNum converted into " + modNum);
                        }
                        modString = dfTrunc.format(modNum);
                        modNumS = modNumS / ((Input.speedpercent) / 100.0);
                        modStringS = dfTrunc.format(modNumS);
                        Output.writeFile(currentLine.substring(0,breakStart + 1) + modString + "," + modStringS);
                        if (superdebug == true)
                        {
                            System.out.println("modNum printed as " + modNum);
                        }
                    }
                }
                else if (timingFound == true)
                {
                    if (currentLine.compareTo("") == 0)
                    {
                        timingFound = false;
                        if (debug == true)
                        {
                            System.out.println("No more timing sections...");
                        }
                        Output.writeFile(currentLine);
                    }
                    else
                    {
                        timingLine = sloser;
                        startPlace = 0;
                        endPlace = 0;
                        modNum = 0.0;
                        modNumS = 0.0;
                        modString = "";
                        modStringS = "";
                        
                        startPlace = timingLine.indexOf(",");
                        endPlace = timingLine.indexOf(",", startPlace + 1);
                        if (endPlace > 1)
                        {
                            modNum = Double.valueOf(timingLine.substring(startPlace + 1, endPlace));
                        }
                        if (modNum > 0)
                        {
                            modNum = modNum / ((Input.speedpercent) / 100.0);
                        }
                        modString = df.format(modNum);
                        modNumS = Double.valueOf(timingLine.substring(0, startPlace));
                        modNumS = modNumS / ((Input.speedpercent) / 100.0);
                        modStringS = dfTrunc.format(modNumS);
                        Output.writeFile(modStringS + "," + modString + timingLine.substring(endPlace, timingLine.length()));
                    }
                }
                else
                {
                    Output.writeFile(sloser);
                }
            }
            else
            {
                foundObjects = true;
                currentObject = currentLine;
                currentPlace = 0;
                startPlace = 0;
                start2 = 0;
                end2 = 0;
                endPlace = 0;
                modNum = 0.0;
                modNumS = 0.0;
                modString = "";
                    
                currentPlace = currentObject.indexOf(","); // first comma
                startPlace = currentObject.indexOf(",", currentPlace + 1); // second comma
                endPlace = currentObject.indexOf(",", startPlace + 1); // third comma
                try
                {
                    lastNum = Double.valueOf(currentObject.substring(currentObject.lastIndexOf(",") + 1, currentObject.length()));
                }
                catch (NumberFormatException e)
                {
                    lastNum = 0;
                }
                if (endPlace > 1)
                {
                    modNum = Double.valueOf(currentObject.substring(startPlace + 1, endPlace));
                }
                modNum = modNum / ((Input.speedpercent) / 100.0);
                modString = dfTrunc.format(modNum);
                currentPlace = currentObject.indexOf(",", endPlace + 1); // fourth comma
                start2 = currentObject.indexOf(",", currentPlace + 1); // fifth comma
                if (isMania == true)
                {
                    end2 = currentObject.indexOf(":", start2 + 1);
                }
                else
                {
                    end2 = currentObject.indexOf(",", start2 + 1); // sixth comma
                }
                if (end2 != -1)
                {
                    try
                    {
                        modNumS = Double.valueOf(currentObject.substring(start2 + 1, end2));
                    }
                    catch (NumberFormatException e)
                    {
                        modNumS = 0;
                    }
                }
                if (modNumS >= 2 && start2 != -1)
                {
                    if (superdebug == true)
                    {
                        System.out.println("modNumS = " + modNumS);
                    }
                    modNumS = modNumS / ((Input.speedpercent) / 100.0);
                    modStringS = dfTrunc.format(modNumS);
                    if (superdebug == true)
                    {
                        System.out.println("currentObject = " + currentObject + ", modString = " + modString + ", modStringS = " + modStringS);
                        System.out.println("startPlace = " + startPlace + ", endPlace = " + endPlace);
                    }
                    Output.writeFile(currentObject.substring(0,startPlace + 1) + modString + currentObject.substring(endPlace, start2 + 1) + modStringS + currentObject.substring(end2, currentObject.length()));
                }
                else if (lastNum > 512)
                {
                    lastNum = lastNum / ((Input.speedpercent) / 100.0);
                    lastNumS = dfTrunc.format(lastNum);
                    Output.writeFile(currentObject.substring(0,startPlace + 1) + modString + currentObject.substring(endPlace, currentObject.lastIndexOf(",") + 1) + lastNumS);
                }
                else Output.writeFile(currentObject.substring(0,startPlace + 1) + modString + currentObject.substring(endPlace, currentObject.length()));
            }
        }
    }
    
     public static void fixFile(File sourceFile, File destFile) throws IOException
     {
         if(!destFile.exists())
         {
             destFile.createNewFile();
         }
         
         FileChannel source = null;
         FileChannel destination = null;
         
         try
         {
             source = new FileInputStream(sourceFile).getChannel();
             source.position(3);
             destination = new FileOutputStream(destFile).getChannel();
             destination.transferFrom( source, 0, source.size() - 3 );
         }
         finally 
         {
             if (source != null)
             {
                 source.close();
             }
             if (destination != null)
             {
                 destination.close();
             }
        }
    }
    
    public static void showConversion()
    {
        System.out.println("Converting from " + df.format(Math.round(Input.oldBPM * 1000) / 1000) + " BPM to " + df.format(Input.newBPM) + " BPM!");
    }
}
