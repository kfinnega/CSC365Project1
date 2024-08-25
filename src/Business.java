import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Objects.isNull;

public class Business {

    public String business_id;
    public int review_count;
    public String name;
    public String categories;

    public ArrayList<Review> reviews;
    public FT termFrequencyTable;
    HashMap<String, Double> tfidfValues;

    String similar1;
    String similar2;

    public Business(String business_id, int review_count, String name, String categories) {
        this.business_id = business_id;
        this.review_count = review_count;
        this.name = name;
        this.categories = categories;
    }

    public Business(String business_id, int review_count, String name) {
        this.business_id = business_id;
        this.review_count = review_count;
        this.name = name;
    }

    public String getBusiness_id(){ return this.business_id; }

    public String getName(){
        return this.name;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        if (isNull(reviews) || reviews.isEmpty()) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

    public String toString() {
        return "name: \"" + this.getName() + "\" business_id: \"" + this.getBusiness_id() + "\"";
    }
}



