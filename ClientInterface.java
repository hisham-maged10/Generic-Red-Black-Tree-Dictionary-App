package pkj;
import java.util.List;
public interface ClientInterface
{
    public boolean loadDictionary();
    public int getDictionarySize();
    public boolean insertWord(String word);
    public boolean lookUpWord(String word);
    public boolean removeWord(String word);
    public int getDictionaryheight();
    public List<RedBlackTree<String>.Node<String>> entryView(); 
}