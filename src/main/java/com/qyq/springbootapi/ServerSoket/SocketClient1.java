package com.qyq.springbootapi.ServerSoket;

import java.io.*;
import java.net.Socket;

public class SocketClient1 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",9999);

        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(reader);
        while (true){
            bufferedWriter.write(bufferedReader.readLine());
            bufferedWriter.write("\n");
            bufferedWriter.flush();
        }

    }
}
