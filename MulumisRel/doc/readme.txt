Simulum, Version @MULUMIS_VERSION@
================================================
Copyright 2000-2004 Michael Meyling <mime@qedeq.org>.

*Simulum* is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License under "doc/gpl.txt" for more details.

This project deals with different simulations of star movements and 
their visualizations. 
To get a nice effect it produces the visual impression of flying 
through a star field.

Look at 
 http://www.mulumis.meyling.com/
 http://sourceforge.net/projects/mulumis/
to get the latest source code and information. 


Technical Details
=================
To start this program you need a java runtime version 1.4.2 or higher.
We assume the following directory structure:
jre           here is the java runtime version located
jre/bin       here should be the program "java"
lib           includes "mulumis.jar"

Now you could start the main program by calling
./bin/java -cp lib/principia.jar com.meyling.mulumis.ToInfinityAndBeyond
The option "-l 0" sets the logging level to 0, level 1 an 2 will give
more details about the programs current status and actions.


Author
======
Michael Meyling <michael@at@mulmis.meyling.com>