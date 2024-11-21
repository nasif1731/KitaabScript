package util;

import com.qcri.farasa.segmenter.Farasa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FarasaPreProcessor {
	private Farasa farasa;

	public FarasaPreProcessor() {
		try {
			this.farasa = new Farasa();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String normalizeText(String text) {
		return text.replaceAll("[\\u064B-\\u0652]", "").trim();
	}

	public List<String> segmentText(String text) {
		List<String> res = new ArrayList<>();
		try {

			List<String> segs = farasa.segmentLine(text);

			for (String word : segs) {
				String[] st = word.split("\\+");
				List<String> x = Arrays.asList(st);
				res.add(segmentArabicWord(x));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private String segmentArabicWord(List<String> word) {
		// Example clitics
		List<String> prefixes = Arrays.asList("و", "ف", "ب", "ك", "ل", "ال", "ه", "ها", "هم", "نا", "كم", "كن", "ي",
				"ك");

		String res = "";
		// Remove prefixes
		for (int i = word.size() - 1; i >= 0; i--) {
			boolean chk = true;
			for (String prefix : prefixes) {
				if (word.get(i).equalsIgnoreCase(prefix)) {
					chk = false;
					break;
				}
			}
			if (chk) {
				res = word.get(i);
			}
		}
		return res;
	}
}