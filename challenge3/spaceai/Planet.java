public class Planet implements Comparable<Planet> {
    double x,y,z;
    double size;
    int ships;
    int owner;
    int idnum;

    double score;

    public int compareTo(Planet o) {
        return -Double.compare(this.score, ((Planet)o).score);
    }
}

