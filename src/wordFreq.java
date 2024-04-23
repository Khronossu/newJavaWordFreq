import java.util.*;
import java.io.*;

class FilesNotFoundException extends Exception {
    public FilesNotFoundException(String message) {
        super(message);
    }
}

public class wordFreq {
    public static void main(String[] args) {


        String txtfilePath = "D://chrome download folder//musictextfilejava"; //set the .txt file directory
        File directory = new File(txtfilePath);
        String[] fileNames = directory.list();

        if (fileNames == null || fileNames.length == 0) {
            try {
                throw new FilesNotFoundException("No files found in directory: " + directory);
            } catch (FilesNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        List<String> words = new ArrayList<>();
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
                        if (!words.contains(word)) {
                            words.add(word);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            }
        }

        // Sort words alphabetically
        Collections.sort(words);

        // 2D array for storing
        int[][] wordFreqMatrix = new int[words.size()][fileNames.length];

        for (String filename : fileNames) {
            int fileIndex = fileIndexMap.get(filename);
            File file = new File(directory, filename);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase().replaceAll("[^a-zA-Z]", ""); // Remove non-alphabetic characters
                    if (!word.isEmpty()) {
                        int wordIndex = words.indexOf(word);
                        wordFreqMatrix[wordIndex][fileIndex]++;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
            }
        }

        // Export word frequencies to CSV
        exportWordFreqToCSV(words, wordFreqMatrix, "word_frequencies.csv", fileNames);
    }

    private static void exportWordFreqToCSV(List<String> wordsList, int[][] wordFreqMatrix, String csvFile, String[] fileNames) {
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("Word"); //set Header
            for (String fileName : fileNames) {
                writer.append(',');
                writer.append(fileName); // set file name as column header
            }
            writer.append('\n');

            // Write the matrix
            for (int i = 0; i < wordsList.size(); i++) {
                writer.append(wordsList.get(i)); // Word
                for (int j = 0; j < fileNames.length; j++) {
                    writer.append(',');
                    writer.append(String.valueOf(wordFreqMatrix[i][j])); // Frequency
                }
                writer.append('\n');
            }

            System.out.println("Word Frequency have been exported to .csv!");
        } catch (IOException e) {
            System.err.println("Something went wrong!: " + e.getMessage());
        }
    }
}
