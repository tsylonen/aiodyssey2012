import java.util.ArrayList;
import java.util.Collections;

public class Ai {
    private State state;
    private ArrayList<Planet> interesting;

    public Ai(State s) {
        state = s;

        interesting = new ArrayList<Planet>(state.planets);

    }

    private void calculateInteresting() {
        for(Planet p : state.planets) {
            p.score = p.size / p.population * (p.owner==2?1.5:1);
        }
        Collections.sort(interesting);
    }

    public int bestPlanet() {
        calculateInteresting();
        return interesting.get(0).idnum;
    }
}
