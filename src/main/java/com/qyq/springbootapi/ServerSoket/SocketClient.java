package com.qyq.springbootapi.ServerSoket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",9999);

        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        writer.write(scanner.nextLine());
        writer.flush();
        socket.shutdownOutput();
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine())!= null){
            System.out.println("服务器端返回："+line);
        }
        bufferedReader.close();
        reader.close();
        writer.close();

    }
}
