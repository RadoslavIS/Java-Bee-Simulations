import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.Callable;

/** Unit of work executed in parallel by the thread-pool.
 * Creates and runs an independent <code>Simulation</code> instance **/
// STYLE: parallel
public class SimulationTask implements Callable<SimulationResult> {
    //GOOD: OOP -> high class cohesion, sole purpose of this class is to wrap a simulation run for the parallel handler
    private final SimulationFactory factory;
    private final String configID;
    private final int runIndex;
    private final Random rng;

    public SimulationTask(SimulationFactory factory, String configID, int runIndex, Random rng) {
        if (factory == null) throw new NullPointerException("factory");
        this.factory = factory;
        this.configID = configID;
        this.runIndex = runIndex;
        this.rng = rng;
    }

    /** Creates a Simulation instance inside a new Thread, runs it and tallies the result */
    // STYLE: parallel -> runs the simulation in the calling worker thread.
    @Override
    public SimulationResult call() throws Exception {
        Simulation sim = factory.create(rng);

        if (Thread.currentThread().isInterrupted()) throw new InterruptedException("SimulationTask interrupted before start");

        sim.simulateTotal();

        return new SimulationResult(configID, runIndex,
                new LinkedHashMap<>(sim.getViewer().getStats4pollinator()),
                new LinkedHashMap<>(sim.getViewer().getStats4flower()));
    }
}
