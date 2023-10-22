package dev.tolja;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.*;

public class ReLinkJar {
    public static void ReLink() {
        String originalJarPath = "out\\input.jar";
        String newFilePath = "out\\cpp\\build\\lib\\x64-windows.dll";

        try {
            File tempJarFile = File.createTempFile("tempJar", ".jar");
            tempJarFile.deleteOnExit();

            JarFile originalJar = new JarFile(originalJarPath);
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tempJarFile));

            Enumeration<JarEntry> entries = originalJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                InputStream inputStream = originalJar.getInputStream(entry);

                JarEntry newEntry = new JarEntry(entry.getName());
                jarOutputStream.putNextEntry(newEntry);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    jarOutputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                jarOutputStream.closeEntry();
            }

            String entryName = "native0/x64-windows.dll";
            JarEntry newJarEntry = new JarEntry(entryName);
            jarOutputStream.putNextEntry(newJarEntry);

            FileInputStream fileInputStream = new FileInputStream(newFilePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                jarOutputStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            jarOutputStream.closeEntry();

            originalJar.close();
            jarOutputStream.close();

            File originalJarFile = new File(originalJarPath);
            if (originalJarFile.exists()) {
                originalJarFile.delete();
            }
            tempJarFile.renameTo(originalJarFile);

            System.out.println("Relink jar file successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}