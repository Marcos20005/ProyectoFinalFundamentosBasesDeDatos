
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class VistaRegistro extends JFrame {

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    public VistaRegistro() throws UnsupportedLookAndFeelException, ClassNotFoundException, SQLException {
        //Ajustes de FlatLaf
        UIManager.setLookAndFeel(new FlatMacLightLaf());
        UIManager.put("Button.arc", 80);
        UIManager.put("Component.arc", 20);
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(160, 160, 160));
        UIManager.put("Component.focusColor", new Color(160, 160, 160));
        UIManager.put("Component.selectionBackground", new Color(160, 160, 160));
        this.setTitle("Registro de nueva cuenta");
        Container conte = new Container();
        this.add(conte);

        //Ajustes de conexion con base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "erpalacios");

        //Declaracion de ajustes para la vista de la ventana
        JLabel eti1 = crearEtiqueta("Informacion de nueva cuenta", 100, 30, 200, 30);
        conte.add(eti1);
        JLabel eti2 = crearEtiqueta("Ingrese su Cedula", 40, 80, 200, 30);
        conte.add(eti2);
        JTextField campoCedula = crearCampoTexto("", 50, 110, 150, 30, "Campo para Cedula");
        conte.add(campoCedula);
        JLabel eti3 = crearEtiqueta("Ingrese su Primer Nombre", 220, 80, 200, 30);
        conte.add(eti3);
        JTextField campoPrimerNombre = crearCampoTexto("", 230, 110, 150, 30, "Campo para Primer Nombre");
        conte.add(campoPrimerNombre);
        JLabel eti4 = crearEtiqueta("Ingrese su Segundo Nombre", 40, 150, 200, 30);
        conte.add(eti4);
        JTextField campoSegundoNombre = crearCampoTexto("", 50, 180, 150, 30, "Campo para Segundo Nombre");
        conte.add(campoSegundoNombre);
        JLabel eti5 = crearEtiqueta("Ingrese su Primer Apellido", 220, 150, 200, 30);
        conte.add(eti5);
        JTextField CampoPrimerApellido = crearCampoTexto("", 230, 180, 150, 30, "Campo para Primer Apellido");
        conte.add(CampoPrimerApellido);
        JLabel eti6 = crearEtiqueta("Ingrese su Segundo Apellido", 40, 220, 200, 30);
        conte.add(eti6);
        JTextField campoSegundoApellido = crearCampoTexto("", 50, 250, 150, 30, "Campo para Segundo Apellido");
        conte.add(campoSegundoApellido);
        JLabel eti7 = crearEtiqueta("Ingrese su Nombre de Usuario", 220, 220, 200, 30);
        conte.add(eti7);
        JTextField campoNombreUsuario = crearCampoTexto("", 230, 250, 150, 30, "Campo para Nombre de Usuario");
        conte.add(campoNombreUsuario);
        JLabel eti8 = crearEtiqueta("Ingrese su Contraseña", 40, 290, 200, 30);
        conte.add(eti8);
        JTextField campoContrasena = crearCampoTexto("", 50, 320, 150, 30, "Campo para Contraseña");
        conte.add(campoContrasena);
        JButton botonCrearCuenta = crearBoton("<html>Crear<br>Cuenta</html>", 140, 370, 150, 50, "Confirmar creacion de cuenta", "Iconos/agregar-usuario.png");
        conte.add(botonCrearCuenta);
        botonCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    stmt = con.createStatement();
                    stmt.executeUpdate("INSERT INTO usuario (cedula,nombre1,nombre2,apellido1,apellido2,login,clave) VALUES ('" + campoCedula.getText() + "','" + campoPrimerNombre.getText() + "','" + campoSegundoNombre.getText() + "','" + CampoPrimerApellido.getText() + "','" + campoSegundoApellido.getText() + "','" + campoNombreUsuario.getText() + "','" + campoContrasena.getText() + "');");
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                campoCedula.setText("");
                campoPrimerNombre.setText("");
                campoSegundoNombre.setText("");
                CampoPrimerApellido.setText("");
                campoSegundoApellido.setText("");
                campoNombreUsuario.setText("");
                campoContrasena.setText("");
                VistaRegistro.this.dispose();

            }
        });

        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    //Metodo declarado para crear botones
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
        boton.setIconTextGap(30);
        return boton;
    }

    //Metodo declarado para crear etiquetas
    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }

    //Metodo declarado para crear campos de texto
    static public JTextField crearCampoTexto(String texto, int x, int y, int ancho, int alto, String toolTip) {
        JTextField campoTexto = new JTextField(texto);
        campoTexto.setBounds(x, y, ancho, alto);
        campoTexto.setToolTipText(toolTip);
        return campoTexto;
    }

}
