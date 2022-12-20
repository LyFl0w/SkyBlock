package net.lyflow.skyblock.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class StringUtils {

    public static String capitalizeSentence(String sentence, String space, String replace) {
        final ArrayList<String> words = new ArrayList<>(Arrays.stream(sentence.split(space)).toList());
        final StringBuilder stringBuilder = new StringBuilder(capitalizeWord(words.get(0)));

        words.remove(0);
        words.stream().forEach(word -> stringBuilder.append(replace).append(capitalizeWord(word)));

        return stringBuilder.toString();
    }

    public static String capitalizeSentence(String sentence, String space) {
        return capitalizeSentence(sentence, space, space);
    }

    public static String capitalizeSentence(String sentence) {
        return capitalizeSentence(sentence, " ");
    }

    public static String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

}
