package by.nosevich.internship.task2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		List<Entry> list = Arrays.asList(
			new Entry(1,null,"Folder1"),
			new Entry(2,null,"Folder2"),
			new Entry(3,1,"InnerFolder1"),
			new Entry(4,1,"InnerFolder2"),
			new Entry(5,2,"InnerFolder3"),
			new Entry(6,1,"File1"),
			new Entry(7,3,"File2"),
			new Entry(8,5,"File3"),
			new Entry(9,10,"RecursiveFile1"),
			new Entry(10,9,"RecursiveFile2"),
			new Entry(11,150,"Folder3"));
		
		if (!hasSameId(list)) {
			showTree(list);
		} else {
			System.out.println("Error. Some records have the same id.");
		}
	}

	private static boolean hasSameId(List<Entry> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int ii = 0; ii < list.size(); ii++) {
				if (i!=ii && list.get(i).getId()==list.get(ii).getId()) {
					return true;
				}
			}
		}
		return false;
	}

	private static void showTree(List<Entry> list) {;
		Map<Entry, Boolean> hasParent = getHasParentMap(list);
		for(Entry elem : list) {
			if (hasParent.get(elem)==false) {
				showTree(elem, list, 0);
			}
		}
	}
	
	private static Map<Entry, Boolean> getHasParentMap(List<Entry> list) {
		Map<Entry, Boolean> result = new HashMap<Entry, Boolean>();
		for(Entry i : list) {
			result.put(i, false);
			for(Entry ii : list) {
				if (i.getParrentId()!=null && i.getParrentId()==ii.getId()) {
					result.put(i, true);
				}
			}
		}
		return result;
	}
	
	private static void showTree(Entry elem, List<Entry> list, int level) {
		String output = "";
		for (int i = 0; i < level; i++) {
			output+="\t";
		}
		output+="|"+elem.getName();
		System.out.println(output);
		list.stream()
			.filter(i -> i.getParrentId()==elem.getId())
			.forEach(i->showTree(i,list,level+1));
	}
}
