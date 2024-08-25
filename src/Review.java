import java.util.HashMap;

public class Review {

    private final String business_id;
    private final String text;
    public FT termFreqTable;


    public Review(String business_id, String text) {
        this.business_id = business_id;
        this.text = text;

    }

    public FT getFrequencyTable() { return termFreqTable; }

    public String getBusiness_id() {
        return business_id;
    }

    public String getText(){
        return text;
    }
































//    public void fillFrequencyTable() {
//        if (this.text == null) return; // return
//
//        String[] splitter = this.text.split(" ");
//        wordFrequencyTable = new FT();
//
//        for (String str : splitter) {
//            if (str != null) {
//                wordFrequencyTable.add(str);
//            }
//        }
//        wordFrequencyTable.getTermFrequency();
//    }
//
//    public double sumTfidfValue() {
//        double sum = 0;
//        int length = wordFrequencyTable.table.length;
//        for(int i = 0; i < length; i++) {
//            for(FT.Node e = wordFrequencyTable.table[i]; e != null; e = e.next) {
//                sum += e.value;
//            }
//        }
//        return sum;
//    }
//
//    public void setSqrtSum() {
//        double sum = sumTfidfValue();
//        this.sqrtSum = Math.sqrt(sum*sum);
//    }





}