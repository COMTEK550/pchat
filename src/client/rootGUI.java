/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


public class rootGUI extends javax.swing.JFrame implements Frontend{
    private Client client;
    private ConversationListModel convList;
    private javax.swing.JList<String> jUserField;
    private DefaultListModel<String> userList;
    /**
     * Creates new form rootGUI
     */
    public rootGUI() throws Exception {
        this.client = new Client(this);
        initComponents();
    }
    public void newTxtMsg(String msg, int conv){
        System.out.println(msg + "Fra ROOTGUI");
    }

    public void newConMsg(int id, String[] users){
        String name = String.join(",", users);
        this.convList.addConv(id, name);
    }

    public void newErrMsg(String msg) {
        JOptionPane.showMessageDialog(this, String.format("Server error: %s", msg));
    }

    public void newRegMsg(String name){
        userList.addElement(name);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jOptionPane1 = new javax.swing.JOptionPane();
        connectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jChatHistory = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jConvField = new javax.swing.JList<>();
        jUserField = new javax.swing.JList<>();
        FilenameTextField = new javax.swing.JTextField();
        newConvButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jChatField = new javax.swing.JTextArea();
        UsernameTextField = new javax.swing.JTextField();

        jLabel1.setText("jLabel1");

        jScrollPane3.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Friland Chat");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        sendButton.setText("Send");

        jChatHistory.setEditable(false);
        jChatHistory.setColumns(20);
        jChatHistory.setRows(5);

        jScrollPane2.setViewportView(jChatHistory);

        this.userList = new DefaultListModel<>();
        jUserField.setModel(this.userList);
        jScrollPane5.setViewportView(jUserField);

        jUserField.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);



        this.convList = new ConversationListModel();
        jConvField.setModel(this.convList);

        jConvField.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jConvField.addListSelectionListener(new javax.swing.event.ListSelectionListener(){
            public void valueChanged(javax.swing.event.ListSelectionEvent evt){
                SelectedConversationListener(evt);
            }
        });
        jScrollPane4.setViewportView(jConvField);

        FilenameTextField.setEditable(false);
        FilenameTextField.setText("Server IP");
        FilenameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                FilenameTextFieldFocusGained(evt);
            }
        });
        FilenameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilenameTextFieldActionPerformed(evt);
            }
        });

        newConvButton.setText("New Conversation");
        newConvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jChatField.setColumns(20);
        jChatField.setRows(5);
        jChatField.setText("Hej julian");
        jChatField.setEditable(true);

        UsernameTextField.setEditable(true);
        UsernameTextField.setText("Username");
        UsernameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(178, 178, 178))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                            .addComponent(jChatField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(UsernameTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FilenameTextField)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newConvButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane5))
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
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newConvButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FilenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jChatField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addContainerGap())
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {FilenameTextField, UsernameTextField, connectButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
                /* Create and display the form */
        String name = this.UsernameTextField.getText();

        SecureRandom rnd = new SecureRandom();
        KeyManager km = new KeyManager();
        km.save_or_load(name, rnd);

        this.client.connect("localhost", 6969, name, km);
    }//GEN-LAST:event_connectButtonActionPerformed

    private void UsernameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameTextFieldActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_UsernameTextFieldActionPerformed

    private void FilenameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilenameTextFieldActionPerformed


    }//GEN-LAST:event_FilenameTextFieldActionPerformed

    private void FilenameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_FilenameTextFieldFocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_FilenameTextFieldFocusGained

    private void SelectedConversationListener(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_FilenameTextFieldFocusGained
        int selIndex = this.jConvField.getSelectedIndex();
        this.jChatHistory.setText(this.convList.getTxtForConv(selIndex));
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //New ConversationButton
        List<String> usersList = this.jUserField.getSelectedValuesList();
        String[] users = new String[usersList.size()];
        usersList.toArray(users);
        try {
            this.client.newConversation(users);
        } catch (Exception e) {
            e.printStackTrace();
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
            java.util.logging.Logger.getLogger(rootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(rootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(rootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(rootGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new rootGUI().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FilenameTextField;
    private javax.swing.JTextField UsernameTextField;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton sendButton;
    private javax.swing.JButton newConvButton;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jConvField;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jChatHistory;
    private javax.swing.JTextArea jChatField;
    // End of variables declaration//GEN-END:variables
}

class ConversationListModel extends AbstractListModel{

    private ArrayList<String> convs;

    public ConversationListModel(){
        this.convs = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return this.convs.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.convs.get(index);
    }

    public void addConv(int id, String name){
        int s = this.convs.size();
        this.convs.add(name);
        this.fireIntervalAdded(this, s, s);
    }
    public String getTxtForConv(int index){
        return "Hej med dig" + this.convs.get(index);
    }

}