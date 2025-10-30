
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

//import com.formdev.flatlaf.FlatLightLaf;
//import com.formdev.flatlaf.themes.FlatMacLightLaf;
public class VistaLogin {

    public static void main(String[] args) throws Exception {
        //Ajustes de FlatLaf
        UIManager.setLookAndFeel(new FlatMacLightLaf());

        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 20);
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(160, 160, 160));
        UIManager.put("Component.focusColor", new Color(160, 160, 160));
        UIManager.put("Component.selectionBackground", new Color(160, 160, 160));

        //Creacion de ventana para loguerce
        JFrame frameLogin = new JFrame("Ingrese sus credenciales");
        Container conte = new Container();
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setSize(400, 500);
        JLabel nombreUsuario = crearEtiqueta("Nombre de Usuario", 70, 30, 200, 30);
        conte.add(nombreUsuario);
        JTextField campoNombreUsuario = crearCampoTexto("", 80, 60, 150, 30, "Campo para nobre de usuario");
        conte.add(campoNombreUsuario);
        JLabel contrasena = crearEtiqueta("Contraseña", 70, 120, 200, 30);
        conte.add(contrasena);
        //JTextField campoContrasena = crearCampoTexto("", 80, 150, 150, 30, "Campo para Contraseña");

        JPasswordField campoContrasena = new JPasswordField();
        campoContrasena.setBounds(80, 150, 150, 30);
        campoContrasena.setEchoChar('*');
        JCheckBox mostrarContrasena = new JCheckBox("Mostrar contraseña");
        mostrarContrasena.setBounds(240, 150, 200, 30);
        mostrarContrasena.addActionListener(e -> {
            if (mostrarContrasena.isSelected()) {
                campoContrasena.setEchoChar((char) 0);
            } else {
                campoContrasena.setEchoChar('*');
            }
        });

        conte.add(campoContrasena);
        conte.add(mostrarContrasena);
        JButton botonRegistrarse = crearBoton("Registrase", 50, 300, 300, 30, "Crear nuevo perfil", "Iconos/agregar-usuario.png");
        conte.add(botonRegistrarse);
        botonRegistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                VistaRegistro vistaRegistro = null;
                try {
                    vistaRegistro = new VistaRegistro();
                } catch (UnsupportedLookAndFeelException e1) {
                    
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                vistaRegistro.setVisible(true);
            }
        });
        JButton botonIngresar = crearBoton("Loguearce", 50, 350, 300, 30, "Iniciar secion", "Iconos/entrar.png");
        conte.add(botonIngresar);
        botonIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                VistaPrincipal vistaPrincipal = null;
                try {
                    vistaPrincipal = new VistaPrincipal();
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                vistaPrincipal.setVisible(true);
            }
        });
        frameLogin.add(conte);
        frameLogin.setLocationRelativeTo(null);
        frameLogin.setVisible(true);
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
        boton.setIconTextGap(80);
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
