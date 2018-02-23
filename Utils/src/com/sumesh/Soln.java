package com.sumesh;

import java.io.IOException;

public class Soln
{

	private static String encode(String text)
	{
		StringBuilder b = new StringBuilder();
		StringBuilder y = new StringBuilder();
		for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			b.append(c += c + i);
		}
		return b.reverse().toString();
	}

	/*
	 * Complete the function below.
	 */
	static String decode(String encodedMessage)
	{
		 return "";
	}

	public static void main(String[] args) throws IOException
	{
		System.out.println("Encode :" + encode("Encoded password"));
		System.out.println("decoded : " + decode(encode("Encoded password")));
	}

}
