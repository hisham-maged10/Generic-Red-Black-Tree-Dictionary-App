package pkj;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
public final class FileHandler implements Handler {

    @Override
    public boolean load(Tree<String> rbTree,File file) {
        if(rbTree == null || !file.exists())
            return false;
        try(BufferedReader input = new BufferedReader(new FileReader(file));)
        {
            String line = null;
            while((line = input.readLine()) != null)
            {
                rbTree.add(line.trim().replaceAll("\\s+$",""));
            }
            return true;
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }catch(Error er)
        {
            er.printStackTrace();
        }catch(Throwable th)
        {
            th.printStackTrace();
        }
        return false;
    }

    @Override
    public File browseFile(String extensionHeader, String... extensionFilter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionHeader,extensionFilter);
        chooser.setFileFilter(filter);
        File file = null;
        try{
                System.out.println("Please Choose the Dictionary txt File");
                chooser.showOpenDialog(null);
                file = chooser.getSelectedFile();
                if(file.exists())
                    return file;
        }catch(NullPointerException ex)
        {
            System.err.println("You can't access Dictionary without loading the file, Terminating!");
            System.exit(1);
        }catch(Exception ex)
        {
            System.err.println("You can't access Dictionary without loading the file, Terminating!");
            System.exit(1);
        }
        return file;
    }

}