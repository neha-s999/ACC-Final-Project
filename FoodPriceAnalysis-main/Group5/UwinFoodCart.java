package Group5;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Actions.BestDeals;
import Actions.DataValidation;
import Actions.InvertedIndexing;
import Actions.PageRanking;
import Actions.ProductCategoryList;
import Actions.SearchFrequency;
import Actions.SpellChecking;
import Actions.WordCompletion;
import HtmlParsing.HtmlParsingSaveOnFoods;
import HtmlParsing.HtmlParsingShopFoodEx;
import HtmlParsing.HtmlParsingYupik;
import Webcrawling.WebCrawlerSaveOnFoods;
import Webcrawling.WebCrawlerShopFoodEx;
import Webcrawling.WebCrawlerYupik;

public class UwinFoodCart {

    private Scanner scanner;
    private Hashtable<String, String> urlMap;

    public static void main(String[] args) {
        UwinFoodCart app = new UwinFoodCart();
        app.run();
    }

    private void run() {
        scanner = new Scanner(System.in);
        urlMap = new Hashtable<>();

        String choice;

        do {
            System.out.println("1. Crawl Websites\n2. Perform Data Validation\n3. Parse Website Data\n4. Search and Analyze Products\n5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    crawlWebsites();
                    break;
                case "2":
                    performDataValidation();
                    break;
                case "3":
                    parseWebsiteData();
                    break;
                case "4":
                    searchAndAnalyzeProducts();
                    break;
                case "5":
                    System.out.println("Thank You");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        } while (!choice.equals("5"));

        scanner.close();
    }

    private void crawlWebsites() {
        System.out.println("Crawling websites...");
        System.out.println("Do you wish to crawl? If Yes, enter Y");
        String ch = scanner.nextLine();
        if (ch.equalsIgnoreCase("Y")) {
            urlMap.putAll(WebCrawlerYupik.startWebCrawling("https://yupik.com/en/"));
            urlMap.putAll(WebCrawlerShopFoodEx.startWebCrawling("https://www.shopfoodex.com/"));
            urlMap.putAll(WebCrawlerSaveOnFoods.startWebCrawling("https://www.saveonfoods.com/"));
        }
        System.out.println("Crawling completed.\n");
    }

    private void performDataValidation() {
        System.out.println("Performing data validation...");
        System.out.println("Do you wish to perform data validation? If Yes, enter Y");
        String dataValidate = scanner.nextLine();
        if (dataValidate.equalsIgnoreCase("Y")) {
            try {
                // Data validation with regular expression
                DataValidation.dataValidation();
            } catch (IOException e) {
                // Handle the exception as needed (e.g., print an error message)
                e.printStackTrace();
            }
        }
        System.out.println("Data validation completed.\n");
    }

    private void parseWebsiteData() {
        System.out.println("Parsing website data...");
        System.out.println("Do you wish to parse? If Yes, enter Y");
        String ch = scanner.nextLine();

        if (ch.equalsIgnoreCase("Y")) {
            System.out.println("Parsing the website data. Please wait...");

            try {
                HtmlParsingSaveOnFoods.parse();
                HtmlParsingShopFoodEx.parse();
                HtmlParsingYupik.parse();
            } catch (IOException e) {
                // Handle the exception as needed (e.g., print an error message)
                e.printStackTrace();
            }

            System.out.println("Done with parsing\n");
        }
    }


    private void searchAndAnalyzeProducts() {
        System.out.println("Searching and analyzing products...");

        Scanner inp = new Scanner(System.in);
        String ch;
        boolean spellCheck = false;

        do {
            System.out.println("\nEnter product name");
            String product = inp.nextLine();

            try {
                // Spell Checking of product
                spellCheck = SpellChecking.SpellChecker(product);

                if (!spellCheck) {
                    System.out.println("Please check the spelling");
                    // Using word completion to give suggestions of words
                    List<String> suggestedWords = WordCompletion.findSimilarWords(product);
                    System.out.println("\nSome suggested words are:");
                    for (String word : suggestedWords) {
                        System.out.println(word);
                    }
                } else {
                    System.out.println("List of products: ");
                    // Get the products that match the search key and its category
                    Map<String, Double> products = ProductCategoryList.getSimilarProducts(product);

                    if (products.isEmpty()) {
                        System.out.println("No products found for the given search key.");
                    } else {
                        System.out.println("\nTop 5 products:");
                        // Get 5 products from the list that are least expensive
                        BestDeals.TopDeals(products);

                        System.out.println("\nFrequency Count:");
                        // Inverted indexing and frequency count
                        InvertedIndexing.Indexing(urlMap, product);

                        // Maintain search frequency
                        SearchFrequency.SearchCount(product);

                        // Page ranking for the product
                        System.out.println("Do you wish to page rank? If Yes, enter Y");
                        ch = inp.nextLine();
                        if (ch.equalsIgnoreCase("y")) {
                            PageRanking.pageRank(product);
                        }
                    }
                }
            } catch (Exception e) {
                // Handle the IOException as needed
                e.printStackTrace();
            }

            System.out.println("\nDo you want to search for another product? If Yes, enter Y");
            ch = inp.nextLine();
        } while (ch.equalsIgnoreCase("Y"));

        System.out.println("Analysis completed.\n");
    }


}


