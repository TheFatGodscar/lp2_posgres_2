

package formularios;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import prg.conecbd;

public class acceso extends javax.swing.JFrame {
    int contador = 0;
    conecbd con;
    ResultSet rs, rs1, rs2, rs3;
    public static String codigo;
    public static String usu_nombre = "";
    
    public acceso() {
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
    }
    
    public void buscar_datos(){
        try {
            String estado = "";
            String sql = "select * from usuarios where usu_nick ='" +txt_usu.getText() + "' and usu_pass = md5 ( '"+ txt_pas.getText() +  "')";
            rs = con.listar_datos(sql);
            System.out.println(txt_usu.getText());
                System.out.println(sql);
            if (rs.next()){
                codigo = rs.getString("usu_cod");
                usu_nombre = rs.getString("usu_nick");
                estado = rs.getString("usu_estado");
                
            }
            //verifica que el usuario que trata de loguearse se encuentre activo
            if(usu_nombre.equals(txt_usu.getText()) || usu_nombre.equals("admin")){
                if (estado.equals("inactivo")){
                    JOptionPane.showMessageDialog(null, "Usuario bloquedo, consulte con el administrador", "Atención", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }else{
                    //si se encuentra activo llama al menú
                    if(estado.equals("activo")){
                        this.consultar();
                        new menu2().setVisible(true);
                        this.setVisible(false);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, " USUARIO INCORRECTO O CLAVE INCORRECTA");
                contador=contador+1;
                if (contador == 3){
                    System.exit(0);
                }
                txt_usu.setEnabled(true);
                txt_usu.requestFocus();
                txt_usu.setText("");
                txt_pas.setText("");
            }
        } catch (SQLException ex) {            
            Logger.getLogger(acceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void consultar(){
        try {
            rs3 = con.listar_datos(" select * from usuarios where usu_cod = " + acceso.codigo + "and cambio_clave=0 ");
            if(rs3.next()){
                int mensaje = JOptionPane.showConfirmDialog(null, "Desea actualizar su contraseña asiganada por el sistema?",
                        "Atención", JOptionPane.YES_NO_OPTION);
                if (mensaje == JOptionPane.YES_OPTION){
                   new actualizar_contrasena(null, true).setVisible(true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(acceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_usu = new javax.swing.JTextField();
        btn_acceder = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        txt_pas = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 36)); // NOI18N
        jLabel1.setText("ACCESO");

        jLabel2.setText("Usuario");

        jLabel3.setText("Contraseña");

        txt_usu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_usuKeyPressed(evt);
            }
        });

        btn_acceder.setText("Acceder");
        btn_acceder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_accederMouseClicked(evt);
            }
        });
        btn_acceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_accederActionPerformed(evt);
            }
        });
        btn_acceder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_accederKeyPressed(evt);
            }
        });

        btn_salir.setText("Salir");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        txt_pas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_pasKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_acceder)
                                .addGap(18, 18, 18)
                                .addComponent(btn_salir, javax.swing.GroupLayout.PREFERRED_SIZE, 70, Short.MAX_VALUE))
                            .addComponent(txt_usu)
                            .addComponent(txt_pas))))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_pas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_acceder)
                    .addComponent(btn_salir))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        int mensaje = JOptionPane.showConfirmDialog(rootPane, " ¿Deseas asalir del sistema? " , " Confirmar" , JOptionPane.YES_NO_OPTION);
        if (mensaje == JOptionPane.YES_OPTION){
        dispose();    
        }
        
    }//GEN-LAST:event_btn_salirActionPerformed

    private void txt_usuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_usuKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER){
            if(txt_usu.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "NO DEBE DEJAR EL CAMPO VACIO");
                txt_usu.requestFocus();
            } else {
                txt_pas.requestFocus();
            }
        }
    }//GEN-LAST:event_txt_usuKeyPressed

    private void txt_pasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pasKeyPressed
        if (evt.getKeyChar()==KeyEvent.VK_ENTER){
            if(txt_pas.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "NO DEBE DEJAR EL CAMPO VACIO");
                txt_pas.requestFocus();
            } else {
                this.buscar_datos();
            }
        }
    }//GEN-LAST:event_txt_pasKeyPressed

    private void btn_accederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_accederActionPerformed
        this.buscar_datos();
    }//GEN-LAST:event_btn_accederActionPerformed

    private void btn_accederKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_accederKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(txt_pas.getText().trim().equals("")){
                JOptionPane.showMessageDialog(rootPane, "El campo no puede estar vacío");
                txt_pas.requestFocus();
            } else {
                this.buscar_datos();
            }
        }
    }//GEN-LAST:event_btn_accederKeyPressed

    private void btn_accederMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_accederMouseClicked
        if(txt_usu.getText().trim().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Ingrese el nombre del usuario");
            txt_usu.requestFocus();
        }
    }//GEN-LAST:event_btn_accederMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new acceso().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_acceder;
    private javax.swing.JButton btn_salir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField txt_pas;
    private javax.swing.JTextField txt_usu;
    // End of variables declaration//GEN-END:variables
}
