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

public class PanelSala extends JPanel{
  //Objetos de conexion SQL
    Statement stmt = null;
    Connection con = null;
    public PanelSala(MantenimientoSala controlOriginal,int funcion) throws SQLException, ClassNotFoundException {
           //MantenimientoSala control = controlOriginal;

        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
                "root", "cRojas34");
        stmt = con.createStatement();

        if(funcion==0){
            JLabel lblTitulo = crearEtiqueta("Datos de nueva sala", 200, 20, 300, 30);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo);
        }else{
            JLabel lblTitulo = crearEtiqueta("Actualizar registro", 200, 20, 300, 30);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo);
        }
        

       
        JLabel lblNumeroSala = crearEtiqueta("Numero de Sala:", 150, 70, 140, 30);
        JTextField txtNumeroSala = crearCampoTexto(300, 70, 200, 30, "Ingrese numero de sala");
        this.add(lblNumeroSala);
        this.add(txtNumeroSala);

      
        JLabel lblCapacidad = crearEtiqueta("Ingrese la capacidad de la sala:", 100, 120, 200, 30);
        JTextField txtCapacidad = crearCampoTexto(300, 120, 200, 30, "Ingrese capacidad de la sala");
        this.add(lblCapacidad);
        this.add(txtCapacidad);

        JLabel lbliDtipo = crearEtiqueta("ID de tipo de sala:", 150, 170, 140, 30);
        JTextField txtIdTipo = crearCampoTexto(300, 170, 200, 30, "Ingrese el ID del tipo de la sala");
        this.add(lbliDtipo);
        this.add(txtIdTipo);
        
          JButton botonCancelar = crearBoton("Cancelar", 420, 220, 100, 40, "Regresar atras", "Iconos/cancelar.png");
botonCancelar.setBackground(new Color(240, 128, 128));
botonCancelar.setForeground(Color.WHITE);
this.add(botonCancelar);
botonCancelar.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e){
  txtNumeroSala.setText("");
                txtCapacidad.setText("");
                txtIdTipo.setText("");
              
                  
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
       
        JButton botonGuardar = crearBoton("Guardar", 300, 220, 100, 40, "Guardar nuevo teléfono", "Iconos/guardar-el-archivo.png");
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
                     
                        stmt.executeUpdate("INSERT INTO sala (numero_sala, capacidad, id_tipo) VALUES ('"
                                        + txtNumeroSala.getText() + "', '" + Integer.parseInt(txtCapacidad.getText()) + "', '"
                                        + txtIdTipo.getText() + "');");
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

              
                txtNumeroSala.setText("");
                txtCapacidad.setText("");
                txtIdTipo.setText("");
              
                  
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
