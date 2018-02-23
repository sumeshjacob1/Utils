package com.sumesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeRanges
{
	public static void main(String[] args)
	{
		List<Interval> intervals = new ArrayList<Interval>();
		intervals.add(new MergeRanges().new Interval(2, 9));
		intervals.add(new MergeRanges().new Interval(20, 45));
		intervals.add(new MergeRanges().new Interval(11, 15));
		intervals.add(new MergeRanges().new Interval(36, 40));
		intervals.add(new MergeRanges().new Interval(25, 33));

		System.out.println("Input : " + intervals);
		sortRange(intervals);
		System.out.println("Sorted : " + intervals);

		System.out.println("Merged : " + mergeRanges(intervals));
	}

	static Interval merge(Interval i1, Interval i2)
	{
		int end = i1.end >= i2.end ? i1.end : i2.end;
		return new MergeRanges().new Interval(i1.start, end);
	}

	static boolean isMergable(Interval i1, Interval i2)
	{
		return i1.end >= i2.start;
	}

	static void sortRange(List<Interval> intervals)
	{
		if (intervals != null)
		{
			for (int i = 0; i < intervals.size(); i++)
			{
				for (int j = i + 1; j < intervals.size(); j++)
				{
					if (intervals.get(i).start > intervals.get(j).start)
					{
						Collections.swap(intervals, i, j);
					}
				}
			}
		}
	}

	static List<Interval> mergeRanges(List<Interval> sortedIntervals)
	{

		List<Interval> mergedIntervals = new ArrayList<Interval>();

		if (sortedIntervals != null && !sortedIntervals.isEmpty())
		{
			mergedIntervals.add(sortedIntervals.get(0));
			for (int i = 0; i < sortedIntervals.size(); i++)
			{
				Interval current = mergedIntervals.get(mergedIntervals.size() - 1);
				Interval next = null;
				if (i + 1 <= sortedIntervals.size() - 1)
				{
					next = sortedIntervals.get(i + 1);
				}
				else
				{
					// already in last node
				}

				// check to see if next can be merged
				if (next != null)
				{
					if (isMergable(current, next))
					{
						// first remove last Interval from list
						mergedIntervals.remove(mergedIntervals.size() - 1);

						// Add new interval
						mergedIntervals.add(merge(current, next));
					}
					else
					{
						mergedIntervals.add(next);
					}

				}
			}

		}
		return mergedIntervals;
	}

	class Interval
	{

		int	start;
		int	end;

		public Interval(int start, int end)
		{
			this.start = start;
			this.end = end;
		}

		public String toString()
		{
			return "[" + start + " , " + end + "]";
		}

	}
}
