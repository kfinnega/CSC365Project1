import java.util.HashMap;

public class ProjectPlayground2 {

    public static double calculateCosSim(double num, double den) {
        return num/den;
    }

    public static double magnitude(HashMap<String,Double> map) {
        double mag= 0.0;
        for (double value : map.values()) {
            mag += value * value;
        }

        return Math.sqrt(mag);
    }

    public static double multiplyMagnitudes(HashMap<String, Double> biz1, HashMap<String, Double> biz2) {
        double magBiz1 = magnitude(biz1);
        double magBiz2 = magnitude(biz2);
        return magBiz1 * magBiz2;
    }

    public static double dotProduct(HashMap<String,Double> map1, HashMap<String, Double> map2) {
        double result = 0.0;
        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {
                result += map1.get(key) * map2.get(key);
            }
        }
        return result;
    }


    public static void main(String[] args) {
        int count = 0;
        double mag = 0;
        double[] a = {0.0, 0.009542425094393249, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.009542425094393249, 0.0, 0.009542425094393249, 0.009542425094393249, 0.0, 0.009542425094393249, 0.009542425094393249, 0.0, 0.009542425094393249, 0.009542425094393249, 0.009542425094393249, 0.0, 0.0, 0.009542425094393249, 0.0, 0.019084850188786497, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.009542425094393249, 0.019084850188786497, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.019084850188786497, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0};
        double[] b = {0.0, 0.009542425094393249, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.009542425094393249, 0.0, 0.009542425094393249, 0.009542425094393249, 0.0, 0.009542425094393249, 0.009542425094393249, 0.0, 0.009542425094393249, 0.009542425094393249, 0.009542425094393249, 0.0, 0.0, 0.009542425094393249, 0.0, 0.019084850188786497, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.009542425094393249, 0.019084850188786497, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.019084850188786497, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0, 0.0, 0.0, 0.009542425094393249, 0.0, 0.009542425094393249, 0.0, 0.0, 0.0};

        double[] arr1 = {1.0,2.0,3.0};
        double[] arr2 = {4.0,5.0,6.0};

        HashMap<String,Double> map1 = new HashMap<>();
        HashMap<String,Double> map2 = new HashMap<>();
        map1.put("the", .53);
        map1.put("need", .743);
        map1.put("for", .56);
        map2.put("the", .834);
        map2.put("need", .67);
        map2.put("for", .023);
        map1.put("speed", .03);

        double dot = dotProduct(map1,map2);
        double denom = multiplyMagnitudes(map1,map2);
        System.out.println(calculateCosSim(dot,denom));

        //System.out.println(cosineSimilarity(map1,map2));

        //System.out.println(dotProduct(map1,map2));


    }
}
