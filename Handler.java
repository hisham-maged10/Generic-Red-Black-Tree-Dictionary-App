package pkj;
import java.io.File;
public interface Handler
{
    public boolean load(Tree<String> rbTree,File file);
    public File browseFile(String extensionHeader, String... extensionFilter);
}