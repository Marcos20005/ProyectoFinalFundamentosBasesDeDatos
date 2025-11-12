
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class MantenimientoEmpleado extends JPanel {

    CallableStatement stmt = null;
    CallableStatement stmt1 = null;
    Connection con = null;
    ResultSet rs = null;
    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    PanelEmpleado panel;

    public MantenimientoEmpleado() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");
        stmt = con.prepareCall("{Call listar_empleado_mantenimiento}");

        JLabel label = new JLabel("Mantenimiento de tabla empleado");
        label.setBounds(200, 20, 250, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"ID", "Primer Nombre", "Segundo Nombre", "Primer Apellido", "Segundo Apellido", "ID Puesto"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        rs = stmt.executeQuery();
        while (rs.next()) {
            String id = rs.getString("id");
            String primerNombre = rs.getString("primer_nombre");
            String segundoNombre = rs.getString("segundo_nombre");
            String primerApellido = rs.getString("primer_apellido");
            String segundoApellido = rs.getString("segundo_apellido");
            String idPuesto = rs.getString("id_puesto");

            String fila[] = {id, primerNombre, segundoNombre, primerApellido, segundoApellido, idPuesto};
            modelo.addRow(fila);
        }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 200);
        this.add(scroll);

        // Botones
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

        // Campos de texto para actualizar, eliminar y consultar
        JTextField campoActualizar = crearCampoTexto(200, 410, 100, 30, "Ingrese Código a actualizar");
        this.add(campoActualizar);

        JTextField campoEliminar = crearCampoTexto(360, 410, 100, 30, "Ingrese Código a eliminar");
        this.add(campoEliminar);

        JTextField campoConsultar = crearCampoTexto(520, 410, 100, 30, "Ingrese Código a consultar");
        this.add(campoConsultar);
        // Acción Insertar
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar);
                this.remove(botonActualizar);
                this.remove(botonEliminar);
                this.remove(botonConsultar);
                // this.remove(campoActualizar);
                // this.remove(campoEliminar);
                // this.remove(campoConsultar);

                panel = new PanelEmpleado(MantenimientoEmpleado.this, 0, "");
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
                rs = stmt.executeQuery();
                while (rs.next()) {
                    if (rs.getString("id").equals(campoActualizar.getText())) {
                        encontrado = true;
                    }
                }
                if (encontrado) {
                    try {
                        this.remove(scroll);
                        this.remove(botonInsertar);
                        this.remove(botonActualizar);
                        this.remove(botonEliminar);
                        this.remove(botonConsultar);
                        //this.remove(campoActualizar);
                        // this.remove(campoEliminar);
                        //  this.remove(campoConsultar);

                        panel = new PanelEmpleado(MantenimientoEmpleado.this, 1, campoActualizar.getText());
                        panel.setLayout(null);
                        panel.setBounds(10, 70, 700, 500);
                        this.add(panel);
                        this.setComponentZOrder(panel, 0);
                        this.revalidate();
                        this.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }

                // Recargar tabla
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String primerNombre = rs.getString("primer_nombre");
                    String segundoNombre = rs.getString("segundo_nombre");
                    String primerApellido = rs.getString("primer_apellido");
                    String segundoApellido = rs.getString("segundo_apellido");
                    String idPuesto = rs.getString("id_puesto");
                    String fila[] = {id, primerNombre, segundoNombre, primerApellido, segundoApellido, idPuesto};
                    modelo.addRow(fila);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            campoActualizar.setText("");

        });

        // Acción Eliminar
        botonEliminar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;
            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }
            try {
                rs = stmt.executeQuery();
                while (rs.next()) {
                    if (rs.getString("id").equals(campoEliminar.getText())) {
                        encontrado = true;
                    }
                }
                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar la eliminación del registro?", "Confirmación",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt1 = con.prepareCall("{CALL eliminar_empleado(?)}");
                        stmt1.setString(1, campoEliminar.getText());
                        stmt1.executeUpdate();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }

                // Recargar tabla
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("id");
                    String primerNombre = rs.getString("primer_nombre");
                    String segundoNombre = rs.getString("segundo_nombre");
                    String primerApellido = rs.getString("primer_apellido");
                    String segundoApellido = rs.getString("segundo_apellido");
                    String idPuesto = rs.getString("id_puesto");
                    String fila[] = {id, primerNombre, segundoNombre, primerApellido, segundoApellido, idPuesto};
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
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        if (rs.getString("id").equals(campoConsultar.getText())) {
                            encontrado = true;
                            String fila[] = {rs.getString("id"), rs.getString("primer_nombre"), rs.getString("segundo_nombre"),
                                rs.getString("primer_apellido"), rs.getString("segundo_apellido"), rs.getString("id_puesto")};
                            modelo.addRow(fila);
                        }
                    }
                    if (!encontrado) {
                        JOptionPane.showMessageDialog(null, "Registro no encontrado");
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            String fila[] = {rs.getString("id"), rs.getString("primer_nombre"), rs.getString("segundo_nombre"),
                                rs.getString("primer_apellido"), rs.getString("segundo_apellido"), rs.getString("id_puesto")};
                            modelo.addRow(fila);
                        }
                    }
                } else {
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String fila[] = {rs.getString("id"), rs.getString("primer_nombre"), rs.getString("segundo_nombre"),
                            rs.getString("primer_apellido"), rs.getString("segundo_apellido"), rs.getString("id_puesto")};
                        modelo.addRow(fila);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            campoConsultar.setText("");
        });
    }

    public void recargarTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) ((JTable) ((JScrollPane) scroll).getViewport().getView()).getModel();
            modelo.setRowCount(0);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String fila[] = {rs.getString("id"), rs.getString("primer_nombre"), rs.getString("segundo_nombre"),
                    rs.getString("primer_apellido"), rs.getString("segundo_apellido"), rs.getString("id_puesto")};
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
