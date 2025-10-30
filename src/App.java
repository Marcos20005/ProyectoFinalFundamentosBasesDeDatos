
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class App {

    static Connection con =null;
    static Statement stmt = null;
    static ResultSet rs = null;
    public static void main(String[] args) throws Exception {

       

      
          Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "cRojas34");
        
      UIManager.setLookAndFeel(new FlatMacLightLaf());

        JFrame pantalla = new JFrame("Multicines S.A");
        
      
        pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      pantalla.setSize(800,700);
     // pantalla.setLayout(null);
       JTabbedPane pestanias = new JTabbedPane();
       pestanias.setSize(800,600);
      // pestanias.setLayout(null);
       JPanel panel1 = new JPanel();
       panel1.setLayout(null);
        JButton opcion1 = crearBoton("<html>Ver<br><center>Cartelera</center></html>", 200,100,200,40);
        
        panel1.add(opcion1);

        //Declaracion de evento de boton opcion1
        opcion1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             pestanias.setSelectedIndex(1);   
            }
        });

        JButton opcion2 = crearBoton("<html>Registrar<br><center>Reservaciones</center></html>", 200,160,200,40);
        panel1.add(opcion2);

         //Declaracion de evento de boton opcion2
        opcion2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             pestanias.setSelectedIndex(2);   
            }
        });


        JButton opcion3 = crearBoton("<html>Cancelar<br><center>Reservaciones</center></html>", 200,220,200,40);
        panel1.add(opcion3);

           //Declaracion de evento de boton opcion2
           opcion3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             pestanias.setSelectedIndex(3);   
            }
        });


        JButton opcion4 = crearBoton("<html>Ver<br><center>Salir</center></html>", 200,280,200,40);
        panel1.add(opcion4);

                   //Declaracion de evento de boton opcion4
           opcion4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          System.exit(0);
            }
        });

       JPanel panel2 = new JPanel();
         panel2.setLayout(null);
         JLabel titulo1 = crearEtiqueta("Ingrese la informacion necesaria", 240, 40, 200, 20);
         panel2.add(titulo1);
         JLabel eti1 = crearEtiqueta("Empleado encargado de venta", 30, 70, 250, 20);
         panel2.add(eti1);
         JLabel eti2 = crearEtiqueta("ID de Empleado", 40, 100, 200, 20);
            panel2.add(eti2);
         JTextField campo1 = crearCampoTexto("", 40, 120, 100, 30);
         panel2.add(campo1);
         JLabel eti3 = crearEtiqueta("Ingrese el primer nombre", 40, 160, 200, 20);
            panel2.add(eti3);
          JTextField campo2 = crearCampoTexto("", 40, 180, 150, 30);
          panel2.add(campo2);
          JLabel eti4 = crearEtiqueta("Ingrese el segundo nombre", 40, 220, 200, 20);
            panel2.add(eti4);
          JTextField campo3 = crearCampoTexto("", 40, 240, 150, 30);
          panel2.add(campo3);
          JLabel eti5 = crearEtiqueta("Ingrese el primer apellido", 40, 280, 200, 20);
            panel2.add(eti5);
          JTextField campo4 = crearCampoTexto("", 40, 300, 150, 30);
          panel2.add(campo4);
          JLabel eti6 = crearEtiqueta("Ingrese el segundo apellido", 40, 340, 200, 20);
            panel2.add(eti6);
          JTextField campo5 = crearCampoTexto("", 40, 360, 150, 30);
          panel2.add(campo5);
          JLabel eti7 = crearEtiqueta("Ingrese el ID de trabajo", 40, 400, 200, 20);
            panel2.add(eti7);
          JTextField campo6 = crearCampoTexto("", 40, 420, 150, 30);
          panel2.add(campo6);

         JButton atras1 = crearBoton("<--", 30, 580, 80, 30);
            panel2.add(atras1);
             atras1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          pestanias.setSelectedIndex(0);
            }
        });

        
         JButton Guardar1 = crearBoton("<html>Guardar<br><center>Informacion</center></html>", 370, 530, 150, 40);
         panel2.add(Guardar1);
             Guardar1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
     
               try {
        stmt = con.createStatement();
         stmt.executeUpdate("INSERT INTO empleado (id,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,id_puesto) VALUES ('"+campo1.getText()+"','"+campo2.getText()+"','"+campo3.getText()+"','"+campo4.getText()+"','"+campo5.getText()+"','"+campo6.getText()+"');");

      } catch (SQLException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
       
             campo1.setText("");   
             campo2.setText("");
             campo3.setText("");
             campo4.setText("");
             campo5.setText("");
             campo6.setText("");
            }
        });
        JButton siguiente1 = crearBoton("-->", 690, 580, 80, 30);
        panel2.add(siguiente1);
                  siguiente1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          pestanias.setSelectedIndex(2);
            }
        });

         JPanel panel3 = new JPanel();
         panel3.setLayout(null);
          JButton atras2 = crearBoton("<--", 30, 580, 80, 30);
            panel3.add(atras2);
             atras2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          pestanias.setSelectedIndex(1);
            }
        });
          JButton siguiente2 = crearBoton("-->", 690, 580, 80, 30);
          panel3.add(siguiente2);
                  siguiente2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          pestanias.setSelectedIndex(3);
            }
        });


         JPanel panel4 = new JPanel();
         panel4.setLayout(null);
         JButton atras3 = crearBoton("<--", 30, 580, 80, 30);
          panel4.add(atras3);
             atras3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          pestanias.setSelectedIndex(2);
            }
        });
        JButton siguiente3 = crearBoton("-->", 690, 580, 80, 30);
         panel4.add(siguiente3);
                  siguiente3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          pestanias.setSelectedIndex(0);
            }
        });

       pestanias.addTab("Opciones",panel1);
       pestanias.addTab("Ver Cartelera", panel2);
         pestanias.addTab("Registrar boleto", panel3);
         pestanias.addTab("Cancelar boleto", panel4);
     

    pantalla.add(pestanias);
       pantalla.setLocationRelativeTo(null);
pantalla.setVisible(true);


        

    }

    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        return boton;
    }

    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }

    static public JTextField crearCampoTexto(String texto, int x,int y, int ancho, int alto){
     JTextField campoTexto = new JTextField(texto);
        campoTexto.setBounds(x, y, ancho, alto);
        return campoTexto;
    }
    

}
