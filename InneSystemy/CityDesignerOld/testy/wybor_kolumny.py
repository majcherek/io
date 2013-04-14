import sys

f = open(sys.argv[1], "r")

index = int(sys.argv[2])

for line in f.readlines():
    print line.replace(".",",").split(" ")[index].rstrip("\n")
