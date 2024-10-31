package amazonproducts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays; 

public class AmazonProductList {

	public AmazonProductList() {
		DEFAULT_TITLE.addAll(Arrays.asList("id", "name", "main_category", "sub_category", "image", "link", 
				"ratings", "no_of_ratings", "discount_price", "actual_price"));
	}

	public static final int NUMCOLS = 10;

	private final ArrayList<AmazonProduct> bestsellers = new ArrayList<AmazonProduct>();

	public final ArrayList<String> DEFAULT_TITLE = new ArrayList<String>(); 

	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";



	public void add(AmazonProduct product) {
		if (product != null) {
			bestsellers.add(product);

		}
	}

	public void createList(String fileName) throws AmazonProductException {
		bestsellers.clear(); 
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			boolean isFirstLine = true;
			while ((line = br.readLine()) != null) {
				String[] fields = AmazonProductUtil.lineReader(line);
				if (isFirstLine) {
					DEFAULT_TITLE.clear();
					DEFAULT_TITLE.addAll(Arrays.asList(fields));
					isFirstLine = false;
					continue;
				}
				if (fields.length == NUMCOLS) {
					try {
						AmazonProduct product = new AmazonProduct(
								Integer.parseInt(fields[0]),
								fields[1],
								new AmazonProductCategory(fields[2]),
								new AmazonProductSubCategory(fields[3]),
								fields[4],
								fields[5],
								Float.parseFloat(fields[6]),
								Integer.parseInt(fields[7].replace(",", "")),
								Float.parseFloat(fields[8]),
								Float.parseFloat(fields[9])
								);

						bestsellers.add(product);

					} catch (NumberFormatException e) {
						System.err.println(ANSI_RED + "Error parsing line: " + line + ANSI_RESET);
						System.err.println(ANSI_RED + "Error details: " + e.getMessage() + ANSI_RESET);

					}		
				}				
			}

		} catch (IOException e) {
			throw new AmazonProductException(ANSI_RED + "Error reading file: " + fileName + ANSI_RESET);

		}
	}	
	public boolean delete(int id) {
		for (int i = 0; i < bestsellers.size(); i++) {
			if (bestsellers.get(i).getId() == id) {
				bestsellers.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean edit(int id, AmazonProduct updatedProduct) {
		for (int i = 0; i < bestsellers.size(); i++) {
			if (bestsellers.get(i).getId() == id) {
				bestsellers.set(i, updatedProduct);
				return true;
			}
		}
		return false;
	}

	public AmazonProduct findProductByIndex(int index) {
		if (index >= 0 && index < bestsellers.size()) {
			return bestsellers.get(index);
		}
		return null;
	}

	public AmazonProduct findProductById(int id) {
		for (AmazonProduct product : bestsellers) {
			if (product.getId() == id) {
				return product;
			}
		}
		return null;
	}

	public int getSize() {
		return bestsellers.size();
	}

	public void printList() {
		for (AmazonProduct product : bestsellers) {
			System.out.println(product.toString());
		}
	}

	public void search(String keyword) {
		for (AmazonProduct product : bestsellers) {
			if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
				System.out.println(ANSI_PURPLE + product + ANSI_RESET);
			}
		}
	}
	public void saveList(String fileName) throws AmazonProductException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

			bw.write(String.join(",", DEFAULT_TITLE));
			bw.newLine();

			for (AmazonProduct product : bestsellers) {
				String[] fields = {
						String.valueOf(product.getId()),
						escapeField(product.getName()),
						escapeField(product.getMain_category().getCategory()),
						escapeField(product.getSub_category().getSubCategory()),
						escapeField(product.getImageURL()),
						escapeField(product.getLink()),
						String.format("%.1f", product.getRatings()),
						String.valueOf(product.getNo_of_rating()),
						String.format("%.2f", product.getDiscount_price()),
						String.format("%.2f", product.getActual_price())
				};
				bw.write(String.join(",", fields));
				bw.newLine();
				System.out.println(ANSI_PURPLE + "Saved product: " + String.join(", ", fields) + ANSI_RESET);
			}
			System.out.println(ANSI_PURPLE + "Finished saving " + bestsellers.size() + " products." + ANSI_RESET);
		} catch (IOException e) {
			throw new AmazonProductException(ANSI_RED + "Error writing to file: " + e.getMessage() + ANSI_RESET);
		}
	}

	private String escapeField(String field) {
		if (field == null) {
			return "";
		}
		if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
			return "\"" + field.replace("\"", "\"\"") + "\"";
		}
		return field;
	}	
}


		
	
	
