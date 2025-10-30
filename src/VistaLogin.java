import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

//import com.formdev.flatlaf.FlatLightLaf;
//import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class VistaLogin  {

    
 public static void main(String[] args) throws Exception {
    //Ajustes de FlatLaf
    UIManager.setLookAndFeel(new FlatMacLightLaf());
  
       UIManager.put("Button.arc", 80);
         UIManager.put("Component.arc", 20);
         UIManager.put("TextComponent.arc", 20);
         UIManager.put("Button.foreground", Color.WHITE);    
         UIManager.put("Button.background", new Color(30, 144, 255));


        //Creacion de ventana para loguerce
  JFrame frameLogin = new JFrame("Ingrese sus credenciales");
   Container conte = new Container();
   frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frameLogin.setSize(400, 500);
    JLabel nombreUsuario = crearEtiqueta("Nombre de Usuario", 70, 30, 200, 30);
    conte.add(nombreUsuario);
    JTextField campoNombreUsuario = crearCampoTexto("", 80, 60, 150, 30);
    campoNombreUsuario.setToolTipText("Nombre de usuario registrado");
    conte.add(campoNombreUsuario);
        JLabel contrasena = crearEtiqueta("Contraseña", 70, 120, 200, 30);
        conte.add(contrasena);
        JTextField campoContrasena = crearCampoTexto("", 80, 150, 150, 30);
        campoContrasena.setToolTipText("Contraseña registrada");
        conte.add(campoContrasena);
        JButton botonRegistrarse = crearBoton("Registrase", 50, 300, 100, 30);
        conte.add(botonRegistrarse);
        botonRegistrarse.addActionListener(new ActionListener(){
            @Override
        public void actionPerformed(java.awt.event.ActionEvent e){
       VistaRegistro vistaRegistro = null;
      try {
            vistaRegistro = new VistaRegistro();
         } catch (UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         vistaRegistro.setVisible(true);
        }   
        });
     JButton botonIngresar = crearBoton("Loguearce",  160, 300, 100, 30);
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
    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        return boton;
    }

    //Metodo declarado para crear etiquetas
    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }


    //Metodo declarado para crear campos de texto
    static public JTextField crearCampoTexto(String texto, int x,int y, int ancho, int alto){
     JTextField campoTexto = new JTextField(texto);
        campoTexto.setBounds(x, y, ancho, alto);
        return campoTexto;
    }

    

}

