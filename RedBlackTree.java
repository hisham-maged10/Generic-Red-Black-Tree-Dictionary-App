package pkj;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


import java.util.ArrayList;
import java.util.Collections;
public class RedBlackTree<E extends Comparable<? super E>> implements BalancedTree<E> {

    private Node<E> root;
    private Node<E> nodePtr;
    private Node<E> tail;
    private Node<E> first;
    private Comparator<? super E> comparator;
    private int size;
    private List<Node<E>> entryView;
    private List<E> listView;
    private boolean changed;
    private boolean changedList;
    private boolean changedFirst;
    private Node<E> nil;

    {
        this.root = new Node<E>(false);
        this.nodePtr = this.root;
        this.tail = this.nodePtr;
        this.first = this.root;
        this.size = 0;
        this.entryView = new ArrayList<>();
        this.listView = new ArrayList<>();
        this.changed=false;
        this.changedList=false;
        this.changedFirst=false;
        this.nil = new Node<E>(true);
    }

    public RedBlackTree()
    {
        this(null,null);
    }

    public RedBlackTree(Tree<E> t)
    {
        this(t,null);
    }

    public RedBlackTree(Comparator<? super E> comparator)
    {
        this(null,comparator);
    }

    private RedBlackTree(Tree<E> t,Comparator<? super E> comparator)
    {
        if(t != null)
            for(E e : t)
            {
                this.add(e);
                ++size;
            }
        this.comparator = comparator;

    }
    @Override
    public Iterator<E> iterator() {
        return this.listView().iterator();
    }

    @Override
    public boolean contains(E element) {
        return containsHelper(this.root, element);
    }
    private boolean containsHelper(Node<E> root, E element)
    {
        if(root == null || root.data == null)
            return false;
        if(this.comparator == null ? root.data.compareTo(element) == 0 : this.comparator.compare(element,root.data) == 0 )
            return true;
        if(this.comparator == null ? element.compareTo(root.data) < 0 : this.comparator.compare(element,root.data) < 0 )
            return containsHelper(root.leftNode,element);
        if(this.comparator == null ? element.compareTo(root.data) > 0 : this.comparator.compare(element,root.data) > 0 )
            return containsHelper(root.rightNode, element);
        else
            return false;
    }
    private Node<E> search(Node<E> root, E element)
    {
        if(root == null || root.data == null)
            return null;
        if(this.comparator == null ? root.data.compareTo(element) == 0 : this.comparator.compare(element,root.data) == 0 )
            return root;
        if(this.comparator == null ? element.compareTo(root.data) < 0 : this.comparator.compare(element,root.data) < 0 )
            return search(root.leftNode,element);
        if(this.comparator == null ? element.compareTo(root.data) > 0 : this.comparator.compare(element,root.data) > 0 )
            return search(root.rightNode, element);
        else
            return null;
    }

    @Override
    public boolean containsAll(Tree<E> tree) {
        for(E e : tree)
        {
            if(!this.contains(e))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Tree<E> tree) {
        int oldSize = this.size;
        for(E e : tree)
            this.add(e);
        return oldSize != this.size;
    }

    @Override
    public boolean addAll(E[] arr) {
        int oldSize = this.size;
        for(E e : arr)
            this.add(e);
        return oldSize != this.size;
    }

    @Override
    public boolean add(E element) {
        
        System.out.println("adding element: "+element);

        if(element == null)
            throw new NullPointerException("Tree doesn't accept null values");

        if(this.contains(element))
            return false;

        this.addHelper(this.root,element);
        System.out.println("Now pointing to node having value : "+this.nodePtr.data);
        this.tail = this.nodePtr;
        applyBalanceRules(this.nodePtr);
        System.out.println("Size of Tree after insertion is : "+this.size());
        System.out.println("Height of Tree after insertion is : "+this.getTreeHeight());
        System.out.println(this.entryView());
        return true;
    }

    private void addHelper(Node<E> root, E element)
    {
        if(root.data == null)
        {
            System.out.println("Now adding Element "+element+" in add helper");
            ++this.size;
            this.changed = true;
            this.changedList = true;
            this.changedFirst = true;
            root.setNodeProperties(new Node<>(element,root.parentNode));
            root.leftNode.parentNode = root;
            root.rightNode.parentNode = root;
            this.nodePtr = root;
            return;
        }

        if(this.comparator == null ? element.compareTo(root.data) < 0 :comparator.compare(element,root.data) < 0 )
            addHelper(root.leftNode, element);

        else if(this.comparator == null ? element.compareTo(root.data) > 0 :comparator.compare(element,root.data) > 0)
             addHelper(root.rightNode, element);

        return;
    }
    /*
        for the tree to remain balanced with a height of logn
        1) root must be black
        2) no two consecutive red nodes occur >> red node can't have red child or red parent
        two cases
        1)nodePtr is root >> recolor to red >> done
        2)nodePtr is not root
            two cases
            1) uncle is red
                - change color of parent and uncle to black
                - change color of grandparent to red and re-invoke method on it
            2)uncle is black
                four cases
                1)left left case >> left child of left parent
                    - right rotate grandparent
                    - swap colors parent and grandparent
                2)right right case >> right child of right parent
                    - left rotate grandparent
                    - swap colors of parent and grandparent
                3)left Right case >> right child of left parent
                    - left rotate parent
                    - left left case
                4)right left case >> left child of right parent
                    - right rotate parent
                    - right right case
    */
    @Override
    public void applyBalanceRules(Node<E> nodePtr) {
        if(nodePtr == root)
        {
            System.out.println("root case, converting root to black color, root: "+nodePtr.data);
            nodePtr.nodeColor = Color.BLACK;
            return;
        }
        if(nodePtr.parentNode.nodeColor == Color.RED)
        {
            Node<E> uncle = this.getUncle(nodePtr);
           
            // case 1 >> uncle is red 
            /*
             * change color of parent and uncle to black
             * change color of grand parent to red and re-invoke method on it
             */
            if(uncle.nodeColor == Color.RED)
            {
                System.out.println("red uncle case with element: "+nodePtr.data);
                uncle.nodeColor = Color.BLACK;
                nodePtr.parentNode.nodeColor = Color.BLACK;
                nodePtr.parentNode.parentNode.nodeColor = Color.RED;
                applyBalanceRules(nodePtr.parentNode.parentNode);
                return;
            }

            //case 2 uncle is black
            else
            {
                // left parent left child
                //left left case >> right rotate grand parent and swap colors of parent and grandparent
                if(nodePtr == nodePtr.parentNode.leftNode && nodePtr.parentNode == nodePtr.parentNode.parentNode.leftNode)
                {
                    System.out.println("left left case with element: "+nodePtr.data);
                    leftLeftCase(nodePtr);
                    return;
                }
                // left parent right child
                // left right case >> left rotate parent and do left left case
                else if(nodePtr == nodePtr.parentNode.rightNode && nodePtr.parentNode == nodePtr.parentNode.parentNode.leftNode)
                {
                    System.out.println("left right case with element : "+nodePtr.data);
                    leftRightCase(nodePtr);
                    return;
                }
                // right parent right child
                // right right case >> left rotate grandparent and swap parent and grandparent color
                else if(nodePtr == nodePtr.parentNode.rightNode && nodePtr.parentNode == nodePtr.parentNode.parentNode.rightNode)
                {
                    System.out.println("right right case with element : "+nodePtr.data);
                    rightRightCase(nodePtr);
                    return;
                }
                // right parent left child
                // right left case >> right rotate parent and do right right case
                else if(nodePtr == nodePtr.parentNode.leftNode && nodePtr.parentNode == nodePtr.parentNode.parentNode.rightNode)
                {
                    System.out.println("right left case with element : "+nodePtr.data);
                    rightLeftCase(nodePtr);
                    return;
                }
                else
                {
                    System.out.println("no problem");
                }
            }
       
        }


    }
    // to know which child is the nodePtr and get the appropiate uncle
    private Node<E> getUncle(Node<E> nodePtr)
    {
        Node<E> uncle = null;
        // to get right uncle
        if(nodePtr.parentNode == nodePtr.parentNode.parentNode.leftNode)
            uncle = nodePtr.parentNode.parentNode.rightNode;
       
            // to get left uncle
        else if (nodePtr.parentNode == nodePtr.parentNode.parentNode.rightNode)
            uncle = nodePtr.parentNode.parentNode.leftNode;        
     
        System.out.println("Getting uncle : "+uncle.data);
        return uncle;

    }
    /*
        left left case: left parent and left child of parent
        1)right rotate grandparent
        2)swap colors of g and p
    */
    private void leftLeftCase(Node<E> nodePtr)
    {
        rotateRight(nodePtr.parentNode.parentNode);
        swapColors(nodePtr.parentNode,nodePtr.parentNode.rightNode);
    }
    /*
        Left Right case: left parent right child
        1)left rotate p
        2)apply left left case on g which is p after left rotate
    */
    private void leftRightCase(Node<E> nodePtr)
    {
        rotateLeft(nodePtr.parentNode);
        // System.out.println("In left Right Case\nThe parent: "+nodePtr.parentNode+" the node: "+nodePtr+" the GrandParent: "+nodePtr.parentNode.parentNode);
        rotateRight(nodePtr.parentNode);
        swapColors(nodePtr,nodePtr.rightNode);
    }

    /*
        right right case : right parent and right child
        1) left rotate g
        2) swap colors of g and p
    */
    private void rightRightCase(Node<E> nodePtr)
    {
        // System.out.println("root was: "+this.root);
        rotateLeft(nodePtr.parentNode.parentNode);
        // swapColors(nodePtr.parentNode,nodePtr.parentNode.parentNode);
        swapColors(nodePtr.parentNode,nodePtr.parentNode.leftNode);
        // System.out.println("root now: "+this.root);
    }

    /*
        right left case : right parent left child
        1)right rotate P
        2) right right case on g which is parent of x after right rotate
    */
    private void rightLeftCase(Node<E> nodePtr)
    {
        rotateRight(nodePtr.parentNode);
        // rightRightCase(nodePtr.parentNode);
        rotateLeft(nodePtr.parentNode);
        swapColors(nodePtr,nodePtr.leftNode);
    }

    private void swapColors(Node<E> parent, Node<E> grandParent)
    {
        Color tempColor = parent.nodeColor;
        parent.nodeColor = grandParent.nodeColor;
        grandParent.nodeColor = tempColor;
    }
    private void rotateRight(Node<E> nodePtr)
    {
        // if not root
        if(nodePtr.parentNode != null)
        {
            boolean leftPos = false;
            if(nodePtr == nodePtr.parentNode.leftNode)
            {
                leftPos = true;
            }else
            {
                leftPos = false;
            }
            Node<E> parent = nodePtr.parentNode; // parent of nodePtr
            Node<E> nodeLeft = nodePtr.leftNode; // left node of nodeptr
            Node<E> nodeLeftRight = nodeLeft.rightNode; // right child of left node of nodeptr
            nodeLeft.rightNode = nodePtr;
            nodePtr.parentNode = nodeLeft;
            nodePtr.leftNode = nodeLeftRight;
            if(nodeLeftRight != null) //if right child of left child isn't null then point it's parent to nodePtr
                nodeLeftRight.parentNode = nodePtr;
            nodeLeft.parentNode = parent;
            if(leftPos) 
                parent.leftNode = nodeLeft;    
            else
                parent.rightNode = nodeLeft;
        }else // if root is needed to be rotated
        {
            Node<E> nodeLeft = this.root.leftNode; // left node of nodeptr
            Node<E> nodeLeftRight = nodeLeft.rightNode; // right child of left node of nodePtr
            nodeLeft.rightNode = this.root;
            this.root.parentNode = nodeLeft;
            if(nodeLeftRight != null)
                nodeLeftRight.parentNode = this.root;
            this.root.leftNode = nodeLeftRight;
            this.root = nodeLeft; // placing the new root
            this.root.parentNode = null; // reseting parent of root to be null
        }
    }

    private void rotateLeft(Node<E> nodePtr)
    {
        if(nodePtr.parentNode != null) // not root
        {
            boolean leftPos = false;
            if(nodePtr == nodePtr.parentNode.leftNode)
            {
                leftPos = true;
            }else
            {
                leftPos = false;
            }
            Node<E> parent = nodePtr.parentNode; // parent node of nodePtr
            Node<E> nodeRight = nodePtr.rightNode; // right node of nodeptr
            Node<E> nodeRightLeft = nodeRight.leftNode; // left childe of right node of nodePtr
            nodeRight.leftNode = nodePtr;
            nodePtr.parentNode = nodeRight;
            if(nodeRightLeft != null)
                nodeRightLeft.parentNode = nodePtr;
            nodePtr.rightNode = nodeRightLeft;
            nodeRight.parentNode = parent;
            if(leftPos)
                parent.leftNode = nodeRight;
            else
                parent.rightNode = nodeRight;
        }else // root rotation
        {
            Node<E> nodeRight = this.root.rightNode; // right node of nodeptr
            Node<E> nodeRightLeft = nodeRight.leftNode; // left childe of right node of nodePtr
            nodeRight.leftNode = this.root;
            this.root.parentNode = nodeRight;
            this.root.rightNode = nodeRightLeft;
            if(nodeRightLeft != null)
                nodeRightLeft.parentNode = this.root;
            this.root = nodeRight;
            this.root.parentNode = null;
        }
        
    }

    @Override
    public String toString()
    {
        
        List<Node<E>> localListView = this.entryView();

        StringBuilder sb = new StringBuilder("+-----------------------------------------------------+\n");
        sb.append(String.format("| %-15s | %-15s | %-15s |%n","Index","Value","Color"));
        sb.append("+-----------------------------------------------------+\n");
        for(int i = 0 ; i<this.size ; ++i)
            sb.append(String.format("| %-15d |",i) + String.format(" %-15s |",localListView.get(i).data.toString()) + String.format(" %-15s |%n",localListView.get(i).nodeColor.toString()));
        sb.append("+-----------------------------------------------------+");
        return sb.toString();
    }

    @Override
    public boolean removeAll(Tree<E> tree) {
        int oldSize = this.size;
        for(E e : tree)
            this.remove(e);
        return this.size != oldSize;
    }

    @Override
    public boolean retainAll(Tree<E> tree) {
        int oldSize = this.size;
        for(E e : tree)
        {
            if(!this.contains(e))
                this.remove(e);
        }
        return oldSize != this.size;
    }

    @Override
    public int getTreeHeight() {
        return this.getTreeHeightHelper(this.root);
    }
    private int getTreeHeightHelper(Node<E> root)
    {
    	 if (root.data == null) {
    	        return -1;
    	    }
    	 	System.out.println(root.data);
    	    int leftH = getTreeHeightHelper(root.leftNode);
    	    System.out.println(leftH);
    	    int rightH = getTreeHeightHelper(root.rightNode);
    	    System.out.println(rightH);
    	    if (leftH > rightH) {
    	        return leftH + 1;
    	    } else {
    	        return rightH + 1;
    	    }
    }

    @Override
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override
    public E first() {

        if(changedFirst)
        {
            changedFirst = false;
            this.first = firstHelper(this.root);
            return this.first.data;
        }
        return this.first.data;
    }
    private Node<E> firstHelper(Node<E> root)
    {
        if(root.data == null)
            return root.parentNode;
        return firstHelper(root.leftNode);
    }

    @Override
    public E last() {
        return this.tail.data;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public List<Node<E>> entryView() {
        
        if(this.entryView.isEmpty() || this.changed)
        {
            this.entryView.clear();
            this.changed=false;
            entryViewHelper(this.root,this.entryView);
        }
        return Collections.unmodifiableList(this.entryView);
    }
    private void entryViewHelper(Node<E> root,List<Node<E>> list)
    {
        if(root.data == null)
            return;
        entryViewHelper(root.leftNode,list);
        list.add(root);
        entryViewHelper(root.rightNode,list);   
        return;
    }

     @Override
    public List<E> listView() {
        if(this.listView.isEmpty() || this.changedList)
        {
            this.listView.clear();
            this.changedList=false;
            listViewHelper(this.root,this.listView);
        }
        return Collections.unmodifiableList(this.listView);
    }

    private void listViewHelper(Node<E> root,List<E> list)
    {
        if(root.data == null)
            return;
        listViewHelper(root.leftNode,list);
        list.add(root.data);
        listViewHelper(root.rightNode,list);   
        return;
    }


    private static enum Color {
        RED, BLACK;
    }

    public final class Node<T extends Comparable<? super T>> {
        private T data;
        private Node<T> leftNode;
        private Node<T> rightNode;
        private Node<T> parentNode;
        private Color nodeColor;

        {
            this.nodeColor = Color.RED;
        }

        public Node(boolean leaf) {
            this.data = null;
            this.leftNode = null;
            this.rightNode = null;
            this.parentNode = null;
            this.nodeColor = leaf ? Color.BLACK : Color.RED;
        }
        public Node(Node<T> nil) {
            this.data = nil.data;
            this.leftNode = nil;
            this.rightNode = nil;
            this.parentNode = nil;
            this.nodeColor = Color.BLACK;
        }

        public Node(T data,Node<T> parentNode) {
            this(data, parentNode, null, null);
        }

        public void setNodeProperties(Node<T> node)
        {
            this.data = node.data;
            this.leftNode = node.leftNode;
            this.rightNode = node.rightNode;
            this.parentNode = node.parentNode;
            this.nodeColor = node.nodeColor;
        }

        public Node(T data, Node<T> parentNode, Node<T> leftNode, Node<T> rightNode) {
            this.data = data;
            this.leftNode = leftNode == null ? new Node<T>(true) : leftNode;
            this.rightNode = rightNode == null ? new Node<T>(true) : rightNode;
            this.parentNode = parentNode;
        }



        @Override
        public String toString()
        {
            // return this.data + "\t\t" + this.nodeColor.toString();
            return String.format("%-15s  %-15s %n",this.data,this.nodeColor.toString());
        }
    }
    void transplant(Node<E> target, Node<E> with){ //this method connect the child of the node to be deleted
        if(target.parentNode == null){             //with the parent of the node to be deleted and delete the node
            root = with;
        }else if(target == target.parentNode.leftNode){
        	target.parentNode.leftNode = with;
        }else
        	target.parentNode.rightNode = with;
        with.parentNode = target.parentNode;
  }
   public boolean remove(E z){
	   Node<E> x;
	if((x = search(root, z)) == null) //search of the node if found it will be deleted
		return false;
		this.changed = true; //those to update the list in the GUI
		this.changedList = true;
		this.changedFirst = true;
		this.size--;
	   return removeHelper(x,z);
    }
   public boolean removeHelper(Node<E> z,E element)
   {
	   Node<E> x = new Node<E>(nil); //this for the child of the node to be deleted
       Node<E> y = z; // temporary reference y
       Color y_original_color = y.nodeColor; //to check for the original color of the node to be deleted
       
       if(z.leftNode.data == nil.data){ //if it have a left child null we will connect it to the parent of the node and delete it
           x = z.rightNode;				//then fix it up
           transplant(z, z.rightNode);  
       }else if(z.rightNode.data == nil.data){//if it have a right child null we will connect it to the parent of the node 
           x = z.leftNode;					  //and delete it then fix it up
           transplant(z, z.leftNode); 
       }else{							//if it have a two value child
           y = treeMinimum(z.rightNode);//we get the successor
           y_original_color = y.nodeColor;//and then change the original color as the successor is now the matters because
           x = y.rightNode;               //the fix operations will work on it  
           if(y.parentNode == z)		  //if the successor is the child of z then connect the child of x to y its parent
               x.parentNode = y;
           else{//to connect if its have a child
               transplant(y, y.rightNode);//then we delete the successor and put it in the place of node
               y.rightNode = z.rightNode;
               y.rightNode.parentNode = y;
           }
           transplant(z, y);//then we delete the node and make the successor or the child in the place of the node to be deleted
           y.leftNode = z.leftNode;//and take its color
           y.leftNode.parentNode = y;
           y.nodeColor = z.nodeColor; 
       }
       if(y_original_color == Color.BLACK)
           deleteFixup(x);  
       return true;
   }
   void deleteFixup(Node<E> x){
       while(x != root && x.nodeColor == Color.BLACK){ //if its case(1) and case (3-1) it won't enter the loop 
    	   											   //and change the color of the child direct to black
           if(x == x.parentNode.leftNode){ //if the child is the left child
        	   Node<E> sibling = x.parentNode.rightNode; //then we take it sibling is the right child
               if(sibling.nodeColor == Color.RED){ //case(3-2) the node to be deleted is black and has black parent and red sibling
                   sibling.nodeColor = Color.BLACK;//we will make a rotate then recolor the sibling and the parent
                   x.parentNode.nodeColor = Color.RED;
                   rotateLeft(x.parentNode);
                   sibling = x.parentNode;
               }
               if(sibling.leftNode.nodeColor == Color.BLACK && sibling.rightNode.nodeColor == Color.BLACK){
                   sibling.nodeColor = Color.RED; //case(3-3) the node to be deleted is black has a black parent
                   x = x.parentNode;			//and a black sibling with 2 black children you so push the double black node up
                   continue;					//to the parent and recolor the sibling 
               }
               else if(sibling.rightNode.nodeColor == Color.BLACK){//case(3-5) the node to be deleted is black has a black parent
                   sibling.leftNode.nodeColor = Color.BLACK;//and a black sibling with 1 inner Red child,you make the inner red 
                   sibling.nodeColor = Color.RED;//child to be outer,so we make rotate and re color the red child and the parent 
                   rotateRight(sibling);
                   sibling = x.parentNode.rightNode;
               }
               if(sibling.rightNode.nodeColor == Color.RED){//case(3-6)terminal
                   sibling.nodeColor = x.parentNode.nodeColor;//i don't care about the color of the parent so we make a rotate to
                   x.parentNode.nodeColor = Color.BLACK;//the deleted node and recolor the parent and the outer red into a black
                   sibling.rightNode.nodeColor = Color.BLACK;
                   rotateLeft(x.parentNode);
                   x = root;//to get out because its a terminal
               }
           }else{
               Node<E> sibling = x.parentNode.leftNode;
               if(sibling.nodeColor == Color.RED){
                   sibling.nodeColor = Color.BLACK;
                   x.parentNode.nodeColor = Color.RED;
                   rotateRight(x.parentNode);
                   sibling = x.parentNode.leftNode;
               }
               if(sibling.rightNode.nodeColor == Color.BLACK && sibling.leftNode.nodeColor == Color.BLACK){
                   sibling.nodeColor = Color.RED;
                   x = x.parentNode;
                   continue;
               }
               else if(sibling.leftNode.nodeColor == Color.BLACK){
                   sibling.rightNode.nodeColor = Color.BLACK;
                   sibling.nodeColor = Color.RED;
                   rotateLeft(sibling);
                   sibling = x.parentNode.leftNode;
               }
               if(sibling.leftNode.nodeColor == Color.RED){
                   sibling.nodeColor = x.parentNode.nodeColor;
                   x.parentNode.nodeColor = Color.BLACK;
                   sibling.leftNode.nodeColor = Color.BLACK;
                   rotateRight(x.parentNode);
                   x = root;
               }
           }
       }
       x.nodeColor = Color.BLACK; 
   }
   
   Node<E> treeMinimum(Node<E> subTreeRoot){
       while(subTreeRoot.leftNode.data  != nil.data){
         subTreeRoot = subTreeRoot.leftNode;
       }
       return subTreeRoot;
   }
   void clear(){
       root.setNodeProperties(nil);
   }
}