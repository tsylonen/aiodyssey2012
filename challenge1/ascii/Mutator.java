import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import java.awt.*;

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

    public static void main(String[] args) throws IOException {
        rng = new Random();

        img = ImageIO.read(new File(args[0]));
        int count = Integer.parseInt(args[1]);
        int generations = Integer.parseInt(args[2]);
        int gensize = Integer.parseInt(args[3]);

        Letter[] res = createRandom(count);
 
        for(int i = 0; i < generations; i++) {
            res = stepGeneration(res, gensize, 1);
        }

        for(Letter l : res) {
            System.out.println(l);
        }

        System.err.println("Solution cost: "+Ascii.cost(img, Arrays.asList(res)));
    }

    private static Letter[] createRandom(int letters) {
        Letter[] res = new Letter[letters];
        for(int i = 0; i < letters; i++) {
            res[i] = randomLetter();
        }
        return res;
    }

    /**
     * Return a random Letter
     */
    private static Letter randomLetter() {
            double x, y, rot, scale;
            float r, g, b, a;
            char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
            char letter = alphabet[Math.abs(rng.nextInt()%26)];

            x = rng.nextDouble()*730;
            y = rng.nextDouble()*547;
            rot = rng.nextDouble()*2*Math.PI;
            scale = rng.nextDouble()*10;
            r = rng.nextFloat();
            g = rng.nextFloat();
            b = rng.nextFloat();
            a = rng.nextFloat();

            return new Letter(x, y, rot, scale, new Color(r,g,b,a), letter);
    }

    /**
     * Make a copy of mother but randomize some letters
     */
    private static Letter[] mutate(Letter[] mother, int mutations) {
        Letter[] ret = new Letter[mother.length];

        for(int i = 0; i < mother.length; i++) {
            ret[i] = mother[i];
        }

        for(int i = 0; i < mutations; i++) {
            int ind = Math.abs(rng.nextInt()%mother.length);
            ret[i] = randomLetter();
        }
        return ret;
    }

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


    /**
     * Make gensize mutations of mother, return the one with the best score
     */
    private static Letter[] stepGeneration(Letter[] mother, int gensize, int mutations){
        Letter[] best, newborn;
        best = mother;
        double bestscore = score(mother);

        for(int i = 0; i < gensize; i++) {
            newborn = mutate(mother, mutations);

            if(score(newborn) < bestscore) {
                best = newborn;
            }
        }
        return best;
    }

    private static double score(Letter[] ascimg) {
        return Ascii.approxCost(img, Arrays.asList(ascimg));
    }
}
