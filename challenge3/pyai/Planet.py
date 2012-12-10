class Planet(object):
    def __init__(self, s, idnum):
        ss = s.split();
        self.x = float(ss[0])
        self.y = float(ss[1])
        self.z = float(ss[2])
        self.size = float(ss[3])
        self.ships = float(ss[4])
        self.owner = int(ss[5])
        self.idnum = idnum
        self.protecttime = None

    def __str__(self):
        return("Planet: " + str(self.idnum) + " owner: " + str(self.owner) + " ships: " + str(self.ships))
