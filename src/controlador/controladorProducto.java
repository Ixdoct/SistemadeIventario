package controlador;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.producto;
import modelo.productoDado;
import vista.interfaz;

public class controladorProducto implements ActionListener {

    //Instancias
    producto Producto = new producto();
    productoDado Productodado = new productoDado();
    interfaz vista = new interfaz();
    DefaultTableModel modeloTabla = new DefaultTableModel();

    //Variables Globales
    private int Codigo;
    private String Nombre;
    private String Marca;
    private double Precio;
    private int Cantidad;

    public controladorProducto(interfaz vista) {
        this.vista = vista;
        vista.setVisible(true);
        agregarEventos();
        listarTabla();
    }

    private void agregarEventos() {
        vista.getBtnAgregar().addActionListener(this);
        vista.getBtnBorrar().addActionListener(this);
        vista.getBtnActualizar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getTblTabla().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                llenarCampos(e);
            }
        });
    }

    private void listarTabla() {
        String[] titulos = new String[]{"Codigo", "Nombre", "Marca", "Precio", "Cantidad"};
        modeloTabla = new DefaultTableModel(titulos, 0);
        List<producto> listaProductos = Productodado.listar();
        for (producto Producto : listaProductos) {
            modeloTabla.addRow(new Object[]{Producto.getCodigo(), Producto.getNombre(), Producto.getMarca(), Producto.getPrecio(), Producto.getCantidad()});
        }
        vista.getTblTabla().setModel(modeloTabla);
        vista.getTblTabla().setPreferredSize(new Dimension(1250, modeloTabla.getRowCount() * 760));
    }

    private void llenarCampos(MouseEvent e) {
        JTable target = (JTable) e.getSource();
        Codigo = (int) vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 0);
        vista.getTxtNombre().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 1).toString());
        vista.getTxtMarca().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 2).toString());
        vista.getTxtPrecio().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 3).toString());
        vista.getTxtCantidad().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 4).toString());
    }

    //--------------------------------------------------------------------- validar formulario
    private boolean validarDatos() {
        if ("".equals(vista.getTxtNombre().getText()) || "".equals(vista.getTxtMarca().getText()) || "".equals(vista.getTxtPrecio().getText()) || "".equals(vista.getTxtCantidad().getText())) {
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios", "Error", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }

    // Cargando datos
    private boolean cargarDatos() {
        try {
            Nombre = vista.getTxtNombre().getText();
            Marca = vista.getTxtMarca().getText();
            Precio = Double.parseDouble(vista.getTxtPrecio().getText());
            Cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Los campos Precio e Inventario deben ser numericos", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al cargar Datos" + e);
            return false;
        }
    }

    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtMarca().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtCantidad().setText("");
        Codigo = 0;
        Nombre = "";
        Marca = "";
        Precio = 0;
        Cantidad = 0;

    }

    //--------------------------------------------------------------
    private void agregarProducto() {
        try {
            if (validarDatos()) {
                if (cargarDatos()) {
                    producto Producto = new producto(Nombre, Marca, Precio, Cantidad);
                    Productodado.agregar(Producto);
                    JOptionPane.showMessageDialog(null, "Registro Exitoso");
                    limpiarCampos();
                }
            }
        } catch (HeadlessException e) {
            System.out.println("Error al AgregaC:r" + e);
        } finally {
            listarTabla();
        }
    }

    private void actualizarProducto() {
        try {
            if (validarDatos()) {
                if (cargarDatos()) {
                    producto Producto = new producto(Codigo, Nombre, Marca, Precio, Cantidad);
                    Productodado.actualizar(Producto);
                    JOptionPane.showMessageDialog(null, "Registro Actualizado");
                    limpiarCampos();
                }
            }
        } catch (HeadlessException e) {
            System.out.println("Error en ActualizarC:" + e);
        } finally {
            listarTabla();
        }
    }

    private void borrarProducto() {
        try {
            if (Codigo != 0) {
                Productodado.borrar(Codigo);
                JOptionPane.showMessageDialog(null, "Registro Borrado");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException e) {
            System.out.println("Error en borrarC:" + e);
        } finally {
            listarTabla();
        }
    }

    private void buscar() {
        Productodado.Buscar(vista.txtValorbusqueda.getText(), vista.cboxFiltro.getSelectedItem().toString(), vista.tblTabla);
        
    }

    //Dar acciones a los botones
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == vista.getBtnAgregar()) {
            agregarProducto();
        }

        if (ae.getSource() == vista.getBtnActualizar()) {
            actualizarProducto();
        }

        if (ae.getSource() == vista.getBtnBorrar()) {
            borrarProducto();
        }

        if (ae.getSource() == vista.getBtnLimpiar()) {
            limpiarCampos();
        }

        if (ae.getSource() == vista.getBtnBuscar()) {
            buscar();
        }
    }

}
