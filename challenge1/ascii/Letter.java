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

    /**
     * Return a random Letter
     */
    public static Letter randomLetter(int width, int height) {
        Random rng = new Random();

        double x, y, rot, scale;
        float r, g, b, a;
        char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char letter = alphabet[Math.abs(rng.nextInt()%26)];

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
    public Letter mutateLetter(int width, int height, float rate) {
        Random rng = new Random();

        float r,g,b,a,x,y,rot,size;
        float[] comps = this.color.getComponents(new float[4]);
        
        x = (float)this.x * (rate * rng.nextFloat() + 1);
        y = (float)this.y * (rate * rng.nextFloat() + 1);
        rot = (float)this.rot * (rate * rng.nextFloat() + 1);
        size = (float)this.size * (rate * rng.nextFloat() + 1);
        r = comps[0] * (rate * rng.nextFloat() + 1);
        g = comps[1] * (rate * rng.nextFloat() + 1);
        b = comps[2] * (rate * rng.nextFloat() + 1);
        a = comps[3] * (rate * rng.nextFloat() + 1);

        if(r>1) r = 1;
        if(b>1) b = 1;
        if(g>1) g = 1;
        if(a>1) a = 1;

        return new Letter(x,y,rot, size, new Color(r,g,b,a), this.letter);

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
