//Clase de Conexion y metedos ABM y Consultas
package prg;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;



public class conecbd {

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    String serv = "localhost";
    String db = "lp2";
    String user = "postgres";
    String clave = "123";
    String local;
    String puerto;

    public Connection conectar() {
        try {

//            Class.forName("com.mysql.jdbc.Driver"); //para mysql
//            con = DriverManager.getConnection("jdbc:mysql://" + serv + "/" + db, user, clave);//para mysql
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://" + serv + "/" + db, user, clave);

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return con;
    }
    public String verificarDuplicado(String tabla, String columna, String valor) {
    try {
        rs = listar_datos("SELECT " + columna + " FROM " + tabla + " WHERE " + columna + " = '" + valor + "'");
        if (rs.next()) {
            return rs.getString(columna);
        }
    } catch (SQLException ex) {
        Logger.getLogger(conecbd.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

    public boolean existeRegistro(String tabla, String condicion) {
    String consulta = "SELECT COUNT(*) FROM " + tabla + " WHERE " + condicion;
    
    try {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(consulta);
        rs.next();
        int count = rs.getInt(1);
        return count > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public ResultSet listar_datos(String consulta) {
        try {
            stmt = con.createStatement(ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);
            rs = stmt.executeQuery(consulta);
        } catch (Exception ex) {
            System.out.println(consulta);
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return rs;
    }

    public void insertar_datos(String tabla, String campos, String valores, int mensaje) {
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("insert into " + tabla + " (" + campos + ") values(" + valores + ")");

            switch (mensaje) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Se ha grabado exitosamente", "ATENCION", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1:
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Se ha grabado exitosamente", "ATENCION", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }

        } catch (Exception ex) {
            System.out.println("insert into " + tabla + " (" + campos + ") values(" + valores + ")");
            JOptionPane.showMessageDialog(null, "Error en la operación", "ATENCION", JOptionPane.WARNING_MESSAGE);
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void actualizar_datos(String tabla, String camposAct, String campoIdentificador, String valorIdentificador) {
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE " + tabla + " SET " + camposAct + " WHERE " + campoIdentificador + "=" + valorIdentificador);
            JOptionPane.showMessageDialog(null, "Se ha actualizado", "ATENCION", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            System.out.println("update " + tabla + " set " + camposAct + " where "+ campoIdentificador + "="  +valorIdentificador);
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error en la operación", "ATENCION", JOptionPane.WARNING_MESSAGE);
        }
    }
    public boolean actualizar_datos2(String tabla, String setValues, String whereCondition) {
    try {
        String sql = "UPDATE " + tabla + " SET " + setValues + " WHERE " + whereCondition;
        PreparedStatement stmt = con.prepareStatement(sql);
        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        
        return rowsAffected > 0; // Devuelve true si se actualizó al menos una fila
    } catch (SQLException e) {
        e.printStackTrace(); // Imprime los detalles del error en la consola
        JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
    public void actualizar_datos3(String tabla, String camposAct, String codigo) {
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("update " + tabla + " set " + camposAct + " where " + codigo);
            JOptionPane.showMessageDialog(null, "Se ha actualizado", "ATENCION", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            System.out.println("update " + tabla + " set " + camposAct + " where " + codigo);
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error en la operación", "ATENCION", JOptionPane.WARNING_MESSAGE);
        }
    }


    public void BorrarDatos(String tabla, String campoCodigo, String codigo) {
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("delete from " + tabla + " where " + campoCodigo + "=" + codigo);
            JOptionPane.showMessageDialog(null, "Se ha borrado exitosamente", "ATENCION", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("delete from " + tabla + " where " + campoCodigo + "=" + codigo);
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en la operación", "ATENCION", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public boolean busqueda(String query) {
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }
    public void cargarCombo(String sql, JComboBox combo) {
        ResultSet rs = null;
        int contar = 0;
        try {
            Statement s = con.createStatement();
            rs = s.executeQuery(sql);
            while (rs.next()) {
                String columnas[] = new String[2];
                columnas[0] = rs.getString(1);
                columnas[1] = rs.getString(2);
                combo.addItem(columnas);
                System.out.println(rs.getString(1) + "-" + rs.getString(2));
                contar++;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ocurrio Un error: " + e.toString(), "Atencion",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (contar > 0) {
            combo.setRenderer(new DefaultListCellRenderer() {

                public java.awt.Component getListCellRendererComponent(
                        JList l, Object o, int i, boolean s, boolean f) {
                    try {
                        return new JLabel(((String[]) o)[1]);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ocurrio Un error", "Atencion",
                                JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                }
            });
        }
        return;
    }
//public void generarreporte(String reporte) {
//
//        HashMap parameters = new HashMap();
//        try{
//            URL urlMaestro = getClass().getClassLoader().getResource("reportes/"+reporte+".jasper");
//            // Cargamos el reporte
//            JasperReport masterReport = null;
//            masterReport = (JasperReport) JRLoader.loadObject(urlMaestro);
//            JasperPrint masterPrint = null;
//            masterPrint = JasperFillManager.fillReport(masterReport, parameters,con);
//
//            JasperViewer ventana = new JasperViewer(masterPrint,false);
//            ventana.setTitle("Vista Previa");
//            ventana.setVisible(true);
//
//        }catch(JRException e){
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Ocurrio un error "+e.toString(),"ATENCION "
//                    , JOptionPane.INFORMATION_MESSAGE);
//        }
//    }    
 /** Cierra la conexion con la base de datos */
    public void cierraConexion()
    {
        try
        {
            con.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void actualizar_datos(String usuario, String string, String string0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void insertarDatos(String clientes, String cod_clie_cli_nombre_cli_apellido_cli_ci_c, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public PreparedStatement prepareStatement(conecbd con) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public PreparedStatement prepareStatement(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void actualizar_datos2(String proveedor, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
