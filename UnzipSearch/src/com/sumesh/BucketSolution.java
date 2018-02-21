package com.sumesh;

import java.util.HashMap;
import java.util.Map;

public class Solution
{
	public static void main(String[] args)
	{
		getVolumeAtIndex(20, 2, 8);
	}

	static void getVolumeAtIndex(float volume, float maxCapacity, int searchIndex)
	{

		if (volume > 0 && searchIndex > 0)
		{
			Map<Integer, Bucket> buckets = new HashMap<Integer, Bucket>();
			Bucket root = new Solution().new Bucket();
			root.setIndex(1);
			root.setLevel(1);

			buckets.put(1, root);

			fillBuckets(volume, maxCapacity, root, buckets);
			Bucket searchBucket = buckets.get(searchIndex);

			if (null == searchBucket)
			{
				System.out.println("Bucket " + searchIndex + " has volume 0");
			}
			else
			{
				System.out.println("Bucket " + searchIndex + " has volume " + searchBucket.getCurrrentVolume());
			}

		}
		else
			System.out.println("Bucket " + searchIndex + " has volume 0");

	}

	static void fillBuckets(float volume, float maxCapacity, Bucket root, Map<Integer, Bucket> buckets)
	{
		float remainingVolume = volume;
		if (remainingVolume > 0)
		{
			if (remainingVolume > maxCapacity)
			{
				fillChilds(maxCapacity, root, buckets, remainingVolume);
			}
			else
			{
				fillLeafNode(maxCapacity, root, buckets, remainingVolume);
			}

		}
	}

	private static void fillLeafNode(float maxCapacity, Bucket root, Map<Integer, Bucket> buckets, float remainingVolume)
	{
		if (root.getCurrrentVolume() == 0)
		{
			// No child needed as no spill over
			System.out.println("Filling bucket " + root.getIndex() + " with volume " + remainingVolume);
			root.setVolume(remainingVolume);
		}
		else if (root.getCurrrentVolume() + remainingVolume <= maxCapacity)
		{
			System.out.println("Filling bucket " + root.getIndex() + " with volume " + (root.getCurrrentVolume() + remainingVolume));
			root.setVolume(root.getCurrrentVolume() + remainingVolume);
		}
		else // current volume plus remaining exceeds bucket capacity
		{
			fillChilds(maxCapacity, root, buckets, root.getCurrrentVolume() + remainingVolume);
		}
	}

	private static void fillChilds(float maxCapacity, Bucket root, Map<Integer, Bucket> buckets, float remainingVolume)
	{
		System.out.println("Filling bucket " + root.getIndex() + " with remaining volume " + remainingVolume);

		// Take in to account if there is a spill from one parent already
		remainingVolume = remainingVolume - (maxCapacity - root.getCurrrentVolume());

		root.setVolume(maxCapacity);

		// Create children to evenly distribute remainingVolume
		int nextLeftNodeIndex = root.getLevel() + root.getIndex();
		int nextRightNodeIndex = root.getLevel() + root.getIndex() + 1;

		Bucket leftChild;
		if (buckets.get(nextLeftNodeIndex) != null)
		{
			leftChild = buckets.get(nextLeftNodeIndex);
		}
		else
		{
			leftChild = new Solution().new Bucket();
			leftChild.setIndex(nextLeftNodeIndex);
			leftChild.setLevel(root.getLevel() + 1);
		}
		buckets.put(leftChild.getIndex(), leftChild);

		Bucket rightChild;
		if (buckets.get(nextRightNodeIndex) != null)
		{
			rightChild = buckets.get(nextRightNodeIndex);
		}
		else
		{
			rightChild = new Solution().new Bucket();
			rightChild.setIndex(nextRightNodeIndex);
			rightChild.setLevel(root.getLevel() + 1);
		}
		buckets.put(rightChild.getIndex(), rightChild);

		root.setLeft(leftChild);
		root.setRight(rightChild);

		if (remainingVolume / 2 > maxCapacity)
		{
			// fill left tree
			fillBuckets(remainingVolume / 2, maxCapacity, leftChild, buckets);

			// fill right tree
			fillBuckets(remainingVolume / 2, maxCapacity, rightChild, buckets);
		}
		else
		{
			fillLeafNode(maxCapacity, leftChild, buckets, remainingVolume / 2);
			fillLeafNode(maxCapacity, rightChild, buckets, remainingVolume / 2);
		}

	}

	class Bucket
	{
		private int		index;
		private Bucket	leftChild;
		private Bucket	rightChild;
		private float	currentVolume;
		private int		level;

		public void setLevel(int level)
		{
			this.level = level;
		}

		public int getLevel()
		{
			return this.level;
		}

		public void setVolume(float volume)
		{
			this.currentVolume = volume;
		}

		public float getCurrrentVolume()
		{
			return this.currentVolume;
		}

		public void setIndex(int index)
		{
			this.index = index;
		}

		public int getIndex()
		{
			return this.index;
		}

		public Bucket getLeft()
		{
			return this.leftChild;
		}

		public Bucket getRight()
		{
			return this.rightChild;
		}

		public void setLeft(Bucket left)
		{
			this.leftChild = left;
		}

		public void setRight(Bucket right)
		{
			this.rightChild = right;
		}
	}
}
