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
    private static int executorCount = 1;
    private static int mutationcount;


    public static void main(String[] args) throws IOException {

        img = ImageIO.read(new File(args[0]));
        int count = Integer.parseInt(args[1]);
        int generations = Integer.parseInt(args[2]);
        int gensize = Integer.parseInt(args[3]);

        
        Dna dna = new Dna(count, img);


        for(int i = 0; i < generations; i++) {
            dna = stepGeneration(dna, gensize, 2);
        }

        System.out.println(dna);
        double cost = dna.cost();
        System.err.println(cost);

        System.exit(0);
    }


    /**
     * Make gensize mutations of mother, return the one with the best score
     */
    private static Dna stepGeneration(Dna mother, int gensize, int rate){
        Dna best, newborn;
        best = mother;

        double bestscore = mother.cost();
        
        final int r = rate;
        final Dna m = mother;
        F f = new F() {
                public Dna f(Object mom) {
                    return ((Dna)mom).mutate(r);
                }
            };

        //Dna[] generation = new Dna[gensize];
        List<Dna> generation = fj.data.List.replicate(gensize, mother); 
        // for(int i = 0; i < gensize; i++) {
        //     generation[i] = mother;
        // }

        ExecutorService executorService = Executors.newFixedThreadPool(executorCount);
        Strategy s = Strategy.executorStrategy(executorService);

        // for(int i = 0; i < gensize; i++) {
        //     newborn = mother.mutate(rate);

        //     if(newborn.cost() < bestscore) {
        //         best = newborn;
        //     }
        // }

        generation = (List<Dna>)s.parMap(f, generation)._1();
        

        for(Dna dna : generation) {
            if(dna.cost() < best.cost()) {
                best = dna;
            }
        }
        return best;
    }


}
