import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class MantenimientoCatalogoPuesto extends JPanel {

    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;
    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    PanelCatalogoPuesto panel;

    public MantenimientoCatalogoPuesto() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                    "root", "erpalacios");
            stmt = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JLabel label = new JLabel("Mantenimiento de tabla catalogo puesto");
        label.setBounds(200, 20, 280, 30); this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"ID Puesto", "Nombre Puesto"};
        DefaultTableModel modelo = new DefaultTableModel(columnas,0);
        tabla.setModel(modelo);

        try {
            rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
            while(rs.next()) modelo.addRow(new String[]{rs.getString("id_puesto"), rs.getString("nombre_puesto")});
        } catch(SQLException e){ e.printStackTrace(); }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30,70,600,200); this.add(scroll);

        // Botones
        botonInsertar = PanelCatalogoPuesto.crearBoton("Insertar", 40,360,100,40,"Insertar nuevo puesto","Iconos/insertar-cuadrado.png");
        botonInsertar.setBackground(new Color(46,204,113)); botonInsertar.setForeground(Color.WHITE); this.add(botonInsertar);
        botonActualizar = PanelCatalogoPuesto.crearBoton("Actualizar", 200,360,100,40,"Actualizar puesto existente","Iconos/boton-editar.png");
        botonActualizar.setBackground(new Color(255,179,71)); botonActualizar.setForeground(Color.WHITE); this.add(botonActualizar);
        botonEliminar = PanelCatalogoPuesto.crearBoton("Eliminar", 360,360,100,40,"Eliminar puesto existente","Iconos/eliminar.png");
        botonEliminar.setBackground(new Color(240,128,128)); botonEliminar.setForeground(Color.WHITE); this.add(botonEliminar);
        botonConsultar = PanelCatalogoPuesto.crearBoton("Consultar", 520,360,100,40,"Consultar puesto existente","Iconos/buscar.png");
        botonConsultar.setBackground(new Color(135,206,250)); botonConsultar.setForeground(Color.WHITE); this.add(botonConsultar);

    

        JTextField campoActualizar = PanelCatalogoPuesto.crearCampoTexto(200,410,100,30,"Ingrese ID a actualizar"); this.add(campoActualizar);
        JTextField campoEliminar = PanelCatalogoPuesto.crearCampoTexto(360,410,100,30,"Ingrese ID a eliminar"); this.add(campoEliminar);
        JTextField campoConsultar = PanelCatalogoPuesto.crearCampoTexto(520,410,100,30,"Ingrese ID a consultar"); this.add(campoConsultar);

        // Funcionalidad botones
        // Insertar
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll); this.remove(botonInsertar); this.remove(botonActualizar);
                this.remove(botonEliminar); this.remove(botonConsultar);
               // this.remove(campoActualizar); this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCatalogoPuesto(MantenimientoCatalogoPuesto.this,0);
                panel.setLayout(null); panel.setBounds(10,70,700,500);
                this.add(panel); this.setComponentZOrder(panel,0); this.revalidate(); this.repaint();
            } catch(Exception ex){ ex.printStackTrace(); }
        });

        // Actualizar
        botonActualizar.addActionListener(e -> {
            modelo.setRowCount(0); boolean encontrado=false;
            if(tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
                rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
                while(rs.next()){ if(rs.getString("id_puesto").equals(campoActualizar.getText())) encontrado=true; }

                if(encontrado){
                   try {
                this.remove(scroll); this.remove(botonInsertar); this.remove(botonActualizar);
                this.remove(botonEliminar); this.remove(botonConsultar);
              // this.remove(campoActualizar); this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCatalogoPuesto(MantenimientoCatalogoPuesto.this,1);
                panel.setLayout(null); panel.setBounds(10,70,700,500);
                this.add(panel); this.setComponentZOrder(panel,0); this.revalidate(); this.repaint();
            } catch(Exception ex){ ex.printStackTrace(); }
                } else JOptionPane.showMessageDialog(null,"Registro no encontrado");

                rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
                while(rs.next()) modelo.addRow(new String[]{rs.getString("id_puesto"), rs.getString("nombre_puesto")});
            } catch(SQLException ex){ ex.printStackTrace(); }
            campoActualizar.setText(""); 
        });

        // Eliminar
        botonEliminar.addActionListener(e -> {
            modelo.setRowCount(0); boolean encontrado=false;
            if(tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try{
                rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
                while(rs.next()){ if(rs.getString("id_puesto").equals(campoEliminar.getText())) encontrado=true; }

                if(encontrado){
                    int eleccion = JOptionPane.showConfirmDialog(null,"¿Desea eliminar este registro?","Confirmar",JOptionPane.YES_NO_OPTION);
                    if(eleccion==0) stmt.executeUpdate("DELETE FROM catalogo_puesto WHERE id_puesto='"+campoEliminar.getText()+"';");
                } else JOptionPane.showMessageDialog(null,"Registro no encontrado");

                rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
                while(rs.next()) modelo.addRow(new String[]{rs.getString("id_puesto"), rs.getString("nombre_puesto")});
            } catch(SQLException ex){ ex.printStackTrace(); }
            campoEliminar.setText("");
        });

        // Consultar
        botonConsultar.addActionListener(e -> {
            modelo.setRowCount(0); boolean encontrado=false;
            if(tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try{
                if(!campoConsultar.getText().isEmpty()){
                    rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
                    while(rs.next()){
                        if(rs.getString("id_puesto").equals(campoConsultar.getText())){
                            encontrado=true;
                            modelo.addRow(new String[]{rs.getString("id_puesto"), rs.getString("nombre_puesto")});
                        }
                    }
                    if(!encontrado){ JOptionPane.showMessageDialog(null,"Registro no encontrado"); recargarTabla(); }
                } else recargarTabla();
            } catch(SQLException ex){ ex.printStackTrace(); }
            campoConsultar.setText("");
        });
    }

    public void recargarTabla(){
        try{
            DefaultTableModel modelo = (DefaultTableModel)((JTable)((JScrollPane)scroll).getViewport().getView()).getModel();
            modelo.setRowCount(0);
            rs = stmt.executeQuery("SELECT * FROM catalogo_puesto");
            while(rs.next()) modelo.addRow(new String[]{rs.getString("id_puesto"), rs.getString("nombre_puesto")});
        } catch(SQLException e){ e.printStackTrace(); }
    }
}


