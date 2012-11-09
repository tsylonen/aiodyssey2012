import java.util.Arrays;

import fj.F;  
import fj.control.parallel.Strategy;
import fj.data.List;
import java.util.concurrent.*;

import java.lang.System;

import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.ArrayList;
//import java.util.List;
//import java.awt.*;

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
    private static int executorCount = 16;
    private static int mutationcount;
    private static ExecutorService executorService;

    public static void main(String[] args) throws IOException {
        executorService = Executors.newFixedThreadPool(executorCount);

        img = ImageIO.read(new File(args[0]));
        int count = Integer.parseInt(args[1]);
        int generations = Integer.parseInt(args[2]);
        int gensize = Integer.parseInt(args[3]);

        
        Dna dna = new Dna(count, img);


        for(int i = 0; i < generations; i++) {
            float heat = (generations+1)/(generations -i) + (float)0.01;
            dna = stepGeneration(dna, gensize, 4, heat);
        }

        System.out.println(dna);
        double cost = dna.cost();
        System.err.println(cost);

        System.exit(0);
    }


    /**
     * Make gensize mutations of mother, return the one with the best score
     */
    private static Dna stepGeneration(Dna mother, int gensize, int mutations, float rate){
        Dna best, newborn;
        best = mother;

        double bestscore = mother.cost();
        
        final int m = mutations;
        final float r = rate;
        F f = new F() {
                public Dna f(Object mom) {
                    return ((Dna)mom).modifyMutate(m, r);
                }
            };

        List<Dna> generation = fj.data.List.replicate(gensize, mother); 

        Strategy s = Strategy.executorStrategy(executorService);

        generation = (List<Dna>)s.parMap(f, generation)._1();
        

        for(Dna dna : generation) {
            if(dna.cost() < best.cost()) {
                best = dna;
            }
        }

        return best;
    }


}
