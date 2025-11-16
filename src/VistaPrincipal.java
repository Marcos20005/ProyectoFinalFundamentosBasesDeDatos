
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
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VistaPrincipal extends JFrame {

    static Connection con = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    PanelBoletos panel3;

    public VistaPrincipal() throws ClassNotFoundException, UnsupportedLookAndFeelException, SQLException {

        //Conexion con la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "cRojas34");
        //Ajustes flatlaf
        UIManager.setLookAndFeel(new FlatMacLightLaf());
        UIManager.put("Button.arc", 80);
        UIManager.put("Component.arc", 20);
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Button.foreground", Color.WHITE);

        UIManager.put("Button.background", new Color(130, 90, 160));

        this.setTitle("Multicines S.A");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 700);
        // pantalla.setLayout(null);
        JTabbedPane pestanias = new JTabbedPane();
        pestanias.setSize(800, 600);
        // pestanias.setLayout(null);

        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setBackground(new Color(245, 245, 245));

        //Panel central de opciones 
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setBounds(200, 80, 400, 500);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(130, 90, 160), 2, true));
        panel1.add(tarjeta);

        //Titulo de panel de opciones
        JLabel tituloOpciones = new JLabel("Sistema Multicines S.A", SwingConstants.CENTER);
        tituloOpciones.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        tituloOpciones.setForeground(new Color(90, 60, 120));
        tituloOpciones.setBounds(0, 20, 400, 40);
        tarjeta.add(tituloOpciones);

        //Botones de las opciones
        JButton btnEmpleado = crearBoton("<html><center>Registrar<br>Empleado</center></html>", 80, 80, 240, 45, "Registrar empleado nuevo", "Iconos/anadir.png");
        tarjeta.add(btnEmpleado);
        btnEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pestanias.setSelectedIndex(1); // suponiendo que es la 2da pestaña
            }
        });

        JButton btnCliente = crearBoton("<html><center>Registrar<br>Cliente</center></html>", 80, 140, 240, 45, "Registrar cliente", "Iconos/anadir.png");
        tarjeta.add(btnCliente);
        btnCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pestanias.setSelectedIndex(2); // suponiendo que es la 3ra pestaña
            }
        });

        JButton btnCartelera = crearBoton("<html><center>Ver<br>Cartelera</center></html>", 80, 200, 240, 45, "Ver cartelera", "Iconos/mostrar-contrasena.png");
        tarjeta.add(btnCartelera);
        btnCartelera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pestanias.setSelectedIndex(3); // suponiendo que es la 4ta pestaña
            }
        });

        JButton btnBoleto = crearBoton("<html><center>Registrar<br>Boleto</center></html>", 80, 260, 240, 45, "Registrar venta de boletos", "Iconos/anadir.png");
        tarjeta.add(btnBoleto);
        btnBoleto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pestanias.setSelectedIndex(4); // suponiendo que es la 5ta pestaña
            }
        });

        JButton btnMantenimiento = crearBoton("Mantenimientos", 80, 380, 240, 45, "Ver tablas mantenimientos", "Iconos/module.png");
        tarjeta.add(btnMantenimiento);
        btnMantenimiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pestanias.setSelectedIndex(6); // suponiendo que es la 6ta pestaña
            }
        });

        JButton btnCancelar = crearBoton("<html><center>Cancelar<br>Boleto</center></html>", 80, 320, 240, 45, "Cancelar boleto", "Iconos/eliminar.png");
        tarjeta.add(btnCancelar);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pestanias.setSelectedIndex(5); // suponiendo que es la 6ta pestaña
            }
        });

        //Paneles para empleado, cliente, cartelera, cancelar boleto, matenimiento
        panel3 = new PanelBoletos(pestanias, con);
        PanelCartelera panel2 = new PanelCartelera(pestanias, con, panel3);
        PanelEmpleados panel5 = new PanelEmpleados(pestanias, con, this);
        PanelClientes panel6 = new PanelClientes(pestanias, con, this);
        PanelCancelarBoleto panel4 = new PanelCancelarBoleto(pestanias, con);
        PanelMantenimiento panel7 = new PanelMantenimiento(pestanias, con, this);

        JButton opcionCancelar = crearBoton("<html>Cerrar<br><center>secion</center></html>", 80, 440, 240, 40, "Regresar a vista login", "Iconos/cerrar-sesion.png");
        tarjeta.add(opcionCancelar);
        opcionCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VistaPrincipal.this.setVisible(false);
            }
        });

        //Pestañas 
        pestanias.addTab("Opciones", panel1);
        pestanias.addTab("Registrar Empleado", panel5);
        pestanias.addTab("Registrar Cliente", panel6);
        pestanias.addTab("Ver Cartelera", panel2);
        pestanias.addTab("Registrar boleto", panel3);
        pestanias.addTab("Cancelar boleto", panel4);
        pestanias.addTab("Mantenimiento de tablas", panel7);

        this.add(pestanias);
        this.setLocationRelativeTo(null);

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

    public void actualizarPaneles() {

        panel3.cargarClientes(this.con);
        panel3.cargarFunciones(this.con);
        panel3.cargarEmpleados(this.con);
        panel3.cargarSalas(this.con);

        // 2. Le dice a MantenimientoCliente que recargue su tabla de clientes
    }
}
