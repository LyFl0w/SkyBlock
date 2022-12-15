package net.lyflow.skyblock.utils;

import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandUtils {

    public static List<String> completionTable(String arg, List<String> completions){
        // Copy the matches of the first argument of the list (e.g. if the first argument is 'm', it will just return 'minecraft')
        return (arg == null) ? null :
                new ArrayList<>(completions.stream().parallel().filter(argument -> StringUtil.startsWithIgnoreCase(argument, arg)).toList());
    }

    public static List<String> completionTable(String arg, String[] completions){
        return completionTable(arg, Arrays.asList(completions));
    }

}
