import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class MantenimientoBoleto extends JPanel {

    //Objetos de conexion SQL
    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;
    JButton botonInsertar;
    JButton botonActualizar;
    JButton botonEliminar;
    JButton botonConsultar;
    JScrollPane scroll;
    PanelBoleto panel;
    DefaultTableModel modelo;
    JTable tabla;


    public MantenimientoBoleto() throws ClassNotFoundException, SQLException {
        this.setLayout(null); 

        // Conexión a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "cRojas34");
        stmt = con.createStatement();

        JLabel label = new JLabel("Mantenimiento de tabla boleto");
        label.setBounds(250, 20, 300, 30);
        this.add(label);

        tabla = new JTable();
        Object columnas[] = {"Codigo", "# asiento", "Precio Final", "Fecha de emicion"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);
        
        recargarTabla(); // Cargar datos iniciales en la tabla

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 250); 
        this.add(scroll);

        // --- BOTONES --- 
        botonInsertar = crearBoton("Insertar", 40, 360, 100, 40, "Insertar nuevo registro",
                "Iconos/insertar-cuadrado.png");
        botonInsertar.setBackground(new Color(46, 204, 113));
        botonInsertar.setForeground(Color.WHITE);
        this.add(botonInsertar);

        botonActualizar = crearBoton("Actualizar", 200, 360, 100, 40, "Actualizar registro existente",
                "Iconos/boton-editar.png");
        botonActualizar.setBackground(new Color(255, 179, 71));
        botonActualizar.setForeground(Color.WHITE);
        this.add(botonActualizar);

        botonEliminar = crearBoton("Eliminar", 360, 360, 100, 40, "Eliminar registro existente",
                "Iconos/eliminar.png");
        botonEliminar.setBackground(new Color(240, 128, 128));
        botonEliminar.setForeground(Color.WHITE);
        this.add(botonEliminar);

        botonConsultar = crearBoton("Consultar", 520, 360, 100, 40, "Consultar registro existente",
                "Iconos/buscar.png");
        botonConsultar.setBackground(new Color(135, 206, 250));
        botonConsultar.setForeground(Color.WHITE);
        this.add(botonConsultar);
;
        
        // --- CAMPO PARA ACTUALIZAR ---
        JTextField campoActualizarId = crearCampoTexto(200, 410, 100, 30, "Ingrese CÓDIGO a actualizar");
        this.add(campoActualizarId);

        // --- CAMPO PARA ELIMINAR ---
        JTextField campoEliminar = crearCampoTexto(360, 410, 120, 30, "Ingrese CÓDIGO a eliminar");
        this.add(campoEliminar);

        // --- CAMPO PARA CONSULTAR ---
        JTextField campoConsultar = crearCampoTexto(520, 410, 120, 30, "Ingrese CÓDIGO a consultar");
        this.add(campoConsultar);

        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar);
                this.remove(botonActualizar);
                this.remove(botonEliminar);
                this.remove(botonConsultar);
               // this.remove(campoActualizarId);
                //this.remove(campoEliminar);
                //this.remove(campoConsultar);


                //Panel de insercion
                panel = new PanelBoleto(this,0, null);
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 500);
                this.add(panel);
                this.setComponentZOrder(panel, 0);
                this.revalidate();
                this.repaint();
            } catch (ClassNotFoundException | SQLException e1) {
                e1.printStackTrace();
            }
        });
        
       botonActualizar.addActionListener(e -> {
    if (campoActualizarId.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar el código del boleto a actualizar.",  "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Verificar si el boleto existe antes de actualizar, usando un procedimiento almacenado
    try (java.sql.CallableStatement cs = con.prepareCall("{CALL consultar_boleto(?)}")) {
        cs.setString(1, campoActualizarId.getText().trim());
        ResultSet rs = cs.executeQuery();

        // Si el boleto existe, abrir el panel de actualización
        if (rs.next()) {
            this.remove(scroll);
            this.remove(botonInsertar);
            this.remove(botonActualizar);
            this.remove(botonEliminar);
            this.remove(botonConsultar);

            try {
                //Panel de actualizacion
                panel = new PanelBoleto(this, 1, campoActualizarId.getText().trim());
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 500);
                this.add(panel);
                this.setComponentZOrder(panel, 0);
                this.revalidate();
                this.repaint();
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el panel de actualización: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el boleto con el código especificado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al buscar el boleto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    // Limpiar campo de actualización
    campoActualizarId.setText("");
});

// Eliminar boleto
     botonEliminar.addActionListener(e -> {
    if (campoEliminar.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar el código a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el boleto con código " + campoEliminar.getText() + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            // Si el usuario confirma la eliminación 
    if (confirmar == JOptionPane.YES_OPTION) {
        try (java.sql.CallableStatement cs = con.prepareCall("{CALL eliminar_boleto(?)}")) {
            cs.setString(1, campoEliminar.getText());
            cs.executeUpdate();
            JOptionPane.showMessageDialog(this, "Boleto eliminado correctamente.");
            recargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar boleto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});

// Consultar boleto
        botonConsultar.addActionListener(e -> {
            if (campoConsultar.getText().isEmpty()) {
                recargarTabla();
                return;
            }

            modelo.setRowCount(0);
            // Llamada al procedimiento almacenado para consultar boleto
             try (java.sql.CallableStatement cs = con.prepareCall("{CALL consultar_boleto(?)}")) {
        cs.setString(1, campoConsultar.getText());
        ResultSet rs = cs.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Registro no encontrado.");
            recargarTabla();
            return;
        }

        do {
            // Agregar fila a la tabla con los datos del boleto
            modelo.addRow(new Object[]{
                rs.getString("codigo"),
                rs.getString("asiento"),
                rs.getString("precio_final"),
                rs.getString("fecha_emision")
            });
        } while (rs.next());

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al consultar boleto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    campoConsultar.setText("");
});
    }

    public void recargarTabla() {
         modelo.setRowCount(0);
         // Llamada al procedimiento almacenado para listar boletos
    try (java.sql.CallableStatement cs = con.prepareCall("{CALL listar_boletos_mantenimiento()}");
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            // Agregar fila a la tabla con los datos del boleto
            String codigo = rs.getString("codigo");
            String asiento = rs.getString("asiento");
            String precioFinal = rs.getString("precio_final");
            String fechaEmision = rs.getString("fecha_emision");
            modelo.addRow(new Object[]{codigo, asiento, precioFinal, fechaEmision});
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al listar boletos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Métodos para crear componentes GUI
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
}