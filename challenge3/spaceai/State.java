import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

/**
 * State contains the current game state and methods to query
 * important things about it.
 */
public class State {
    public List<Planet> planets;
    public List<Planet> ownPlanets;
    public List<Planet> enemyPlanets;
    public List<Flight> ownFlights;
    public List<Flight> enemyFlights;

    public int enemyShipRate, ownShipRate;
    public int ownShips, enemyShips;

    public double [][] distances;

    public State(Planet[] ps) {
        ownFlights = new ArrayList<Flight>();
        enemyFlights = new ArrayList<Flight>();
        setPlanets(ps);
    }

    /**
     * Set planets to a given array 
     */
    public void setPlanets(Planet[] ps) {
        this.planets = Arrays.asList(ps);

        ownPlanets = new ArrayList<Planet>();
        enemyPlanets = new ArrayList<Planet>();

        for(Planet p: planets) {
            if (p.owner == 1) {
                ownPlanets.add(p);                
            } else if(p.owner == 2) {
                enemyPlanets.add(p);
            }
        }

        refreshShipCounts();
        calculateDistances();
    }

    public void refreshHack() {
        setPlanets((Planet[])planets.toArray());
    }

    public void addFlight(int owner, int from, int to, int count) {
        Flight f = new Flight();
        f.destination = to;
        f.size = count;
        f.arrival = Calendar.getInstance().getTimeInMillis() + Math.round(1000 * distance(from, to) / 18);

        if(owner == 1) {
            ownFlights.add(f);
            ownShips += f.size;
        } else if(owner == 2) {
            enemyFlights.add(f);
            enemyShips += f.size;
        }
    }

    /**
     * Remove flights that have arrived from the flight lists.
     */
    public void removeOldFlights() {
        long t = Calendar.getInstance().getTimeInMillis();
        Iterator<Flight> it = this.ownFlights.iterator();
        while(it.hasNext()) {
            Flight f = (Flight)it.next();
            if(f.arrival < t) {
                it.remove();
            }
        }
        
        it = this.enemyFlights.iterator();
        while(it.hasNext()) {
            Flight f = (Flight)it.next();
            if(f.arrival < t) {
                it.remove();
            }
        }
    }

    /**
     * How many ships are there on planet p after t seconds have elapsed?
     */
    public int shipsAt(Planet p, double t) {
        int newships = (int)(p.size * t);
        
        long time = Calendar.getInstance().getTimeInMillis() + (long)(t*1000);
        for(Flight f : ownFlights) {
            if (f.destination==p.idnum && f.arrival < time) {
                newships += f.size * (p.owner==1?1:-1);
            } 
        }

        for(Flight f : enemyFlights) {
            if (f.destination==p.idnum && f.arrival < time) {
                newships += f.size * (p.owner==1?-1:1);
            } 
        }

        return p.ships + newships;
    }

    /**
     * Return the euclidean distance between planets a and b
     */
    public double distance(int a, int b) {
        return Math.sqrt((planets.get(a).x - planets.get(b).x) * (planets.get(a).x - planets.get(b).x) +
                         (planets.get(a).y - planets.get(b).y) * (planets.get(a).y - planets.get(b).y) +
                         (planets.get(a).z - planets.get(b).z) * (planets.get(a).z - planets.get(b).z));
    }

    private void calculateDistances() {
        distances = new double[planets.size()][planets.size()];
        for(int i = 0; i < planets.size(); i++) {
            for(int j = 0; j < planets.size(); j++) {
                distances[i][j] = distance(i,j);
            }
        }
    }

    /**
     * Count the ships and ship generation counts of both players
     */
    private void refreshShipCounts() {
        ownShipRate = 0;
        enemyShipRate = 0;

        for(Planet p : this.planets) {
            if (p.owner == 1) {
                this.ownShips+=p.ships;
                ownShipRate += p.size;
            } else if (p.owner == 2) {
                this.enemyShips+=p.ships;
                enemyShipRate += p.size;
            }
        }
        for(Flight f : this.ownFlights) {
            ownShips+=f.size;
        }
        for(Flight f : this.enemyFlights) {
            enemyShips+=f.size;
        }
    }
}

