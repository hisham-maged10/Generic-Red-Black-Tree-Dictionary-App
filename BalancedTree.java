package pkj;

public interface BalancedTree<E extends Comparable<? super E>> extends Tree<E>
{
    public void applyBalanceRules(RedBlackTree<E>.Node<E> root); // done 
}