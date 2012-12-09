public class Planet implements Comparable<Planet> {
    double x,y,z;
    double size;
    int ships;
    int owner;
    int idnum;


    // These are calculated from the global situation. They should
    // possibly be somewhere else but it was easy to stick them here.
    double score;
    double ownInfluence;
    double enemyInfluence;

    public int compareTo(Planet o) {
        return -Double.compare(this.score, ((Planet)o).score);
    }
}

