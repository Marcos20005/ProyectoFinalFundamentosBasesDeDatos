import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class PanelBoletos extends JPanel {

    private JTextField campoCodigo, campoAsiento, campoPrecio;
    private JComboBox<String> comboCliente, comboEmpleado, comboSala, comboFuncion;
    private JLabel TituloPelicula;
    private String codigoPelicula, tituloPelicula;  

    public PanelBoletos(JTabbedPane pestanias, Connection con) {
        this.setLayout(null);
        this.setBackground(new Color(245, 245, 245));

        //Tarjeta de registro de boleto
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setBounds(80, 60, 640, 460);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(130, 90, 160), 2, true));
        this.add(tarjeta);

        //Titulo de la tarjeta
        JLabel titulo = new JLabel("Registro de Boleto", JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titulo.setForeground(new Color(90, 60, 120));
        titulo.setBounds(0, 20, 640, 40);
        tarjeta.add(titulo);

        //Pelicula seleccionada
        TituloPelicula = new JLabel("Película seleccionada: Ninguna");
        TituloPelicula.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        TituloPelicula.setForeground(new Color(70,70,70));
        TituloPelicula.setBounds(40, 70, 560, 25);
        tarjeta.add(TituloPelicula);

        //Campo de texto y etiquetas a la izquierda
        JLabel Cod = VistaPrincipal.crearEtiqueta("Código boleto:", 80, 110, 200, 25);
        tarjeta.add(Cod);
        campoCodigo = VistaPrincipal.crearCampoTexto("", 250, 110, 150, 25, "Ingrese el código");
        tarjeta.add(campoCodigo);

        JLabel Asien = VistaPrincipal.crearEtiqueta("Asiento:", 80, 150, 200, 25);
        tarjeta.add(Asien);
        campoAsiento = VistaPrincipal.crearCampoTexto("", 250, 150, 150, 25, "Ingrese el asiento");
        tarjeta.add(campoAsiento);

        JLabel Prec = VistaPrincipal.crearEtiqueta("Precio final:", 80, 190, 200, 25);
        tarjeta.add(Prec);
        campoPrecio = VistaPrincipal.crearCampoTexto("", 250, 190, 150, 25, "Ingrese el precio final");
        tarjeta.add(campoPrecio);

        //Campo de texto y etiquetas a la derecha
        JLabel Clien = VistaPrincipal.crearEtiqueta("Cliente:", 80, 230, 200, 25);
        tarjeta.add(Clien);
        comboCliente = new JComboBox<>();
        comboCliente.setBounds(250, 230, 150, 25);
        tarjeta.add(comboCliente);

        JLabel Emple = VistaPrincipal.crearEtiqueta("Empleado:", 80, 270, 200, 25);
        tarjeta.add(Emple);
        comboEmpleado = new JComboBox<>();
        comboEmpleado.setBounds(250, 270, 150, 25);
        tarjeta.add(comboEmpleado);

        JLabel Sal = VistaPrincipal.crearEtiqueta("Sala:", 80, 310, 200, 25);
        tarjeta.add(Sal);
        comboSala = new JComboBox<>();
        comboSala.setBounds(250, 310, 150, 25);
        tarjeta.add(comboSala);

        JLabel Func = VistaPrincipal.crearEtiqueta("Función:", 80, 350, 200, 25);
        tarjeta.add(Func);
        comboFuncion = new JComboBox<>();
        comboFuncion.setBounds(250, 350, 150, 25);
        tarjeta.add(comboFuncion);

        //Botones de la tarjeta
        JButton btnAtras = VistaPrincipal.crearBoton("", 120, 400, 60, 40, "Volver", "Iconos/paso-atras.png");
        tarjeta.add(btnAtras);
        btnAtras.addActionListener(e -> pestanias.setSelectedIndex(3));

        JButton btnGuardar = VistaPrincipal.crearBoton("Registrar venta", 250, 400, 160, 40, "Guardar boleto", "Iconos/guardar-el-archivo.png");
        tarjeta.add(btnGuardar);
        btnGuardar.addActionListener(e -> {
            try {
                registrarBoleto(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JButton btnSiguiente = VistaPrincipal.crearBoton("-->", 470, 400, 60, 40, "Siguiente", "Iconos/un-paso-adelante.png");
        tarjeta.add(btnSiguiente);
        btnSiguiente.addActionListener(e -> pestanias.setSelectedIndex(5));

        cargarClientes(con);
        cargarEmpleados(con);
        cargarSalas(con);
        cargarFunciones(con);
    }

  public void setPeliculaSeleccionada(String codigo, String titulo) {
    this.codigoPelicula = codigo.trim(); // Asegura que no haya espacios
    this.tituloPelicula = titulo.trim();
    TituloPelicula.setText("Película seleccionada: " + titulo);
}
      public void cargarClientes(Connection con) {
    try (java.sql.CallableStatement cstmt = con.prepareCall("{CALL listar_cliente()}");
         ResultSet rs = cstmt.executeQuery()) {
        comboCliente.removeAllItems();
        while (rs.next()) {
            comboCliente.addItem(rs.getString("cedula") + " - " + rs.getString("primer_nombre"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los clientes: " + e.getMessage());
    }
}


    public void cargarEmpleados(Connection con) {
        try (java.sql.CallableStatement stmt = con.prepareCall("{CALL listar_empleado()}");
             ResultSet rs = stmt.executeQuery()) {
            comboEmpleado.removeAllItems();
            while (rs.next()) {
                comboEmpleado.addItem(rs.getString("id") + " - " + rs.getString("primer_nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarSalas(Connection con) {
        try (java.sql.CallableStatement stmt = con.prepareCall("{CALL listar_sala()}");
             ResultSet rs = stmt.executeQuery()) {
            comboSala.removeAllItems();
            while (rs.next()) {
                comboSala.addItem(rs.getString("numero_sala"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarFunciones(Connection con) {
        try (java.sql.CallableStatement stmt = con.prepareCall("{CALL listar_funciones()}");
             ResultSet rs = stmt.executeQuery()) {
            comboFuncion.removeAllItems();
            while (rs.next()) {
                comboFuncion.addItem(rs.getString("codigo") + " - " + rs.getString("hora") + " (" + rs.getString("fecha") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarBoleto(Connection con) throws SQLException {
    if (campoCodigo.getText().isEmpty() || campoAsiento.getText().isEmpty() ||
        campoPrecio.getText().isEmpty() || comboCliente.getSelectedItem() == null ||
        comboEmpleado.getSelectedItem() == null || comboSala.getSelectedItem() == null ||
        comboFuncion.getSelectedItem() == null) {

        JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
        
    }

    String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String codigoFuncion = comboFuncion.getSelectedItem().toString().split(" - ")[0];
    String cedulaCliente = comboCliente.getSelectedItem().toString().split(" - ")[0];
    String idEmpleado = comboEmpleado.getSelectedItem().toString().split(" - ")[0];
    String numeroSala = comboSala.getSelectedItem().toString();

    try {
        System.out.println("Código película enviado: '" + codigoPelicula + "'");
        java.sql.CallableStatement cstmt = con.prepareCall("{CALL insertar_boleto(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        cstmt.setString(1, campoCodigo.getText());
        cstmt.setString(2, campoAsiento.getText());
        cstmt.setDouble(3, Double.parseDouble(campoPrecio.getText()));
        cstmt.setString(4, fecha);
        cstmt.setString(5, cedulaCliente);
        cstmt.setString(6, codigoPelicula);
        cstmt.setString(7, numeroSala);
        cstmt.setString(8, codigoFuncion);
        cstmt.setString(9, idEmpleado);

        cstmt.executeUpdate();
        cstmt.close();

        String resumen = " BOLETO EMITIDO EXITOSAMENTE\n\n"
                + "Código de boleto: " + campoCodigo.getText() + "\n"
                + "Película: " + tituloPelicula + "\n"
                + "Sala: " + numeroSala + "\n"
                + "Función: " + comboFuncion.getSelectedItem().toString() + "\n"
                + "Asiento: " + campoAsiento.getText() + "\n"
                + "Precio final: ₡" + campoPrecio.getText() + "\n"
                + "Cliente: " + comboCliente.getSelectedItem().toString() + "\n"
                + "Empleado: " + comboEmpleado.getSelectedItem().toString() + "\n"
                + "Fecha de emisión: " + fecha + "\n\n"
                + "Gracias por su compra.";

        JOptionPane.showMessageDialog(this, resumen, "Boleto registrado", JOptionPane.INFORMATION_MESSAGE);

        campoCodigo.setText("");
        campoAsiento.setText("");
        campoPrecio.setText("");

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error al registrar el boleto: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
}