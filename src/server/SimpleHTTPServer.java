package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SimpleHTTPServer extends Thread implements ServerConstants {
    private ServerWorkPanel workPanel;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String clientName;
    private boolean loggingDebug = true;

    public SimpleHTTPServer(Socket socket, ServerWorkPanel workPanel) throws IOException{
        this.workPanel = workPanel;
        clientName = socket.getInetAddress().getCanonicalHostName();
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        outputStream.flush();
        start();
    }

    public void run() {
        try {
            String request, response;
            while (true){
                request = readRequest();
                if (request != null && request.length() > 0){
                    response = getResponseByURL(getURLFromRequest(request));
                    outputStream.write(response.getBytes());
                    if (isLoggingDebug()){
                        workPanel.getTextArea().append("Request from " + clientName + " :\n");
                        workPanel.getTextArea().append(request + "\n");
                        workPanel.getTextArea().append("Response to " + clientName + " : \n");
                        workPanel.getTextArea().append(response);
                        workPanel.getTextArea().append("\n\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getResponseByURL(String url) {
        String response = "";
        File f = new File(START_DIRECTORY + url);
        try {
            Scanner in = new Scanner(f);
            while (in.hasNext()){
                response = response + in.nextLine();
            }
            response = getResponse(response);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response = getResponse(ERROR_MESSAGE);
        }
        return response;
    }

    private String getResponse(String consist){
        String response  = "HTTP/1.1 200 OK\r\n" +
                "Server: Test server\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + consist.length() + "\r\n" +
                "Connection: close\r\n\r\n" + consist;
        return response;
    }

    private String readRequest() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String request = "", line;

        while(true) {
            line = bufferedReader.readLine();
            if(line == null || line.trim().length() == 0) {
                break;
            }
            request = request + line + "\n";
        }

        return request;
    }

    private String getURLFromRequest(String request){
        String url = "error.html";
        if (isGETRequest(request) || isPOSTRequest(request) || isHEADRequest(request)){
            int l = request.indexOf(32) + 1;
            int r = l;

            while (request.charAt(r) != 32){
                r++;
            }
            url = request.substring(l, r);
            if (url.equals("/")){
                url = HOME_PAGE;
            }
            crutch(url);
        }
        return url;
    }

    private boolean isGETRequest(String request) {
        return request.substring(0, 3).equals(GET)?true:false;
    }

    private boolean isPOSTRequest(String request) {
        return request.substring(0, 4).equals(POST)?true:false;
    }

    private boolean isHEADRequest(String request) {
        return request.substring(0, 4).equals(HEAD)?true:false;
    }

    public boolean isLoggingDebug() {
        return loggingDebug;
    }

    public void setLoggingDebug(boolean loggingDebug) {
        this.loggingDebug = loggingDebug;
    }

    private void crutch(String url){
        if (url.equals("/favicon.ico")){
            setLoggingDebug(false);
        } else {
            setLoggingDebug(true);
        }
    }
}
