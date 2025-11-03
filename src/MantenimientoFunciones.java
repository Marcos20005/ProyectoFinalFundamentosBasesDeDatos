import java.awt.Color;
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

public class MantenimientoFunciones extends JPanel {
    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;

    JButton botonInsertar, botonActualizar, botonEliminar, botonConcultar;
    JScrollPane scroll;
    JComboBox<Object> combo;
    PanelFunciones panel;

    public MantenimientoFunciones() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");

        JLabel label = new JLabel("Mantenimiento de tabla funciones");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"Código", "Fecha", "Hora", "Precio base"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM funciones");
        while (rs.next()) {
            String codigo = rs.getString("codigo");
            String fecha = rs.getString("fecha");
            String hora = rs.getString("hora");
            String precioBase = rs.getString("precio_base");
            String fila[] = {codigo, fecha, hora, precioBase};
            modelo.addRow(fila);
        }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 200);
        this.add(scroll);

        // Botones
        botonInsertar = crearBoton("Insertar", 40, 300, 100, 40, "Insertar nueva función", "src/imagenes/insertar.png");
        botonInsertar.setBackground(new Color(46, 204, 113));
        botonInsertar.setForeground(Color.WHITE);
        this.add(botonInsertar);

        botonActualizar = crearBoton("Actualizar", 200, 300, 100, 40, "Actualizar función existente", "src/imagenes/actualizar.png");
        botonActualizar.setBackground(new Color(255, 179, 71));
        botonActualizar.setForeground(Color.WHITE);
        this.add(botonActualizar);

        botonEliminar = crearBoton("Eliminar", 360, 300, 100, 40, "Eliminar función existente", "src/imagenes/eliminar.png");
        botonEliminar.setBackground(new Color(240, 128, 128));
        botonEliminar.setForeground(Color.WHITE);
        this.add(botonEliminar);

        botonConcultar = crearBoton("Consultar", 520, 300, 100, 40, "Consultar función existente", "src/imagenes/consultar.png");
        botonConcultar.setBackground(new Color(135, 206, 250));
        botonConcultar.setForeground(Color.WHITE);
        this.add(botonConcultar);

        // Combo box para actualizar
        String lista[] = {"Código", "Fecha", "Hora", "Precio base"};
        combo = new JComboBox<>(lista);
        combo.setBounds(200, 360, 140, 30);
        this.add(combo);

        // Campos de texto para actualizar, eliminar y consultar
        JTextField campoActualizar = crearCampoTexto(200, 400, 100, 30, "Ingrese Código a actualizar");
        this.add(campoActualizar);
        JTextField campoValorActualizar = crearCampoTexto(200, 440, 100, 30, "Ingrese nuevo valor");
        this.add(campoValorActualizar);

        JTextField campoEliminar = crearCampoTexto(360, 360, 100, 30, "Ingrese Código a eliminar");
        this.add(campoEliminar);

        JTextField campoConsultar = crearCampoTexto(520, 360, 100, 30, "Ingrese Código a consultar");
        this.add(campoConsultar);

        // Acción Insertar -> muestra panel de inserción
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar);
                this.remove(botonActualizar);
                this.remove(botonEliminar);
                this.remove(botonConcultar);
                this.remove(combo);
                this.remove(campoActualizar);
                this.remove(campoValorActualizar);
                this.remove(campoEliminar);
                this.remove(campoConsultar);

                panel = new PanelFunciones(MantenimientoFunciones.this);
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 350);
                this.add(panel);
                this.setComponentZOrder(panel, 0);
                this.revalidate();
                this.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Acción Actualizar
        botonActualizar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;

            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }

            try {
                rs = stmt.executeQuery("SELECT * FROM funciones");
                while (rs.next()) {
                    if (rs.getString("codigo").equals(campoActualizar.getText())) {
                        encontrado = true;
                    }
                }

                if (encontrado) {
                    int eleccion = javax.swing.JOptionPane.showConfirmDialog(null,
                            "¿Desea guardar los cambios?", "Confirmar acción",
                            javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                String columna = "";
                switch (combo.getSelectedIndex()) {
                    case 0: columna = "codigo"; break;
                    case 1: columna = "fecha"; break;
                    case 2: columna = "hora"; break;
                    case 3: columna = "precio_base"; break;
                }

                if (columna.equals("precio_base")) {
                    try {
                        double precio = Double.parseDouble(campoValorActualizar.getText());
                        stmt.executeUpdate("UPDATE funciones SET " + columna + "='" + precio
                                + "' WHERE codigo='" + campoActualizar.getText() + "';");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Debe ingresar un número válido para el precio base");
                        return;
                    }
                } else {
                    stmt.executeUpdate("UPDATE funciones SET " + columna + "='" + campoValorActualizar.getText()
                            + "' WHERE codigo='" + campoActualizar.getText() + "';");
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Registro no encontrado");
        }

                // Recargar tabla
                rs = stmt.executeQuery("SELECT * FROM funciones");
                while (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String fecha = rs.getString("fecha");
                    String hora = rs.getString("hora");
                    String precioBase = rs.getString("precio_base");
                    String fila[] = {codigo, fecha, hora, precioBase};
                    modelo.addRow(fila);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            campoActualizar.setText("");
            campoValorActualizar.setText("");
        });

        // Acción Eliminar
        botonEliminar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;

            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }

            try {
                rs = stmt.executeQuery("SELECT * FROM funciones");
                while (rs.next()) {
                    if (rs.getString("codigo").equals(campoEliminar.getText())) {
                        encontrado = true;
                    }
                }

                if (encontrado) {
                    int eleccion = javax.swing.JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar la eliminación del registro?", "Confirmación",
                            javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt.executeUpdate("DELETE FROM funciones WHERE codigo='" + campoEliminar.getText() + "';");
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }

                rs = stmt.executeQuery("SELECT * FROM funciones");
                while (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String fecha = rs.getString("fecha");
                    String hora = rs.getString("hora");
                    String precioBase = rs.getString("precio_base");
                    String fila[] = {codigo, fecha, hora, precioBase};
                    modelo.addRow(fila);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            campoEliminar.setText("");
        });

        // Acción Consultar
        botonConcultar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;

            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }

            try {
                if (!campoConsultar.getText().isEmpty()) {
                    rs = stmt.executeQuery("SELECT * FROM funciones");
                    while (rs.next()) {
                        if (rs.getString("codigo").equals(campoConsultar.getText())) {
                            encontrado = true;
                            String fila[] = {rs.getString("codigo"), rs.getString("fecha"),
                                    rs.getString("hora"), rs.getString("precio_base")};
                            modelo.addRow(fila);
                        }
                    }
                    if (!encontrado) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Registro no encontrado");
                        rs = stmt.executeQuery("SELECT * FROM funciones");
                        while (rs.next()) {
                            String fila[] = {rs.getString("codigo"), rs.getString("fecha"),
                                    rs.getString("hora"), rs.getString("precio_base")};
                            modelo.addRow(fila);
                        }
                    }
                } else {
                    rs = stmt.executeQuery("SELECT * FROM funciones");
                    while (rs.next()) {
                        String fila[] = {rs.getString("codigo"), rs.getString("fecha"),
                                rs.getString("hora"), rs.getString("precio_base")};
                        modelo.addRow(fila);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            campoConsultar.setText("");
        });
    }

    // Método para recargar tabla
    public void recargarTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) ((JTable) ((JScrollPane) scroll).getViewport().getView())
                    .getModel();
            modelo.setRowCount(0);

            rs = stmt.executeQuery("SELECT * FROM funciones");
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String fecha = rs.getString("fecha");
                String hora = rs.getString("hora");
                String precioBase = rs.getString("precio_base");
                String fila[] = {codigo, fecha, hora, precioBase};
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

    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }
}
