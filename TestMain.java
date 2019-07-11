package pkj;
public class TestMain
{
    public static void main(String[] args)
    {
        TestMain.test();
    }

    private static void test()
    {
        Tree<Integer> test = new RedBlackTree<>();
        // test.addAll(new String[]{"A","D","G","B","F","E","C"});
//         test.addAll(new Integer[]{10,20,30,15});
//        test.addAll(new String[]{"Abonimation","dude","hey","hello","what","is","the","up","down","bottom","right"
//        		,"left","beside","infront","foo","clazz"});
         test.addAll(new Integer[]{});
        System.out.println(test);
        //System.out.println(test.first());
        //System.out.println(test.last());
        //System.out.println(test.listView());
        //System.out.println(test.entryView());
        //System.out.println(test.isEmpty());
//        System.out.println(test.remove("beside") + " this is delete");
        System.out.println(test.size());
        System.out.println(test.getTreeHeight());
//        System.out.println(test.contains(7));
//        System.out.println(test.contains(3));
//        System.out.println(test.contains(5));
//        System.out.println(test.contains(11));
//        System.out.println(test.contains(100));
        // System.out.println(test.headTree(10));

    }
}