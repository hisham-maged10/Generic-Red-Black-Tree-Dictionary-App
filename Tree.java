package pkj;
import java.util.Comparator;
import java.util.List;
public interface Tree<E extends Comparable<? super E>> extends Iterable<E>
{
    public boolean contains(E element); // done
    public boolean containsAll(Tree<E> tree); // done
    public boolean addAll(Tree<E> tree); // done
    public boolean addAll(E[] arr); // done
    public boolean add(E element); // done
    public boolean remove(E element);
    public boolean removeAll(Tree<E> tree); // done
    public boolean retainAll(Tree<E> tree); //done
    public int getTreeHeight(); // done
    public Comparator<? super E> comparator(); //done
    public E first(); //done
    public E last(); //done
    public boolean isEmpty(); //done
    public int size(); // done
    public List<RedBlackTree<E>.Node<E>> entryView(); // done
    public List<E> listView(); // done


}