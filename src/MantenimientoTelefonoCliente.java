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

public class MantenimientoTelefonoCliente extends JPanel {
    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;

    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    JComboBox<Object> combo;
    PanelTelefonoCliente panel;

    public MantenimientoTelefonoCliente() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");

        JLabel label = new JLabel("Mantenimiento de tabla teléfono cliente");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"ID", "Teléfono", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
        while (rs.next()) {
            String id = rs.getString("id");
            String telefono = rs.getString("telefono");
            String descripcion = rs.getString("descripcion");
            String fila[] = {id, telefono, descripcion};
            modelo.addRow(fila);
        }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 200);
        this.add(scroll);

        // Botones alineados horizontalmente
       
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

        // Combo box debajo de los botones
        String lista[] = {"ID", "Teléfono", "Descripción"};
        combo = new JComboBox<>(lista);
        combo.setBounds(200, 410, 140, 30);
        this.add(combo);

        // Campos de texto para actualizar, eliminar y consultar
        JTextField campoActualizar = crearCampoTexto(200, 450, 100, 30, "Ingrese ID a actualizar");
        this.add(campoActualizar);
        JTextField campoValorActualizar = crearCampoTexto(200, 490, 100, 30, "Ingrese nuevo valor");
        this.add(campoValorActualizar);

        JTextField campoEliminar = crearCampoTexto(360, 410, 100, 30, "Ingrese ID a eliminar");
        this.add(campoEliminar);

        JTextField campoConsultar = crearCampoTexto(520, 410, 100, 30, "Ingrese ID a consultar");
        this.add(campoConsultar);

        // Acción Insertar -> muestra panel de inserción
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar);
                this.remove(botonActualizar);
                this.remove(botonEliminar);
                this.remove(botonConsultar);
                this.remove(combo);
                this.remove(campoActualizar);
                this.remove(campoValorActualizar);
                this.remove(campoEliminar);
                this.remove(campoConsultar);

                panel = new PanelTelefonoCliente(this);
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 500);
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
                rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                while (rs.next()) {
                    if (rs.getString("id").equals(campoActualizar.getText())) {
                        encontrado = true;
                    }
                }

                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?",
                            "Confirmar acción", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        String columna = "";
                        switch (combo.getSelectedIndex()) {
                        case 0:
                            columna = "id";
                            break;
                        case 1:
                            columna = "telefono";
                            break;
                        case 2:
                            columna = "descripcion";
                            break;
                        }
                        stmt.executeUpdate("UPDATE telefono_cliente SET " + columna + "='" + campoValorActualizar.getText()
                                + "' WHERE id='" + campoActualizar.getText() + "';");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }

                // Recargar tabla después de la acción
                rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                while (rs.next()) {
                    String id = rs.getString("id");
                    String telefono = rs.getString("telefono");
                    String descripcion = rs.getString("descripcion");
                    String fila[] = {id, telefono, descripcion};
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
                rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                while (rs.next()) {
                    if (rs.getString("id").equals(campoEliminar.getText())) {
                        encontrado = true;
                    }
                }

                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar la eliminación del registro?",
                            "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt.executeUpdate("DELETE FROM telefono_cliente WHERE id='" + campoEliminar.getText() + "';");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }

                // Recargar tabla
                rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                while (rs.next()) {
                    String id = rs.getString("id");
                    String telefono = rs.getString("telefono");
                    String descripcion = rs.getString("descripcion");
                    String fila[] = {id, telefono, descripcion};
                    modelo.addRow(fila);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            campoEliminar.setText("");
        });

        // Acción Consultar
        botonConsultar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;

            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }

            try {
                if (!campoConsultar.getText().isEmpty()) {
                    rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                    while (rs.next()) {
                        if (rs.getString("id").equals(campoConsultar.getText())) {
                            encontrado = true;
                            String fila[] = {rs.getString("id"), rs.getString("telefono"), rs.getString("descripcion")};
                            modelo.addRow(fila);
                        }
                    }
                    if (!encontrado) {
                        JOptionPane.showMessageDialog(null, "Registro no encontrado");
                        rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                        while (rs.next()) {
                            String fila[] = {rs.getString("id"), rs.getString("telefono"), rs.getString("descripcion")};
                            modelo.addRow(fila);
                        }
                    }
                } else {
                    rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
                    while (rs.next()) {
                        String fila[] = {rs.getString("id"), rs.getString("telefono"), rs.getString("descripcion")};
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

            rs = stmt.executeQuery("SELECT * FROM telefono_cliente");
            while (rs.next()) {
                String id = rs.getString("id");
                String telefono = rs.getString("telefono");
                String descripcion = rs.getString("descripcion");
                String fila[] = {id, telefono, descripcion};
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
