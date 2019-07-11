package pkj;
import java.util.Comparator;
import java.util.List;

public final class Client implements ClientInterface {
    private static Client instance = null;

    private Handler handler;
    private Tree<String> dictionary;

    {
        this.handler = new FileHandler();
        this.dictionary = new RedBlackTree<>(new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
    }

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null)
            return instance = new Client();
        else
            return instance;
    }

    @Override
    public boolean loadDictionary() {
        return this.handler.load(this.dictionary, handler.browseFile("Text Files", "TXT", "txt", "Txt"));
    }

    @Override
    public int getDictionarySize() {
        return this.dictionary.size();
    }

    @Override
    public boolean insertWord(String word) {
        return this.dictionary.add(word);
    }

    @Override
    public boolean lookUpWord(String word) {
        return this.dictionary.contains(word);
    }

    @Override
    public boolean removeWord(String word) {
        return this.dictionary.remove(word);
    }

    @Override
    public List<RedBlackTree<String>.Node<String>> entryView() {
        return this.dictionary.entryView();
    }
    @Override
    public int getDictionaryheight() {
    	return this.dictionary.getTreeHeight();
    }
}