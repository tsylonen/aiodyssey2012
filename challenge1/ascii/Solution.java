import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;
import java.awt.*;

/**
 * Contains a very bad example solution.
 */
public class Solution {
	/**
	 * Takes as parameters the original image and characted count
	 */
	public static void main(String[] args) throws IOException {
		BufferedImage img = ImageIO.read(new File(args[0]));
		int count = Integer.parseInt(args[1]);

		Letter[] res = new Letter[count];
		for(int i=0; i<count; ++i) {
			double fi = (double)i / count;
			double width = img.getWidth(), height = img.getHeight();
			Letter l = new Letter(fi*width, fi*height, 2*Math.PI*fi, 1, Color.red, 'a');
			res[i] = l;
		}

		for(Letter l : res) {
			System.out.println(l);
		}

		System.err.println("Solution cost: "+Ascii.cost(img, Arrays.asList(res)));
	}
}
