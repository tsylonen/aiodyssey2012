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
    public List<Flight> ownFlights;
    public List<Flight> enemyFlights;
    int ownShips, enemyShips;

    public State(Planet[] ps) {
        this.planets = Arrays.asList(ps);
        this.ownFlights = new ArrayList<Flight>();
        this.enemyFlights = new ArrayList<Flight>();

        this.refreshShipCounts();
    }

    public void setPlanets(Planet[] ps) {
        this.planets = Arrays.asList(ps);
        refreshShipCounts();
    }

    public void addFlight(int owner, int from, int to, int count) {
        Flight f = new Flight();
        f.destination = to;
        f.size = count;
        f.arrival = Calendar.getInstance().getTimeInMillis() + Math.round(1000 * distance(from, to) / 18);

        if(owner == 1) {
            this.ownFlights.add(f);
            this.ownShips += f.size;
        } else if(owner == 2) {
            this.enemyFlights.add(f);
            this.enemyShips += f.size;
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
     * Return the euclidean distance between planets a and b
     */
    public double distance(int a, int b) {
        return Math.sqrt((this.planets.get(a).x - this.planets.get(b).x) * (this.planets.get(a).x - this.planets.get(b).x) +
                         (this.planets.get(a).y - this.planets.get(b).y) * (this.planets.get(a).y - this.planets.get(b).y) +
                         (this.planets.get(a).z - this.planets.get(b).z) * (this.planets.get(a).z - this.planets.get(b).z));
    }

    /**
     * Count the ships of both players
     */
    private void refreshShipCounts() {
        for(Planet p : this.planets) {
            if (p.owner == 1) {
                this.ownShips+=p.population;
            } else if (p.owner == 2) {
                this.enemyShips+=p.population;
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

class Flight {
    int destination;
    int size;
    long arrival;
}
