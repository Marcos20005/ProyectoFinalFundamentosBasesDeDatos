import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelPelicula extends JPanel{
  //Objetos de conexion SQL
    Statement stmt = null;
    Connection con = null;
    public PanelPelicula(MantenimientoPelicula controlOriginal) throws SQLException, ClassNotFoundException {
           

        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
                "root", "cRojas34");
        stmt = con.createStatement();

        JLabel lBlcodigo = crearEtiqueta("Datos de nueva pelicula", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);

       
        JLabel lblCodigo = crearEtiqueta("codigo de pelicula:", 150, 70, 140, 30);
        JTextField txtCodigo = crearCampoTexto(300, 70, 200, 30, "Ingrese codigo de pelicula");
        this.add(lblCodigo);
        this.add(txtCodigo);

      
        JLabel lblTitulo = crearEtiqueta("Ingrese el titulo de la pelicula:", 100, 120, 200, 30);
        JTextField txxTitulo = crearCampoTexto(300, 120, 200, 30, "Ingrese titulo de la pelicula");
        this.add(lblTitulo);
        this.add(txxTitulo);

        JLabel lblGenero = crearEtiqueta("ID de Genero:", 150, 170, 140, 30);
        JTextField txtGenero = crearCampoTexto(300, 170, 200, 30, "Ingrese el ID de Genero");
        this.add(lblGenero);
        this.add(txtGenero);

        JLabel lblDuracion = crearEtiqueta("Duracion de la pelicula:", 150, 220, 140, 30);
        JTextField txtDuracion = crearCampoTexto(300, 220, 200, 30, "Ingrese la duracion de la pelicula");
        this.add(lblDuracion);
        this.add(txtDuracion);

         JLabel lblIDclasificacion = crearEtiqueta("ID de Clasificacion de pelicula:", 100, 270, 200, 30);
        JTextField txtIDclasificacion = crearCampoTexto(300, 270, 200, 30, "Ingrese ID de Clasificacion de pelicula");
        this.add(lblIDclasificacion);
        this.add(txtIDclasificacion);
       
        JButton botonGuardar = crearBoton("Guardar", 300, 320, 100, 40, "Guardar nuevo teléfono", "Iconos/guardar.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                     
                        stmt.executeUpdate("INSERT INTO pelicula (codigo, titulo, id_genero,duracion,id_clasificacion) VALUES ('"
                                        + txtCodigo.getText() + "', '" + txxTitulo.getText() + "', '"
                                        + txtGenero.getText() + "', '" + txtDuracion.getText() + "', '"
                                        + txtIDclasificacion.getText() 
                                        + "');");
                    }
                } catch (SQLException e1) {
                  JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

              
                txtCodigo.setText("");
                txxTitulo.setText("");
                txtIDclasificacion.setText("");
              
                  
                controlOriginal.add(controlOriginal.botonActualizar);
                controlOriginal.add(controlOriginal.botonInsertar);
                controlOriginal.add(controlOriginal.botonEliminar);
                controlOriginal.add(controlOriginal.botonConsultar);
                controlOriginal.add(controlOriginal.scroll);
                controlOriginal.add(controlOriginal.combo);
                controlOriginal.remove(controlOriginal.panel);

                
                controlOriginal.recargarTabla();
                controlOriginal.revalidate();
                controlOriginal.repaint();
            }

        });

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
