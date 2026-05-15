import com.sun.jdi.LocalVariable;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Test {
    /*
	contributions:
	Boris - general structure, Observation, Bee, WildBee, Test
	Daniel - LasioglossumCalceatum, OsmiaCornuta, AndrenaBucephala, Bumblebee
	Radoslav - SocialBee, SolitaryBee, CommunalBee, Honeybee, FlowerFly, Test

	structure explanation:
	Nothing, that is a subtype of Wasp, is a subtype of FlowerFly and vice versa,
	because FlowerFlies are not Wasps and Wasps are not FlowerFlies.
	OsmiaCornuta and AndrenaBucephala are not subtypes of SocialBee,
	because bees from these species don't display social behaviour.
	OsmiaCornuta, HoneyBee and BumbleBee are not subtypes of CommunalBee,
	because bees from these species don't display communal behaviour.
	HoneyBee is not a subtype of WildBee, because all honeybees are bred by humans.
	SocialBee is not a subtype of WildBee, because then HoneyBee would also have to be a subtype of WildBee, which it can't.
	LasioglossumCalceatum is not a subtype of CommunalBee, because bees from this specie don't display solitary behaviour.
	LasioglossumCalceatum, OsmiaCornuta, AndrenaBucephala, Honeybee and Bumblebee are not subtypes of one another,
	because they are distinct bee species.
	 */


    public static void main(String[] args){
        NavigableMap<LocalDateTime, Observation> allObservations = new TreeMap<>();

        LocalDateTime t1 = LocalDateTime.of(2025, 1, 1, 10, 12, 23);
        LocalDateTime t2 = LocalDateTime.of(2025, 2, 1, 11, 32, 42);
        LocalDateTime t3 = LocalDateTime.of(2025, 3, 2, 9, 35, 2);
        LocalDateTime t4 = LocalDateTime.of(2025, 3, 2, 10, 2, 45);
        LocalDateTime t5 = LocalDateTime.of(2025, 4, 3, 10, 9, 4);
        LocalDateTime t6 = LocalDateTime.of(2025, 6, 3, 11, 43, 1);
        LocalDateTime t7 = LocalDateTime.of(2025, 8, 3, 11, 59, 41);
        LocalDateTime t8 = LocalDateTime.of(2025, 5, 3, 11, 20, 1);

        Honeybee honey1 = new Honeybee(allObservations, t1, "fluttered by", 0);
        Honeybee honey2 = new Honeybee(allObservations, t2, "honey1 pollinated a flower", honey1);
        Honeybee honey3 = new Honeybee(allObservations, LocalDateTime.now(), "likes jazz",
                1, honey1);

        Bumblebee bumble1 = new Bumblebee(allObservations, t3, "danced with friends", 1,
                true, Bumblebee.BumblebeeCaste.WORKER);
        Bumblebee bumble2 = new Bumblebee(allObservations, LocalDateTime.now(), "bumble1 buzzed around",
                bumble1, true, Bumblebee.BumblebeeCaste.WORKER);

        AndrenaBucephala andrena1 = new AndrenaBucephala(allObservations, t4, "dug a hole", true, ObservedBehaviour.COMMUNAL);
        AndrenaBucephala andrena2 = new AndrenaBucephala(allObservations, LocalDateTime.now(), "hit a window", 2, false, ObservedBehaviour.COMMUNAL);

        OsmiaCornuta osmia1 = new OsmiaCornuta(allObservations, t5, "hid from a wasp", true, ObservedNesting.CAVITY);
        OsmiaCornuta osmia2 = new OsmiaCornuta(allObservations, t6, "drank a cup of water", osmia1, false, ObservedNesting.CAVITY);

        LasioglossumCalceatum lasio1 = new LasioglossumCalceatum(allObservations, t7, "overdosed on pollen", 4, true,
                ObservedBehaviour.SOCIAL, LasioglossumCalceatum.LasioglossumCalceatumCaste.FOUNDRESS, ObservedNesting.CAVITY);
        LasioglossumCalceatum lasio2 = new LasioglossumCalceatum(allObservations, LocalDateTime.now(), "ate from a grape", 5, lasio1, false,
                ObservedBehaviour.SOCIAL, LasioglossumCalceatum.LasioglossumCalceatumCaste.MALE, ObservedNesting.UNKNOWN);

        FlowerFly fly1 = new FlowerFly(allObservations, t8, "hovered by");
        FlowerFly fly2 = new FlowerFly(allObservations, LocalDateTime.now(), "ate my dinner");


        allObservations.forEach((time, obs) -> System.out.printf(" %s -> %d, %s (%s)\n\n", time, obs.hashCode(), obs.getComment(), obs.getClass().getSimpleName()));

        testLaterEarlier(honey3, "later/earlier");

        testSameBee(honey1, "Honeybee.sameBee");
        testSameBee(bumble1, "BumbleBee.sameBee");

        testWild(bumble2, "BumbleBee.wild", true);

        testSocial(honey1, "Honeybee.social");
        testSocial(bumble1, "BumbleBee.social");

        testCommunal(andrena1, "AndrenaBucephala.communal");
        testSolitary(osmia1, "OsmiaCornuta.solitary");
        testSocial(lasio1, "LasioglossumCalceatum.social");
    }

    private static void testLaterEarlier(Observation caller, String label) {
        System.out.println("=== " + label + " ===");
        System.out.println("later() from caller:");
        Iterator<Observation> laterIt = caller.later();
        while (laterIt.hasNext()) {
            Observation o = laterIt.next();
            System.out.printf(" - %s @ %s (%s)\n", o.getComment(), o.getTime(), o.getClass().getSimpleName());
        }
        System.out.println("earlier() from caller:");
        Iterator<Observation> earlierIt = caller.earlier();
        while (earlierIt.hasNext()) {
            Observation o = earlierIt.next();
            System.out.printf(" - %s @ %s (%s)\n", o.getComment(), o.getTime(), o.getClass().getSimpleName());
        }
        System.out.println();
    }

    private static void testSameBee(Bee caller, String label) {
        System.out.println("=== " + label + " ===");
        Iterator<Bee> it = caller.sameBee();
        boolean runtimeMatch = true;
        while (it.hasNext()) {
            Bee b = it.next();
            if (b.getClass() != caller.getClass()) runtimeMatch = false;
        }
        System.out.printf("All returned elements have same runtime class as caller? %b \n\n", runtimeMatch);
    }

    private static void testWild(WildBee caller, String label, boolean isBred) {
        System.out.println("=== " + label + " ===");
        Iterator<WildBee> it = caller.wild(isBred);
        boolean runtimeMatch = true;
        while (it.hasNext()) {
            WildBee b = it.next();
            if (b.getClass() != caller.getClass()) runtimeMatch = false;
        }
        System.out.printf("All returned elements have same runtime class as caller? %b \n\n", runtimeMatch);
    }

    private static void testSocial(SocialBee caller, String label) {
        System.out.println("=== " + label + " ===");
        Iterator<SocialBee> it = caller.social();
        boolean runtimeMatch = true;
        while (it.hasNext()) {
            SocialBee b = it.next();
            if (b.getClass() != caller.getClass()) runtimeMatch = false;
        }
        System.out.println("All returned elements have same runtime class as caller? " + runtimeMatch);
        System.out.println();
    }

    private static void testCommunal(CommunalBee caller, String label) {
        System.out.println("=== " + label + " ===");
        Iterator<CommunalBee> it = caller.communal();
        boolean runtimeMatch = true;
        while (it.hasNext()) {
            CommunalBee b = it.next();
            if (b.getClass() != caller.getClass()) runtimeMatch = false;
        }
        System.out.println("All returned elements have same runtime class as caller? " + runtimeMatch);
        System.out.println();
    }

    private static void testSolitary(SolitaryBee caller, String label) {
        System.out.println("=== " + label + " ===");
        Iterator<SolitaryBee> it = caller.solitary();
        boolean runtimeMatch = true;
        while (it.hasNext()) {
            SolitaryBee b = it.next();
            if (b.getClass() != caller.getClass()) runtimeMatch = false;
        }
        System.out.println("All returned elements have same runtime class as caller? " + runtimeMatch);
        System.out.println();
    }
}
