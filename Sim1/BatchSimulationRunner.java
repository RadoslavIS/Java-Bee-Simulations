import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Runs simulations in parallel and returns a list of results **/
// STYLE: parallel -> parallel simulation runs using a fixed thread pool
public class BatchSimulationRunner {
    private final ExecutorService pool;

    public BatchSimulationRunner(int threads) {
        //BAD: OOP object coupling -> in case we sometime want to switch to another technology such as virtual threads, we may need to overhaul the class methods
        this.pool = Executors.newFixedThreadPool(threads > 0 ? threads : Runtime.getRuntime().availableProcessors());
    }

    /** Runs 'numRuns' simulations using the given factory and Random instance **/
    // STYLE: parallel -> submission and collection logic
    // The factory and SimulationTask must not share mutable state with the caller or other tasks
    public List<SimulationResult> runBatch(SimulationFactory factory, String configID,
                                           int numRuns, long baseSeed)
            throws InterruptedException {
        CompletionService<SimulationResult> ecs = new ExecutorCompletionService<>(pool);

        // submit tasks with different rngs
        for (int i = 0; i < numRuns; i++) {
            long seed = baseSeed + i * 1000L;
            ecs.submit(new SimulationTask(factory, configID, i, new Random(seed)));
        }

        // collect results in completion order
        List<SimulationResult> results = new ArrayList<>();
        for (int i = 0; i < numRuns; i++) {
            Future<SimulationResult> complete;
            try {
                complete = ecs.take();
                results.add(complete.get());
            } catch (ExecutionException e) {
                System.err.printf("Simulation task failed: %s", e.getCause());
            }
        }

        return results;
    }

    public void shutdown() { pool.shutdown(); }

    public void shutdownNow() { pool.shutdownNow(); }
}
