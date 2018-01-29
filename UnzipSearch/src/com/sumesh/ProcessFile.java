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

public class ProcessFile
{
	private static String		SEARCH_TEXT_2			= "LOC+11+TWKHH";
	private static String		SEARCH_TEXT_1			= "LOC+9+ITSPE";
	private static final String	START_DELIMITER			= "TDT+20";
	private static final String	EMPTY_STRING			= "";
	private static final String	SEARCH_RESULTS_LOCATION	= "\\output\\Search_Results";
	private static final String	EXTRACTED_FILE_LOCATION	= "\\output\\extracted";

	public static void main(String... args) throws IOException
	{
		if (null != args && args.length > 0 && null != args[0])
		{
			SEARCH_TEXT_1 = args[0];
			if (null != args[1])
			{
				SEARCH_TEXT_2 = args[1];
			}
			else
			{
				System.out.println("Search text two set  as " + SEARCH_TEXT_2);
			}
		}
		else
		{
			System.out.println("Search text set as " + SEARCH_TEXT_1 + " and " + SEARCH_TEXT_2);
		}

		File currentFolder = new File(System.getProperty("user.dir"));
		Scanner scanner = new Scanner(System.in);
		System.out.println("Do you want to unzip the files and start (Y/N)?");
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
		System.out.println("Searching files in progress");
		if (!new File(System.getProperty("user.dir") + SEARCH_RESULTS_LOCATION).exists())
		{
			new File(System.getProperty("user.dir") + SEARCH_RESULTS_LOCATION).mkdirs();
		}
		if (null != listOfFiles)
		{
			for (File file : listOfFiles)
			{
				if (file.getName().contains(".txt"))
				{
					System.out.println("Processing " + file.getName());
					processFile(file, System.getProperty("user.dir") + SEARCH_RESULTS_LOCATION + "\\result_" + file.getName());
				}
			}
			System.out.println("Searching completed for " + listOfFiles.length + " files");
		}
		else
		{
			System.out.println("No files found for search");
		}

		scanner.close();
	}

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
						if (currentSchedule.contains(SEARCH_TEXT_1) && currentSchedule.contains(SEARCH_TEXT_2))
						{
							matchCounter++;
							writer.write(currentSchedule + System.getProperty("line.separator"));
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