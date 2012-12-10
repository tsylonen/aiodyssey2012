import random
import sys

from Ai import Ai

from Planet import Planet
class play(object):
    def __init__(self):
        self.planets = []
        self.ai = None

    def run(self):
        numplanets = int(input())
        sys.stderr.write("numplanets: " + str(numplanets) + "\n")
        for i in range(numplanets):
            a = input()
            sys.stderr.write(a + "\n")
        
            self.planets.append(Planet(a,i))

        self.ai = Ai(self.planets)
        
        while(True):
            self.readInput()
            self.play()

    def readInput(self):
        print("STATUS")
        while(True):
            msg = input().split()
            if msg[0] == "PLANETS":
                msg = msg[1:]

                for i in range(len(self.planets)):
                    self.planets[i].ships = float(msg[i*2])
                    self.planets[i].owner = int(msg[i*2+1])
                self.ai.refresh()
                return

            elif msg[0] == "SEND":
                self.ai.addFlight(int(msg[1]), int(msg[2]), int(msg[3]), int(msg[4]))
            
    def play(self):
#        self.ai.invade(self.ai.mostProfitableNotOwn())
        self.ai.invadeGoodPlanets()

        
if __name__ == "__main__":
    play().run()
    # while(True):
    #     sys.stderr.write(input()+ "\n")
