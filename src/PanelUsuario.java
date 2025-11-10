
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.mysql.cj.jdbc.CallableStatement;

public class PanelUsuario extends JPanel{
    //Objetos de conexion SQL
    CallableStatement stmt = null;
    Connection con = null;
    

    public PanelUsuario(MantenimientoUsuario controlOriginal, int funcion) throws ClassNotFoundException, SQLException {
        MantenimientoUsuario control = controlOriginal;
        //Estableciendo conexion a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "erpalacios");
        stmt = (CallableStatement) con.prepareCall("{CALL insertarDatosEstudiantes(?, ?, ?, ?, ?, ?, ?)}");
        
        if (funcion==0) {
           JLabel lblTitulo = crearEtiqueta("Datos de nuevo registro", 250, 20, 300, 30);
this.add(lblTitulo); 
        }else{
             JLabel lblTitulo = crearEtiqueta("Actualizar registro", 250, 20, 300, 30);
this.add(lblTitulo);
        }



JLabel lblID = crearEtiqueta("ID:", 150, 70, 140, 30);
JTextField txtID = crearCampoTexto(300, 70, 200, 30, "Ingrese ID del usuario");
this.add(lblID);
this.add(txtID);

JLabel lblNombre1 = crearEtiqueta("Primer Nombre:", 150, 110, 140, 30);
JTextField txtNombre1 = crearCampoTexto(300, 110, 200, 30, "Ingrese primer nombre");
this.add(lblNombre1);
this.add(txtNombre1);

JLabel lblNombre2 = crearEtiqueta("Segundo Nombre:", 150, 150, 140, 30);
JTextField txtNombre2 = crearCampoTexto(300, 150, 200, 30, "Ingrese segundo nombre");
this.add(lblNombre2);
this.add(txtNombre2);

JLabel lblApellido1 = crearEtiqueta("Primer Apellido:", 150, 190, 140, 30);
JTextField txtApellido1 = crearCampoTexto(300, 190, 200, 30, "Ingrese primer apellido");
this.add(lblApellido1);
this.add(txtApellido1);

JLabel lblApellido2 = crearEtiqueta("Segundo Apellido:", 150, 230, 140, 30);
JTextField txtApellido2 = crearCampoTexto(300, 230, 200, 30, "Ingrese segundo apellido");
this.add(lblApellido2);
this.add(txtApellido2);

JLabel lblUsuario = crearEtiqueta("Usuario:", 150, 270, 140, 30);
JTextField txtUsuario = crearCampoTexto(300, 270, 200, 30, "Ingrese nombre de usuario");
this.add(lblUsuario);
this.add(txtUsuario);

JLabel lblClave = crearEtiqueta("Contraseña:", 150, 310, 140, 30);
JTextField txtClave = crearCampoTexto(300, 310, 200, 30, "Ingrese contraseña");
this.add(lblClave);
this.add(txtClave);


JButton botonGuardar = crearBoton("Guardar", 270, 360, 100, 40, "Guardar nuevo usuario", "Iconos/guardar-el-archivo.png");
botonGuardar.setBackground(new Color(46, 204, 113));
botonGuardar.setForeground(Color.WHITE);
this.add(botonGuardar);

JButton botonCancelar = crearBoton("Cancelar", 400, 360, 100, 40, "Regresar atras", "Iconos/cancelar.png");
botonCancelar.setBackground(new Color(240, 128, 128));
botonCancelar.setForeground(Color.WHITE);
this.add(botonCancelar);
botonCancelar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e){
 txtID.setText("");
     txtNombre1.setText("");
     txtNombre2.setText("");
     txtApellido1.setText("");
     txtApellido2.setText("");
     txtUsuario.setText("");
     txtClave.setText("");
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
        int eleccion = JOptionPane.showConfirmDialog(null,"¿Desea confirmar nuevo registro?","Confirmar acción",JOptionPane.YES_NO_OPTION,    JOptionPane.QUESTION_MESSAGE);
        if (eleccion==0) {
           stmt.executeUpdate("INSERT INTO usuario (cedula, nombre1, nombre2, apellido1, apellido2, login, clave) VALUES ('"+txtID.getText()+"', '"+txtNombre1.getText()+"', '"+txtNombre2.getText()+"', '"+txtApellido1.getText()+"', '"+txtApellido1.getText()+"', '"+txtUsuario.getText()+"', '"+txtClave.getText()+"');"); 
        }
        
     } catch (SQLException e1) {
        JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
     }
     txtID.setText("");
     txtNombre1.setText("");
     txtNombre2.setText("");
     txtApellido1.setText("");
     txtApellido2.setText("");
     txtUsuario.setText("");
     txtClave.setText("");
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

      //Metodo declarado para crear campos de texto
    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(x, y, ancho, alto);
        campoTexto.setToolTipText(toolTip);
        return campoTexto;
    }
    //Metodo declarado para crear etiquetas
    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }

}
