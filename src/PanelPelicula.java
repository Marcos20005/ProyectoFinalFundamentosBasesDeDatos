import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelPelicula extends JPanel{
  //Objetos de conexion SQL
    Statement stmt = null;
    Connection con = null;

    //Clase interna para manejar los ítems del combo box
        class Item {
        int id;
        String nombre;

        public Item(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre; // Lo que se muestra en el combo
        }
    }

    public PanelPelicula(MantenimientoPelicula controlOriginal, int funcion, String codigo) throws SQLException, ClassNotFoundException {
           

        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
                "root", "cRojas34");
        stmt = con.createStatement();

        if(funcion==0){
 JLabel lBlcodigo = crearEtiqueta("Datos de nueva pelicula", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);
        }else{
             JLabel lBlcodigo = crearEtiqueta("Actualizar registro", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);
        }
       
        JLabel lblCodigo = crearEtiqueta("codigo de pelicula:", 150, 70, 140, 30);
        JTextField txtCodigo = crearCampoTexto(300, 70, 200, 30, "Ingrese codigo de pelicula");
        this.add(lblCodigo);
        this.add(txtCodigo);

      
        JLabel lblTitulo = crearEtiqueta("Ingrese el titulo de la pelicula:", 100, 120, 200, 30);
        JTextField txtTitulo = crearCampoTexto(300, 120, 200, 30, "Ingrese titulo de la pelicula");
        this.add(lblTitulo);
        this.add(txtTitulo);

        JLabel lblGenero = crearEtiqueta("Género:", 150, 220, 140, 30);
        JComboBox<Item> comboGenero = new JComboBox<>();
        comboGenero.setBounds(300, 220, 200, 30);
        this.add(lblGenero);
        this.add(comboGenero);

        JLabel lblDuracion = crearEtiqueta("Duracion de la pelicula:", 150, 170, 140, 30);
        JTextField txtDuracion = crearCampoTexto(300, 170, 200, 30, "Ingrese la duracion de la pelicula");
        this.add(lblDuracion);
        this.add(txtDuracion);

        JLabel lblClasificacion = crearEtiqueta("Clasificación:", 150, 270, 140, 30);
        JComboBox<Item> comboClasificacion = new JComboBox<>();
        comboClasificacion.setBounds(300, 270, 200, 30);
        this.add(lblClasificacion);
        this.add(comboClasificacion);
        //Metodo para llenar los combobox de genero y clasificacion
        cargarCatalogos(comboGenero, comboClasificacion);


                  JButton botonCancelar = crearBoton("Cancelar", 420, 320, 100, 40, "Regresar atras", "Iconos/cancelar.png");
botonCancelar.setBackground(new Color(240, 128, 128));
botonCancelar.setForeground(Color.WHITE);
this.add(botonCancelar);
botonCancelar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e){

                controlOriginal.add(controlOriginal.botonActualizar);
                controlOriginal.add(controlOriginal.botonInsertar);
                controlOriginal.add(controlOriginal.botonEliminar);
                controlOriginal.add(controlOriginal.botonConsultar);
                controlOriginal.add(controlOriginal.scroll);
                controlOriginal.remove(controlOriginal.panel);

                
                controlOriginal.recargarTabla();
                controlOriginal.revalidate();
                controlOriginal.repaint();
    }
});

        if (funcion == 1 && codigo != null && !codigo.isEmpty()) {
            CallableStatement buscar = con.prepareCall("{CALL buscar_pelicula(?)}");
            buscar.setString(1, codigo);
            ResultSet rs = buscar.executeQuery();
            if (rs.next()) {
                txtCodigo.setText(rs.getString("codigo"));
                txtTitulo.setText(rs.getString("titulo"));
                txtDuracion.setText(rs.getString("duracion"));
                String generoActual = rs.getString("nombre_genero");
                String clasificacionActual = rs.getString("tipo_clasificacion");

            comboGenero.setSelectedItem(generoActual);
            comboClasificacion.setSelectedItem(clasificacionActual);
                txtCodigo.setEditable(false);
            }
        }
       
        JButton botonGuardar = crearBoton("Guardar", 300, 320, 100, 40, "Guardar nuevo teléfono", "Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CallableStatement stmt = null;
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        if(funcion == 0 ){
                            stmt = con.prepareCall("{CALL insertar_pelicula_mantenimiento(?,?,?,?,?)}");
                        }else{
                            stmt = con.prepareCall("{CALL actualizar_pelicula(?,?,?,?,?)}");
                        }

                        Item genero = (Item) comboGenero.getSelectedItem();
                        Item clasificacion = (Item) comboClasificacion.getSelectedItem();

                        stmt.setString(1, txtCodigo.getText());
                        stmt.setString(2, txtTitulo.getText());
                        stmt.setInt(3, genero.id);
                        stmt.setString(4, txtDuracion.getText());
                        stmt.setInt(5, clasificacion.id);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Operación realizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException e1) {
                  JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

                txtCodigo.setText("");
                txtTitulo.setText("");
                comboGenero.setSelectedIndex(0);
                txtDuracion.setText("");
                comboClasificacion.setSelectedIndex(0);

                  
                controlOriginal.add(controlOriginal.botonActualizar);
                controlOriginal.add(controlOriginal.botonInsertar);
                controlOriginal.add(controlOriginal.botonEliminar);
                controlOriginal.add(controlOriginal.botonConsultar);
                controlOriginal.add(controlOriginal.scroll);
                controlOriginal.remove(controlOriginal.panel);

                
                controlOriginal.recargarTabla();
                controlOriginal.revalidate();
                controlOriginal.repaint();
            }

        });

    }

    private void cargarCatalogos(JComboBox<Item> comboGenero, JComboBox<Item> comboClasificacion) {
        try {
        CallableStatement stmtGenero = con.prepareCall("{CALL listar_catalogo_genero()}");
        ResultSet rsGenero = stmtGenero.executeQuery();
        while (rsGenero.next()) {
            int id = rsGenero.getInt("id_genero");
            String nombre = rsGenero.getString("nombre_genero");
            comboGenero.addItem(new Item(id, nombre));
        }

        CallableStatement stmtClasif = con.prepareCall("{CALL listar_catalogo_clasificacion()}");
        ResultSet rsClasif = stmtClasif.executeQuery();
        while (rsClasif.next()) {
            int id = rsClasif.getInt("id_clasificacion");
            String nombre = rsClasif.getString("tipo_clasificacion");
            comboClasificacion.addItem(new Item(id, nombre));
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
