package com.github.hanavan99.flashcards.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class QueryHelper<T> {

	private HashMap<String, Comparator<T>> sortMap = new HashMap<String, Comparator<T>>();
	private HashMap<String, BiFunction<T, String, Boolean>> filterMap = new HashMap<String, BiFunction<T, String, Boolean>>();

	public void addSortFunction(String name, Comparator<T> comparator) {
		sortMap.put(name, comparator);
	}

	public void addFilterFunction(String name, BiFunction<T, String, Boolean> function) {
		filterMap.put(name, function);
	}

	@SuppressWarnings("unchecked")
	public List<T> query(Collection<T> collection, String query) {
		// convert collection into a list
		ArrayList<T> list = new ArrayList<T>();
		for (T item : collection) {
			list.add(item);
		}

		// parse commands
		String[] commands = query.split(";");
		System.out.println(commands[0]);

		// create comparator function
		Comparator<T> master = (a, b) -> {
			int val = 0;
			for (String command : commands) {
				String[] args = command.split("=", 2);
				if (args[0].equals("sort")) {
					Comparator<T> comp = sortMap.get(args[1]);
					if (comp != null) {
						val = comp.compare(a, b);
						if (val != 0) {
							break; // break out of the loop
						}
					}
				}
			}
			return val;
		};

		// sort the list
		list.sort(master);

		// filter the list
		for (String command : commands) {
			String[] args = command.split("=", 2);
			if (args[0].equals("filter")) {
				String[] filterargs = args[1].split(":");
				BiFunction<T, String, Boolean> filter = filterMap.get(filterargs[0]);
				if (filter != null) {
					T[] array = (T[]) list.toArray();
					for (T item : array) {
						if (!filter.apply(item, filterargs.length > 1 ? filterargs[1] : null)) {
							list.remove(item);
						}
					}
				}
			}
		}

		return list;
	}

}
