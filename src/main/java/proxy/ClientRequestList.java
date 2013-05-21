//package proxy;
//
//import java.util.*;
//import proxy.Proxy;
//import proxy.DocumentInfo;
//
//
////List of client requests which are currently being executed
////For a sake of reduction of internet traffic in case when
////2 or more clients requested almost simultaneously the same document
//public class ClientRequestList
//{
//    public ClientRequestList() {
//        requests = new Hashtable();
//    }
//
//
//    public synchronized boolean isCurrentlyServed (String demanded,
//                                                   Proxy thr)
//    {
//        boolean result;
//
//        result = requests.containsKey(demanded);
//        if (result) {
//            Vector waiting = (Vector) requests.get(demanded);
//            waiting.addElement(thr);
//            try {
//                thr.wait();     //Wait until the queue is mine
//            } catch (java.lang.InterruptedException err) {}
//            waiting.removeElementAt(0);
//        }
//        else {
//            requests.put(demanded, new Vector());
//        }
//        return result;
//    }
//
//
//    public synchronized void unregister (String served,
//                                         DocumentInfo documentInfo)
//    {
//        Vector waiting = (Vector)requests.get(served);
//        if (waiting.isEmpty())
//            requests.remove(served);
//        else {
//            Proxy notified = (Proxy) waiting.firstElement();
//            notified.setDocumentInfo(documentInfo);
//            notified.notify();
//        }
//    }
//
//
//    private Hashtable requests;
//}