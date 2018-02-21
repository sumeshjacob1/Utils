package com.sumesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 
 * @author sja002
 *
 */
public class ProcessFile
{
	private static String		SEARCH_TEXT_2			= null;
	private static String		SEARCH_TEXT_1			= "LOC+9+ITSPE";
	private static String		START_DELIMITER			= "TDT+20";
	private static final String	EMPTY_STRING			= "";
	private static final String	SEARCH_RESULTS_LOCATION	= "\\output\\Search_Results";
	private static final String	EXTRACTED_FILE_LOCATION	= "\\output\\extracted";

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException
	{
		readSearchSettings();

		File currentFolder = new File(System.getProperty("user.dir"));
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nDo you want to unzip the files and start (Y/N)?");
		String userPrompt = scanner.nextLine();

		if ("Y".equalsIgnoreCase(userPrompt) || "YES".equalsIgnoreCase(userPrompt))
		{
			File[] listOfFiles = currentFolder.listFiles();
			System.out.println("Unzipping files in progress");
			Integer zipFileCounter = 0;
			for (File file : listOfFiles)
			{
				if (file.getName().contains(".zip"))
				{
					zipFileCounter++;
					unzip(file, System.getProperty("user.dir") + EXTRACTED_FILE_LOCATION);
				}
			}
			System.out.println("Unzipped " + zipFileCounter + " files");

			currentFolder = new File(System.getProperty("user.dir") + EXTRACTED_FILE_LOCATION);

		}

		File[] listOfFiles = currentFolder.listFiles();
		System.out.println("\nSearching files in progress");
		if (!new File(System.getProperty("user.dir") + SEARCH_RESULTS_LOCATION).exists())
		{
			new File(System.getProperty("user.dir") + SEARCH_RESULTS_LOCATION).mkdirs();
		}
		if (null != listOfFiles)
		{
			Integer textFileCounter = 0;
			for (File file : listOfFiles)
			{
				if (file.getName().contains(".txt"))
				{
					textFileCounter++;
					System.out.println("\nProcessing " + file.getName());
					processFile(file, System.getProperty("user.dir") + SEARCH_RESULTS_LOCATION + "\\result_" + file.getName());
				}
			}
			System.out.println("\nSearching completed for " + textFileCounter + " files");
		}
		else
		{
			System.out.println("No files found for search");
		}

		scanner.close();
	}

	/**
	 * 
	 */
	private static void readSearchSettings()
	{
		if (null != System.getProperty("delimiter"))
		{
			System.out.println("Setting delimiter");
			START_DELIMITER = System.getProperty("delimiter");
		}

		if (null != System.getProperty("search.text.1"))
		{
			System.out.println("Setting search.text.1");
			SEARCH_TEXT_1 = System.getProperty("search.text.1");
		}

		if (null != System.getProperty("search.text.2"))
		{
			System.out.println("Setting search.text.2");
			SEARCH_TEXT_2 = System.getProperty("search.text.2");
		}

		System.out.println("\n----------------- Search settings ---------------");
		System.out.println("Delimter      :  " + START_DELIMITER);
		System.out.println("Search text 1 :  " + SEARCH_TEXT_1);
		if (null != System.getProperty("search.text.2"))
		{
			System.out.println("Search text 2 :  " + SEARCH_TEXT_2);
		}
		System.out.println("------------------------------------------------");
	}

	/**
	 * 
	 * @param inputFile
	 * @param outFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void processFile(File inputFile, String outFile) throws FileNotFoundException, IOException
	{
		StringBuilder bufferedString = new StringBuilder();
		Integer schedulesCounter = 0;
		Integer matchCounter = 0;
		FileInputStream fstream = null;
		BufferedReader br = null;
		FileWriter writer = null;
		try
		{
			fstream = new FileInputStream(inputFile);
			br = new BufferedReader(new InputStreamReader(fstream));
			writer = new FileWriter(new File(outFile));
			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{
				// Start of a new schedule
				if (strLine.contains(START_DELIMITER))
				{
					schedulesCounter++;
					String currentSchedule = bufferedString.toString();
					bufferedString = new StringBuilder(EMPTY_STRING);

					if (currentSchedule != null && !EMPTY_STRING.equals(currentSchedule.toString()))
					{
						if (null != SEARCH_TEXT_2)
						{
							if (currentSchedule.contains(SEARCH_TEXT_1) && currentSchedule.contains(SEARCH_TEXT_2))
							{
								matchCounter++;
								writer.write(currentSchedule + System.getProperty("line.separator"));
							}
						}
						else
						{
							if (currentSchedule.contains(SEARCH_TEXT_1))
							{
								matchCounter++;
								writer.write(currentSchedule + System.getProperty("line.separator"));
							}
						}
					}

					bufferedString.append(strLine);
				}
				else
				{
					bufferedString.append(strLine + System.getProperty("line.separator"));
				}
			}
		}
		finally
		{
			System.out.println("Scanned " + schedulesCounter + " schedules and found " + matchCounter + " match");
			if (br != null)
			{
				br.close();
			}
			if (fstream != null)
			{
				fstream.close();
			}

			if (writer != null)
			{
				writer.close();
			}
		}

	}

	/**
	 * 
	 * @param zipFilePath
	 * @param destDir
	 */
	private static void unzip(File zipFilePath, String destDir)
	{
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists()) dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try
		{
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null)
			{
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileWriter zipFileWriter = new FileWriter(newFile);

				String tempLine;
				while (zis.read(buffer) > 0)
				{
					tempLine = new String(buffer);
					String[] lines = tempLine.split("'");
					for (String line : lines)
					{
						zipFileWriter.write(line + "'" + System.getProperty("line.separator"));
					}
				}
				zipFileWriter.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}