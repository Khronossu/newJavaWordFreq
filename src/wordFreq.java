import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class wordFreq {
    public static void main(String[] args) {
        String txtfileDirec = "D://chrome download folder//musictextfilejava";
        File Directory = new File(txtfileDirec);
        String[] fileNames = Directory.list();
        Map<String, int[]> wordFreqMap = new HashMap<>();

        for (String filename : fileNames) {
            File file = new File(Directory, filename); // Correcting the file path
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!word.isEmpty() && word.matches("[a-z]+")) { // Check if word is not empty and contains only alphabets
                        int[] frequencies = wordFreqMap.getOrDefault(word, new int[fileNames.length]);
                        frequencies[getIndex(fileNames, filename)]++;
                        wordFreqMap.put(word, frequencies);
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            }
        }

        List<Map.Entry<String, int[]>> sortedEntries = new ArrayList<>(wordFreqMap.entrySet());
        Collections.sort(sortedEntries, Map.Entry.comparingByKey());

        // Export word frequencies to CSV
        exportWordFreqToCSV(wordFreqMap, "word_frequencies.csv");
    }

    private static int getIndex(String[] fileNames, String fileName) {
        for (int i = 0; i < fileNames.length; i++) {
            if (fileNames[i].equals(fileName)) {
                return i;
            }
        }
        return -1;
    }

    private static void exportWordFreqToCSV(Map<String, int[]> wordFreqMap, String csvFile) {
        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write header
            writer.append("Word");
            for (int i = 0; i < wordFreqMap.entrySet().iterator().next().getValue().length; i++) {
                writer.append(',');
                writer.append("File" + (i + 1)); // File names as column headers
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
            e.printStackTrace();
        }
    }

    }

