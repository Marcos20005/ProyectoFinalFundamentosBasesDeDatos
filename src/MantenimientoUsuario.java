
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;

public class MantenimientoUsuario extends JPanel {

//Objetos de conexion SQL
    //Statement stmt = null;
    Connection con = null;
    CallableStatement stmt = null;

    ResultSet rs = null;
    JButton botonInsertar;
    JButton botonActualizar;
    JButton botonEliminar;
    JButton botonConsultar;
    JScrollPane scroll;
    PanelUsuario panel;

    public MantenimientoUsuario() throws ClassNotFoundException, SQLException {

//Estableciendo conexion a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "cRojas34");

        JLabel label = new JLabel("Mantenimiento de tabala usuario");
        label.setBounds(200, 20, 200, 30);
        this.add(label);
        JTable tabla = new JTable();
        Object columnas[] = {"ID", "Primer Nombre", "Segundo Nombre", "Primer Apellido", "Segundo Apellido", "Usuario", "Contraseña"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);
        stmt = (CallableStatement) con.prepareCall("{CALL listar_usuario()}");
        


            rs = stmt.executeQuery();
        while (rs.next()) {
            //Captura los datos de la base de datos
            String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);

             
        }

         scroll = new JScrollPane(tabla);
        scroll.setBounds(10, 70, 700, 200);
        this.add(scroll);

        //Creacion de botones insertar, actualizar, eliminar y consultar
     botonInsertar = crearBoton("Insertar", 40, 360, 100, 40, "Insertar nuevo usuario", "Iconos/insertar-cuadrado.png");
        botonInsertar.setBackground(new Color(46, 204, 113)); 
         botonInsertar.setForeground(Color.WHITE);            
        this.add(botonInsertar);
        

         botonActualizar = crearBoton("Actualizar", 200, 360, 100, 40, "Actualizar usuario existente", "Iconos/boton-editar.png");
        botonActualizar.setBackground(new Color(255, 179, 71));
        botonActualizar.setForeground(Color.WHITE);
        this.add(botonActualizar);

        

        JTextField campoActualizar = crearCampoTexto(200,410,100,30,"Ingrese cedula de registro a actualizar");
        this.add(campoActualizar);



        botonActualizar.addActionListener(new ActionListener() {
          @Override
            public void actionPerformed(ActionEvent e){
                modelo.setRowCount(0);
                boolean encontrado = false;
                  if (tabla.isEditing()) {
    tabla.getCellEditor().stopCellEditing(); 
}

                int eleccion =0;
          if (campoActualizar.getText().equals("")) {
            try {
                rs = stmt.executeQuery();
                while(rs.next()){
                    
                     String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
                     

                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            }else{
          try {
                      rs = stmt.executeQuery();
                         while(rs.next()){
                            if(rs.getString("cedula").equals(campoActualizar.getText())){
                              encontrado=true;
                        
                            }
                           
                         }
                      
                         if(encontrado==true){

                           MantenimientoUsuario.this.remove(scroll);
                MantenimientoUsuario.this.remove(botonInsertar);
                MantenimientoUsuario.this.remove(botonActualizar);
                MantenimientoUsuario.this.remove(botonEliminar);
                MantenimientoUsuario.this.remove(botonConsultar);
                 

                panel = new PanelUsuario(MantenimientoUsuario.this,1);
                panel.setLayout(null);
                 panel.setBounds(10, 70, 700, 500);
                 MantenimientoUsuario.this.add(panel);
                  MantenimientoUsuario.this.setComponentZOrder(panel, 0); 
                 MantenimientoUsuario.this.revalidate();
                 MantenimientoUsuario.this.repaint();

                         
                         }else{
                            JOptionPane.showMessageDialog(null, "No se encontro el registro buscado");
                         }
                       
                     
                
                      rs = stmt.executeQuery();
                    while(rs.next()){
                                           
                        //Captura los datos de la base de datos
                         String cedula = rs.getString("cedula");
             String primerNombre = rs.getString("nombre1");
             String segundoNombre = rs.getString("nombre2");
             String primerApellido = rs.getString("apellido1");
             String segundoApellido = rs.getString("apellido2");
             String usuario = rs.getString("login");
             String contraseña = rs.getString("clave");
             String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
             modelo.addRow(fila);
                    }
           
                    
                    } catch (SQLException ex) {
                 } catch (ClassNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                  campoActualizar.setText("");
            }
           
            
        });

         botonEliminar = crearBoton("Eliminar", 360, 360, 100, 40, "Eliminar usuario existente", "Iconos/eliminar.png");
        botonEliminar.setBackground(new Color(240, 128, 128));
        botonEliminar.setForeground(Color.WHITE);
        this.add(botonEliminar);

        JTextField campoEliminar = crearCampoTexto(360, 410, 100, 30, "Ingrese cedula de registro a actualizar");
        this.add(campoEliminar);

        botonEliminar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                  if (tabla.isEditing()) {
    tabla.getCellEditor().stopCellEditing();
}

             modelo.setRowCount(0);
             boolean encontrado = false;
             int eleccion = 0;
              if (campoEliminar.getText().equals("")) {
               

            try {
                 rs = stmt.executeQuery();
                while(rs.next()){
                    
                     String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
                     

                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            }else{
                
                 try {
                      rs = stmt.executeQuery();
                     while(rs.next()){
                      if (rs.getString("cedula").equals(campoEliminar.getText())) {
                          encontrado=true;
                      }
                     }
                  
                      if (encontrado==true) {
                          eleccion=JOptionPane.showConfirmDialog(null, "¿Desea confirmar la eliminacion del registro?","Confirmacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            
                         
                          if(eleccion==0){
  stmt.executeUpdate("DELETE FROM usuario WHERE cedula = '"+campoEliminar.getText()+"';");
                            }
                        }else{
                        JOptionPane.showMessageDialog(null, "Registro no encontrado");
                      }
                  
                     rs = stmt.executeQuery();
                     while(rs.next()){
                       
                        //Captura los datos de la base de datos 
                        String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
                         
                     }} catch (SQLException ex) {
                 }

            }
          campoEliminar.setText("");
            }
        });

         botonConsultar = crearBoton("Consultar", 520, 360, 100, 40, "Consultar usuario existente", "Iconos/buscar.png");
        botonConsultar.setBackground(new Color(135, 206, 250));
        botonConsultar.setForeground(Color.WHITE);
        this.add(botonConsultar);
        
        JTextField campoConsultar = crearCampoTexto(520,410,100,30,"Ingrese cedula de registro a consultar");
        this.add(campoConsultar);

        botonConsultar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
              modelo.setRowCount(0);
              boolean encontrado = false;
            if (!campoConsultar.getText().equals("")) {
               

            try {
                 rs = stmt.executeQuery();
                while(rs.next()){
                     if(rs.getString("cedula").equals(campoConsultar.getText())){
                      encontrado=true;
                     String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
                     }
            
                }
                if (encontrado==false) {
                 JOptionPane.showMessageDialog(null, "Registro no encontrado");   
                   rs = stmt.executeQuery();
                while(rs.next()){
                    
                     
                     String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
                     
            
                }   
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            }else{
                try {
                 rs = stmt.executeQuery();
                while(rs.next()){
                    
                     String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");
            

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
                     }

                } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            }
            campoConsultar.setText("");

            }

           
        });
      botonInsertar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
             
             try {
                MantenimientoUsuario.this.remove(scroll);
                MantenimientoUsuario.this.remove(botonInsertar);
                MantenimientoUsuario.this.remove(botonActualizar);
                MantenimientoUsuario.this.remove(botonEliminar);
                MantenimientoUsuario.this.remove(botonConsultar);
                 

                panel = new PanelUsuario(MantenimientoUsuario.this,0);
                panel.setLayout(null);
                 panel.setBounds(10, 70, 700, 500);
                 MantenimientoUsuario.this.add(panel);
                  MantenimientoUsuario.this.setComponentZOrder(panel, 0); 
                 MantenimientoUsuario.this.revalidate();
                 MantenimientoUsuario.this.repaint();

             } catch (ClassNotFoundException | SQLException e1) {
              
                e1.printStackTrace();
             } 
            }
        });

    }
    // Metodo para volver a cargar tabla
    public void recargarTabla() {
    try {
        DefaultTableModel modelo = (DefaultTableModel) ((JTable) ((JScrollPane) scroll).getViewport().getView()).getModel();
        modelo.setRowCount(0); 

         rs = stmt.executeQuery();
        while (rs.next()) {
            String cedula = rs.getString("cedula");
            String primerNombre = rs.getString("nombre1");
            String segundoNombre = rs.getString("nombre2");
            String primerApellido = rs.getString("apellido1");
            String segundoApellido = rs.getString("apellido2");
            String usuario = rs.getString("login");
            String contraseña = rs.getString("clave");

            String fila[] = {cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, usuario, contraseña};
            modelo.addRow(fila);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    //Metodo declarado para crear botones
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

      //Metodo declarado para crear campos de texto
    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(x, y, ancho, alto);
        campoTexto.setToolTipText(toolTip);
        return campoTexto;
    }

    //Metodo declarado para crear etiquetas
    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }
}
