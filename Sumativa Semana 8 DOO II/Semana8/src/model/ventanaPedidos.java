package model;

import dao.pedidodao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ventanaPedidos extends JPanel {

    pedidodao dao = new pedidodao();

    JTextField txtDireccion = new JTextField(20);
    JComboBox<String> cmbTipo   = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});
    JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"PENDIENTE", "EN REPARTO", "ENTREGADO"});

    // filtros
    JComboBox<String> cmbFiltroTipo   = new JComboBox<>(new String[]{"", "COMIDA", "ENCOMIENDA", "EXPRESS"});
    JComboBox<String> cmbFiltroEstado = new JComboBox<>(new String[]{"", "PENDIENTE", "EN REPARTO", "ENTREGADO"});

    JTable tabla;
    DefaultTableModel modelo;
    int idSeleccionado = -1;

    public ventanaPedidos() {
        setLayout(new BorderLayout(5, 5));
        armarPantalla();
        cargarDatos();
    }

    void armarPantalla() {
        // formulario
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Pedido"));
        form.add(new JLabel("Direccion:"));
        form.add(txtDireccion);
        form.add(new JLabel("Tipo:"));
        form.add(cmbTipo);
        form.add(new JLabel("Estado:"));
        form.add(cmbEstado);

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");
        form.add(btnAgregar); form.add(btnEditar);
        form.add(btnEliminar); form.add(btnLimpiar);

        // filtros
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtros.setBorder(BorderFactory.createTitledBorder("Filtros (dejar vacio para ver todos)"));
        filtros.add(new JLabel("Tipo:"));
        filtros.add(cmbFiltroTipo);
        filtros.add(new JLabel("Estado:"));
        filtros.add(cmbFiltroEstado);
        JButton btnFiltrar = new JButton("Filtrar");
        filtros.add(btnFiltrar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(form, BorderLayout.NORTH);
        norte.add(filtros, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);

        //tabla
        modelo = new DefaultTableModel(new String[]{"ID", "Direccion", "Tipo", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        //botones eventos
        btnAgregar.addActionListener(e -> agregar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnFiltrar.addActionListener(e -> cargarDatos());

        btnEditar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(this, "Selecciona un pedido primero");
                    return;
                }
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                txtDireccion.setText((String) modelo.getValueAt(fila, 1));
                cmbTipo.setSelectedItem(modelo.getValueAt(fila, 2));
                cmbEstado.setSelectedItem(modelo.getValueAt(fila, 3));
                btnEditar.setText("Confirmar");
            } else {
                confirmarEdicion();
                btnEditar.setText("Editar");
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                txtDireccion.setText((String) modelo.getValueAt(fila, 1));
                cmbTipo.setSelectedItem(modelo.getValueAt(fila, 2));
                cmbEstado.setSelectedItem(modelo.getValueAt(fila, 3));
            }
        });
    }

    void agregar() {
        if (txtDireccion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La direccion es obligatoria");
            return;
        }
        pedido p = new pedido(
                txtDireccion.getText().trim(),
                (String) cmbTipo.getSelectedItem(),
                (String) cmbEstado.getSelectedItem()
        );
        if (dao.create(p)) {
            JOptionPane.showMessageDialog(this, "Pedido registrado");
            limpiar(); cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el pedido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void confirmarEdicion() {
        if (txtDireccion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La direccion no puede estar vacia");
            return;
        }
        pedido p = new pedido(idSeleccionado,
                txtDireccion.getText().trim(),
                (String) cmbTipo.getSelectedItem(),
                (String) cmbEstado.getSelectedItem()
        );
        if (dao.update(p)) {
            JOptionPane.showMessageDialog(this, "Pedido actualizado");
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiar(); cargarDatos();
    }

    void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona un pedido"); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar pedido #" + id + "?") != JOptionPane.YES_OPTION) return;
        if (dao.delete(id)) {
            JOptionPane.showMessageDialog(this, "Pedido eliminado");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //recarga la tabla usando los filtros que se seleccionen
    public void cargarDatos() {
        modelo.setRowCount(0);
        String ft = (String) cmbFiltroTipo.getSelectedItem();
        String fe = (String) cmbFiltroEstado.getSelectedItem();
        List<pedido> lista = dao.readAll(ft, fe);
        for (pedido p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getDireccion(), p.getTipo(), p.getEstado()});
        }
    }

    void limpiar() {
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}