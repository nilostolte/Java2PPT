# Java2PPT

In this project, the Java vector information shown on the screen, such as a Graphics User interface, is
automatically converted to PowerPoint by using a program in an intermediate language, which is run by our 
[MicroVBA](https://github.com/nilostolte/MicroVBA-PowerPoint) interpreter 
written in VBA to be used in PowerPoint in order to be able to import large vector graphics files. The information is
written in a file containing the MicroVBA instructions that are read within a macro in the PowerPoint file
where the vector information is created in our MicroVBA interpreter by executing the instructions. MicroVBA language
is a subset of VBA for PowerPoint. This language bypasses the weaknesses of the EMF format (the lack of
gradient being the most handicapping) and of VBA itself (limitations on the size of functions) in order to import
complete vector files from Java programs to PowerPoint.
The program in this repository transforms the Java information to MicroVBA is a modification of FreeHEP library. It 
uses an independent graphics superclass that writes the MicroVBA instructions into a
file. The user triggers the conversion by clicking on a bar on the top of the screen.
A PowerPoint presentation that mocks the converted interface can be found 
[here](https://github.com/nilostolte/MicroVBA-PowerPoint/blob/main/Example/testfontsembedded.pptm).

In this source code we used our [MenuInfographics6](https://github.com/nilostolte/Java-Vector-GUI/tree/main/MenuInfographics6) example.
Any class extending JPanel that displays vector graphics can be used here instead of 
[MenuInfographics6.java](https://github.com/nilostolte/Java-Vector-GUI/blob/main/MenuInfographics6/src/com/MenuInfographics6.java).

In this snapshot of the program one can clearly see MenuInfographics6 and additional buttons on the right. One triggers the 
file MicroVBA file generation and another exits the program:

![image](https://user-images.githubusercontent.com/80269251/127778555-dce4006e-c0b8-4f30-8367-79bf96000b11.png)

