import java.util.stream.Stream;

@FunctionalInterface
public interface RandomPointFunction {
    Stream<double[]> generate(double[] center, Bounds w, double s);
}