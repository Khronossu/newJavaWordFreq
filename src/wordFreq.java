import java.util.*;
import java.io.*;

class FilesNotFoundException extends Exception {
    public FilesNotFoundException(String message) {
        super(message);
    }
}


public class wordFreq {
    public static void main(String[] args) {
        String txtfileDirec = "D://chrome download folder//musictextfilejava";
        File directory = new File(txtfileDirec);
        String[] fileNames = directory.list();

        if (fileNames == null || fileNames.length == 0) {
            try {
                throw new FilesNotFoundException("No files found in directory: " + txtfileDirec);
            } catch (FilesNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        Map<String, Integer> fileIndexMap = new HashMap<>();
        for (int i = 0; i < fileNames.length; i++) {
            fileIndexMap.put(fileNames[i], i);
        }

        Map<String, int[]> wordFreqMap = new TreeMap<>();

        for (String filename : fileNames) {
            File file = new File(directory, filename);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!word.isEmpty() && word.matches("[a-z]+")) {
                        int[] frequencies = wordFreqMap.getOrDefault(word, new int[fileNames.length]);
                        frequencies[fileIndexMap.get(filename)]++;
                        wordFreqMap.put(word, frequencies);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            }
        }

        // Export word frequencies to CSV
        exportWordFreqToCSV(wordFreqMap, "word_frequencies.csv", fileNames);
    }

    private static void exportWordFreqToCSV(Map<String, int[]> wordFreqMap, String csvFile, String[] fileNames) {
        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write header
            writer.append("Word");
            for (String fileName : fileNames) {
                writer.append(',');
                writer.append(fileName); // File names as column headers
            }
            writer.append('\n');

            // Write data
            for (Map.Entry<String, int[]> entry : wordFreqMap.entrySet()) {
                writer.append(entry.getKey()); // Word
                for (int freq : entry.getValue()) {
                    writer.append(',');
                    writer.append(String.valueOf(freq)); // Frequency
                }
                writer.append('\n');
            }

            System.out.println("Word frequencies exported to CSV file successfully!");
        } catch (IOException e) {
            System.err.println("Error exporting word frequencies to CSV file: " + e.getMessage());
        }
    }
}
