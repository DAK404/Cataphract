/*
*                                                      |
*                                                     ||
*  |||||| ||||||||| |||||||| ||||||||| |||||||  |||  ||| ||||||| |||||||||  |||||| ||||||||
* |||            ||    |||          ||       || |||  |||       ||       || |||        |||
* |||      ||||||||    |||    ||||||||  ||||||  ||||||||  ||||||  |||||||| |||        |||
* |||      |||  |||    |||    |||  |||  |||     |||  |||  ||  ||  |||  ||| |||        |||
*  ||||||  |||  |||    |||    |||  |||  |||     |||  |||  ||   || |||  |||  ||||||    |||
*                                               ||
*                                               |
*
* A Cross Platform OS Shell
* Powered By Truncheon Core
*/

/*
* This file is part of the Cataphract project.
* Copyright (C) 2024 DAK404 (https://github.com/DAK404)
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * An Application to sign the build files, to ensure the program integrity.
 *
 * @author: DAK
 * @version: 1.0
 */
public class BuildSigner
{
    private static final List<String> filePaths = new ArrayList<>();
    private static final String fileSeparator = System.getProperty("file.separator");
    private static final String MANIFEST_DIR = "./.Manifest/Cataphract";
    private static final String[] IGNORE_LIST = {
        ".Manifest", "System", "Users", "org", "JRE", "BootShell.cmd",
        "BuildSigner.java", "Logs"
    };

    /**
     * Logic to sign the build.
     *
     * @param args arguments passed during invocation
     */
    public static void main(String[] args)
    {
        try
        {
            new File(MANIFEST_DIR).mkdirs();
            BuildSigner signer = new BuildSigner();
            System.out.println();
            System.out.println("Phase 1: File Enumeration\n");
            signer.enumerateFiles(new File("./"));
            System.out.println();
            System.out.println("Phase 2: Files to Manifest M1\n");
            signer.storeHashes();
            System.out.println();
            System.out.println("Phase 3: Files to Manifest M2\n");
            signer.storeFileSizes();
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Enumerate all the files and subdirectory in the specified directory.
     *
     * @param directory The directory to be traversed through to enumerate contents
     */
    private void enumerateFiles(File directory)
    {
        System.out.println("- Discovering Files in Directory: " + directory.getPath());
        File[] filesList = directory.listFiles();
        if (filesList == null) return;

        for (File file : filesList)
        {
            if (ignoreFiles(file.getName()))
            continue;
            if (file.isDirectory())
            {
                System.out.println("-- Entering Directory: " + file.getPath());
                enumerateFiles(file);
                System.out.println("-- Exiting Directory: " + file.getPath());
            }
            else if (file.isFile())
            {
                System.out.println("--- Discovered File: " + file.getPath());
                filePaths.add(file.getPath());
            }
        }
    }

    /**
     * Stores the file hashes in a properties file.
     */
    private void storeHashes()
    {
        Properties props = new Properties();
        try (FileOutputStream output = new FileOutputStream(MANIFEST_DIR + "/KernelFilesHashes.m1"))
        {
            for (String fileName : filePaths)
            {
                String formattedPath = formatPath(fileName);
                props.setProperty(formattedPath, fileToSHA3_256(fileName));
                System.out.println("[MANIFEST 1] Signing File: " + formattedPath);
            }
            props.storeToXML(output, "File Manifest");
        }
        catch (Exception e)
        {
            System.out.println("Error storing hashes: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Stores the file sizes in a properties file.
     *
     * @throws Exception
     */
    private void storeFileSizes() throws Exception
    {
        Properties props = new Properties();
        try (FileOutputStream output = new FileOutputStream(MANIFEST_DIR + "/KernelFiles.m2"))
        {
            for (String fileName : filePaths)
            {
                if (fileName.endsWith(".class"))
                {
                    String formattedPath = formatPath(fileName);
                    props.setProperty(formattedPath, String.valueOf(new File(fileName).length()));
                    System.out.println("[MANIFEST 2] Adding File: " + formattedPath);
                }
            }
            props.storeToXML(output, "File Sizes");
        }
    }

    /**
     * Logic to ignore a few files/directories.
     *
     * @param fileName The file name to be checked
     * @return True if the file/directory is to be ignored, false otherwise
     */
    private boolean ignoreFiles(String fileName)
    {
        for (String ignored : IGNORE_LIST)
        {
            if (fileName.equalsIgnoreCase(ignored))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts file path to platform-independent format.
     *
     * @param fileName The file path
     * @return The formatted file path
     */
    private String formatPath(String fileName)
    {
        return fileName.replaceAll(Matcher.quoteReplacement(fileSeparator), "|");
    }

    /**
     * Generates the SHA3-256 hash of a file.
     *
     * @param fileName The file name
     * @return The SHA3-256 hash
     * @throws Exception
     */
    private String fileToSHA3_256(String fileName) throws Exception
    {
        return hashFile(new File(fileName), "SHA3-256");
    }

    /**
     * Converts a byte array to a hex string.
     *
     * @param arrayBytes The byte array
     * @return The hex string
     */
    private String convertByteArrayToHexString(byte[] arrayBytes)
    {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte arrayByte : arrayBytes)
        {
            stringBuffer.append(Integer.toString((arrayByte & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    /**
     * Hashes a file using the specified algorithm.
     *
     * @param file The file
     * @param algorithm The hashing algorithm
     * @return The hash of the file
     * @throws Exception
     */
    private String hashFile(File file, String algorithm) throws Exception
    {
        try (FileInputStream inputStream = new FileInputStream(file))
        {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] bytesBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(bytesBuffer)) != -1)
            {
                digest.update(bytesBuffer, 0, bytesRead);
            }
            return convertByteArrayToHexString(digest.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("Unsupported Algorithm: " + e);
            e.printStackTrace();
            throw e;
        }
    }
}
