package com.company;



import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.net.Socket;

public class Main {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 4004);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                class ReadMsg extends Thread {
                    @Override
                    public void run() {
                        //System.out.println("Read");
                        String str;
                        try {
                            while (true) {
                                str = in.readLine();
                                System.out.println(str);
                                if (str.equals("stop")) {

                                    break;
                                }
                            }
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    }
                }
                class WriteMsg extends Thread {

                    @Override
                    public void run() {
                        while (true) {
                            String userWord;
                            try {
                                userWord =  reader.readLine();
                                if (userWord.equals("stop")) {
                                    out.write("stop" + "\n");
                                    break;
                                } else {
                                    out.write(userWord + "\n");
                                }
                                out.flush(); // чистим
                            } catch (IOException e) {
                                System.err.println(e);
                            }

                        }
                    }
                }
                WriteMsg  w = new WriteMsg();
                w.start();
                ReadMsg r=new ReadMsg();
                r.run();
            } finally {
                System.out.println("You`ve been closed...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}