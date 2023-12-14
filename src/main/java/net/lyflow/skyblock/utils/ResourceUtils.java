package net.lyflow.skyblock.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class ResourceUtils {

    public static void saveResourceFile(@NotNull String resourcePath, @NotNull File destFile, @NotNull JavaPlugin javaPlugin, boolean replace) {
        if (resourcePath.isBlank()) throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        if (!resourcePath.contains(".")) throw new IllegalArgumentException("ResourcePath cannot be a folder");

        resourcePath = resourcePath.replace('\\', '/');
        final InputStream in = javaPlugin.getResource(resourcePath);
        if (in == null)
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");

        final File outFile = new File(destFile, resourcePath);
        final File outDir = new File(destFile, resourcePath.substring(0, Math.max(resourcePath.lastIndexOf(47), 0)));
        if (!outDir.exists()) outDir.mkdirs();

        try {
            if (outFile.exists() && !replace) {
                javaPlugin.getLogger().warning("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                in.close();
                return;
            }
            final OutputStream out = new FileOutputStream(outFile);
            in.transferTo(out);
            out.close();
            in.close();
        } catch (IOException exception) {
            javaPlugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, exception);
        }
    }

    public static void saveResourceFolder(@NotNull String resourcePath, @NotNull File destFile, @NotNull JavaPlugin javaPlugin, boolean replace) {
        if (resourcePath.isBlank()) throw new IllegalArgumentException("ResourcePath cannot be null or empty");

        resourcePath = resourcePath.replace('\\', '/');

        final URL url = javaPlugin.getClass().getClassLoader().getResource(resourcePath);
        final String jarPath = url.getPath().substring(5, url.getPath().indexOf("!")); //strip out only the JAR file

        try {
            // decode the compiled jar for iteration
            final JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
            final Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName().replace('\\', '/');
                if (name.startsWith(resourcePath)) {
                    final String entry = name.substring(resourcePath.length() + 1);
                    final String last = name.substring(name.length() - 1);
                    if (!last.equals("/") && entry.matches(".*[a-zA-Z0-9].*"))
                        saveResourceFileFromFolder(resourcePath, entry, destFile, javaPlugin, replace);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveResourceFileFromFolder(@NotNull String resourcePath, @NotNull String entry, @NotNull File destFile, @NotNull JavaPlugin javaPlugin, boolean replace) {
        if (resourcePath.isBlank()) throw new IllegalArgumentException("ResourcePath cannot be null or empty");

        resourcePath = resourcePath.replace('\\', '/');
        final InputStream in = javaPlugin.getResource(resourcePath + "/" + entry);
        if (in == null)
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");

        final File outFile = new File(destFile, entry);
        if (!outFile.getParentFile().exists()) outFile.getParentFile().mkdirs();

        try {
            if (outFile.exists() && !replace) {
                javaPlugin.getLogger().warning("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                in.close();
                return;
            }
            final OutputStream out = new FileOutputStream(outFile);
            in.transferTo(out);
            out.close();
            in.close();
        } catch (IOException exception) {
            javaPlugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, exception);
        }
    }

}