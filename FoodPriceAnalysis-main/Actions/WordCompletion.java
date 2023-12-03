package Actions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord;

    TrieNode() {
        isEndOfWord = false;
        for (int i = 0; i < 26; i++) {
            children[i] = null;
        }
    }
}

class Trie {
    TrieNode root;

    Trie() {
        root = new TrieNode();
    }

    void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = Character.toLowerCase(word.charAt(i));
            if (currentChar < 'a' || currentChar > 'z') {
                // Skip non-alphabetic characters
                continue;
            }
            int index = currentChar - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }



    List<String> autoComplete(String prefix) {
        TrieNode current = root;
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';
            if (current.children[index] == null) {
                return new ArrayList<>();
            }
            current = current.children[index];
        }
        return getAllWordsFromNode(current, prefix);
    }

    private List<String> getAllWordsFromNode(TrieNode node, String prefix) {
        List<String> result = new ArrayList<>();
        if (node.isEndOfWord) {
            result.add(prefix);
        }
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                result.addAll(getAllWordsFromNode(node.children[i], prefix + (char) ('a' + i)));
            }
        }
        return result;
    }
    List<String> getAllWords() {
        return getAllWordsFromNode(root, "");
    }
}

public class WordCompletion {

    private static final int MAX_DISTANCE = 2;

    private static List<String> readWordsFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                words.addAll(Arrays.asList(lineWords));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

//    public static List<String> findSimilarWords(String word) {
//        List<String> filenames = Arrays.asList("yupik.txt", "saveonfoods.txt", "shopfoodex.txt");
//        Trie trie = new Trie();
//        
//        for (String filename : filenames) {
//            readWordsFromFile(filename).forEach(trie::insert);
//        }
//
//        List<String> suggestions = trie.autoComplete(word.toLowerCase());
//        return suggestions.subList(0, Math.min(suggestions.size(), 5));
//    }
    public static List<String> findSimilarWords(String word) {
        List<String> filenames = Arrays.asList("yupik.txt", "saveonfoods.txt", "shopfoodex.txt");
        Trie trie = new Trie();

        // Insert words into the Trie (case-insensitive)
        for (String filename : filenames) {
            readWordsFromFile(filename).forEach(w -> trie.insert(w.toLowerCase()));
        }

        // Perform auto-complete and get suggestions
        List<String> suggestions = trie.autoComplete(word.toLowerCase());

        // Print suggestions for debugging
        System.out.println("Suggestions: " + suggestions);

        if (suggestions.isEmpty()) {
            // If Trie's autoComplete method provides no suggestions, use an alternative approach.
            // For example, you can implement a backup strategy, like a Levenshtein distance check
            // against all words in the Trie.

            List<String> allWords = trie.getAllWords(); // Assuming you have a method to get all words from the Trie

            // Use Levenshtein distance for suggestions
            List<String> levenshteinSuggestions = new ArrayList<>();
            for (String w : allWords) {
                int distance = getLevenshteinDistance(word.toLowerCase(), w.toLowerCase());
                if (distance <= 3) { // Adjust the threshold as needed
                    levenshteinSuggestions.add(w);
                }
            }

            return levenshteinSuggestions.subList(0, Math.min(levenshteinSuggestions.size(), 5));
        }

        // Use the Trie suggestions
        return suggestions.subList(0, Math.min(suggestions.size(), 5));
    }

    private static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length();
        int m = t.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int[][] d = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1;
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + cost);
            }
        }

        return d[n][m];
    }


}

