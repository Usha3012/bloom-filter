package com.dkb;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String INPUT_FILE = "words.txt";
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) throws URISyntaxException, IOException {
        List<String> words = readFile(INPUT_FILE);
        BloomFilter bloomFilter = new BloomFilter(words.size(), 0.005);
        addWords(bloomFilter, words);
        List<String> result = checkResult(bloomFilter, words);
        writeToFile(result);

    }

    private static void writeToFile(List<String> result) throws IOException {
        if (Files.exists(Paths.get(OUTPUT_FILE))) {
            Files.delete(Paths.get(OUTPUT_FILE));
        }
        Path output = Files.createFile(Paths.get(OUTPUT_FILE));
        Files.write(output, Arrays.asList(result.toArray(new CharSequence[0])), StandardOpenOption.APPEND);
    }

    private static List<String> checkResult(BloomFilter bloomFilter, List<String> words) {
        return words.stream()
                .map(bloomFilter::contains)
                .map(String::valueOf)
                .collect(Collectors.toList());

    }

    private static void addWords(BloomFilter bloomFilter, List<String> words) {
        for (String word : words) {
            try {
                bloomFilter.add(word);
            } catch (Exception ex) {
                // in case any word throw error dont stop processing. Better to log!!
            }
        }
    }

    private static List<String> readFile(String fileName) throws URISyntaxException, IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream != null) {
            return IOUtils.readLines(inputStream, StandardCharsets.ISO_8859_1);
        }
        return Collections.emptyList();
    }
}
