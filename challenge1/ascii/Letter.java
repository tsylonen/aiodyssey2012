import java.awt.*;
import java.awt.geom.*;
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
