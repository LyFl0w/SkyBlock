package net.lyflow.skyblock.utils;

import java.util.List;

public class ArrayUtils {

    private ArrayUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static float[] toFloatArray(List<Float> list) {
        final float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);

        return array;
    }


}
