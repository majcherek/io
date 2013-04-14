# Pomija wszystkie te polaczenia ktore nie dotycza wezlow
# nie zaczynajacych sie od PREFIX
# [prefix_glowny],[prefix_boczny]

import sys


if (len(sys.argv) != 3):
    print "Argumenty: [prefix_nie_do_pominiecia] [sciezka_do_pliku]"
    exit() 

PREFIX_MAIN = sys.argv[1].split(",")[0]
PREFIXES = tuple(sys.argv[1].split(","))

file = open(sys.argv[2],"r")
for line in file:
    if (line[0] == " "):
        splited_line = line.split()
        try:
            if ((splited_line[2]).isdigit()):
                # analizuje prefix
                if ((splited_line[0].startswith(PREFIX_MAIN) and splited_line[1].startswith(PREFIXES)) 
                        or (splited_line[1].startswith(PREFIX_MAIN) and splited_line[0].startswith(PREFIXES))):
                    print ";".join(line.split())
        except IndexError:
            pass
