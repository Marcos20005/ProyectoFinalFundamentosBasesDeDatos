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
import javax.swing.JComboBox;
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
    JComboBox<Object> combo;
    PanelBoleto panel;
    DefaultTableModel modelo;
    JTable tabla;


    public MantenimientoBoleto() throws ClassNotFoundException, SQLException {
        this.setLayout(null); // Usamos un layout nulo para posicionar componentes manualmente

        //Estableciendo conexion a la base de datos
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
        
        recargarTabla(); // Carga inicial de datos

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 250); // Ajustado para un mejor tamaño
        this.add(scroll);

        // --- FILA DE BOTONES ---
        botonInsertar = crearBoton("Insertar", 40, 360, 120, 40, "Insertar nuevo boleto", "src/imagenes/insertar.png");
        botonInsertar.setBackground(new Color(46, 204, 113));
        botonInsertar.setForeground(Color.WHITE);
        this.add(botonInsertar);

        botonActualizar = crearBoton("Actualizar", 220, 360, 120, 40, "Actualizar boleto existente", "src/imagenes/actualizar.png");
        botonActualizar.setBackground(new Color(255, 179, 71));
        botonActualizar.setForeground(Color.WHITE);
        this.add(botonActualizar);

        botonEliminar = crearBoton("Eliminar", 400, 360, 120, 40, "Eliminar boleto existente", "src/imagenes/eliminar.png");
        botonEliminar.setBackground(new Color(240, 128, 128));
        botonEliminar.setForeground(Color.WHITE);
        this.add(botonEliminar);

        botonConsultar = crearBoton("Consultar", 580, 360, 120, 40, "Consultar boleto existente", "src/imagenes/consultar.png");
        botonConsultar.setBackground(new Color(135, 206, 250));
        botonConsultar.setForeground(Color.WHITE);
        this.add(botonConsultar);

        // --- CAMPOS PARA ACTUALIZAR ---
        String lista[] = {"asiento", "precio_final", "fecha_emicion"};
        combo = new JComboBox<>(lista);
        combo.setBounds(220, 410, 120, 30);
        this.add(combo);
        
        JTextField campoActualizarId = crearCampoTexto(220, 450, 120, 30, "Ingrese CÓDIGO a actualizar");
        this.add(campoActualizarId);
        JTextField campoValorActualizar = crearCampoTexto(220, 490, 120, 30, "Ingrese nuevo valor");
        this.add(campoValorActualizar);

        // --- CAMPO PARA ELIMINAR ---
        JTextField campoEliminar = crearCampoTexto(400, 410, 120, 30, "Ingrese CÓDIGO a eliminar");
        this.add(campoEliminar);

        // --- CAMPO PARA CONSULTAR ---
        JTextField campoConsultar = crearCampoTexto(580, 410, 120, 30, "Ingrese CÓDIGO a consultar");
        this.add(campoConsultar);


        // --- ACTION LISTENERS ---

        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar);
                this.remove(botonActualizar);
                this.remove(botonEliminar);
                this.remove(botonConsultar);
                // Ocultar todos los campos de texto y combo
                this.remove(combo);
                this.remove(campoActualizarId);
                this.remove(campoValorActualizar);
                this.remove(campoEliminar);
                this.remove(campoConsultar);


                panel = new PanelBoleto(this);
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
            boolean encontrado = false;
            if (campoActualizarId.getText().isEmpty() || campoValorActualizar.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar el código a buscar y el nuevo valor.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                rs = stmt.executeQuery("SELECT * FROM boleto WHERE codigo = '" + campoActualizarId.getText() + "'");
                if (rs.next()) {
                    encontrado = true;
                }

                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?", "Confirmar acción", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        String columna = combo.getSelectedItem().toString();
                        stmt.executeUpdate("UPDATE boleto SET " + columna + "='" + campoValorActualizar.getText() + "' WHERE codigo = '" + campoActualizarId.getText() + "';");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el registro buscado");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            recargarTabla();
            campoActualizarId.setText("");
            campoValorActualizar.setText("");
        });
        
        botonEliminar.addActionListener(e -> {
            boolean encontrado = false;
            if (campoEliminar.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar el código a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                rs = stmt.executeQuery("SELECT * FROM boleto WHERE codigo = '" + campoEliminar.getText() + "'");
                if (rs.next()) {
                    encontrado = true;
                }

                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar la eliminación del registro?", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt.executeUpdate("DELETE FROM boleto WHERE codigo = '" + campoEliminar.getText() + "';");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            recargarTabla();
            campoEliminar.setText("");
        });
        
        botonConsultar.addActionListener(e -> {
            if (campoConsultar.getText().isEmpty()) {
                recargarTabla();
                return;
            }

            modelo.setRowCount(0);
            boolean encontrado = false;
            try {
                rs = stmt.executeQuery("SELECT * FROM boleto WHERE codigo = '" + campoConsultar.getText() + "'");
                while (rs.next()) {
                    encontrado = true;
                    String codigo = rs.getString("codigo");
                    String asiento = rs.getString("asiento");
                    String precioFinal = rs.getString("precio_final");
                    String fechaEmicion = rs.getString("fecha_emicion");
                    String[] fila = {codigo, asiento, precioFinal, fechaEmicion};
                    modelo.addRow(fila);
                }
                if (!encontrado) {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado.");
                    recargarTabla();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            campoConsultar.setText("");
        });
    }

    public void recargarTabla() {
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }
        modelo.setRowCount(0);
        try {
            rs = stmt.executeQuery("SELECT * FROM boleto");
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String asiento = rs.getString("asiento");
                String precioFinal = rs.getString("precio_final");
                String fechaEmicion = rs.getString("fecha_emicion");
                String[] fila = {codigo, asiento, precioFinal, fechaEmicion};
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}