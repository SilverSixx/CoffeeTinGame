import utils.TextIO;

import java.util.*;

/**
 *  @overview A program that performs the coffee tin game on a
 *    tin of beans and display result on the standard output.
 *
 */
public class CoffeeTinGame {
    /** constant value for the green bean*/
    private static final char GREEN = 'G';
    /** constant value for the blue bean*/
    private static final char BLUE = 'B';
    /** constant for removed beans */
    private static final char REMOVED = '-';
    /** the null character*/
    private static final char NULL = '\u0000';

    // static initializer beansbag
    private static final char[] BeansBag = {
        BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE, BLUE,
        REMOVED,REMOVED,REMOVED,REMOVED,REMOVED,REMOVED,REMOVED,REMOVED,REMOVED,REMOVED,
        GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN, GREEN,GREEN,
        };

    /**
     * the main procedure
     * @effects
     *    initialise a coffee tin
     *    {@link TextIO#putf(String, Object...)}: print the tin content
     *    {@link @tinGame(char[])}: perform the coffee tin game on tin
     *    {@link TextIO#putf(String, Object...)}: print the tin content again
     *    if last bean is correct
     *      {@link TextIO#putf(String, Object...)}: print its colour
     *    else
     *      {@link TextIO#putf(String, Object...)}: print an error message
     */
    public static void main(String[] args) {

        // initialise some beans
        char[][] tins = {
                {BLUE, BLUE, BLUE, GREEN, GREEN},
                {BLUE, BLUE, BLUE, GREEN, GREEN, GREEN},
                {GREEN},
                {BLUE},
                {BLUE, GREEN}
        };

        for (int i = 0; i < tins.length; i++) {
            char[] tin = tins[i];

            // expected last bean
            // p0 = green parity /\
            // (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)
            // count number of greens
            int greens = 0;
            for (char bean : tin) {
                if (bean == GREEN)
                    greens++;
            }
            // expected last bean
            final char last = (greens % 2 == 1) ? GREEN : BLUE;

            // print the content of tin before the game
            System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(tin));

            // perform the game
            // get actual last bean
            char lastBean = tinGame(tin);
            // lastBean = last \/ lastBean != last

            // print the content of tin and last bean
            System.out.printf("tin after: %s %n", Arrays.toString(tin));

            // check if last bean as expected and print
            if (lastBean == last) {
                System.out.printf("last bean: %c%n", lastBean);
            } else {
                System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
            }
        }
    }

    /**
     * Performs the coffee tin game to determine the colour of the last bean
     *
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects <pre>
     *   take out two beans from tin
     *   if same colour
     *     throw both away, put one blue bean back
     *   else
     *     put green bean back
     *   let p0 = initial number of green beans
     *   if p0 = 1
     *     result = `G'
     *   else
     *     result = `B'
     *   </pre>
     */
    public static char tinGame(char[] tin) {
        while (hasAtLeastTwoBeans(tin)) {
            // take two beans from tin
            char[] twoBeans = takeTwo(tin);
            // process beans to update tin
            if (twoBeans != null) {
                updateTin(tin, twoBeans);
            }

        }
        return anyBean(tin);
    }

    /**
     * @effects
     *  if tin has at least two beans
     *    return true
     *  else
     *    return false
     */
    private static boolean hasAtLeastTwoBeans(char[] tin) {
        int count = 0;
        for (char bean : tin) {
            if (bean != REMOVED) {
                count++;
            }

            if (count >= 2) // enough beans
                return true;
        }

        // not enough beans
        return false;
    }

    /**
     * This method takes a tin of beans as parameter and returns new 2 beans
     * @param tin a tin of beans
     * @requires tin has at least 2 beans left
     * @modifies tin
     * @effects
     *  remove any two beans from tin and return them
     */
    private static char[] takeTwo(char[] tin) {
        if(hasAtLeastTwoBeans(tin)){
            // Call takeOne method to get the first bean
            char first = takeOne(tin);
            // Call takeOne method again to get the second bean
            char second = takeOne(tin);
            // Return a new array with the first and second beans
            return new char[]{first, second};
        }
        return null;
    }

    /**
     * This method takes an array of characters representing a tin of beans, counts the number of beans in the tin,
     *      and randomly selects and returns one of the non-removed beans from the tin.
     *
     * @param tin the tin of beans from which to be processed
     * @effects
     *      return the selected bean, or NULL if all beans have already been removed
     */
    public static char takeOne(char[] tin) {
        // Count the number of beans in the tin
        int numBeans = 0;
        for (char bean : tin) {
            if (bean != REMOVED) {
                numBeans++;
            }
        }
        // If there are no beans, return NULL
        if(numBeans == 0)   return NULL;
        while(true){
            int index = randInt(tin.length);
            char b = tin[index];
            if(b != REMOVED){
                tin[index] = REMOVED;
                return b;
            }
        }
    }

    /**
     * get a random integer number in the given range from 0 to n (but not equal n)
     *
     * @requires int n /\ n > 0
     * @effects return a random integer from 0 to n
     */
    public static int randInt(int n) {
        Random rand = new Random();
        return rand.nextInt(n);
    }

    /**
     * Returns a randomly selected bean of the given type from the given array of beans, and marks it as removed.
     *
     * @param beansBag the array of beans from which to select a bean
     * @param bean the type of bean to select
     * @modifies tin
     * @effects
     *   remove the same bean as in parameter from tin and return it
     */
    public static char getBean(char[] beansBag, char bean) {
        int numBeans = 0;
        for (char c : beansBag){
            if (c == bean) {
                numBeans++;
            }
        }
        if (numBeans == 0) return NULL; // Bean not found
        int index = randInt(numBeans) + 1; // Randomly select a bean to remove
        for (int i = 0; i < beansBag.length; i++) {
            if (beansBag[i] == bean) {
                index--;
                if (index == 0) {
                    beansBag[i] = REMOVED; // Mark the bean as removed
                    return bean;
                }
            }
        }
        return NULL;
    }

    /**
     * remove one of the two beans (or replacing both) base on the color
     *
     * @param tin an array of characters representing a tin of beans
     * @param twoBeans an array of characters containing two beans that need to be updated in the tin
     * @requires tin is not null /\ twoBeans is not null /\
     *          twoBeans.length == 2
     * @modifies tin
     * @effects if both of the beans are the same color remove both the beans, put a
     *          blue beans from beansbag back else remove the blue bean put the green bean from twobeans back
     */
    public static void updateTin(char[] tin, char[] twoBeans) {
        if(twoBeans[0] == twoBeans[1]){
            char blueBeanFromBeansBag = getBean(BeansBag,BLUE);
            putIn(tin, blueBeanFromBeansBag);
        }
        else{
            char greenBeanFromTwoBeans = getBean(twoBeans,GREEN);
            putIn(tin, greenBeanFromTwoBeans);
        }

    }


    /**
     * @requires tin has vacant positions for new beans
     * @modifies tin
     * @effects
     *   place bean into any vacant position in tin
     */
    private static void putIn(char[] tin, char bean) {
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == REMOVED) { // vacant position
                tin[i] = bean;
                break;
            }
        }
    }

    /**
     * @effects
     *  if there are beans in tin
     *    return any such bean
     *  else
     *    return '\u0000' (null character)
     */
    private static char anyBean(char[] tin) {
        for (char bean : tin) {
            if (bean != REMOVED) {
                return bean;
            }
        }
        // no beans left
        return NULL;
    }

}
