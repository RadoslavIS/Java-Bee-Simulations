import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;

/* Contributions:
    -Boris: BeeAlgorithm, parameters for test cases in Test
    -Radoslav: BeeAlgorithm, BAParams, Bounds, ObjectiveFunction
    -Daniel: Test structure, RandomPointFunction
 */


public class Test {
    public static void main(String[] args) {

        // a Anzahl der Parameter der zu untersuchenden Funktion (a > 0)
        int a = 1;

        // f die zu untersuchende Funktion
        ObjectiveFunction f = x -> Math.pow(x[0],4)/20 - Math.pow(x[0], 2);
        // w zu untersuchende Wertebereiche aller Argumente
        Bounds w = new Bounds(new double[]{-4000.0}, new double[]{4000.0});

        // c Vergleichsfunktion, die die bessere von zwei Zahlen ermittelt
        Comparator<Double> c = Comparator.naturalOrder();

        // t Anzahl der Suchschritte nach denen abgebrochen wird (t > 0)
        int t = 2000;

        // n Anzahl der Kundschafterinnen
        int n = 200;

        // m Anzahl der Felder, die (weiter) untersucht werden (m < n)
        int m = 50;

        // e Anzahl exzellenter Felder, die sehr genau untersucht werden (e < m)
        int e = 25;

        // p Anzahl der für ein exzellentes Feld rekrutierten Bienen
        int p = 50;

        // q Anzahl der für ein anderes Feld rekrutierten Bienen (q < p)
        int q = 40;

        // s Größe der Felder relativ zum untersuchten Bereich
        double s = 0.001;

        // r Anzahl der am Ende zurückzugebenden besten gefundenen Stellen
        int r = 6;

        BAParams params = new BAParams(a,f,w,c,t,n,m,e,p,q,s,r);

        ObjectiveFunction f2 = x -> Math.pow(x[0]*x[0] + x[1]*x[1] - 1, 2);

        Bounds w2 = new Bounds(
                new double[]{-2.0, -2.0},
                new double[]{ 2.0,  2.0}
        );

        BAParams params2 = new BAParams(2, f2, w2, Comparator.naturalOrder(),2000, 40, 20, 10,6,4, 0.25, 6);

        ObjectiveFunction f3 = x -> Math.cos(x[0]) + Math.cos(x[1]) + Math.cos(x[2]);

        Bounds w3 = new Bounds(
                new double[]{-15.0, -15.0, -15.0},
                new double[]{ 15.0,  15.0,  15.0}
        );

        BAParams params3 = new BAParams(3, f3, w3, Comparator.reverseOrder(), 2000, 60, 30, 15, 10, 5, 0.005, 9);

        ObjectiveFunction f4 = x -> Math.sin(x[0]);

        Bounds w4 = new Bounds(
                new double[]{Math.toRadians(-1800.0)},
                new double[]{Math.toRadians(1800.0)}
        );

        BAParams params4 = new BAParams(1, f4, w4, Comparator.reverseOrder(), 2000, 45, 20, 10, 5, 3, 0.05, 10);

        ObjectiveFunction f5 = x -> Math.abs(x[0])*Math.pow(Math.abs(x[0]) - 1, 2);

        Bounds w5 = new Bounds(
                new double[]{-300.0, -300.0},
                new double[]{ 30.0,  30.0}
        );

        BAParams params5 = new BAParams(2, f5, w5, Comparator.naturalOrder(), 2000, 100, 50, 30, 20, 10, 0.0001, 10);


        System.out.println("=========== Test 1 ===========");
        System.out.println("f(x) = x^2");
        List<AbstractMap.SimpleEntry<double[], Double>> result = BeeAlgorithm.APPLY.apply(params);
        printResults(result);


        System.out.println("=========== Test 2 ===========");
        System.out.println("f(x,y) = (x^2 + y^2 - 1)^2");
        List<AbstractMap.SimpleEntry<double[], Double>> result2 = BeeAlgorithm.APPLY.apply(params2);
        printResults(result2);

        System.out.println("=========== Test 3 ===========");
        System.out.println("f(x,y,z) = cos(x) + cos(y) + cos(z)");
        List<AbstractMap.SimpleEntry<double[], Double>> result3 = BeeAlgorithm.APPLY.apply(params3);
        printResults(result3);

        System.out.println("=========== Test 4 ===========");
        System.out.println("f(x) = sin(x)\n--Note: Found parameters are in radians--");
        List<AbstractMap.SimpleEntry<double[], Double>> result4 = BeeAlgorithm.APPLY.apply(params4);
        printResults(result4);

        System.out.println("=========== Test 5 ===========");
        System.out.println("f(x,y) = |x|(|x| - 1)^2");
        List<AbstractMap.SimpleEntry<double[], Double>> result5 = BeeAlgorithm.APPLY.apply(params5);
        printResults(result5);
    }

    private static void printResults(List<AbstractMap.SimpleEntry<double[], Double>> result) {
        result.forEach(x -> {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            boolean flag = true;
            for (double d : x.getKey()) {
                if(flag) {
                    sb.append(" ").append(d);
                    flag = false;
                } else {
                    sb.append(", ").append(d);
                }
            }
            sb.append(']');
            System.out.println(sb + " -> " + x.getValue().toString());
        });
    }
}