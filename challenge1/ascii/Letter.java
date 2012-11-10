import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

class Letter {
    double x,y,rot,size;
    Color color;
    char letter;
    
    Letter(double x, double y, double rot, double size, Color color, char letter) {
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.size = size;
        this.color = color;
        this.letter = letter;
    }

    private static char randomLetter() {
        Random rng = new Random();
        char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char letter = alphabet[Math.abs(rng.nextInt(26))];
        return letter;
    }

    /**
     * Return a random Letter
     */
    public static Letter randomLetter(int width, int height) {
        Random rng = new Random();

        double x, y, rot, scale;
        float r, g, b, a;
        char letter = randomLetter();
     
        x = rng.nextDouble()*width;
        y = rng.nextDouble()*height;
        rot = rng.nextDouble()*2*Math.PI;
        scale = rng.nextDouble()*25;
        r = rng.nextFloat();
        g = rng.nextFloat();
        b = rng.nextFloat();
        a = rng.nextFloat();

        return new Letter(x, y, rot, scale, new Color(r,g,b,a), letter);
    }

    /**
     * Modify a letter by randomizing it a bit.
     */
    public static Letter mutateLetter(Letter l, int width, int height, float rate) {
        Random rng = new Random();

        float r,g,b,a;
        double x,y,rot,size;
        float[] comps = l.color.getComponents(new float[4]);
        
        x = l.x + rate * width * rng.nextDouble() - (rate * width / 2);
        y = l.y + rate * height * rng.nextDouble()- (rate * height / 2);

        //make sure that the coordinates don't go sailing off
        if(x < 0) x = 0;
        if(x > width) x = width;
        if(y < 0) y = 0;
        if(y > width) y = height;
        
        

        rot = l.rot + rate * Math.PI * 2 * rng.nextDouble() - (rate * Math.PI);
        size = l.size + rate * rng.nextDouble() - (rate/2);

        r = comps[0] + rate * rng.nextFloat() - (rate/2);
        g = comps[1] + rate * rng.nextFloat() - (rate/2);
        b = comps[2] + rate * rng.nextFloat() - (rate/2);
        a = comps[3] + rate * rng.nextFloat() - (rate/2);


        // check that the color components are in the correct ranges
        if(r>1) r = 1;
        if(b>1) b = 1;
        if(g>1) g = 1;
        if(a>1) a = 1;
        if(r<0) r = 0;
        if(b<0) b = 0;
        if(g<0) g = 0;
        if(a<0) a = 0;

        char letter = l.letter;
        if(rng.nextFloat() < rate) {
            letter = randomLetter();
        }

        return new Letter(x,y,rot, size, new Color(r,g,b,a), letter);

    }


    AffineTransform transform() {
        AffineTransform res = AffineTransform.getTranslateInstance(x,y);
        res.scale(size, size);
        res.rotate(rot);
        return res;
    }
    public String toString() {
        float[] c = color.getComponents(null);
        return letter+" "+x+" "+y+" "+rot+" "+size+" "+c[0]+" "+c[1]+" "+c[2]+" "+c[3];
    }
}
