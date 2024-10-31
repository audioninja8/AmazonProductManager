package amazonproducts;

import java.util.ArrayList;

public class AmazonProductUtil {

	public static float convertStrToFloat(String nextLine) {
		return Float.parseFloat(nextLine);
	}

	public static String[] lineReader(String line) {

		ArrayList<String> fields = new ArrayList<>();

		StringBuilder currentField = new StringBuilder();

		boolean inQuotes = false;

		for (char c : line.toCharArray()) {
			if (c == '"') {
				inQuotes = !inQuotes;
				currentField.append(c);
			} else if (c == ',' && !inQuotes) {
				fields.add(currentField.toString().trim());
				currentField = new StringBuilder();
			} else {
				currentField.append(c);
			}
		}

		fields.add(currentField.toString().trim());

		for (int i = 0; i < fields.size(); i++) {
			String field = fields.get(i);
			if (field.startsWith("\"") && field.endsWith("\"")) {
				field = field.substring(1, field.length() - 1);
			}

			if (field.startsWith("₹")) {
				field = field.substring(1).replace(",", "");
			}

			fields.set(i, field);
		}

		return fields.toArray(new String[0]);

	}
}
