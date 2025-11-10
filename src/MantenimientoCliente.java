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

public class MantenimientoCliente extends JPanel {
    CallableStatement stmt = null;
    CallableStatement stmt1 = null;
    Connection con = null;
    ResultSet rs = null;
    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    PanelCliente panel;

    public MantenimientoCliente() {
        try {
            setLayout(null);
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                    "root", "erpalacios");
            stmt = con.prepareCall("{CALL listar_cliente_mantenimiento()}");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JLabel label = new JLabel("Mantenimiento de tabla cliente");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"Cedula", "Primer Nombre", "Segundo Nombre", "Primer Apellido", "Segundo Apellido"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        try {
         stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
            rs = stmt.executeQuery();
            while (rs.next()) {
                modelo.addRow(new String[]{
                        rs.getString("cedula"),
                        rs.getString("primer_nombre"),
                        rs.getString("segundo_nombre"),
                        rs.getString("primer_apellido"),
                        rs.getString("segundo_apellido")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }

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


        JTextField campoActualizar = PanelCliente.crearCampoTexto(200, 410, 100, 30,"Ingrese Cedula a actualizar"); this.add(campoActualizar);
        JTextField campoEliminar = PanelCliente.crearCampoTexto(360,410,100,30,"Ingrese Cedula a eliminar"); this.add(campoEliminar);
        JTextField campoConsultar = PanelCliente.crearCampoTexto(520,410,100,30,"Ingrese Cedula a consultar"); this.add(campoConsultar);

        // Funcionalidad botones
        // Insertar
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll); this.remove(botonInsertar); this.remove(botonActualizar); this.remove(botonEliminar); this.remove(botonConsultar);
           //  this.remove(campoActualizar); this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCliente(MantenimientoCliente.this,0,"");
                panel.setLayout(null); panel.setBounds(10,70,700,500);
                this.add(panel); this.setComponentZOrder(panel,0); this.revalidate(); this.repaint();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        // Actualizar
        botonActualizar.addActionListener(e -> {
            modelo.setRowCount(0); boolean encontrado=false;
            if(tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
             stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
                rs = stmt.executeQuery();
                while(rs.next()) { if(rs.getString("cedula").equals(campoActualizar.getText())) { encontrado=true; } }

                if(encontrado) {
                    try {
                this.remove(scroll); this.remove(botonInsertar); this.remove(botonActualizar); this.remove(botonEliminar); this.remove(botonConsultar);
             //this.remove(campoActualizar); this.remove(campoEliminar); this.remove(campoConsultar);

                panel = new PanelCliente(MantenimientoCliente.this,1,campoActualizar.getText());
                panel.setLayout(null); panel.setBounds(10,70,700,500);
                this.add(panel); this.setComponentZOrder(panel,0); this.revalidate(); this.repaint();
            } catch (Exception ex) { ex.printStackTrace(); }
                } else { JOptionPane.showMessageDialog(null,"Registro no encontrado"); }
           stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
                rs = stmt.executeQuery();
                while(rs.next()) { modelo.addRow(new String[]{rs.getString("cedula"),rs.getString("primer_nombre"),rs.getString("segundo_nombre"),rs.getString("primer_apellido"),rs.getString("segundo_apellido")}); }
            } catch(SQLException ex){ ex.printStackTrace(); }
            campoActualizar.setText(""); 
        });

        // Eliminar
        botonEliminar.addActionListener(e -> {
            modelo.setRowCount(0); boolean encontrado=false;
            if(tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
                stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
                rs = stmt.executeQuery();
                while(rs.next()) { if(rs.getString("cedula").equals(campoEliminar.getText())) encontrado=true; }
                if(encontrado) {
                    int eleccion = JOptionPane.showConfirmDialog(null,"¿Desea eliminar este registro?","Confirmar",JOptionPane.YES_NO_OPTION);
                    if(eleccion==0){
                        stmt1 = (CallableStatement) con.prepareCall("{CALL eliminar_cliente(?)}");
                    stmt1.setString(1, campoEliminar.getText());
                    stmt1.executeUpdate();
                    }
                   

                } else { JOptionPane.showMessageDialog(null,"Registro no encontrado"); }
                stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
                rs = stmt.executeQuery();
                while(rs.next()) { modelo.addRow(new String[]{rs.getString("cedula"),rs.getString("primer_nombre"),rs.getString("segundo_nombre"),rs.getString("primer_apellido"),rs.getString("segundo_apellido")}); }
            } catch(SQLException ex){ ex.printStackTrace(); }
            campoEliminar.setText("");
        });

        // Consultar
        botonConsultar.addActionListener(e -> {
            modelo.setRowCount(0); boolean encontrado=false;
            if(tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
            try {
                if(!campoConsultar.getText().isEmpty()) {
                   stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
                    rs = stmt.executeQuery();
                    while(rs.next()) {
                        if(rs.getString("cedula").equals(campoConsultar.getText())) {
                            encontrado=true;
                            modelo.addRow(new String[]{rs.getString("cedula"),rs.getString("primer_nombre"),rs.getString("segundo_nombre"),rs.getString("primer_apellido"),rs.getString("segundo_apellido")});
                        }
                    }
                    if(!encontrado){ JOptionPane.showMessageDialog(null,"Registro no encontrado"); recargarTabla(); }
                } else recargarTabla();
            } catch(SQLException ex){ ex.printStackTrace(); }
            campoConsultar.setText("");
        });
    }

    public void recargarTabla() {
            try {
        DefaultTableModel modelo = (DefaultTableModel)((JTable)((JScrollPane)scroll).getViewport().getView()).getModel();
        modelo.setRowCount(0);
        stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
        rs = stmt.executeQuery();
        while (rs.next()) {
            modelo.addRow(new String[]{
                rs.getString("cedula"),
                rs.getString("primer_nombre"),
                rs.getString("segundo_nombre"),
                rs.getString("primer_apellido"),
                rs.getString("segundo_apellido")
            });
        }
        revalidate();
        repaint();
    } catch(SQLException e){ e.printStackTrace(); }
}
    
    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto, String toolTip, String ruta) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setToolTipText(toolTip);
        ImageIcon icono = new ImageIcon(ruta);
        boton.setIcon(icono);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        if (texto.equals("")) { boton.setHorizontalAlignment(SwingConstants.CENTER); }
        return boton;
    }

    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, ancho, alto);
        campo.setToolTipText(toolTip);
        return campo;
    }
}
