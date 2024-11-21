package pl;

import com.qcri.farasa.segmenter.Farasa;

import java.io.IOException;
import java.util.ArrayList;

public class FarasaPreprocessor {
    public static void main(String[] args) {
        try {
            // Initialize Farasa segmenter
            Farasa farasa = new Farasa();

            // Sample Arabic text
            String arabicText = "اذهب الأطفال إلى الحديقة للعب الألعاب والاستمتاع بالجو الجميل في فصل الصيف";

            // Normalize text
            String normalizedText = arabicText.replaceAll("[\\u064B-\\u0652]", "");
            System.out.println("Normalized Text: " + normalizedText);

            // Segment the text
            ArrayList<String> segmentedOutput = farasa.segmentLine(normalizedText);
            System.out.println("Segmented Output: " + segmentedOutput);

            // Lemmatize the text
            ArrayList<String> lemmatizedOutput = farasa.lemmatizeLine(normalizedText);
            System.out.println("Lemmatized Output: " + lemmatizedOutput);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
