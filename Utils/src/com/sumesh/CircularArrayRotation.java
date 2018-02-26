package com.sumesh;

import java.util.Scanner;

public class CircularArrayRotation
{
	static int[] circularArrayRotation(int[] a, int[] m, int k)
	{
		int[] rotated = new int[a.length];
		for (int i = 0; i < a.length; i++)
		{
			int newIndex = getNewIndex(i, k, a.length);
			rotated[newIndex] = a[i];
		}

		for (int i = 0; i < m.length; i++)
		{
			if (m[i] >= 0 && m[i] <= rotated.length)
			{
				System.out.println(rotated[m[i]]);
			}
			else
			{
				// System.out.println("Invalid query at m[" + i + "] with index
				// " + m[i]);
			}

		}
		return rotated;

	}

	static int getNewIndex(int currentIndex, int rotation, int length)
	{
		int newIndex;
		if (currentIndex + rotation > length - 1)
		{
			if ((currentIndex + rotation) - length <= length - 1)
			{
				newIndex = (currentIndex + rotation) - length;
			}
			else
			{
				while (rotation >= length)
				{
					rotation -= length;
				}
				newIndex = getNewIndex(currentIndex, rotation, length);
			}

		}
		else
		{
			newIndex = currentIndex + rotation;
		}

		return newIndex;
	}

	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int k = in.nextInt();
		int q = in.nextInt();
		int[] a = new int[n];
		for (int a_i = 0; a_i < n; a_i++)
		{
			a[a_i] = in.nextInt();
		}
		int[] m = new int[q];
		for (int m_i = 0; m_i < q; m_i++)
		{
			m[m_i] = in.nextInt();
		}
		circularArrayRotation(a, m, k);

		in.close();
	}
}
