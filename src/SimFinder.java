import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimFinder {
    public static HashMap<String, Business> businessMap;
    public static HashMap<String, String> businessNameMap;

    public SimFinder() throws IOException {
        businessNameMap = new HashMap<String, String>();
        businessMap = new HashMap<String, Business>();
        FT documentFrequencyTable = new FT();
        addBusinessDataToBusinessMap();
        addReviewDataToBusinessMap();
        removeProblemBizs();

        for (Map.Entry<String, Business> entry : businessMap.entrySet()) {
            Business biz = entry.getValue();
            biz.termFrequencyTable = fillTFTables(biz);
            getTFValue(biz.termFrequencyTable);
        }

        documentFrequencyTable = fillDocFreqTable(businessMap, documentFrequencyTable);
        getIDFValue(documentFrequencyTable, businessMap.size());
        int count = 0;
        for (Map.Entry<String, Business> entry : businessMap.entrySet()) {
            Business biz = entry.getValue();
            createTfidfValue(biz, documentFrequencyTable);
        }
    }


    public static void addBusinessDataToBusinessMap() throws IOException {
        int count = 0;
        Gson gson = new GsonBuilder().setLenient().create();
        BufferedReader reader = new BufferedReader(new FileReader("yelp_data/yelp_academic_dataset_business.json"));
        String currentLine;
        Business business;

        while (count < 10000) {
            currentLine = reader.readLine();
            business = gson.fromJson(currentLine, Business.class);
            String categories = business.categories;

            if (categories != null && categories.contains("Restaurants") && !(businessNameMap.containsKey(business.getName()) && business.review_count != 0)){
                businessNameMap.put(business.getName(), business.getBusiness_id());
                businessMap.put(business.getBusiness_id(), business);
                count++;
            }
        }
    }

    public static void removeProblemBizs() {
        String[] bizIds = {"k3EItHl2rtEz4opr39b99g", "QXlQzuc_LIfln_lG0Nm4PA", "o1I-pKoa6WUNpMViWaA9Fw","BuEEKAmr1JDfhwG8Q0ODoA", "cU0_JyREJ-3vwaNxRdmmig", "AFU5ZlFCYA0_Eqw5MlqJOg", "VSQjT5mn45JKtSSL56jNPg", "RE9UD8J2t650OqDtqxPfEw", "JL6U8xzmWZJgVTZy6S4rsQ", "AqOSpSqdocwtIjqTECW2ng", "2VzbT8h9cct4UZdYMQlgrA", "HnESQwAjd3nZXkb9A_ixMg"};
        for (String id : bizIds) {
            businessMap.remove(id);
        }
    }

    public static void addReviewDataToBusinessMap() throws IOException {
        int count = 0;
        Gson gson = new GsonBuilder().setLenient().create();
        String currentLine;
        Business business;

        BufferedReader reader = new BufferedReader(new FileReader("yelp_data/yelp_academic_dataset_review.json"));

        while (count < 999018) {
            currentLine = reader.readLine();
            Review review = gson.fromJson(currentLine, Review.class);

            if (businessMap.containsKey(review.getBusiness_id())) {
                business = businessMap.get(review.getBusiness_id());
                business.addReview(review);
                count++;
            }
        }

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

    public static String lookUpBizId(String name) {
        if(businessNameMap.containsKey(name)){
            return businessNameMap.get(name);
        }
        return "BIZDNE";
    }

    public static String[] getResults(String bizId) {
        Business biz1 = findSimilarBizs(businessMap.get(bizId), businessMap);
        String[] retval = new String[2];
        retval[0] = biz1.similar1;
        retval[1] = biz1.similar2;

        return retval;
    }

}
