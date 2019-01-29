package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */

	
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		// if empty
		if(allWords.length==0) return null;
		 
		// built is trie to be returned
		TrieNode built = new TrieNode(null, null, null);
		TrieNode storage;
		TrieNode hold;
		String word;
		boolean match= false; 
		
		// check each string in array of strings
		// for each loop will not update index vals
		for(int i=0; i<allWords.length; i++) {
			word = allWords[i];
		
			// built is initially empty and has no first child, store first word in
			if(built.firstChild==null) {
				Indexes store=new Indexes(0, (short)0, (short)(word.length()-1));
				storage=new TrieNode(store, null, null);
				built.firstChild=storage;
				continue;
				} 
			
			// store value so as to check sibling
			hold=built.firstChild;
			
			// compare strings to check if there's a match
			while (hold!=null) {
				String check=allWords[hold.substr.wordIndex];
				if(word.charAt(0)==check.charAt(hold.substr.startIndex)) {
					match=true;
					// match is found, add it to trie
					break;
				}
				else {
					match=false;
				// if no match, continue through 
					hold=hold.sibling;
				}
			}
			// make room for sibling instead of child
		  if(match==true) {
			  // find out how many characters are shared in common
			  int count=common(hold.substr, word, allWords);
			 // use this int to modify end index, remember to subtract by 1
			  hold=populate(allWords, i, hold.substr.startIndex, hold.substr.startIndex+count-1, word, hold);	 
		  }  
		  else if (match==false) {
			  TrieNode x=built.firstChild;
			  while(x.sibling!=null) {
				  x=x.sibling;
			  }
			  int y=word.length()-1;
			  Indexes sibl=new Indexes(i, (short)0, (short)y);
			  TrieNode newSibl=new TrieNode(sibl, null, null);
			  hold=newSibl;
			  x.sibling=hold;
		  }
			
		} // for loop ends integer 
		return built;
	}
	
	
	
	// purpose is to return int of common characters shared by the string from allWords and string we are testing it against
	private static int common(Indexes toCheckFrom, String secondary, String[] allWords) {
		int count=0;
		String main = allWords[toCheckFrom.wordIndex];
		
		if(toCheckFrom.startIndex!=0&&toCheckFrom.endIndex==allWords[toCheckFrom.wordIndex].length()-1) {
			main = main.substring(toCheckFrom.startIndex);
		} else if(toCheckFrom.startIndex==0&&toCheckFrom.endIndex!=allWords[toCheckFrom.wordIndex].length()-1) {
			main = main.substring(0, toCheckFrom.endIndex+1);
		} else if(toCheckFrom.startIndex!=0&&toCheckFrom.endIndex!=allWords[toCheckFrom.wordIndex].length()-1) {
			main = main.substring(toCheckFrom.startIndex, toCheckFrom.endIndex+1);
		}
		
		for(int i=0; i<main.length();i++) {
			if(main.charAt(i)==secondary.charAt(i)) {
				count++; 
			}
			else {
				break;
			}
		}
		return count;
	}
	
	

	// add items to trie
	private static TrieNode populate(String [] allWords, int ind, int start, int end, String kid, TrieNode pri) {
		//add siblings
		if(pri.firstChild==null) {
			int a=pri.substr.wordIndex;
			short newStart=(short)(end+1);
			
			Indexes root1 = new Indexes(a, (short) start, (short)end);
			Indexes firstSib = new Indexes(a, newStart, (short)(allWords[a].length()-1));
			Indexes secSib = new Indexes(ind, newStart, (short)(allWords[ind].length()-1));
			
			pri.substr = root1;
			TrieNode second= new TrieNode(secSib, null, null);
			pri.firstChild = new TrieNode(firstSib, null, second);
			return pri;
		}
		
		else {
			int a=pri.substr.wordIndex;
			
			Indexes root1 = new Indexes(a,(short)start, (short)end);
			pri.substr = root1;
			kid = kid.substring(end+1);
			
			TrieNode hold = pri.firstChild;
			boolean match = false;
			
			while (hold!=null) {
				String check=allWords[hold.substr.wordIndex];
				match=false;
				if(kid.charAt(0)==check.charAt(hold.substr.startIndex)) {
					match=true;
					break;
				}
				hold=hold.sibling;	
			} 
			if(match == false) {
				Indexes sibl= new Indexes(start, (short)(end+1), (short)(allWords[ind].length()-1));
				TrieNode newSibl=new TrieNode(sibl, null, null);
				hold = newSibl;
				return pri;
				
			}
			else {
				int count= common(hold.substr, kid, allWords);
				int st=end+1;
				int en=end+count;
				hold=populate(allWords, ind, st, en, kid, hold);
				return pri;
			}
		}
	}
	
	
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		ArrayList<TrieNode> complete=new ArrayList<TrieNode>(); // returned list
		ArrayList<TrieNode> storage=new ArrayList<TrieNode>();
		TrieNode prefInd=root;
		
		if (root==null) 	return null;
		
		
		while (prefInd!=null) {
			
			if (prefInd.substr==null) {
				prefInd=prefInd.firstChild;
			}
			int wordInd=prefInd.substr.wordIndex;
			int endInd=prefInd.substr.endIndex+1;
			boolean pre=prefix.startsWith(allWords[wordInd].substring(0, endInd));
			boolean sta=allWords[wordInd].startsWith(prefix);
		
			// no matches, continue on to next
			if (!pre && !sta) {
				
				prefInd=prefInd.sibling; 
			
			}
			// match found and it has children, add match as well as children, recursively call CL
			else if ((pre && prefInd.firstChild!=null) || 
			(sta && prefInd.firstChild!=null)) {
			
				storage.addAll(completionList(prefInd.firstChild,allWords, prefix));
				prefInd=prefInd.sibling;
			}
			// match found, no children, just add match
			else if ((pre && prefInd.firstChild==null) || 
			(sta && prefInd.firstChild==null)) {
			
				storage.add(prefInd);
				prefInd=prefInd.sibling;
			
			}
			// match found, no children, just add match
			else if ((pre && prefInd.firstChild==null) || 
			(sta && prefInd.firstChild==null)) {
				storage.add(prefInd);
				prefInd=prefInd.sibling;
			}
		
		}
		complete=storage;
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return complete;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
// finish