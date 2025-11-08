

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelTelefonoEmpleado extends JPanel {
    // Objetos de conexión SQL
    Statement stmt = null;
    Connection con = null;

    public PanelTelefonoEmpleado(MantenimientoTelefonoEmpleado controlOriginal, int funcion) throws ClassNotFoundException, SQLException {
        MantenimientoTelefonoEmpleado control = controlOriginal;

        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
                "root", "cRojas34");
        stmt = con.createStatement();
         if(funcion==0){
        JLabel lblTitulo = crearEtiqueta("Datos de nuevo teléfono", 200, 20, 300, 30);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo);
         }else{
            JLabel lblTitulo = crearEtiqueta("Actualizar Registro", 200, 20, 300, 30);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo);
         }
        

       
        JLabel lblID = crearEtiqueta("ID:", 150, 70, 140, 30);
        JTextField txtID = crearCampoTexto(300, 70, 200, 30, "Ingrese ID del registro");
        this.add(lblID);
        this.add(txtID);

      
        JLabel lblTelefono = crearEtiqueta("Teléfono:", 150, 120, 140, 30);
        JTextField txtTelefono = crearCampoTexto(300, 120, 200, 30, "Ingrese número de teléfono");
        this.add(lblTelefono);
        this.add(txtTelefono);

        JLabel lblDescripcion = crearEtiqueta("Descripción:", 150, 170, 140, 30);
        JTextField txtDescripcion = crearCampoTexto(300, 170, 200, 30, "Ingrese descripción");
        this.add(lblDescripcion);
        this.add(txtDescripcion);

       
        JButton botonGuardar = crearBoton("Guardar", 270, 220, 100, 40, "Guardar nuevo teléfono", "Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        JButton botonCancelar = crearBoton("Cancelar", 400, 220, 100, 40, "Regresar atras", "Iconos/cancelar.png");
botonCancelar.setBackground(new Color(240, 128, 128));
botonCancelar.setForeground(Color.WHITE);
this.add(botonCancelar);
botonCancelar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e){
 txtID.setText("");
                txtTelefono.setText("");
                txtDescripcion.setText("");
              
                  
                control.add(control.botonActualizar);
                control.add(control.botonInsertar);
                control.add(control.botonEliminar);
                control.add(control.botonConsultar);
                control.add(control.scroll);
                control.remove(control.panel);

                
                control.recargarTabla();
                control.revalidate();
                control.repaint();
    }
});

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                     
                        stmt.executeUpdate(
                                "INSERT INTO telefono_empleado (id, telefono, descripcion) VALUES ('"
                                        + txtID.getText() + "', '" + txtTelefono.getText() + "', '"
                                        + txtDescripcion.getText() + "');");
                    }
                } catch (SQLException e1) {
                   JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

              
                txtID.setText("");
                txtTelefono.setText("");
                txtDescripcion.setText("");
              
                  
                control.add(control.botonActualizar);
                control.add(control.botonInsertar);
                control.add(control.botonEliminar);
                control.add(control.botonConsultar);
                control.add(control.scroll);
                control.remove(control.panel);

                
                control.recargarTabla();
                control.revalidate();
                control.repaint();
            }
        });
    }

   
    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto, String toolTip, String ruta) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setToolTipText(toolTip);
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        boton.setIcon(iconoOriginal);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        if (texto.equals("")) {
            boton.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return boton;
    }

    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(x, y, ancho, alto);
        campoTexto.setToolTipText(toolTip);
        return campoTexto;
    }

    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }
}
