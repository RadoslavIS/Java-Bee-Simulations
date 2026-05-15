import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

@Author("Rado, Daniel")
public class Test {
    private static final int TOTAL_SIMULATION_RUNS = 3;
    private static final int MAX_VISITS_PER_DAY = 9;

    public static void main(String[] args) {
       ContextA();
       ContextB();
    }

    @Author("Rado")
    private static void ContextA() {
        Random rng = new Random();
        System.out.println("\n=== Context A: Simulation tests ===\n");

        for (int i = 0; i < TOTAL_SIMULATION_RUNS; i++) {
            int maxVisitsPerDay = rng.nextInt(2, MAX_VISITS_PER_DAY);
            System.out.printf("\n---Run %d with %d max visits per day---\n", i, maxVisitsPerDay);
            runSimulation(maxVisitsPerDay, rng);
        }
    }

    @Author("Daniel")
    private static void ContextB() {
        System.out.println("\n=== Context B ===\n");

        Class<?>[] classes = {
                Bee.class, Plant.class, Compatibility.class, Simulation.class, U.class, V.class, W.class, X.class, Y.class, Z.class,
                Set.class, EntityVisitor.class,
                ProgramPart.class, Author.class, ClientSideHistoryConstraint.class, Invariant.class, Postcondition.class, Precondition.class, ServerSideHistoryConstraint.class
        };

        //1

        System.out.println("====== 1 ======");

        for(Class<?> c : classes) {
            if(c.isAnnotationPresent(ProgramPart.class)) {
                System.out.println("Name of class: " + c.getName());
            }
        }

        System.out.println();

        //2

        System.out.println("====== 2 ======");

        for(Class<?> c : classes) {
            if(c.isAnnotationPresent(ProgramPart.class)) {
                if(c.isAnnotationPresent(Author.class)) {
                    Author author = c.getAnnotation(Author.class);
                    System.out.println(author.value() + " responsible for " + c.getName());
                }
            }
        }


        System.out.println();

        //3

        System.out.println("====== 3 ======");

        for(Class<?> c : classes) {
            if(c.getSuperclass() != null) {
                System.out.println("Class/Interface: " + c.getName() + " extends " + c.getSuperclass());
            } else {
                System.out.println("Class/Interface: " + c.getName());
            }

            if(c.isAnnotationPresent(Invariant.class)) {
                System.out.println("Invariant: " + Arrays.toString(c.getAnnotation(Invariant.class).value()));
            }

            if(c.isAnnotationPresent(ServerSideHistoryConstraint.class)) {
                System.out.println("Server side history constraint: " + Arrays.toString(c.getAnnotation(ServerSideHistoryConstraint.class).value()));
            }

            if(c.isAnnotationPresent(ClientSideHistoryConstraint.class)) {
                System.out.println("Client side history constraint: " + Arrays.toString(c.getAnnotation(ClientSideHistoryConstraint.class).value()));
            }

            System.out.println("Constructors: ");
            for(Constructor<?> ctor : c.getDeclaredConstructors()) {
                System.out.println(" " +ctor.toString());

                if(ctor.isAnnotationPresent(Precondition.class)) {
                    System.out.println("   Precondition: " + Arrays.toString(ctor.getAnnotation(Precondition.class).value()));
                }

                if(ctor.isAnnotationPresent(Postcondition.class)) {
                    System.out.println("   Postcondition: " + Arrays.toString(ctor.getAnnotation(Postcondition.class).value()));
                }
                System.out.println();
            }

            System.out.println("Methods: ");
            for(Method m : c.getMethods()) {
                System.out.println(" " +m.toString());

                if(m.isAnnotationPresent(Precondition.class)) {
                    System.out.println("   Precondition: " + Arrays.toString(m.getAnnotation(Precondition.class).value()));
                }

                if(m.isAnnotationPresent(Postcondition.class)) {
                    System.out.println("   Postcondition: " + Arrays.toString(m.getAnnotation(Postcondition.class).value()));
                }
            }
            System.out.println();
        }


        //4

        System.out.println("====== 4 ======");

        Map<String, Integer> nameCount = new LinkedHashMap<>();



        for (Class<?> c : classes) {
            if(c.isAnnotationPresent(Author.class)) {
                Author author = c.getAnnotation(Author.class);
                putInMap(nameCount, author.value(), 1);
            }
        }

        nameCount.forEach((k,v) -> System.out.println(k + ": " + v));


        System.out.println();

        //5

        System.out.println("====== 5 ======");

        nameCount.clear();

        for (Class<?> c : classes) {
            if(!c.isInterface()) {
                for(Constructor<?> ctor : c.getDeclaredConstructors()) {

                    if(ctor.isAnnotationPresent(Author.class)) {
                        Author author = ctor.getAnnotation(Author.class);
                        putInMap(nameCount, author.value(), 1);
                    }


                }

                for(Method m : c.getDeclaredMethods()) {
                    if(m.isAnnotationPresent(Author.class)) {
                        Author author = m.getAnnotation(Author.class);

                        putInMap(nameCount, author.value(), 1);
                    }
                }
            }
        }

        nameCount.forEach((k,v) -> System.out.println(k + ": " + v));

        System.out.println();

        //6

        System.out.println("====== 6 ======");

        nameCount.clear();

        for(Class<?> c : classes) {

            int count = 0;

            if(c.isAnnotationPresent(Invariant.class)) {
                count++;
            }

            if(c.isAnnotationPresent(ServerSideHistoryConstraint.class)) {
                count++;
            }

            if(c.isAnnotationPresent(ClientSideHistoryConstraint.class)) {
                count++;
            }

            if(c.isAnnotationPresent(Author.class) && count != 0) {
                Author author = c.getAnnotation(Author.class);

                putInMap(nameCount, author.value(), count);
            }

            for(Constructor<?> ctor : c.getDeclaredConstructors()) {

                int i = 0;

                if(ctor.isAnnotationPresent(Precondition.class)) {
                    i++;
                }

                if(ctor.isAnnotationPresent(Postcondition.class)) {
                    i++;
                }

                if(ctor.isAnnotationPresent(Author.class)) {
                    Author author = ctor.getAnnotation(Author.class);

                    putInMap(nameCount, author.value(), i);
                }

            }


            for(Method m : c.getDeclaredMethods()) {
                int i = 0;

                if(m.isAnnotationPresent(Precondition.class)) {
                    i++;
                }

                if(m.isAnnotationPresent(Postcondition.class)) {
                    i++;
                }

                if(m.isAnnotationPresent(Author.class)) {
                    Author author = m.getAnnotation(Author.class);

                    putInMap(nameCount, author.value(), i);
                }
            }

        }


        nameCount.forEach((k,v) -> System.out.println(k + ": " + v));
    }

    @Author("Daniel")
    private static void putInMap(Map<String, Integer> map, String author, int c) {
        if(map.containsKey(author)) {
            int old = map.get(author);
            map.replace(author, old + c);
        } else {
            map.put(author, c);
        }
    }

    @Author("Rado")
    private static void runSimulation(int maxVisitsPerDay, Random rng) {

        Simulation sim = new Simulation(maxVisitsPerDay, rng);
        sim.simulate();

        Aggregator agg = new Aggregator();

        sim.aggregate(agg);

        // Summary
        System.out.println("Bee visits and totals:");
        System.out.printf("  U bees: count: %d, total visits: %d, avg visits per plant: %.3f%n",
                agg.beeUCount, agg.beeUTotalVisits, ((double)agg.beeUTotalVisits / (agg.plantXCount + agg.plantYCount)));
        System.out.printf("  V bees: count: %d, total visits: %d, avg visits per plant: %.3f%n",
                agg.beeVCount, agg.beeVTotalVisits, ((double)agg.beeVTotalVisits / (agg.plantYCount + agg.plantZCount)));
        System.out.printf("  W bees: count: %d, total visits: %d, avg visits per plant: %.3f%n",
                agg.beeWCount, agg.beeWTotalVisits, ((double)agg.beeWTotalVisits / (agg.plantYCount + agg.plantZCount)));

        System.out.println("Plant visits and totals:");
        System.out.printf("  X plants: count: %d, total visits: %d, avg visits per bee: %.3f%n",
                agg.plantXCount, agg.plantXTotalVisits, ((double)agg.plantXTotalVisits / (agg.beeUCount + agg.beeWCount)));
        System.out.printf("  Y plants: count: %d, total visits: %d, avg visits per bee: %.3f%n",
                agg.plantYCount, agg.plantYTotalVisits, ((double)agg.plantYTotalVisits /(agg.beeUCount + agg.beeVCount)));
        System.out.printf("  Z plants: count: %d, total visits: %d, avg visits per bee: %.3f%n",
                agg.plantZCount, agg.plantZTotalVisits, ((double)agg.plantZTotalVisits / (agg.beeVCount + agg.beeWCount)));


        System.out.println(" total bees: " + (agg.beeUCount + agg.beeVCount + agg.beeWCount) +
                ", total plants = " + (agg.plantXCount + agg.plantYCount + agg.plantZCount));
    }

    @Author("Rado")
    private static class Aggregator implements EntityVisitor {
        // bee counts
        int beeUCount, beeVCount, beeWCount;
        int beeUTotalVisits,beeVTotalVisits, beeWTotalVisits;

        // plant counts
        int plantXCount, plantYCount, plantZCount;
        int plantXTotalVisits, plantYTotalVisits, plantZTotalVisits;

        @Override
        public void visit(U u) {
            beeUCount++;
            beeUTotalVisits += u.collectedFromX() + u.collectedFromY() + u.collectedFromZ();
        }

        @Override
        public void visit(V v) {
            beeVCount++;
            beeVTotalVisits += v.collectedFromX() + v.collectedFromY() + v.collectedFromZ();
        }

        @Override
        public void visit(W w) {
            beeWCount++;
            beeWTotalVisits += w.collectedFromX() + w.collectedFromY() + w.collectedFromZ();
        }

        @Override
        public void visit(X x) {
            plantXCount++;
            plantXTotalVisits += x.visitedByU() + x.visitedByV() + x.visitedByW();
        }

        @Override
        public void visit(Y y) {
            plantYCount++;
            plantYTotalVisits += y.visitedByU() + y.visitedByV() + y.visitedByW();
        }

        @Override
        public void visit(Z z) {
            plantZCount++;
            plantZTotalVisits += z.visitedByU() + z.visitedByV() + z.visitedByW();
        }
    }
}
