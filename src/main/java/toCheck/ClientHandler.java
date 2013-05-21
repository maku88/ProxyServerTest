//package ProxyServer;
//
//import java.net.Socket;
//
///**
// * Created with IntelliJ IDEA.
// * User: mno
// * Date: 17.05.13
// * Time: 15:42
// * To change this template use File | Settings | File Templates.
// */
//class ClientHandler extends Thread
//{
//    private Socket socket = null;
//    private Socket remoteSocket = null;
//    private HTTPReqHeader request = null;
//    ClientHandler(Socket socket)
//    {
//        this.socket = socket;
//        request = new HTTPReqHeader();
//        request.parse(socket); // I read and parse the HTTP request here
//    }
//
//    public void run()
//    {
//        if(!request.isSecure() )
//        {
//            remoteSocket = new Socket(request.url,request.port);
//        }
//        else
//        {
//            // now what should I do to established a secured socket?
//        }
//
//        // start connecting remoteSocket and clientSocket
//        ...........
//    }
//}
