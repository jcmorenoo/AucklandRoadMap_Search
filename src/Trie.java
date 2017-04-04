import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trie {

	private Map<String, Trie> children;
	private String value;
	private boolean isMarked = false;
	
	public Trie(){
		this.children = new HashMap<String, Trie>();
	}
	
	public Map<String, Trie> getChildren(){
		return this.children;
	}
	
	public void addChild(String s, Trie t){
		this.children.put(s,t);
	}
	
	public Trie getChild(String key){
		return this.children.get(key);
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void mark(){
		this.isMarked = true;
	}
	
	public void unMark(){
		this.isMarked = false;
	}
	
	public boolean isMarked(){
		return this.isMarked;
	}
	
	public void addWord(String word){
		Trie current = this;
		for(String letter : word.split("")){
			if(current.getChild(letter) == null){
				current.addChild(letter, new Trie());
				
			}
			
			current = current.getChild(letter);
			
		}
		
		current.mark();
	}
	
	public boolean contains(String word){
		Trie current = this;
		for(String letter : word.split("")){
			if(current.getChild(letter) == null){
				return false;
			}
			current = current.getChild(letter);
			
		}
		return current.isMarked();
	}
	
	public Set<String> getAll(String prefix){
		Trie current = this;
		Set<String> values = new HashSet<String>();
		
		for(String letter : prefix.split("")){
			if(current.getChild(letter) == null){
				return null;
			}
			else{
			current = current.getChild(letter);
			}
		}
		
		getAllFromNode(current, values, prefix);
		return values;
	}
	
	public void getAllFromNode(Trie t, Set<String> values, String prefix){
			StringBuilder word = new StringBuilder(prefix);
			Map<String, Trie> children = t.getChildren();
			
//			for(Map.Entry<String, Trie> entry : children.entrySet()){
//				
//				String k = entry.getKey();
//				if(entry.getValue().isMarked()){
//					word.append(k);
//					values.add(word.toString());
//				}
//				else{
//					word.append(k);
//				}
//				getAllFromNode(entry.getValue().getChild(k), values, word.toString());
//				
//			}
		
		
		
		for(String letter : children.keySet()){
			StringBuilder newWord = new StringBuilder(word.toString());
			newWord.append(letter);
			if(children.get(letter).isMarked()){
				
				values.add(newWord.toString());
				
			}
			
			getAllFromNode(children.get(letter), values, newWord.toString());
			
		}
	}
	
	
}
