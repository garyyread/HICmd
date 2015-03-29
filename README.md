# HICmd for HI-Maude
	A tool gives HI-Maude users a GUI with textual/visual analysis.
	
# Features
	* Document editor.
	* Dedicated command analysis panel/editor.
	* Allows for multiple simultanious analyses to be carried out.
	* Dedicated tab created for each analysis set performed.
	* Result tab contains a break down of analyses in a readable format, along with the RAW data.
	* Ability to plot result data from HI-Maude's data collector.
	* Compare plots of each resut from an analyses set.
	
# Getting started...
	1. Install Maude to "C:\EXAMPLE_PATH\MaudeFW"
		> HICmd\build\classes\hicmd\resources\MaudeFW_2.4.exe
		
	2. Copy required resources to "C:\EXAMPLE_PATH\MaudeFW\"
		> HICmd\build\classes\hicmd\resources\hi-maude.maude
		> HICmd\build\classes\hicmd\resources\eflib-float.maude
		> HICmd\build\classes\hicmd\resources\real-time-maude.maude
		
	2. Set environment path to Maude installation folder, for windows use cmd with:
		> setx path "%PATH%;C:\EXAMPLE_PATH\MaudeFW\" /m
		
	3. Run HICmd!
		> HICmd\dist\HICmd.jar

# TODO...
* Editor syntax highlighting
* Command builder
* Tidy and comment
* UI Improvemnts

# Known Issues
* Unchecked types, compile with -Xlint for more details.
* Weak processing of maude output, i.e., <b>not robust</b>.
* Result process thread may never die, incorrect files can cause this.
* On close of last result tab, new result tab chart lines are not drawn.

# See also
* <a href="http://folk.uio.no/mohamf/HI-Maude/">HI-Maude: The Rewriting-Logic-Based Tool for Modeling Interacting Hybrid Systems</a>

* <a href="http://heim.ifi.uio.no/peterol/RealTimeMaude/">The Real-Time Maude Tool</a>

* <a href="http://maude.cs.illinois.edu/w/index.php?title=The_Maude_System">The Maude System</a>

* <a href="http://en.wikipedia.org/wiki/Maude_system">Maude system (Wikipedia)</a>
