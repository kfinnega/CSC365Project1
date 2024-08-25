import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectPlayground {

    public static void main(String[] args) {

        FT documentFrequencyTable = new FT();

        Business biz1 = new Business("1", 8, "The lizzards thicket");
        Business biz2 = new Business("2", 8, "Moes");
        Business biz3 = new Business("3", 8, "Miller AleHouse");
        Business biz4 = new Business("4", 8, "BJ TapHouse");
        Business biz5 = new Business("5", 8, "Hogwarts");
        Business biz6 = new Business("6", 8, "Xbox");

        biz1.reviews = createCorpus1();
        biz2.reviews = createCorpus2();
        biz3.reviews = createCorpus3();
        biz4.reviews = createCorpus4();
        biz5.reviews = createCorpus5();
        biz6.reviews = createCorpus6();


        HashMap<String, Business> bizs = new HashMap<>();

        bizs.put("The lizzards thicket", biz1);
        bizs.put("Moes", biz2);
        bizs.put("Miller AleHouse", biz3);
        bizs.put("BJ Taphouse", biz4);
        bizs.put("Hogwarts", biz5);
        bizs.put("Xbox", biz6);

        for (Map.Entry<String, Business> entry : bizs.entrySet()) {
            Business biz = entry.getValue();
            biz.termFrequencyTable = fillTFTables(biz);
            getTFValue(biz.termFrequencyTable);
        }

        documentFrequencyTable = fillDocFreqTable(bizs, documentFrequencyTable);
        getIDFValue(documentFrequencyTable, bizs.size());

        for (Map.Entry<String, Business> entry : bizs.entrySet()) {
            Business biz = entry.getValue();
            createTfidfValue(biz, documentFrequencyTable);
        }


        System.out.println(biz1.getName() + " | " + compareBusiness(biz3,biz1));
        System.out.println(biz2.getName() + " | " + compareBusiness(biz3,biz2));
//        System.out.println(biz3.getName() + " | " + compareBusiness(biz3,biz3));
        System.out.println(biz4.getName() + " | " + compareBusiness(biz3,biz4));
        System.out.println(biz5.getName() + " | " + compareBusiness(biz3,biz5));
        System.out.println(biz6.getName() + " | " + compareBusiness(biz3,biz6));

        biz3 = findSimilarBizs(biz3, bizs);
        System.out.println(biz3.similar1 + " | " + biz3.similar2);

    }

    public static Business findSimilarBizs(Business biz1 , HashMap<String, Business> bizMap) {
        double sim1Value = 0;
        double sim2Value = 0;
        for(Map.Entry<String, Business> entry : bizMap.entrySet()) {
            Business compareBiz = entry.getValue();
            if (compareBiz.getBusiness_id().equals(biz1.getBusiness_id())) continue;
            double cosSimValue = compareBusiness(biz1, compareBiz);
            if (biz1.similar1 == null) {
                sim1Value = cosSimValue;
                biz1.similar1 = compareBiz.getName();
                continue;
            }

            if (biz1.similar2 == null) {
                sim2Value = cosSimValue;
                biz1.similar2 = compareBiz.getName();
                continue;
            }

            if(cosSimValue >= sim1Value) {
                sim1Value = cosSimValue;
                biz1.similar1 = compareBiz.getName();
                continue;
            }

            if(cosSimValue >= sim2Value) {
                sim2Value = cosSimValue;
                biz1.similar2 = compareBiz.getName();
            }
        }
        return biz1;
    }


    public static double compareBusiness(Business biz1, Business biz2) {
        double dot = dotProduct(biz1.tfidfValues, biz2.tfidfValues);
        double mag = multiplyMagnitudes(biz1.tfidfValues, biz2.tfidfValues);

        return calculateCosSim(dot, mag);
    }

    public static double calculateCosSim(double num, double den) {
        return num/den;
    }

    public static double magnitude(HashMap<String,Double> map) {
        double mag= 0.0;
        for (double value : map.values()) {
            mag += value * value;
        }

        return Math.sqrt(mag);
    }

    public static double multiplyMagnitudes(HashMap<String, Double> biz1, HashMap<String, Double> biz2) {
        double magBiz1 = magnitude(biz1);
        double magBiz2 = magnitude(biz2);
        return magBiz1 * magBiz2;
    }

    public static double dotProduct(HashMap<String,Double> map1, HashMap<String, Double> map2) {
        double result = 0.0;
        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {
                result += map1.get(key) * map2.get(key);
            }
        }
        return result;
    }

    public static double cosineSimilarity(HashMap<String, Double> map1, HashMap<String, Double> map2) {
        // Compute the dot product of the two vectors
        double dotProduct = 0;
        for (String key : map1.keySet()) {
            if (map2.containsKey(key)) {
                dotProduct += map1.get(key) * map2.get(key);
            }
        }

        // Compute the magnitudes of the two vectors
        double mag1 = 0;
        double mag2 = 0;
        for (double value : map1.values()) {
            mag1 += Math.pow(value, 2);
        }
        mag1 = Math.sqrt(mag1);
        for (double value : map2.values()) {
            mag2 += Math.pow(value, 2);
        }
        mag2 = Math.sqrt(mag2);

        // Compute the cosine similarity
        double cosineSimilarity = dotProduct / (mag1 * mag2);
        return cosineSimilarity;
    }


    public static FT fillTFTables(Business biz) {
        FT tf = new FT();
        for (Review review : biz.reviews) {
            String[] splitter = review.getText().split(" ");
            for (String word : splitter) {
                tf.add(word);
            }
        }
        return tf;
    }

    public static FT fillDocFreqTable(HashMap<String, Business> bizs, FT docFT) {
        for (Map.Entry<String, Business> entry : bizs.entrySet()) {
            Business biz = entry.getValue();
            HashMap<String, Boolean> checker = new HashMap<>();

            for (Review review : biz.reviews) {
                String[] splitter = review.getText().split(" ");
                for (String word : splitter) {
                    if(!checker.containsKey(word)) {
                        checker.put(word,true);
                        docFT.add(word);
                    }
                }
            }
        }
        return docFT;
    }

    public static void getTFValue(FT ft) {
        FT.Node[] table = ft.table;
        for (int i = 0; i < table.length; ++i) {
            for (FT.Node e = table[i]; e != null; e = e.next) {
                e.value = e.count / ft.totalCount;
            }
        }
    }

    public static void getIDFValue(FT ft, int numOfDocs) {
        FT.Node[] table = ft.table;
        for(int i = 0; i < table.length; ++i){
            for(FT.Node e = table[i]; e != null; e = e.next) {
                e.value = Math.log10(numOfDocs/e.count);
            }
        }
    }

    public static void createTfidfValue(Business biz, FT idfvals) {
        HashMap<String, Double> tfidf = new HashMap<>();
        FT.Node[] table = biz.termFrequencyTable.table;
        for (int i = 0; i < table.length; i++) {
            for (FT.Node e = table[i]; e != null; e = e.next) {
                double idf = idfvals.getValue(e.key);
                double tfidfVal = e.value * idf;
                tfidf.put(e.key, tfidfVal);
            }
        }
        biz.tfidfValues = tfidf;
    }


    public static void printHashMap(HashMap<String,Double> hm) {
        for (HashMap.Entry<String, Double> entry : hm.entrySet()){
            String key = entry.getKey();
            double value = entry.getValue();
            System.out.println(key + ": " + value);
        }
    }


    public static ArrayList<Review> createCorpus1() {
        ArrayList<Review> corpus1 = new ArrayList<>();
        String d1 = "you were born with potential";
        String d2 = "you were born with goodness and trust";
        String d3 = "you were born with ideals and dreams";
        String d4 = "you were born with greatness";
        String d5 = "you were born with wings";
        String d6 = "you are not meant for crawling, so don't";
        String d7 = "you have wings";
        String d8 = "learn to use them and fly";

        Review r1 = new Review("1", d1);
        Review r2 = new Review("1", d2);
        Review r3 = new Review("1", d3);
        Review r4 = new Review("1", d4);
        Review r5 = new Review("1", d5);
        Review r6 = new Review("1", d6);
        Review r7 = new Review("1", d7);
        Review r8 = new Review("1", d8);

        corpus1.add(r1);
        corpus1.add(r2);
        corpus1.add(r3);
        corpus1.add(r4);
        corpus1.add(r5);
        corpus1.add(r6);
        corpus1.add(r7);
        corpus1.add(r8);

        return corpus1;
    }

    public static ArrayList<Review> createCorpus2() {
        ArrayList<Review> corpus2 = new ArrayList<>();
        String d1 = "The mandolarian is a good show";
        String d2 = "The Jai Un is not a good pitcher";
        String d3 = "The last of us is a show on hbo, about a girls whose mom was killed when she was born";
        String d4 = "I don't like the price of hbo";
        String d5 = "What I am going to do";
        String d6 = "I need to pass these classes";
        String d7 = "I want to graduate";
        String d8 = "The freshman are characters";

        Review r1 = new Review("2", d1);
        Review r2 = new Review("2", d2);
        Review r3 = new Review("2", d3);
        Review r4 = new Review("2", d4);
        Review r5 = new Review("2", d5);
        Review r6 = new Review("2", d6);
        Review r7 = new Review("2", d7);
        Review r8 = new Review("2", d8);

        corpus2.add(r1);
        corpus2.add(r2);
        corpus2.add(r3);
        corpus2.add(r4);
        corpus2.add(r5);
        corpus2.add(r6);
        corpus2.add(r7);
        corpus2.add(r8);

        return corpus2;
    }

    public static ArrayList<Review> createCorpus3() {
        ArrayList<Review> corpus3 = new ArrayList<>();
        String d1 = "I need more beer to watch the mandolarian";
        String d2 = "The beer here is very good";
        String d3 = "I need to visit this place more for their beer";
        String d4 = "The food wasn't the best";
        String d5 = "I liked the burger that I got";
        String d6 = "My kids love this place";
        String d7 = "The family and I visit at least once a week";
        String d8 = "This place help out the local high school";

        Review r1 = new Review("3", d1);
        Review r2 = new Review("3", d2);
        Review r3 = new Review("3", d3);
        Review r4 = new Review("3", d4);
        Review r5 = new Review("3", d5);
        Review r6 = new Review("3", d6);
        Review r7 = new Review("3", d7);
        Review r8 = new Review("3", d8);

        corpus3.add(r1);
        corpus3.add(r2);
        corpus3.add(r3);
        corpus3.add(r4);
        corpus3.add(r5);
        corpus3.add(r6);
        corpus3.add(r7);
        corpus3.add(r8);

        return corpus3;
    }

    public static ArrayList<Review> createCorpus4() {
        ArrayList<Review> corpus4 = new ArrayList<>();
        String d1 = "The beer in this place is very strong";
        String d2 = "I the cocktails here a good";
        String d3 = "My bartender was very good, they also had a ton of options for beer";
        String d4 = "I need to come here more it has a great atmosphere";
        String d5 = "I came here with my high school team and it was able to fit all of us";
        String d6 = "The beer here is good";
        String d7 = "Music here was bad";
        String d8 = "The music in this place need to be lowered";

        Review r1 = new Review("4", d1);
        Review r2 = new Review("4", d2);
        Review r3 = new Review("4", d3);
        Review r4 = new Review("4", d4);
        Review r5 = new Review("4", d5);
        Review r6 = new Review("4", d6);
        Review r7 = new Review("4", d7);
        Review r8 = new Review("4", d8);

        corpus4.add(r1);
        corpus4.add(r2);
        corpus4.add(r3);
        corpus4.add(r4);
        corpus4.add(r5);
        corpus4.add(r6);
        corpus4.add(r7);
        corpus4.add(r8);

        return corpus4;
    }

    public static ArrayList<Review> createCorpus5() {
        ArrayList<Review> corpus5 = new ArrayList<>();
        String d1 = "This place is magical";
        String d2 = "The butter beer here is good";
        String d3 = "I wouldn't bring the family";
        String d4 = "This is not like my high school";
        String d5 = "I haven't been able to watch the mandolarian";
        String d6 = "The bartender is friends with you know who";
        String d7 = "The family and I visit at least once a week";
        String d8 = "This place help out the local high school";

        Review r1 = new Review("5", d1);
        Review r2 = new Review("5", d2);
        Review r3 = new Review("5", d3);
        Review r4 = new Review("5", d4);
        Review r5 = new Review("5", d5);
        Review r6 = new Review("5", d6);
        Review r7 = new Review("5", d7);
        Review r8 = new Review("5", d8);

        corpus5.add(r1);
        corpus5.add(r2);
        corpus5.add(r3);
        corpus5.add(r4);
        corpus5.add(r5);
        corpus5.add(r6);
        corpus5.add(r7);
        corpus5.add(r8);

        return corpus5;
    }

    public static ArrayList<Review> createCorpus6() {

        ArrayList<Review> corpus6 = new ArrayList<>();

        String d1 = "I am waiting for this to download";
        String d2 = "This is taking a long time to download";
        String d3 = "I am currently coding while waiting for this to download";
        String d4 = "I want to play r6";
        String d5 = "I am going to play cod";
        String d6 = "Danny is going to draft for me";
        String d7 = "cortland sucks";
        String d8 = "We need to beat cortland";

        Review r1 = new Review("6", d1);
        Review r2 = new Review("6", d2);
        Review r3 = new Review("6", d3);
        Review r4 = new Review("6", d4);
        Review r5 = new Review("6", d5);
        Review r6 = new Review("6", d6);
        Review r7 = new Review("6", d7);
        Review r8 = new Review("6", d8);

        corpus6.add(r1);
        corpus6.add(r2);
        corpus6.add(r3);
        corpus6.add(r4);
        corpus6.add(r5);
        corpus6.add(r6);
        corpus6.add(r7);
        corpus6.add(r8);

        return corpus6;
    }

    public static int countsSum(FT ft) {
        int sum = 0;
        FT.Node[] table = ft.table;
        for (int i = 0; i < table.length; ++i) {
            for (FT.Node e = table[i]; e != null; e = e.next) {
                sum += e.count;
            }
        }
        return sum;
    }
}


