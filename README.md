# Bloom Filter

This Bloom filter implementation takes arguement numberOfElements and falsePositiveProbability.

BloomFilter bloomFilter = new BloomFilter(10,0.005);

If we use default constructor the default number of element is 16 and falsePositiveProbability =  0.05 used .

BloomFilter bloomFilter = new BloomFilter();


## How to run

- Make sure Java 8 is installed.
- Run mvn clean install
- Run java -jar target/bloom-filter-1.0.0-jar-with-dependencies.jar 

words.txt file in the code contains a list of files, the program reads the file and output to output.txt.

## Improvements

The program currently only reads a default words.txt, it would be better to supply the file as argument.
Opt out of the feature due to time .
