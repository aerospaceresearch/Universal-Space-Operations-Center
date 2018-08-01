/*
 * MIT License
 *
 * Copyright (c) 2017 KSat e.V. and AerospaceResearch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.ksatstuttgart.usoc.controller.communication;

import com.ksatstuttgart.usoc.controller.Utility;
import com.ksatstuttgart.usoc.data.DataSource;
import com.ksatstuttgart.usoc.data.MailEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

/**
 * <h1>MailReceiver</h1>
 * This class creates a thread that constantly looks for new messages on a
 * specified mail account.
 * <p>
 * Classes implementing the MailUpdateListener interface can register to listen
 * for updates on the specified mail account.
 *
 * @author Valentin Starlinger
 * @version 1.0
 */
public class MailReceiver {

    private static final String USER_NAME = "mirka2iridium@gmail.com";  // GMail user name
    private static final String PW = "RXLaunchMarch2016"; // GMail password

    public boolean running = true;
    private ArrayList<MailUpdateListener> listeners = new ArrayList<>();
    private Store store;
    private Folder folder;
    private Session session;

    private int numMessages = 0;

    private static MailReceiver instance;

    public static MailReceiver getInstance() {
        if (instance == null) {
            instance = new MailReceiver();
        }

        return instance;
    }

    /**
     * isConnected returns true if the program is currently connected to a mail
     * server
     *
     * @return true if connected to the mail server.
     */
    public boolean isConnected() {
        if (folder == null) {
            return false;
        }
        return folder.isOpen();
    }

    /**
     * This method sets the number of messages that will be read on the next
     * reconnect. Note that this does not skip invalid messages so if the last x
     * messages are all invalid no messages will be shown
     *
     * @param numMessages - int: Number of messages to be read on reconnect
     */
    public void setMessagesOnReconnect(int numMessages) {
        this.numMessages = numMessages;
    }

    /**
     * This method connects to a gmail account using a predefined password and
     * user name.
     */
    public void connect() {
        try {
            String host = "imap.gmail.com";
            final String user = USER_NAME;
            final String password = PW;
            
            Properties props = new Properties();
            props.setProperty("mail.imap.ssl.enable", "true");
            session = Session.getInstance(props);

            store = session.getStore("imap");
            store.connect(host, user, password);
            if (store.isConnected()) {
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                startEmailThread(folder);
            } else {
                error("Error: Couldn't connect to e-mail server.");
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
            error("Error: Couldn't connect to e-mail server.");
        }

    }

    /**
     * This method starts a new thread checking for new e-mails every 500 ms
     *
     * @param folder
     */
    private void startEmailThread(final Folder folder) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    //wait for folder to be opened
                    int counter = 5;
                    while (counter >= 0 && !folder.isOpen()) {
                        Thread.sleep(500);
                        counter--;
                    }
                    if (counter < 0) {
                        error("Error: Couldn't connect to INBOX");
                        return;
                    }

                    //get the last x messages. note that this does not skip 
                    //invalid messages so if the last x messages are all invalid
                    //no messages will be shown
                    int messageCount = folder.getMessageCount();

                    if (messageCount > numMessages) {
                        for (int i = 0; i < numMessages; i++) {
                            getMessage(folder, messageCount - (numMessages - i));
                            //wait for 500ms so messages to not get shown to fast after each other
                            Thread.sleep(500);
                        }
                    }
                    while (running) {
                        int diff = folder.getMessageCount() - messageCount;
                        if (diff < 0) {
                            messageCount = folder.getMessageCount();
                        } else if (diff > 0) {
                            for (int i = 0; i < diff; i++) {
                                getMessage(folder, messageCount + i);
                            }
                            messageCount = folder.getMessageCount();
                        }
                        Thread.sleep(500);
                    }
                    if (folder.isOpen()) {
                        folder.close(true);
                    }
                    if (store.isConnected()) {
                        store.close();
                    }
                } catch (InterruptedException ex) {
                    error("Warning: Message Thread interrupted.");
                } catch (MessagingException ex) {
                    error("Error: Couldn't close folder or store.");
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public void close() {
        running = false;
        try {
            if (folder.isOpen()) {
                folder.close(true);
            }

            if (store.isConnected()) {
                store.close();
            }
        } catch (MessagingException ex) {
            error("Error: Couldn't close folder or store.");
        }
    }

    public void reconnect() {
        if (isConnected()) {
            close();
        }
        connect();
    }

    public void getMessage(Folder folder, int i) {

        try {
            Message[] message = folder.getMessages();
            if (message[i].getSubject().toLowerCase().contains("message")) {
                if (message[i].getContent() instanceof Multipart) {
                    Multipart multipart = (Multipart) message[i].getContent();
                    for (int j = 0; j < multipart.getCount(); j++) {

                        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            //System.out.println("reading attachment!");
                            InputStream stream = part.getInputStream();
                            int b;
                            String text = "";
                            while ((b = stream.read()) != -1) {
                                String t = Utility.intToBits(b);
                                text += t;
                            }
                            text = text.trim();
                            update(new MailEvent(message[i].getFrom(), message[i].getSubject(),
                                    part.getFileName(), text, System.currentTimeMillis(),
                                    message[i].getReceivedDate().getTime(), DataSource.MAIL));

                        }
                    }
                } else if (message[i].getContent() instanceof String) {
                    //System.out.println("message as string: " + message[i].getContent());
                }
            } else {
                error("message not valid: " + message[i].getSubject());
            }
        } catch (MessagingException | IOException ex) {
            error("Error: Error while parsing message.");
        }
    }

    public void addMailUpdateListener(MailUpdateListener mul) {
        listeners.add(mul);
    }

    public void removeMailUpdateListener(MailUpdateListener mul) {
        listeners.remove(mul);
    }

    private void update(MailEvent e) {
        for (MailUpdateListener listener : listeners) {
            listener.mailUpdated(e);
        }
    }

    private void error(String msg) {
        for (MailUpdateListener listener : listeners) {
            listener.error(msg);
        }
    }

}
