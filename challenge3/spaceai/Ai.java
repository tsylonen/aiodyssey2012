import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Ai {
    private State state;
    private ArrayList<Planet> interesting;

    public Ai(State s) {
        state = s;

        interesting = new ArrayList<Planet>(state.planets);

    }

    private void calculateInteresting() {
        for(Planet p : state.planets) {
            p.score = p.size / p.ships * (p.owner==2?1.5:1);
        }
        Collections.sort(interesting);
    }

    public int bestPlanet() {
        calculateInteresting();
        return interesting.get(0).idnum;
    }

    /**
     * Return true if enemy can be beaten immediately by attack
     */
    public boolean enemyBeatable() {
        // This is actually an upper bound for new enemy ships built
        int newships = (int)(Util.maxDistance(state.ownPlanets, state.enemyPlanets)/18 * state.enemyShipRate);
        if ((state.enemyShips + newships) < state.ownShips - state.ownPlanets.size())  {
            return true;
        } 
        return false;
    }

    private void send(Planet from, Planet to, int count) {
        System.out.printf("SEND %d %d %d\n", from.idnum, to.idnum, count);
        from.ships -= count;
        state.addFlight(from.owner, from.idnum, to.idnum, count);
    }

    /**
     * Conquer every enemy planet
     */
    public void nuclearTesuji() {
        double maxdist = Util.maxDistance(state.ownPlanets, state.enemyPlanets);

        Iterator<Planet> enemyIt = state.enemyPlanets.iterator();
        Iterator<Planet> ownIt = state.ownPlanets.iterator();

        Planet own = ownIt.next();
        while(enemyIt.hasNext() && ownIt.hasNext()) {
            Planet enemy = enemyIt.next();
            int toKill = state.shipsAt(enemy, maxdist/18);
            while(toKill > 0 && ownIt.hasNext()) {
                if(own.ships > toKill + 1) {
                    send(own, enemy, toKill);
                } else {
                    send(own, enemy, own.ships - 1);
                    own = ownIt.next();
                }
            }
        }
    }
}
