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

        img = ImageIO.read(new File(args[0]));
        int count = Integer.parseInt(args[1]);
        int generations = Integer.parseInt(args[2]);
        int gensize = Integer.parseInt(args[3]);

        double s = 0;

        for(int j = 0; j < 10; j++) {

            Dna dna = new Dna(2, count, img);


            for(int i = 0; i < generations; i++) {
                dna = stepGeneration(dna, gensize);
            }

            //            System.out.println(dna);
            double cost = dna.calculateCost();
            //System.out.println(cost);

            s += cost;


        }

        System.out.println(s/10);
    }


    /**
     * Make gensize mutations of mother, return the one with the best score
     */
    private static Dna stepGeneration(Dna mother, int gensize){
        Dna best, newborn;
        best = mother;

        double bestscore = mother.calculateCost();

        ExecutorService executorService = Executors.newFixedThreadPool(executorCount);
        Strategy s = Strategy.executorStrategy(executorService);

        for(int i = 0; i < gensize; i++) {
            newborn = mother.mutate(2);

            if(newborn.calculateCost() < bestscore) {
                best = newborn;
            }
        }
        return best;
    }


}
