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
The program that transforms the Java information to MicroVBA is a modification of FreeHEP library. As
mentioned in Part 1, it uses an independent graphics superclass that writes the MicroVBA instructions into a
file. The user triggers the conversion by clicking on a bar on the top of the screen.
A PowerPoint presentation that mocks the converted interface can be found [here](https://github.com/nilostolte/MicroVBA-PowerPoint/blob/main/Example/testfontsembedded.pptm)
