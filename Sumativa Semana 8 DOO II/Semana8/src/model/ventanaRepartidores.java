package model;

import dao.repartidordao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//pantalla para gestionar repartidores
public class ventanaRepartidores extends JPanel {

    repartidordao dao = new repartidordao();

    JTextField txtNombre = new JTextField(20);
    JTable tabla;
    DefaultTableModel modelo;
    int idSeleccionado = -1; //guarda el id cuando se edita

    public ventanaRepartidores() {
        setLayout(new BorderLayout(5, 5));
        armarPantalla();
        cargarDatos();
    }

    void armarPantalla() {
        //panel del formulario
        JPanel form = new JPanel();
        form.setBorder(BorderFactory.createTitledBorder("Repartidor"));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");

        form.add(btnAgregar);
        form.add(btnEditar);
        form.add(btnEliminar);
        form.add(btnLimpiar);

        add(form, BorderLayout.NORTH);

        //tabla
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        //eventos botones
        btnAgregar.addActionListener(e -> agregar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        //boton editar, primero carga datos, despues los confirma
        btnEditar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                // todavia no hay nada seleccionado, cargamos la fila
                int fila = tabla.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(this, "Selecciona un repartidor para editar");
                    return;
                }
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                txtNombre.setText((String) modelo.getValueAt(fila, 1));
                btnEditar.setText("Confirmar");
            } else {
                //ya habia algo seleccionado, se confirma la edicion
                confirmarEdicion();
                btnEditar.setText("Editar");
            }
        });

        //al hacer click en la tabla se carga en el campo
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                txtNombre.setText((String) modelo.getValueAt(fila, 1));
            }
        });
    }

    void agregar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio");
            return;
        }
        boolean ok = dao.create(new repartidor(nombre));
        if (ok) {
            JOptionPane.showMessageDialog(this, "Repartidor agregado!");
            limpiar();
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un error al agregar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void confirmarEdicion() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio");
            return;
        }
        boolean ok = dao.update(new repartidor(idSeleccionado, nombre));
        if (ok) {
            JOptionPane.showMessageDialog(this, "Repartidor actualizado!");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiar();
        cargarDatos();
    }

    void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un repartidor para eliminar");
            return;
        }
        int id = (int) modelo.getValueAt(fila, 0);
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este repartidor?");
        if (respuesta != JOptionPane.YES_OPTION) return;

        boolean ok = dao.delete(id);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Eliminado!");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarDatos() {
        modelo.setRowCount(0);
        List<repartidor> lista = dao.readAll();
        for (repartidor r : lista) {
            modelo.addRow(new Object[]{r.getId(), r.getNombre()});
        }
    }

    void limpiar() {
        txtNombre.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}