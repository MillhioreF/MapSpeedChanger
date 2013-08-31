Map Speed Changer v1.32
by MillhioreF


~~~*~* HOW TO USE *~*~~~

0. Make sure you have an mp3 of the song speed you want, this program doesn't make one for you!
1. Extract the contents of this zip file into any folder.
2. Drag the .osu file that you want to change the speed of into the same folder (not the jar folder)
3. Run the .bat file and follow the instructions on screen.
4. Drag the new .osu file back into the folder you got it from and refresh the song list.


~~~*~* USAGE TIPS *~*~~~

- Odds are you'll have to edit the offset of the map this program creates; Audacity adds a few ms when it encodes an mp3, so that'll throw off the timing.

- The .osu filename MAY be weird when you drop it into the song folder - this fixes itself after saving it in the editor, which I hope you do due to the point above.

- Source code is public and can be found at https://github.com/MillhioreF/MapSpeedChanger/ . Be warned that the code is absolute shit and probably is hardly readable - feel free to ask me what the hell some of my code is doing.

- Bugs, questions and comments should be posted in this tool's thread: http://osu.ppy.sh/forum/t/114765/


Enjoy!


~~*~* CHANGELOG *~*~~

Version 1.32 (Aug 31, 2013)
-Pushed to github.

Version 1.311 (Aug 31, 2013)
-Fixed broken debug code
-Removed leftover code from a failed improvement that broke the program
-Fixed double linebreak not appearing after hitobjects and making the new .osu less authentic

Version 1.31 (Mar 7, 2013)
-Fixed a critical error with [ and ] in filenames (which is most of them, oops)
-Fixed the fix for AudioFilename vanishing

Version 1.3 (Feb 27, 2013)
-Attempted fixing number format corruption with nonstandard system locale
-Added a prompt to continue converting files
-Preliminary internal Unicode support (not finished yet)
-Heavily cleaned up debug calls internally. This is for me :D

Version 1.2 (Feb 16, 2013)
-MP3 input removed on a trial basis (change it manually, defaults to old mp3)
-.osu extension no longer required to be entered (program works with or without it)
-Brackets in the filename (difficulty) are now changed to the new difficulty name if applicable
-Minor cleanup to debug printing and map name additions
-jar file is now 15% of its former size (I kept including my test maps inside it, oops)
-Readme cleanup (standardized formatting, removed some obsolete usage tips)

Version 1.12 (Feb 16, 2013)
-Fixed some older maps parsing wrongly
-Fixed spinners going much longer than they should in some cases

Version 1.11 (Feb 6, 2013)
-Fixed hold notes in some mania maps derping out
-Fixed maps without preview times getting a broken one

Version 1.1 (Feb 5, 2013)
-Added support for specifying a totally new BPM, rather than a percentage of the current one
-Made error messages spew upon invalid BPM
-Fixed a bug where several minor fields were being eaten by the program

Version 1.0 (Jan 7, 2013)
-Initial release


~~*~* KNOWN ISSUES *~*~~

-If the output filename is the same as an existing filename, it'll overwrite. I'll fix this later.
-Stack leniency, difficulty settings, etc. aren't adjusted to fit the new speed. Low priority for the moment.