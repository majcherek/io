# bierze ROUTE STATY i zapisuje je do pliku podanego jako argument w postaci CSV

import sys

if (len(sys.argv) != 3):
    print "Argumenty: [plik_ze_statystykami.sum.s] [plik_docelowy]"
    exit()

sourceFile = open(sys.argv[1],"r") 
targetFile = open(sys.argv[2],"w") 

readingMode = False
for line in sourceFile:
    if (line[0].isalpha()):
        if line.startswith("LINK STATS"):
            readingMode = True
        else:
            readingMode = False

    if readingMode:
        if (line[0] == " "):
            result = (";".join(line.split()))+"\n"
            targetFile.write(result);
            print result,
