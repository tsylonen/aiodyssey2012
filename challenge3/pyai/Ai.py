import sys
import math
import time as t
from Planet import Planet
from Flight import Flight

class Ai(object):
    def __init__(self, planets):
        self.planets = planets;
        self.ownFlights = []
        self.enemyFlights = []
        self.ownShips = 0;
        self.enemyShips = 0;
        self.startTime = t.time()

        self.ownplanets = []
        self.frontier = []
        self.reserve = []
        self.enemyPlanets = []
        self.protected = []

        self.refresh()


    def refresh(self):
        """Run this on every "turn". """
        self.enemyPlanets = list(filter(lambda p: p.owner == 2, self.planets))
        self.ownPlanets = list(filter(lambda p: p.owner == 1, self.planets))
        self.countShips()
        self.removeOldFlights()
        self.calculateInfluences()
        self.calculateValues()
        self.calculateProfitabilities()
        self.removeOldProtects()

    def addFlight(self, owner, fromP, toP, shipCount):
        arrival = self.dist(self.planets[fromP], self.planets[toP])/18 + t.time()
        if(owner == 1):
            self.ownFlights.append(Flight(self.planets[toP], shipCount, arrival))
        else:
            newflight = Flight(self.planets[toP], shipCount, arrival)
            self.enemyFlights.append(newflight)
            self.handleFlight(newflight)


    def calculateValues(self):
        timeLeft = 30 - (t.time()-self.startTime)
        for p in self.planets:
            p.value = p.size*timeLeft

    def calculateProfitabilities(self):
        for p in self.planets:
            shipmod = 1
            if p.owner == 1:
                ownermod = 0.3
                shipmod = 3
            elif p.owner == 2:
                ownermod = 2
            else:
                ownermod = 1
                shipmod = 1.5

            p.profitability = ownermod * p.value - p.ships * shipmod

    def send(self, fromP, toP, ships):
        self.addFlight(1, fromP.idnum, toP.idnum, ships)
        fromP.ships -= ships
        print("SEND " + str(fromP.idnum) + " " + str(toP.idnum) + " " + str(ships))

    def calculateInfluences(self):
        for p in self.planets:
            p.ownInfluence = 0
            p.enemyInfluence = 0


            # planet distances are squared, they can only send ships to
            # one planet at a time
            for i in self.planets:
                if i.owner == 1:
                    p.ownInfluence += i.ships / (self.dist(p,i)**2 + 1)
                if i.owner == 2:
                    p.enemyInfluence += i.ships / (self.dist(p,i)**2 + 1)

            # flight distances are not squared, they have already been
            # chosen to go to the destina
            for f in self.ownFlights:
                dist = (f.arrival - t.time()) * 18
                p.ownInfluence += f.size / (dist+1)

            for f in self.enemyFlights:
                dist = (f.arrival - t.time()) * 18
                p.enemyInfluence += f.size / (dist+1)

    def calculateFrontier(self):
        self.frontier = []
        self.reserve = self.planets.copy()

        
    def invade(self, planet):
        """send enough ships to planet to invade it if we can"""
        canInvade = False
        ownp = self.ownPlanets.copy()
        ownp.sort(key=lambda p: self.dist(p,planet))
        if(planet.owner==1):
            # no need to send any ships to invade an own planet
            return canInvade

        attacks = []
        toKill = self.shipsafter(planet, self.maxdist(self.ownPlanets, [planet])/18)
        
        for p in ownp:
            if p.protecttime:
                continue
            if p.ships > toKill + 6:
                attacks.append((p, toKill+2))
                canInvade = True
                break
            else:
                toKill -= p.ships-4
                attacks.append((p, p.ships-4))


        for a in attacks:
            self.send(a[0],planet, a[1])
        return canInvade

    def handleFlight(self, flight):
        dest = flight.dest
        shipsleft = self.shipsafter(dest, flight.arrival-t.time() + 0.1)

        if dest.owner == 1:
            if shipsleft < 0:
                self.protect(dest, -shipsleft + 1, flight.arrival + 0.1)
        elif shipsleft < 0:
            #the planet will be conquered when the flight arrives
            self.protect(dest, shipsleft + 1, flight.arrival + 0.1)

    def protect(self, planet, amount, protecttime):
        self.protected.append(planet)
        planet.protecttime = protecttime

        ownp = self.ownPlanets.copy()
        ownp.sort(key=lambda p: self.dist(p,planet))

        needed = amount

        for p in ownp:
            if p.protecttime:
                continue
            if p.ships > needed:
                self.send(p, planet, needed)
                break
            else:
                needed -= planet.ships-1
                self.send(p, planet, planet.ships-1)
            
    def invadeGoodPlanets(self):
        notown = list(filter(lambda p: p.owner != 1, self.planets))
        
        #invade really cheap planets
        cheap = list(filter(lambda p: p.ships < 5,notown))
        cheap.sort(key=lambda p: p.ships)
        for p in cheap:
            self.invade(p)

        notown.sort(key=lambda p: -p.profitability)
        notown = notown[:3]
        notown.sort(key=lambda p: -p.ownInfluence)
        notown = notown[:1]
        for p in notown:
            if p.profitability > 0:
                self.invade(p)
            

    def maxdist(self, groupa, groupb):
        """the maximum distance between a planet in groupa and groupb"""
        dists = [self.dist(x,y) for x in groupa for y in groupb]
        if len(dists) == 0:
            return 100000
        return max(dists)

    def shipsafter(self, planet, time):
        if planet.owner == 0:
            newships = 0
        else:
            newships = planet.size * time

        arr = t.time() + time
        ownornot = 1 if planet.owner == 1 else -1

        for f in self.ownFlights:
            if f.arrival < arr:
                newships += f.size * ownornot

        for f in self.enemyFlights:
            if f.arrival < arr:
                newships += f.size * -ownornot

        return planet.ships + newships

    def closest(self, plista, plistb):
        closest = plista[0]
        mindist = 100000;

        for p in plista:
            for i in plistb:
                if dist(p,i) < mindist:
                    closest = p
                    mindist = dist(p,i)
            return (closest, mindist)
                                 
    def mostProfitableNotOwn(self):
        notown = list(filter(lambda p: p.owner != 1, self.planets))
        if len(notown) == 0:
            return self.planets[0]

        best = notown[0]

        for p in notown:
            if p.value > best.profitability:
                best = p
        return best
                
            

    def countShips(self):
        self.ownShips = 0
        self.enemyShips = 0

        for p in self.planets:
            if p.owner == 1:
                self.ownShips += p.ships
            elif p.owner == 2:
                self.enemyShips += p.ships

        for f in self.ownFlights:
            self.ownShips += float(f.size)
        for f in self.enemyFlights:
            self.enemyShips += f.size

    def removeOldFlights(self):
        now = t.time()
        self.ownFlights = list(filter(lambda f: f.arrival > now, self.ownFlights))
        self.enemyFlights = list(filter(lambda f: f.arrival > now, self.enemyFlights))

    def removeOldProtects(self):
        now = t.time()
        for p in self.protected:
            if p.protecttime == None:
                self.protected.remove(p)
            elif now > p.protecttime:
                p.protecttime = None
                self.protected.remove(p)

    def dist(self, pa, pb):
        """Distance between two self.planets, slow."""
        return math.sqrt((pa.x- pb.x)**2 + (pa.y - pb.y)**2 + (pa.z - pb.z)**2)
        
