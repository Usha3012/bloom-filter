package com.dkb;

import com.dkb.exception.InvalidInputException;
import com.sangupta.murmur.Murmur3;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter {
    private static final double DEFAULT_FALSE_POSITIVE_PROBABILITY = 0.05;
    private static final int DEFAULT_NUM_OF_ELEMENTS = 16;
    private static final double LOG_2 = Math.log(2);
    private static final double LOG_2_SQUARE = Math.pow(Math.log(2), 2);
    private int numberOfElements;
    private double falsePositiveProbability;
    private int bitArraySize;
    private int hashCount;
    private BitSet bitSet;

    public BloomFilter() {
        this(DEFAULT_NUM_OF_ELEMENTS, DEFAULT_FALSE_POSITIVE_PROBABILITY);
    }

    public BloomFilter(int numberOfElements, double falsePositiveProbability) {
        this.falsePositiveProbability = falsePositiveProbability;
        this.numberOfElements = numberOfElements;
        this.bitArraySize = getSize(this.numberOfElements, this.falsePositiveProbability);
        this.hashCount = getHashCount(this.bitArraySize, this.numberOfElements);
        bitSet = new BitSet(this.bitArraySize);
    }

    /**
     * m: size of bit array
     * n: number of items expected to be inserted
     * hashCount = m/n *log(2)
     */
    private int getHashCount(int m, int n) {
        return (int) (m / n * LOG_2);
    }

    /**
     * p: probability false positive
     * n: number of items expected to be inserted
     * hashCount = m/n *log(2)
     */
    private int getSize(double n, double p) {
        double size = -(n * Math.log(p)) / LOG_2_SQUARE;
        return (int) size;
    }

    /**
     * Compute hashCount number of hashes for given token
     * and set the position bit to true
     */
    public void add(String token) {
        validateToken(token);
        bitSet.or(computeHashes(token)
                .stream()
                .collect(BitSet::new, BitSet::set, BitSet::or));
    }

    /**
     * Compute hashcount number of hashes for given token
     * check for existing bitset if it was set.
     */
    public boolean contains(String token) {
        validateToken(token);
        return computeHashes(token)
                .stream()
                .map(bitSet::get)
                .reduce((previous, current) -> previous && current)
                .orElse(true);
    }

    /**
     * Computing hashCount number of hashes.
     * Hash function is Murmur3 , stable non-cryptographic hash.
     */
    private List<Integer> computeHashes(final String token) {
        byte[] tokenBytes = token.getBytes();
        List<Integer> digests = new ArrayList<>();
        for (int i = 1; i <= hashCount; i++) {
            long digest = Murmur3.hash_x86_32(tokenBytes, tokenBytes.length, 257 * i) % bitArraySize;
            digests.add((int) digest);
        }
        return digests;
    }

    private void validateToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new InvalidInputException("Token cannot be null,empty or blank");
        }
    }

}
