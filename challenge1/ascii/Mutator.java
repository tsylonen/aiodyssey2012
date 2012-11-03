import java.util.Arrays;

import fj.F;  
import fj.control.parallel.Strategy;
import java.util.concurrent.*;

import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import java.awt.*;

//import extra166y.Ops.LongOp;



import java.util.Random;

/**
 * Contains a very bad example solution.
 */
public class Mutator {
    /**
     * Takes as parameters the original image and characted count
     */

    private static Random rng;
    private static BufferedImage img;
    private static int width, height;
    private static int executorCount = 15;
    private static int mutationcount;


    public static void main(String[] args) throws IOException {
        rng = new Random();

        img = ImageIO.read(new File(args[0]));
        int count = Integer.parseInt(args[1]);
        int generations = Integer.parseInt(args[2]);
        int gensize = Integer.parseInt(args[3]);
        
        this.mutationcount = 1;

        Letter[] res = createRandom(count);

        for(int i = 0; i < generations; i++) {
            res = stepGeneration(res, gensize, 1);
        }

        for(Letter l : res) {
            System.out.println(l);
        }

        System.err.println("Solution cost: "+Ascii.cost(img, Arrays.asList(res)));
    }


    /**
     * Make gensize mutations of mother, return the one with the best score
     */
    private static Letter[] stepGeneration(Letter[] mother, int gensize, int mutations){
        Letter[] best, newborn;
        best = mother;
        double bestscore = score(mother);
        ExecutorService executorService = Executors.newFixedThreadPool(executorCount);
        Strategy s = Strategy.executorStrategy(executorService);

        for(int i = 0; i < gensize; i++) {
            newborn = mutate(mother, mutations);

            if(score(newborn) < bestscore) {
                best = newborn;
            }
        }
        return best;
    }


}
