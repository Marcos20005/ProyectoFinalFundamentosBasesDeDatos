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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class MantenimientoTelefonoEmpleado extends JPanel {
    // Objetos de conexión SQL
    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;
    JButton botonInsertar;
    JButton botonActualizar;
    JButton botonEliminar;
    JButton botonConsultar;
    JScrollPane scroll;
    JComboBox<Object> combo;
    PanelTelefonoEmpleado panel;

    public MantenimientoTelefonoEmpleado() throws ClassNotFoundException, SQLException {
        // Estableciendo conexión a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");

        JLabel label = new JLabel("Mantenimiento de tabla teléfono empleado");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"ID", "Teléfono", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);
        stmt = con.createStatement();

        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
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

        String lista[] = {"ID", "Teléfono", "Descripción"};
        combo = new JComboBox<>(lista);
        combo.setBounds(200, 410, 140, 30);
        this.add(combo);

        JTextField campoActualizar = crearCampoTexto(200, 450, 100, 30, "Ingrese ID de registro a actualizar");
        this.add(campoActualizar);
        JTextField campoValorActualizar = crearCampoTexto(200, 490, 100, 30, "Ingrese nuevo valor");
        this.add(campoValorActualizar);

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.setRowCount(0);
                boolean encontrado = false;
                if (tabla.isEditing()) {
                    tabla.getCellEditor().stopCellEditing();
                }

                int eleccion = 0;
                if (campoActualizar.getText().equals("")) {
                    try {
                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String telefono = rs.getString("telefono");
                            String descripcion = rs.getString("descripcion");
                            String fila[] = {id, telefono, descripcion};
                            modelo.addRow(fila);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    String columnaParaActualizar = "";
                    switch (combo.getSelectedIndex()) {
                        case 0:
                            columnaParaActualizar = "id";
                            break;
                        case 1:
                            columnaParaActualizar = "telefono";
                            break;
                        case 2:
                            columnaParaActualizar = "descripcion";
                            break;
                    }

                    try {
                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            if (rs.getString("id").equals(campoActualizar.getText())) {
                                encontrado = true;
                            }
                        }

                        if (encontrado) {
                            eleccion = javax.swing.JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?",
                                    "Confirmar acción", javax.swing.JOptionPane.YES_NO_OPTION,
                                    javax.swing.JOptionPane.QUESTION_MESSAGE);
                            if (eleccion == 0) {
                                stmt.executeUpdate("UPDATE telefono_empleado SET " + columnaParaActualizar + "='"
                                        + campoValorActualizar.getText() + "' WHERE id='" + campoActualizar.getText()
                                        + "';");
                            }
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(null, "Registro no encontrado");
                        }

                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String telefono = rs.getString("telefono");
                            String descripcion = rs.getString("descripcion");
                            String fila[] = {id, telefono, descripcion};
                            modelo.addRow(fila);
                        }
                    } catch (SQLException ex) {
                    }
                }
                campoActualizar.setText("");
                campoValorActualizar.setText("");
            }
        });

        JTextField campoEliminar = crearCampoTexto(360, 410, 100, 30, "Ingrese ID de registro a eliminar");
        this.add(campoEliminar);

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabla.isEditing()) {
                    tabla.getCellEditor().stopCellEditing();
                }

                modelo.setRowCount(0);
                boolean encontrado = false;
                int eleccion = 0;
                if (campoEliminar.getText().equals("")) {
                    try {
                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String telefono = rs.getString("telefono");
                            String descripcion = rs.getString("descripcion");
                            String fila[] = {id, telefono, descripcion};
                            modelo.addRow(fila);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            if (rs.getString("id").equals(campoEliminar.getText())) {
                                encontrado = true;
                            }
                        }

                        if (encontrado) {
                            eleccion = javax.swing.JOptionPane.showConfirmDialog(null,
                                    "¿Desea confirmar la eliminación del registro?", "Confirmación",
                                    javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                            if (eleccion == 0) {
                                stmt.executeUpdate(
                                        "DELETE FROM telefono_empleado WHERE id='" + campoEliminar.getText() + "';");
                            }
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(null, "Registro no encontrado");
                        }

                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String telefono = rs.getString("telefono");
                            String descripcion = rs.getString("descripcion");
                            String fila[] = {id, telefono, descripcion};
                            modelo.addRow(fila);
                        }
                    } catch (SQLException ex) {
                    }
                }
                campoEliminar.setText("");
            }
        });

        JTextField campoConsultar = crearCampoTexto(520, 410, 100, 30, "Ingrese ID de registro a consultar");
        this.add(campoConsultar);

        botonConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.setRowCount(0);
                boolean encontrado = false;
                if (!campoConsultar.getText().equals("")) {
                    try {
                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            if (rs.getString("id").equals(campoConsultar.getText())) {
                                encontrado = true;
                                String id = rs.getString("id");
                                String telefono = rs.getString("telefono");
                                String descripcion = rs.getString("descripcion");
                                String fila[] = {id, telefono, descripcion};
                                modelo.addRow(fila);
                            }
                        }
                        if (!encontrado) {
                            javax.swing.JOptionPane.showMessageDialog(null, "Registro no encontrado");
                            rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                            while (rs.next()) {
                                String id = rs.getString("id");
                                String telefono = rs.getString("telefono");
                                String descripcion = rs.getString("descripcion");
                                String fila[] = {id, telefono, descripcion};
                                modelo.addRow(fila);
                            }
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
                        while (rs.next()) {
                            String id = rs.getString("id");
                            String telefono = rs.getString("telefono");
                            String descripcion = rs.getString("descripcion");
                            String fila[] = {id, telefono, descripcion};
                            modelo.addRow(fila);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                campoConsultar.setText("");
            }
        });

        botonInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MantenimientoTelefonoEmpleado.this.remove(scroll);
                    MantenimientoTelefonoEmpleado.this.remove(botonInsertar);
                    MantenimientoTelefonoEmpleado.this.remove(botonActualizar);
                    MantenimientoTelefonoEmpleado.this.remove(botonEliminar);
                    MantenimientoTelefonoEmpleado.this.remove(botonConsultar);
                    MantenimientoTelefonoEmpleado.this.remove(combo);

                    panel = new PanelTelefonoEmpleado(MantenimientoTelefonoEmpleado.this);
                    panel.setLayout(null);
                    panel.setBounds(10, 70, 700, 500);
                    MantenimientoTelefonoEmpleado.this.add(panel);
                    MantenimientoTelefonoEmpleado.this.setComponentZOrder(panel, 0);
                    MantenimientoTelefonoEmpleado.this.revalidate();
                    MantenimientoTelefonoEmpleado.this.repaint();

                } catch (ClassNotFoundException | SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    public void recargarTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) ((JTable) ((JScrollPane) scroll).getViewport().getView())
                    .getModel();
            modelo.setRowCount(0);

            rs = stmt.executeQuery("SELECT * FROM telefono_empleado");
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

