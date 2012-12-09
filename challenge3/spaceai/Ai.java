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
            int ownermod;
            if(p.owner == 1) ownermod = 2;
            else if(p.owner == 2) ownermod = 3;
            else ownermod = 1;

            p.score = p.size / p.ships * ownermod * Math.abs(p.ownInfluence-p.enemyInfluence);

            //            p.score = (p.size/2*1+p.ships) + ownmod * p.ownInfluence - ownmod * p.enemyInfluence;
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
        if ((state.enemyShips + newships) < state.ownShips - state.ownPlanets.size()*10)  {
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
     * send ships to juicy places
     */
    public void makeAttacks() {
        for(int i = 0; i < 3; i++) {
            attack(interesting.get(i));
        }
    }


    /**
     * Find enough ships to attack a planet and attack it
     */
    public void attack(Planet enemy)  {
        double maxdist = Util.maxDistance(state.ownPlanets, state.enemyPlanets);
        int toKill = state.shipsAt(enemy, maxdist/18);
        if(enemy.owner==1) {
            toKill = (int)(enemy.ownInfluence - enemy.enemyInfluence);
        }

        Iterator<Planet> it = state.ownPlanets.iterator();
        while(toKill > 0 && it.hasNext()) {
            Planet own = it.next();
            int reserve =  (int)(own.ownInfluence-own.enemyInfluence);
            if(own.ships > reserve) {
                send(own, enemy, toKill);
                toKill = 0;
            } else {
                toKill -= reserve;
                send(own, enemy, reserve);
            }
        }
    }


    /**
     * Conquer every enemy planet
     */
    public void nuclearTesuji() {
        // needed to calculate maximum new ships built in the target
        // planet in the time it takes our ships to get there
        double maxdist = Util.maxDistance(state.ownPlanets, state.enemyPlanets);

        Iterator<Planet> enemyIt = state.enemyPlanets.iterator();
        Iterator<Planet> ownIt = state.ownPlanets.iterator();

        Planet own = ownIt.next();
        while(enemyIt.hasNext() && ownIt.hasNext()) {
            Planet enemy = enemyIt.next();
            int toKill = state.shipsAt(enemy, maxdist/18);
            while(toKill > 0 && ownIt.hasNext()) {
                if(own.ships > toKill + 5) {
                    send(own, enemy, toKill);
                    toKill = 0;
                } else {
                    toKill -= own.ships-5;
                    send(own, enemy, own.ships - 5);
                    own = ownIt.next();
                }
            }
        }
    }
}
