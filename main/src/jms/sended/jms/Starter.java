package jms.sended.jms;

import jms.sended.JmsSender;

import java.util.HashMap;
import java.util.Map;

public class Starter {

    public static void main(String[] args) {

        JmsSender sender = new JmsSender();

        Map<String, String> headers = new HashMap<>();
        headers.put("sender", "VSRZ_EAIST");
        headers.put("receiver", "OOC");

        sender.setHeaders(headers);


        sender.sendMessage();
    }
}
