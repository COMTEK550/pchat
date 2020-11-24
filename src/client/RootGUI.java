/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class RootGUI extends javax.swing.JFrame implements Frontend{
    private Client client;
    private ConversationListModel convList;
    private javax.swing.JList<String> jUserField;
    private DefaultListModel<String> userList;
    private ConcurrentHashMap<Integer, ArrayList<String>> history;
    private DateFormat dateFormat;

    public RootGUI() throws Exception {
        new SongPlayer("song.wav");

        this.client = Client.getInstance(this);
        this.history = new ConcurrentHashMap<>();
        this.dateFormat = new SimpleDateFormat("MM-dd'T'HH:mm:ss");
        initComponents();
    }
    public void newTxtMsg(String msg, int conv, String user, Date stamp){
        ArrayList<String> messages = this.history.get(conv);
        if(messages == null){
            messages = new ArrayList<>();
        }
        messages.add(String.format("[%s:%s] %s", this.dateFormat.format(stamp), user, msg));
        this.history.put(conv, messages);

        int selIndex = this.jConvField.getSelectedIndex();
        if(conv == this.convList.getSelectedConv(selIndex)){
            printMsgs();
        }
    }

    private void printMsgs(){
        int selIndex = this.jConvField.getSelectedIndex();
        int id = this.convList.getSelectedConv(selIndex);
        this.jChatHistory.setText("");
        if(this.history.get(id) == null){
            return;
        }
        for(String message : this.history.get(id)){
            this.jChatHistory.append(message + "\n");
        }
    }

    public void newConMsg(int id, String[] users){
        String name = String.join(",", users);
        this.convList.addConv(id, name);
    }

    public void newErrMsg(String msg) {
        JOptionPane.showMessageDialog(this, String.format("Server error: %s", msg));
    }

    public void newRegMsg(String name){
        DefaultListModel listmodel = this.userList;
        System.out.printf("New user '%s'%n", name);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                listmodel.addElement(name);
            }
        });
    }

    private void initComponents() {

        userList = new DefaultListModel<>();
        connectButton = new javax.swing.JButton();
        sendButton = new javax.swing.JButton();
        jChatHistoryScroll = new javax.swing.JScrollPane();
        jChatHistory = new javax.swing.JTextArea();
        jConvScroll = new javax.swing.JScrollPane();
        jConvField = new javax.swing.JList<>();
        jUserField = new javax.swing.JList<>(userList);
        ServerTextField = new javax.swing.JTextField();
        newConvButton = new javax.swing.JButton();
        jUserScroll = new javax.swing.JScrollPane();
        jChatField = new javax.swing.JTextArea();
        UsernameTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Picochat");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });


        jChatHistory.setEditable(false);
        jChatHistory.setColumns(20);
        jChatHistory.setRows(5);

        jChatHistoryScroll.setViewportView(jChatHistory);

        jUserScroll.setViewportView(jUserField);

        jUserField.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.convList = new ConversationListModel();
        jConvField.setModel(this.convList);

        jConvField.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jConvField.addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            public void valueChanged(javax.swing.event.ListSelectionEvent evt){
                SelectedConversationListener(evt);
            }
        });
        jConvScroll.setViewportView(jConvField);

        ServerTextField.setEditable(true);
        ServerTextField.setText("localhost");

        newConvButton.setText("New Conversation");
        newConvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewConversationButton(evt);
            }
        });

        jChatField.setColumns(10);
        jChatField.setRows(5);
        jChatField.setEditable(true);

        UsernameTextField.setEditable(true);
        UsernameTextField.setText("Username");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(178, 178, 178))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jChatHistoryScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                            .addComponent(jChatField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(UsernameTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ServerTextField)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jConvScroll)
                    .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newConvButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jUserScroll, 200, 200, Short.MAX_VALUE))
            )
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(connectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jConvScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newConvButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ServerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jChatHistoryScroll)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jChatField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addContainerGap())
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jUserScroll))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ServerTextField, UsernameTextField, connectButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
                /* Create and display the form */
        String name = this.UsernameTextField.getText();
        Destination dest = new Destination(this.ServerTextField.getText());
        SecureRandom rnd = new SecureRandom();
        KeyManager km = new KeyManager();
        km.save_or_load(name, rnd);

        try {

            this.client.connect(dest.host, dest.port, name, km);
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int selIndex = this.jConvField.getSelectedIndex();
        int id = this.convList.getSelectedConv(selIndex);
        if (id < 0) {
            return;
        }
        try {
            this.client.sendTxtMsg(this.jChatField.getText(), id);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void SelectedConversationListener(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_FilenameTextFieldFocusGained
        printMsgs();
    }
    private void NewConversationButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //New ConversationButton
        List<String> usersList = this.jUserField.getSelectedValuesList();
        String[] users = new String[usersList.size()];
        usersList.toArray(users);
        try {
            this.client.newConversation(users);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        new RootGUI().setVisible(true);
    }

    private javax.swing.JTextField ServerTextField;
    private javax.swing.JTextField UsernameTextField;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton newConvButton;
    private javax.swing.JList<String> jConvField;
    private javax.swing.JScrollPane jChatHistoryScroll;
    private javax.swing.JScrollPane jConvScroll;
    private javax.swing.JScrollPane jUserScroll;
    private javax.swing.JTextArea jChatHistory;
    private javax.swing.JTextArea jChatField;
}
class ConversationMini{
    public int id;
    public String name;

    public ConversationMini(int id, String name){
        this.id = id;
        this.name = name;
    }
}

class ConversationListModel extends AbstractListModel<String> {

    private ArrayList<ConversationMini> convs;

    public ConversationListModel(){
        this.convs = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return this.convs.size();
    }

    @Override
    public String getElementAt(int index) {
        return this.convs.get(index).name;
    }

    public void addConv(int id, String name){
        int s = this.convs.size();
        this.convs.add(new ConversationMini(id, name));
        this.fireIntervalAdded(this, s, s);
    }
    public int getSelectedConv(int index){
        if (index < 0 || index >= this.convs.size()) {return -1;}
        return this.convs.get(index).id;
    }

}
