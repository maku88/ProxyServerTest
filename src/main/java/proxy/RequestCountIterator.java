//
//package proxy;
//
//import java.util.Hashtable;
//import java.util.Enumeration;
//import proxy.cache;
//
//
////Accumulates reference counter increments.
////Updates the database from time to time in order not to
////overload the DB and to gain the system speed
//public class RequestCountIterator extends Thread
//{
//    public RequestCountIterator()
//    {
//        increased = new Hashtable();
//    }
//
//
//    public synchronized void incRC (int id, cache cache)
//    {
//        Integer key = new Integer(id);
//        IncrementValue iv;
//        if (increased.containsKey(key)) {
//            iv = (IncrementValue) increased.get(key);
//            iv.incRC();
//        }
//        else {
//            increased.put(key, new IncrementValue(cache));
//        }
//        try {
//            resume();
//        } catch (Exception e) {}
//    }
//
//
//    public void run()
//    {
//        Enumeration e;
//        while (true) {
//            while (!increased.isEmpty())
//                for (e=increased.keys(); e.hasMoreElements(); ) {
//                    updateDB((Integer) e.nextElement());
//
//                }
//            try {
//                suspend();
//            } catch (Exception exc) {}
//        }
//    }
//
//
//    private void updateDB (Integer key)
//    {
//        IncrementValue iv;
//        synchronized (this) {
//            iv = (IncrementValue) increased.get(key);
//            increased.remove(key);
//        }
//        if (iv != null) {
//            iv.getCache().incrementReferences(key.intValue(), iv.getCount());
//        }
//    }
//
//
//
//    private Hashtable increased;
//
//
//
//
//    class IncrementValue
//    {
//        public IncrementValue (cache p_cache)
//        {
//            count = 1;
//            cache = p_cache;
//        }
//
//
//        public void incRC()
//        {
//            ++count;
//        }
//
//
//        public cache getCache()
//        {
//            return cache;
//        }
//
//
//        public int getCount()
//        {
//            return count;
//        }
//
//
//
//        private int count;
//        private cache cache;
//    }
//
//}
