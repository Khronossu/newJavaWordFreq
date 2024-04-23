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

        List<String> wordsList = new ArrayList<>();
        Map<String, Integer> fileIndexMap = new HashMap<>();
        for (int i = 0; i < fileNames.length; i++) {
            fileIndexMap.put(fileNames[i], i);
        }

        for (String filename : fileNames) {
            File file = new File(directory, filename);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase().replaceAll("[^a-zA-Z]", ""); // Remove non-alphabetic characters
                    if (!word.isEmpty()) {
                        if (!wordsList.contains(word)) {
                            wordsList.add(word);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            }
        }

        // Sort words alphabetically
        Collections.sort(wordsList);

        // Create word frequency matrix
        int[][] wordFreqMatrix = new int[wordsList.size()][fileNames.length];

        for (String filename : fileNames) {
            int fileIndex = fileIndexMap.get(filename);
            File file = new File(directory, filename);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase().replaceAll("[^a-zA-Z]", ""); // Remove non-alphabetic characters
                    if (!word.isEmpty()) {
                        int wordIndex = wordsList.indexOf(word);
                        wordFreqMatrix[wordIndex][fileIndex]++;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            }
        }

        // Export word frequencies to CSV
        exportWordFreqToCSV(wordsList, wordFreqMatrix, "word_frequencies.csv", fileNames);
    }

    private static void exportWordFreqToCSV(List<String> wordsList, int[][] wordFreqMatrix, String csvFile, String[] fileNames) {
        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write header
            writer.append("Word");
            for (String fileName : fileNames) {
                writer.append(',');
                writer.append(fileName); // File names as column headers
            }
            writer.append('\n');

            // Write data
            for (int i = 0; i < wordsList.size(); i++) {
                writer.append(wordsList.get(i)); // Word
                for (int j = 0; j < fileNames.length; j++) {
                    writer.append(',');
                    writer.append(String.valueOf(wordFreqMatrix[i][j])); // Frequency
                }
                writer.append('\n');
            }

            System.out.println("Word frequencies exported to CSV file successfully!");
        } catch (IOException e) {
            System.err.println("Error exporting word frequencies to CSV file: " + e.getMessage());
        }
    }
}
