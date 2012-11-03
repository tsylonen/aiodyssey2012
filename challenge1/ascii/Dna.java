import java.util.Arrays;
import fj.F;  
import java.awt.image.*;

public class Dna {
    private Letter[] dna;
    private int cost;
    private BufferedI

    public Dna(Letter[] letters) {
        this.dna = letters;
        this.cost = Letter.
    }

    /**
     * Make a copy of mother but randomize some letters
     */
    private static Letter[] mutate(Letter[] mother, int mutations) {
        Letter[] ret = Arrays.copyOf(mother, mother.length);

        for(int i = 0; i < mutations; i++) {
            int ind = Math.abs(rng.nextInt()%mother.length);
            ret[i] = randomLetter();
        }
        return ret;
    }

    private double calculateCost(Letter[] ascimg) {
        return Ascii.cost(img, Arrays.asList(dna));
    }

    // an F-subclass that does mutation
    public F mutatorF = new F() {
            public Letter[] f(Letter[] mother) {
                return mutate(mother, mutationcount);
            }
        };

    public static Letter[] random(int letters) {
        Letter[] res = new Letter[letters];
        for(int i = 0; i < letters; i++) {
            res[i] = Letter.randomLetter();
        }
        return res;
    }








    // dark dwelling of abandoned functions


    // private static Letter[] modifyMutate(Letter[] mother, int mutations) {
    //     Letter[] ret = new Letter[mother.length];

    //     for(int i = 0; i < mother.length; i++) {
    //         ret[i] = mother[i];
    //     }

    //     for(int i = 0; i < mutations; i++) {
    //         int ind = Math.abs(rng.nextInt()%mother.length);
    //         int r,g,b,a;
    //         double x,y,rot,size;

    //         r = mother[ind].color.getRed() + (rng.nextInt()%100) - 50;
    //         g = mother[ind].color.getGreen() + (rng.nextInt()%100) - 50;
    //         b = mother[ind].color.getBlue() + (rng.nextInt()%100) - 50;
    //         a = mother[ind].color.getTransparency() + (rng.nextInt()%100) - 50;

    //         x = mother[ind].x * ((rng.nextDouble() * 2 + 9) / 10);
    //         x = mother[ind].x * ((rng.nextDouble() * 2 + 9) / 10);
    //         x = mother[ind].x * ((rng.nextDouble() * 2 + 9) / 10);

    //     }
    //     return ret;
    // }



}
