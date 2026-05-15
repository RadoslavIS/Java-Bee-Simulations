import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {

    /*
    contributions:
    Radoslav: general structure, custom container classes, Modifiable, Ordered, OrdSet, ISet
    Boris: general structure, OSet, MSet, Ordering, HoneyBee, Num
    Daniel: Test, WildBee, Bee

     */


    public static void main(String[] args) {

        //1
        Num[] elements = {new Num(1), new Num(2), new Num(3), new Num(4)};

        Bee[] bees = {new Bee(LocalDateTime.of(2025, 11, 23, 10, 0), "Bumblebee", 1),
                new WildBee(LocalDateTime.of(2025, 3, 15, 7, 42), "Osmia Cornuta", 2, 100)
        };
        WildBee[] wildBees = {new WildBee(LocalDateTime.of(2025, 8, 10, 14, 30), "Andrena", 4, 120),
                new WildBee(LocalDateTime.of(2025, 5, 20, 9, 33), "Green bea", 8, 110)
        };
        HoneyBee[] honeyBees = {new HoneyBee(LocalDateTime.of(2025, 5, 20, 9, 0), "Apis dorsata", 5, "Honey"),
                new HoneyBee(LocalDateTime.of(2025, 8, 19, 13, 12), "Apis florea", 3, "Honey")
        };


        ISet<Num> INum = new ISet<>(null);
        OSet<Num> ONum = new OSet<>(null);

        fillSet(INum, elements);
        fillSet(ONum, elements);

        ISet<Bee> IBee = new ISet<>(null);
        OSet<Bee> OBee = new OSet<>(null);

        fillSet(IBee, bees);
        fillSet(OBee, bees);


        ISet<WildBee> IWildBee = new ISet<>(null);
        OSet<WildBee> OWildBee = new OSet<>(null);

        fillSet(IWildBee, wildBees);
        fillSet(OWildBee, wildBees);

        ISet<HoneyBee> IHoneyBee = new ISet<>(null);
        OSet<HoneyBee> OHoneyBee = new OSet<>(null);

        fillSet(IHoneyBee, honeyBees);
        fillSet(OHoneyBee, honeyBees);


        MSet<Num, Num> mNumNum = new MSet<>(null);
        fillSet(mNumNum, elements);

        MSet<WildBee, Integer> mWildBeeInt = new MSet<>(null);
        fillSet(mWildBeeInt, wildBees);

        MSet<HoneyBee, String> mHoneyBeeString = new MSet<>(null);
        fillSet(mHoneyBeeString, honeyBees);

        //2

        ISet<Bee> a1 = IBee;
        OSet<Bee> a2 = OBee;

        MSet<WildBee, Integer> b1 = mWildBeeInt;
        MSet<HoneyBee, String> b2 = mHoneyBeeString;

        OSet<WildBee> c1 = OWildBee;
        ISet<HoneyBee> c2 = IHoneyBee;

        List<WildBee> wildList = new ArrayList<>();
        Iterator<WildBee> itC1 = c1.iterator();
        while(itC1.hasNext()) {
            WildBee wb = itC1.next();
            wb.length();
            wildList.add(wb);
        }

        for(int i=0;i<wildList.size();i++){
            for(int j=0;j<wildList.size();j++){
                if(i==j) continue;
                WildBee x = wildList.get(i);
                WildBee y = wildList.get(j);
                if(c1.before(x,y) != null){
                    try { a1.setBefore(x, y); } catch(Exception ignored){}
                    try { b1.setBefore(x, y); } catch(Exception ignored){}
                }
            }
        }

        List<HoneyBee> honeyList = new ArrayList<>();
        Iterator<HoneyBee> itC2 = c2.iterator();
        while(itC2.hasNext()) {
            HoneyBee hb = itC2.next();
            hb.sort();
            honeyList.add(hb);
        }

        for(int i=0;i<honeyList.size();i++){
            for(int j=0;j<honeyList.size();j++){
                if(i==j) continue;
                HoneyBee x = honeyList.get(i);
                HoneyBee y = honeyList.get(j);
                if(c2.before(x,y) != null){
                    try { a2.setBefore(x, y); } catch(Exception ignored){}
                    try { b2.setBefore(x, y); } catch(Exception ignored){}
                }
            }
        }

        //3

        ONum.check(INum);
        ONum.check(mNumNum);
        ONum.checkForced(INum);
        ONum.checkForced(mNumNum);


        INum.check(ONum);
        INum.check(mNumNum);
        INum.checkForced(ONum);
        INum.checkForced(mNumNum);


        IWildBee.check(IBee);
        IWildBee.check(mWildBeeInt);
        IWildBee.checkForced(IBee);
        IWildBee.checkForced(mWildBeeInt);
        
        OHoneyBee.checkForced(IBee);

        //4
        ISet<Num> ISet1 = new ISet<>(null);
        OSet<Num> OSet2 = new OSet<>(null);
        MSet<WildBee, Integer> mNum = new MSet<>(null);

        // OSet subtype of ISet
        ISet<Num> test1 = OSet2;

        // MSet subtype of  ISet and OSet
        ISet<WildBee> test2 = mNum;
        OSet<WildBee> test3 = mNum;

        //5

        // Num Sets
        System.out.println("\nNum Sets:");
        System.out.println("ISet<Num> elements: ");
        INum.iterator().forEachRemaining(System.out::println);
        System.out.println("OSet<Num> elements: ");
        ONum.iterator().forEachRemaining(System.out::println);
        System.out.println("MSet<Num, Num> elements: ");
        mNumNum.iterator().forEachRemaining(System.out::println);

        mNumNum.minus(new Num(2));
        mNumNum.plus(new Num(4));
        System.out.println("MSet<Num,Num> after plus/minus:");
        mNumNum.iterator().forEachRemaining(System.out::println);

        // Bee Sets
        System.out.println("\nBee Sets:");
        System.out.println("\nISet<Bee> Elements:");
        IBee.iterator().forEachRemaining(b -> System.out.println(b.toString()));
        System.out.println("OSet<Bee> Elements:");
        OBee.iterator().forEachRemaining(b -> System.out.println(b.toString()));

        //WildBee Sets
        System.out.println("\nWildBee Sets:");
        System.out.println("ISet<WildBee> length:");
        IWildBee.iterator().forEachRemaining(wb -> System.out.println(wb.toString()));
        System.out.println("OSet<WildBee> length:");
        OWildBee.iterator().forEachRemaining(wb -> System.out.println(wb.toString()));
        System.out.println("MSet<WildBee,Integer>");
        mWildBeeInt.iterator().forEachRemaining(wb -> System.out.println(wb.toString()));

        mWildBeeInt.minus(10);
        mWildBeeInt.plus(15);
        System.out.println("MSet<WildBee,Integer> after plus/minus:");
        mWildBeeInt.iterator().forEachRemaining(System.out::println);

        //HoneyBee Sets
        System.out.println("\nHoneyBee Sets:");
        System.out.println("ISet<HoneyBee> sort:");
        IHoneyBee.iterator().forEachRemaining(HoneyBee::sort);
        IHoneyBee.iterator().forEachRemaining(hb -> System.out.println(hb.toString()));
        System.out.println("OSet<HoneyBee> sort:");
        OHoneyBee.iterator().forEachRemaining(HoneyBee::sort);
        OHoneyBee.iterator().forEachRemaining(hb -> System.out.println(hb.toString()));
        System.out.println("MSet<HoneyBee,String> sort:");
        mHoneyBeeString.iterator().forEachRemaining(HoneyBee::sort);
        mHoneyBeeString.iterator().forEachRemaining(hb -> System.out.println(hb.toString()));


    }
    public static <E> void fillSet(ISet<E> set, E[] elements) {
        if (set == null || elements == null || elements.length == 0) return;

        for (int i = 0; i < elements.length - 1; i++) {
            try {
                set.setBefore(elements[i], elements[i + 1]);
            } catch (IllegalArgumentException ignored) {}
        }
    }

}
