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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

public class MantenimientoCatalogoGenero extends JPanel {

    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;
    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    JComboBox<Object> combo;
    PanelCatalogoGenero panel;

    public MantenimientoCatalogoGenero() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                    "root", "cRojas34");
            stmt = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JLabel label = new JLabel("Mantenimiento de tabla catalogo genero");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"ID Genero", "Nombre Genero"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        try {
            rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
            while (rs.next()) {
                String id = rs.getString("id_genero");
                String nombre = rs.getString("nombre_genero");
                modelo.addRow(new String[]{id, nombre});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 600, 200);
        this.add(scroll);

        // Botones
        botonInsertar = crearBoton("Insertar", 40, 300, 100, 40, "Insertar nuevo genero", "src/imagenes/insertar.png");
        botonInsertar.setBackground(new Color(46, 204, 113));
        botonInsertar.setForeground(Color.WHITE);
        this.add(botonInsertar);

        botonActualizar = crearBoton("Actualizar", 200, 300, 100, 40, "Actualizar genero existente", "src/imagenes/actualizar.png");
        botonActualizar.setBackground(new Color(255, 179, 71));
        botonActualizar.setForeground(Color.WHITE);
        this.add(botonActualizar);

        botonEliminar = crearBoton("Eliminar", 360, 300, 100, 40, "Eliminar genero existente", "src/imagenes/eliminar.png");
        botonEliminar.setBackground(new Color(240, 128, 128));
        botonEliminar.setForeground(Color.WHITE);
        this.add(botonEliminar);

        botonConsultar = crearBoton("Consultar", 520, 300, 100, 40, "Consultar genero existente", "src/imagenes/consultar.png");
        botonConsultar.setBackground(new Color(135, 206, 250));
        botonConsultar.setForeground(Color.WHITE);
        this.add(botonConsultar);

        // Combo para actualizar
        String lista[] = {"ID Genero", "Nombre Genero"};
        combo = new JComboBox<>(lista);
        combo.setBounds(200, 360, 140, 30);
        this.add(combo);

        
       
        JTextField campoActualizar = crearCampoTexto(200, 400, 140, 30, "Ingrese ID a actualizar");
        this.add(campoActualizar);
        JTextField campoValorActualizar = crearCampoTexto(200, 440, 140, 30, "Ingrese nuevo valor");
        this.add(campoValorActualizar);

        JTextField campoEliminar = crearCampoTexto(360,360,100,30, "Ingrese ID a eliminar");
        this.add(campoEliminar);

        JTextField campoConsultar = crearCampoTexto(520,360,100,30, "Ingrese ID a consultar");
        this.add(campoConsultar);

        // Accion Insertar
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar); this.remove(botonActualizar); this.remove(botonEliminar); this.remove(botonConsultar);
                this.remove(combo); this.remove(campoActualizar); this.remove(campoValorActualizar);
                this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCatalogoGenero(MantenimientoCatalogoGenero.this); 
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 300);
                this.add(panel);
                this.setComponentZOrder(panel, 0);
                this.revalidate(); this.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Hubo un error por favor vuelva a intentar",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Accion Actualizar
        botonActualizar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;
            if (tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
                rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                while (rs.next()) {
                    if (rs.getString("id_genero").equals(campoActualizar.getText())) { encontrado = true; }
                }
                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea guardar los cambios?", "Confirmar acción", JOptionPane.YES_NO_OPTION);
                    if (eleccion == 0) {
                        String columna = combo.getSelectedIndex() == 0 ? "id_genero" : "nombre_genero";
                        stmt.executeUpdate("UPDATE catalogo_genero SET " + columna + "='" + campoValorActualizar.getText() +
                                "' WHERE id_genero='" + campoActualizar.getText() + "';");
                    }
                } else { JOptionPane.showMessageDialog(null, "Registro no encontrado"); }

                rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                while (rs.next()) {
                    modelo.addRow(new String[]{rs.getString("id_genero"), rs.getString("nombre_genero")});
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
            campoActualizar.setText(""); campoValorActualizar.setText("");
        });

        // Accion Eliminar
        botonEliminar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;
            if (tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
                rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                while (rs.next()) { if (rs.getString("id_genero").equals(campoEliminar.getText())) { encontrado = true; } }
                if (encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (eleccion == 0) {
                        stmt.executeUpdate("DELETE FROM catalogo_genero WHERE id_genero='" + campoEliminar.getText() + "';");
                    }
                } else { JOptionPane.showMessageDialog(null, "Registro no encontrado"); }

                rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                while (rs.next()) {
                    modelo.addRow(new String[]{rs.getString("id_genero"), rs.getString("nombre_genero")});
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
            campoEliminar.setText("");
        });

        // Accion Consultar
        botonConsultar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;
            if (tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
                if (!campoConsultar.getText().isEmpty()) {
                    rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                    while (rs.next()) {
                        if (rs.getString("id_genero").equals(campoConsultar.getText())) {
                            encontrado = true;
                            modelo.addRow(new String[]{rs.getString("id_genero"), rs.getString("nombre_genero")});
                        }
                    }
                    if (!encontrado) { JOptionPane.showMessageDialog(null, "Registro no encontrado"); }
                } else {
                    rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                    while (rs.next()) {
                        modelo.addRow(new String[]{rs.getString("id_genero"), rs.getString("nombre_genero")});
                    }
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
            campoConsultar.setText("");
        });
    }

    public void recargarTabla(){
        try{
            DefaultTableModel modelo = (DefaultTableModel)((JTable)((JScrollPane)scroll).getViewport().getView()).getModel();
            modelo.setRowCount(0);
            rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
            while(rs.next()) modelo.addRow(new String[]{rs.getString("id_puesto"), rs.getString("nombre_puesto")});
        } catch(SQLException e){ e.printStackTrace(); }
    }

    // Métodos para crear componentes
    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto, String toolTip, String ruta) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setToolTipText(toolTip);
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        boton.setIcon(iconoOriginal);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        if (texto.equals("")) boton.setHorizontalAlignment(SwingConstants.CENTER);
        return boton;
    }

    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, ancho, alto);
        campo.setToolTipText(toolTip);
        return campo;
    }
}

