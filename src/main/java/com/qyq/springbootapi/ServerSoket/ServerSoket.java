package com.qyq.springbootapi.ServerSoket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSoket {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);

        while (true){
            Socket accept = serverSocket.accept();
            Thread thread = new Thread(new SocketThread(accept));
            thread.start();


//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        InputStreamReader reader = new InputStreamReader(accept.getInputStream());
//                        BufferedReader bufferedReader = new BufferedReader(reader);
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            System.out.println("服务器端接收：" + line);
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();

        }

    }
}

class SocketThread implements Runnable{

    Socket socket = null;

    public SocketThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        OutputStreamWriter writer = null;
        BufferedWriter bufferedWriter = null;

        try {
            reader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(reader);
            String read;
            while ((read = bufferedReader.readLine())!= null){
                System.out.println("接收到客户端"+socket.getInetAddress().getHostAddress()+"的信息："+read);
            }
            socket.shutdownInput();

            writer = new OutputStreamWriter(socket.getOutputStream());
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("我已收到你发的信息");
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (reader != null){
                    reader.close();
                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
                if(writer != null){
                    writer.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}

