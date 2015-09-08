package hr.fer.zemris.java.tecaj.hw4.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author ilijan
 */
public class SimpleHashtableTest {

    @Test
    public void testPuttingData() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(5));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put(null, Integer.valueOf(2));
    }

    @Test
    public void testChangeValue() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(5));
        examMarks.put("Ivana", Integer.valueOf(1));
    }


    @Test
    public void testSize() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Assert.assertEquals(3, examMarks.size());
    }

    @Test
    public void testGet() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Assert.assertEquals(3, examMarks.get("Jasna"));
    }

    @Test
    public void testGetNull() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        Assert.assertTrue(examMarks.get(null) == null);
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Assert.assertTrue(examMarks.containsKey("Ante"));
    }

    @Test
    public void testContainsNull() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        Assert.assertFalse(examMarks.containsKey(null));
    }

    @Test
    public void testNotContainsKey() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Assert.assertFalse(examMarks.containsKey("Josko"));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(6));
        Assert.assertTrue(examMarks.containsValue(Integer.valueOf(1)));
    }

    @Test
    public void testNotContainsValue() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(4));
        examMarks.put("Jasna", Integer.valueOf(7));
        Assert.assertFalse(examMarks.containsValue(Integer.valueOf(1)));
    }

    @Test
    public void testRemove() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(4));
        examMarks.put("Jasna", Integer.valueOf(7));
        examMarks.remove("Ivana");
        Assert.assertFalse(examMarks.containsKey("Ivana"));
    }

    @Test
    public void testRemoveFromEmptySlot() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(7));
        examMarks.remove("Ante");
    }

    @Test
    public void testRemoveNull() {
        SimpleHashtable examMarks = new SimpleHashtable();
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.remove(null);
    }

    @Test
    public void testRemoveFromEndOfList() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(2));
        examMarks.put("Ante", Integer.valueOf(4));
        examMarks.put("Jasna", Integer.valueOf(7));
        examMarks.remove("Jasna");
        Assert.assertFalse(examMarks.containsKey("Jasna"));
    }

    @Test
    public void testToString() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Assert.assertTrue(examMarks.toString().equals("[Ante=2, Ivana=1, Jasna=3]"));
    }

    @Test
    public void testEmptyTableToString() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        Assert.assertTrue(examMarks.toString().equals("[]"));
    }

    @Test
    public void testIsEmpty() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        Assert.assertTrue(examMarks.isEmpty());
    }

    @Test
    public void testIsNotEmpty() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ante", Integer.valueOf(2));
        Assert.assertFalse(examMarks.isEmpty());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInitialCapacityNonPositive() {
        SimpleHashtable examMarks = new SimpleHashtable(-5);
    }

    @Test
    public void testIteration() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Iterator<SimpleHashtable.TableEntry> iterator = examMarks.iterator();
        while(iterator.hasNext()) {
            SimpleHashtable.TableEntry pair = iterator.next();
            Assert.assertTrue(examMarks.containsKey(pair.getKey()));
        }
    }


    @Test
    public void testIterationRemove() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Iterator<SimpleHashtable.TableEntry> iterator = examMarks.iterator();
        while(iterator.hasNext()) {
            SimpleHashtable.TableEntry pair = iterator.next();
            System.out.println(pair.toString());
            if(pair.getKey().equals("Ivana")) {
                iterator.remove();
            }
        }
        Assert.assertFalse(examMarks.containsKey("Ivana"));
    }

    @Test (expected = IllegalStateException.class)
    public void testIllegalStateException() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));

        Iterator<SimpleHashtable.TableEntry> iterator = examMarks.iterator();
        while(iterator.hasNext()) {
            SimpleHashtable.TableEntry pair = iterator.next();
            if(pair.getKey().equals("Jasna")) {
                iterator.remove();
                iterator.remove();
            }
        }
    }

    @Test (expected = ConcurrentModificationException.class)
    public void testConcurrentModificationException() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));

        Iterator<SimpleHashtable.TableEntry> iterator = examMarks.iterator();
        while(iterator.hasNext()) {
            SimpleHashtable.TableEntry pair = iterator.next();
            if(pair.getKey().equals("Jasna")) {
                examMarks.remove("Jasna");
            }
        }
    }

    @Test (expected = NoSuchElementException.class)
    public void testNoSuchElementException() {
        SimpleHashtable examMarks = new SimpleHashtable(2);
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));

        Iterator<SimpleHashtable.TableEntry> iterator = examMarks.iterator();
        while(iterator.hasNext()) {
            iterator.next();
        }
        iterator.next();
    }

    @Test
    public void testIterationWithEmptySlots() {
        SimpleHashtable examMarks = new SimpleHashtable();
        examMarks.put("Ivana", Integer.valueOf(1));
        examMarks.put("Ante", Integer.valueOf(2));
        examMarks.put("Jasna", Integer.valueOf(3));
        Iterator<SimpleHashtable.TableEntry> iterator = examMarks.iterator();
        while(iterator.hasNext()) {
            SimpleHashtable.TableEntry pair = iterator.next();
            Assert.assertTrue(examMarks.containsKey(pair.getKey()));
        }
    }

    @Test
    public void testEmptyIterator() {
        SimpleHashtable examMarks = new SimpleHashtable();
        for(SimpleHashtable.TableEntry node : examMarks) {
            Assert.assertTrue(false);
        }
    }
}
