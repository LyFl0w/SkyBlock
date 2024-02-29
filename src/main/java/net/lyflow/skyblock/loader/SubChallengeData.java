package net.lyflow.skyblock.loader;

import net.lyflow.skyblock.challenge.SubChallenge;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SubChallengeData {

    private final String type;
    private final List<Integer> counterList;
    private final List<List<String>> elementsCounter;

    public SubChallengeData(String type, List<Integer> counterList, List<List<String>> elementsCounter) {
        this.type = type;
        this.counterList = counterList;
        this.elementsCounter = elementsCounter;
    }

    public SubChallenge<?> toSubChallenge() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Constructor<?> constructor = SubChallenge.Type.getTypeByName(type).getSubChallengeClass().getConstructor(List.class, List.class);
        return (SubChallenge<?>) constructor.newInstance(counterList, elementsCounter);
    }

}
