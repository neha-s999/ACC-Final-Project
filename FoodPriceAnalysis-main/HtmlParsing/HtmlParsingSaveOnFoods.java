package HtmlParsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlParsingSaveOnFoods {

    public static HashMap<String, HashMap<String, String>> Collectionof_foodAndPrice = new HashMap<>();
    public static List<String> fileName = new ArrayList<>();

    public static List<String> parse() throws IOException {
        HtmlParsingSaveOnFoods htmlParser = new HtmlParsingSaveOnFoods();

        File directory = new File("SaveOnFoodsHtml");
        File[] listOfFiles = directory.listFiles((dir, name) -> name.endsWith(".html"));

        for (File file : listOfFiles) {
            if (file.isFile()) {
                htmlParser.parseFile(file.getAbsolutePath());
            }
        }
        return fileName;
    }

    public void parseFile(String url) {
        try {
            File inputFile = new File(url);
            Document doc = Jsoup.parse(inputFile, null);

            String title = doc.title();
            List<Element> pt_list = doc.select("[class^=ProductCardTitle]");

            if (pt_list.size() == 0) {
                return;
            }
            Element pt_CollectionName = pt_list.get(0);
            String CollectionName = pt_CollectionName.child(0).ownText();

            List<Element> pp_List = doc.select("[class^=ProductCardPrice]");
            if (pp_List.size() == 0) {
                return;
            }
            Element pp_CollectionPrice = pp_List.get(0);
            String CollectionPrice = pp_CollectionPrice.text();

            if (!Collectionof_foodAndPrice.containsKey(CollectionName)) {
                HashMap<String, String> foodAndPrice = new HashMap<>();
                foodAndPrice.put("price", CollectionPrice);
                foodAndPrice.put("title", title);
                Collectionof_foodAndPrice.put(CollectionName, foodAndPrice);
            }

            String outputFileName = "saveonfoods.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true))) {
                for (Map.Entry<String, HashMap<String, String>> product : Collectionof_foodAndPrice.entrySet()) {
                    String productName = product.getKey();
                    HashMap<String, String> foodAndPrice = product.getValue();
                    String productPrice = foodAndPrice.get("price");
                    String productTitle = foodAndPrice.get("title");
                    String output = productTitle + " | " + productName + " | " + productPrice;
                    if (!fileName.contains(output)) {
                        fileName.add(output);
                        writer.write(output);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error parsing file: " + e.getMessage());
        }
    }
}
