package amazonproducts;

import java.util.Scanner;

public class AmazonProductManager {

	public String fileName;
	public final int FIRST_OPTION = 0;
	public final int LAST_OPTION = 7;
	public final int OPTION0_EXT = 0;
	public final int OPTION1_CRT = 1;
	public final int OPTION2_LST = 2;
	public final int OPTION3_ADD = 3;
	public final int OPTION4_EDT = 4;
	public final int OPTION5_DEL = 5;
	public final int OPTION6_SAV = 6;
	public final int OPTION7_SRC = 7;
	public static Scanner input = new Scanner(System.in);
	private AmazonProductList productList;

	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";

	public AmazonProductManager() {
		productList = new AmazonProductList();
	}

	public static void main(String[] args) {
	    AmazonProductManager manager = new AmazonProductManager();
	    try {
	        manager.manageProductsList();
	    } catch (RuntimeException e) {
	        System.err.println(ANSI_RED + "An unexpected critical error occurred: " + e.getMessage() + ANSI_RESET);
	        e.printStackTrace();
	        
	    } catch (AmazonProductException e) {
			e.printStackTrace();
		}
	}

	public void addProduct() throws AmazonProductException {
		if (productList == null) {
			throw new AmazonProductException("Product list has not been created. Please load a product first.");
		}

		int newID = productList.getSize();
		AmazonProduct newProduct = null;

		while (newProduct == null) {
			try {
				newProduct = createProduct(newID);
			} catch (NumberFormatException e) {
				System.out.println(ANSI_RED + "Invalid input. Please try again." + ANSI_RESET);
			}
		}

		productList.add(newProduct);
		System.out.println(ANSI_PURPLE + "Product added successfully! New list size: " + productList.getSize() + ANSI_RESET);
	}
	public AmazonProduct createProduct(int id) throws AmazonProductException {
		System.out.print("Enter product name: ");
		String name = input.nextLine();
		System.out.print("Enter product category: ");
		String category = input.nextLine();
		System.out.print("Enter product subcategory: ");
		String subcategory = input.nextLine();
		System.out.print("Enter image URL: ");
		String imageURL = input.nextLine();
		System.out.print("Enter product link: ");
		String link = input.nextLine();
		System.out.print("Enter rating: ");
		float rating = AmazonProductUtil.convertStrToFloat(input.nextLine());
		System.out.print("Enter number of ratings: ");
		int nRatings = Integer.parseInt(input.nextLine());
		System.out.print("Enter discount price: ");
		float discountPrice = AmazonProductUtil.convertStrToFloat(input.nextLine());
		System.out.print("Enter actual price: ");
		float actualPrice = AmazonProductUtil.convertStrToFloat(input.nextLine());

		return new AmazonProduct(id, name, new AmazonProductCategory(category),
				new AmazonProductSubCategory(subcategory), imageURL, link, rating, nRatings,
				discountPrice, actualPrice);
	}

	public void createProductList() throws AmazonProductException {
		boolean isValidFile = false;

		while (!isValidFile) {
			System.out.print("Enter the filename to load: ");
			String fileName = input.nextLine();

			try {
				productList.createList(fileName);
				System.out.println(ANSI_PURPLE + "Product list created successfully!" + ANSI_RESET);
				isValidFile = true;
				
			} catch (AmazonProductException e) {
				System.err.println("Failed to create product list. Please enter valid file name.");
			}
		}
	}


	public void deleteProduct() throws AmazonProductException {
		while (true) {
			System.out.print("Enter the ID of the product to delete: ");
			try {
				int id = Integer.parseInt(input.nextLine());
				AmazonProduct existingProduct = productList.findProductById(id);

				if (existingProduct == null) {
					System.out.println(ANSI_RED + "Product with ID " + id + " not found. Please try again." + ANSI_RESET);
					continue;  
				}

				boolean deleted = productList.delete(id);

				if (deleted) {
					System.out.println(ANSI_PURPLE + "Product successfully deleted!" + ANSI_RESET);

				} else {
					System.out.println(ANSI_RED + "Failed to delete product." + ANSI_RESET);
				}
				return; 
				
			} catch (NumberFormatException e) {
				System.out.println(ANSI_RED + "Invalid ID. Please enter a valid number." + ANSI_RESET);

			}
		}
	}

	public void editProduct() throws AmazonProductException {
		while (true) {
			System.out.print("Enter the ID of the product to edit: ");
			try {
				int id = Integer.parseInt(input.nextLine());
				AmazonProduct existingProduct = productList.findProductById(id);

				if (existingProduct == null) {
					System.out.println(ANSI_RED + "Product with ID " + id + " not found. Please try again." + ANSI_RESET);
					continue;  
				}

				AmazonProduct updatedProduct = createProduct(id);
				boolean edited = productList.edit(id, updatedProduct);

				if (edited) {
					System.out.println(ANSI_PURPLE + "Product successfully edited!" + ANSI_RESET);
					
				} else {
					System.out.println(ANSI_RED + "Failed to edit product." + ANSI_RESET);
				}
				return; 
				
			} catch (NumberFormatException e) {
				System.out.println(ANSI_RED + "Invalid ID. Please enter a valid number." + ANSI_RESET);

			}
		}
	}

	public void showProductList() {
		if (productList == null || productList.getSize() == 0) {
			System.out.println("The product list is empty.");
			return;

		} else 

			System.out.println(ANSI_PURPLE + "PRODUCTLIST............");
		System.out.println(ANSI_PURPLE + "BOOKLIST............");;

		for (int i = 0; i < productList.getSize(); i++) {
			AmazonProduct product = productList.findProductByIndex(i);
			
			if (product != null) {
				System.out.println(ANSI_PURPLE + product.toString() + ANSI_RESET);
			}	
		}
	}

	public void saveProductList() throws AmazonProductException {
		if (productList == null || productList.getSize() == 0) {
			System.out.println("The product list is empty. Nothing to save.");
			return;
		}

		System.out.print("Enter the filename to save: ");
		String fileName = input.nextLine();
		productList.saveList(fileName);
	}

	public void search() throws AmazonProductException  {
		System.out.print("Enter a keyword to find: ");
		String keyword = input.nextLine();
		productList.search(keyword);
	}

	public void manageProductsList() throws AmazonProductException {
		int choice = -1;
		do {
			showMenu();
			try {
				String userInput = input.nextLine();
				choice = Integer.parseInt(userInput);
				switch (choice) {
				case OPTION0_EXT:
					exit();
					break;
				case OPTION1_CRT:
					createProductList();
					break;
				case OPTION2_LST:
					showProductList();
					break;
				case OPTION3_ADD:
					addProduct();
					break;
				case OPTION4_EDT:
					editProduct();
					break;
				case OPTION5_DEL:
					deleteProduct();
					break;
				case OPTION6_SAV:
					saveProductList();
					break;
				case OPTION7_SRC:
					search();
					break;
				default:
					System.out.println(ANSI_RED + "Invalid input." + ANSI_RESET);
					choice = -1;  
				}
			} catch (NumberFormatException e) {    
				System.out.println(ANSI_RED + "Invalid input." + ANSI_RESET);
				choice = -1;  
			} catch (AmazonProductException e) {
				System.out.println(ANSI_RED + "Invalid input," + ANSI_RESET);
				choice = -1;  	
			} catch (Exception e) {
				System.out.println(ANSI_RED + "Invalid input." + ANSI_RESET);
				choice = -1;  


			}
			
		} while (choice != OPTION0_EXT);
	}
	
	public void showMenu() {
		System.out.println(ANSI_BLACK + "================================\r\n"
				+ "|| Menu - Amazon Products: A1 ||\r\n"
				+ "================================");
		System.out.println(OPTION0_EXT + ". Exit");
		System.out.println(OPTION1_CRT + ". Load product list");
		System.out.println(OPTION2_LST + ". Show product list");
		System.out.println(OPTION3_ADD + ". Add product");
		System.out.println(OPTION4_EDT + ". Edit a product");
		System.out.println(OPTION5_DEL + ". Delete a product");
		System.out.println(OPTION6_SAV + ". Save product list");
		System.out.println(OPTION7_SRC + ". Search in the list");
		System.out.print("Choose an option: ");
	}

	public void exit() {
		System.out.println("================================\r\n"
				+  "||     [Application ended]    ||\r\n"
				+ "================================" + ANSI_RESET);
		input.close();
		System.exit(0);
	}
}



