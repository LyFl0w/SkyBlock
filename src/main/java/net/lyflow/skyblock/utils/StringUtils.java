package net.lyflow.skyblock.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String capitalizeSentence(String sentence, String space, String replace) {
        final ArrayList<String> words = new ArrayList<>(Arrays.stream(sentence.split(space)).toList());
        final StringBuilder stringBuilder = new StringBuilder(capitalizeWord(words.get(0)));

        words.remove(0);
        words.forEach(word -> stringBuilder.append(replace).append(capitalizeWord(word)));

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

    public static String[] toStringList(String startingPoint, List<? extends Enum<?>> enumList) {
        final String[] messages = new String[enumList.size()];
        for (int i = 0; i < enumList.size(); i++) messages[i] = startingPoint + capitalizeWord(enumList.get(i).name());
        return messages;
    }

    public static String[] prefixWords(String toAdd, String[] message) {
        final String[] messages = new String[message.length];
        for (int i = 0; i < messages.length; i++) messages[i] = toAdd + message[i];
        return messages;
    }


}
