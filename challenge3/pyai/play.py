import random
import sys

from Planet import Planet
planets = []

def run():
    numplanets = int(input())
    sys.stderr.write("numplanets: " + str(numplanets) + "\n")
    for i in range(numplanets):
        a = input()
        sys.stderr.write(a + "\n")
        
        planets.append(Planet(a,i))

    while(True):
        readInput()
        play()

def readInput():
    while(True):
        print("STATUS")
        msg = input().split()
        if msg[0] == "PLANETS":
            msg = msg[1:]

            for i in range(len(planets)):
                planets[i].ships = float(msg[i*2])
                planets[i].owner = int(msg[i*2+1])
            return

        elif msg[0] == "SEND":
            pass

def play():
    for p in planets:

        if p.owner == 1 and p.ships > 20:
            to = random.randint(0, len(planets))
            shipcount = random.randint(0, int(p.ships))
            print("SEND " + str(p.idnum) + " " + str(to) + " " + str(shipcount))
        
if __name__ == "__main__":
    run()
