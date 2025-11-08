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

        
       
        JTextField campoActualizar = crearCampoTexto(200, 410, 100, 30, "Ingrese ID a actualizar");
        this.add(campoActualizar);

        JTextField campoEliminar = crearCampoTexto(360,410,100,30, "Ingrese ID a eliminar");
        this.add(campoEliminar);

        JTextField campoConsultar = crearCampoTexto(520,410,100,30, "Ingrese ID a consultar");
        this.add(campoConsultar);

        // Accion Insertar
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar); this.remove(botonActualizar); this.remove(botonEliminar); this.remove(botonConsultar);
             //   this.remove(campoActualizar);
               // this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCatalogoGenero(MantenimientoCatalogoGenero.this,0); 
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 400);
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
                 try {
                this.remove(scroll);
                this.remove(botonInsertar); this.remove(botonActualizar); this.remove(botonEliminar); this.remove(botonConsultar);
                //this.remove(campoActualizar);
                //this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCatalogoGenero(MantenimientoCatalogoGenero.this,1); 
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 400);
                this.add(panel);
                this.setComponentZOrder(panel, 0);
                this.revalidate(); this.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Hubo un error por favor vuelva a intentar",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
                } else { JOptionPane.showMessageDialog(null, "Registro no encontrado"); }

                rs = stmt.executeQuery("SELECT * FROM catalogo_genero");
                while (rs.next()) {
                    modelo.addRow(new String[]{rs.getString("id_genero"), rs.getString("nombre_genero")});
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
            campoActualizar.setText(""); 
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
            while(rs.next()) modelo.addRow(new String[]{rs.getString("id_genero"), rs.getString("nombre_genero")});
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

