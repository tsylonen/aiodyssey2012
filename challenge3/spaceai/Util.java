/**
 * Utility functions that don't really fit anywhere else
 */

import java.util.List;

public class Util {
    /**
     * Maximum distance from a planet in List a to a planet in list b
     */
    public static double maxDistance(List<Planet> a, List<Planet> b) {
        double max = 0.0;
        for(Planet i : a) {
            for(Planet j : b) {
                if (distance(i,j)>max) {
                    max = distance(i,j);
                } 
            }
        }
        return max;
    }

    /**
     * Euclidean distance of two planets
     */
    public static double distance(Planet a, Planet b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) +
                         (a.y - b.y) * (a.y - b.y) +
                         (a.z - b.z) * (a.z - b.z));
    }

    
}
