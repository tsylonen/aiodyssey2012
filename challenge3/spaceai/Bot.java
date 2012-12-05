import java.util.*;
public class Bot {
    public static void main(String[] args) {
        new Bot().run();
    }

    Scanner s = new Scanner(System.in);
    Random rng = new Random();
    Planet[] planets;
    State state;
    Ai ai;

    void run() {
        s.useLocale(Locale.US);
        planets = new Planet[s.nextInt()];
        for(int i=0; i<planets.length; ++i) {
            Planet p = new Planet();
            p.x = s.nextDouble();
            p.y = s.nextDouble();
            p.z = s.nextDouble();
            p.size = s.nextDouble();
            p.ships = (int)s.nextDouble();
            p.owner = s.nextInt();
            p.idnum = i;
            planets[i] = p;
        }
        state = new State(planets);
        ai = new Ai(state);

        while(true) {
            readInput();
            play();
        }
    }

    void readInput() {
        System.out.println("STATUS");
        while(true) {
            String msg = s.next();
            if (msg.equals("PLANETS")) {
                for(int i=0; i<planets.length; ++i) {
                    Planet p = state.planets.get(i);
                    p.ships = (int)s.nextDouble();
                    p.owner = s.nextInt();
                }
                break;
            } else if (msg.equals("SEND")) {
                int owner = s.nextInt();
                int from = s.nextInt();
                int to = s.nextInt();
                int count = s.nextInt();
                
                this.state.addFlight(owner, from, to, count);
            }
            state.refreshHack();
        }
    }

    void play() {

        for(int i=0; i<planets.length; ++i) {
            if(ai.enemyBeatable()) {
                System.err.println("nuclear!");
                ai.nuclearTesuji();
            }
            else {
                                       
                Planet p = planets[i];
                if (p.owner==1 && p.ships>=20) {
                    int count = rng.nextInt((int)p.ships);
                    int to = ai.bestPlanet();
                    // int to = ((Planet)state.planets.get(0)).idnum;
                    System.out.printf("SEND %d %d %d\n", i, to, count);
                    p.ships -= count;
                }
            }
        }
    }
}
