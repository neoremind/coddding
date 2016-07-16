package net.neoremind.mycode.apache.commons.lang3;

import org.apache.commons.collections.list.TreeList;
import org.junit.Test;

/**
 * 内部用AVL树做的一个List。常用操作的时间复杂度均降低到了O(log N)。
 * <p>
 * A <code>List</code> implementation that is optimised for fast insertions and
 * removals at any index in the list.
 * <p>
 * This list implementation utilises a tree structure internally to ensure that
 * all insertions and removals are O(log n). This provides much faster performance
 * than both an <code>ArrayList</code> and a <code>LinkedList</code> where elements
 * are inserted and removed repeatedly from anywhere in the list.
 * <p>
 * The following relative performance statistics are indicative of this class:
 * <pre>
 *              get  add  insert  iterate  remove
 * TreeList       3    5       1       2       1
 * ArrayList      1    1      40       1      40
 * LinkedList  5800    1     350       2     325
 * </pre>
 * <code>ArrayList</code> is a good general purpose list implementation.
 * It is faster than <code>TreeList</code> for most operations except inserting
 * and removing in the middle of the list. <code>ArrayList</code> also uses less
 * memory as <code>TreeList</code> uses one object per entry.
 * <p>
 * <code>LinkedList</code> is rarely a good choice of implementation.
 * <code>TreeList</code> is almost always a good replacement for it, although it
 * does use sligtly more memory.
 *
 * @author zhangxu
 */
public class TreeListTest {

    @Test
    public void testTreeList() {
        TreeList treeList = new TreeList();
        treeList.add("a");
        treeList.add("A");
        treeList.add("a");
        treeList.add("b");
        treeList.add("c");
        treeList.add("c");
        System.out.println(treeList);
    }

}
