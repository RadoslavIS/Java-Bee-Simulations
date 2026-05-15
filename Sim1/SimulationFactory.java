import java.util.Random;

/** Functional interface for Simulation factories*/
public interface SimulationFactory {
    /** Creates a new Simulation */
    Simulation create(Random rng);
}
