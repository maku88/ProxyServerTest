package ProxyServer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 20.07.13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class LotteryTest {

    @Data
    @AllArgsConstructor
    private class Sections {
        private String sectionNumber;
        private double probability;
        private int tagNumber;
        @Getter private final List<String> listOfTags = new ArrayList<String>();

        public void addToList(String s) {
            if( s == null ) throw new NullPointerException("null");
            listOfTags.add(s);
        }

        @Override
        public String toString() {
            return "Sections{" +
                    "sectionNumber='" + sectionNumber + '\'' +
                    ", probability=" + probability +
                    ", tagNumber=" + tagNumber +
                    ", listOfTagsSize=" + listOfTags.size() +
                    '}';
        }
    }


    private int tagNumber = 10000;
    private int pozostalo = tagNumber;

    private Random random = new Random();
    private List<Integer> sectionsSize = new ArrayList<Integer>();
    private boolean flag = true;
    private double pozostaloProb = 1;
    private List<Sections> listOfSections = new ArrayList<Sections>();
    private List<String> tagSet = new ArrayList<String>();

    @Before
    public void initTagSet() {

       for(int i = 1; i<=tagNumber;i++ ) {
           String tag = "Znacznik "+i;
//           System.out.println(tag);
           tagSet.add(tag);


       }
    }



    @Test
    public void main() {
        generateSections();

      int x = 1;
      for(int i : sectionsSize) {

          Sections s = new Sections("Sekcja : " + x,0,i);
          listOfSections.add(s);

          x++;
      }

      for(int i = 0; i<listOfSections.size();i++) {
          Sections s = listOfSections.get(i);

          boolean isLastElem = false;
          if(i == listOfSections.size()-1) isLastElem = true;

          s.setProbability(generateProbForSection(isLastElem));
      }



        for(Sections s : listOfSections) {

            for(int a = 1; a <= s.getTagNumber(); a++) {
                String tag = tagSet.get(random.nextInt(tagSet.size()));
                tagSet.remove(tag);
                s.addToList(tag);
            }
        }

        for(Sections s :listOfSections) {
            System.out.println(s.toString());
        }




    }

    private void random() {
        RandomCollection<Sections> items = new RandomCollection<Sections>();


        for(Sections s : listOfSections) {
            items.add(s.getProbability(),s);
        }

// add some stuff, weighted as you like
//        items.add(0.1, "diamond");
//        items.add(1.0, "stone");
//        items.add(0.5, "iron");
// etc... this could easily be read from a config file

// get a random item
        for(int i = 0;i<100;i++ ) {
            System.out.println(items.next().toString());

        }


    }


    private void lottoSection() {
//        int totalSum = 0;
//
//        for(Sections item : listOfSections) {
//            totalSum = totalSum + item.getProbability();
//        }
//
//        int index = random.nextInt(totalSum);
//        System.out.println("index : " +index);
//
//        int sum = 0;
//        int i=0;
//        while(sum < index ) {
//            sum = sum + listOfSections.get(i++).getProbability();
//        }
//
////        System.out.println("---- " + (i-1));
//        System.out.println(listOfSections.get(i - 1).toString());
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public double generateProbForSection(boolean isLastElem) {

        double p = round(random.nextDouble(),2);
//        System.out.println("wylosowalem p : " + p + " pozostalo " + pozostaloProb );

        if(isLastElem) {
            return round(pozostaloProb,2);
        }

        if(p < pozostaloProb) {
            pozostaloProb -= p;
            return p;
        }else if(p == pozostaloProb) {
            return p;
        }else {
            return generateProbForSection(isLastElem);
        }



    }



    public void generateSections() {

        /**
         * 1. wylosuj liczbę x od 1 do n
         2. jeżeli x < n to dodaj do zmniejsz n o x, zapisz sobie x losuj dalej
         3. jeżeli x = n to skończ losowanie, zapisz sobie x
         4. jeżeli x > n to olej, losuj dalej
         */
         int x = generateSection(pozostalo);
//        System.out.println("wylosowalem : " + x + " pozostalo " + pozostalo );

        if(x < pozostalo) {
            sectionsSize.add(x);
            pozostalo -= x;
            generateSections();
        }else if(x == pozostalo) {
            sectionsSize.add(x);
            return;
        }else if(pozostalo == 1) {
            sectionsSize.add(1);
            return;
        }else {
            generateSections();
        }
    }

    private int generateSection(int size) {
        return random.nextInt(size)+1;
    }

    private boolean countNumbers(int lastNumber) {
        int sum = 0;

        for(int i : sectionsSize ){
            sum += i;
        }

        if(sum + lastNumber >tagNumber) return false;
        else if(sum + lastNumber == tagNumber) {
           sectionsSize.add(lastNumber);
            return false;
        }else {
            sectionsSize.add(lastNumber);
            return true;
        }

    }


}
