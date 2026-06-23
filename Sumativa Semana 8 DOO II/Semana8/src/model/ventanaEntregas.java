package model;

import dao.entregadao;
import dao.pedidodao;
import dao.repartidordao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//pantalla para gestionar las entregas
public class ventanaEntregas extends JPanel {

    entregadao   daoEntrega    = new entregadao();
    pedidodao     daoPedido     = new pedidodao();
    repartidordao daoRepartidor = new repartidordao();

    JComboBox<pedido>      cmbPedido     = new JComboBox<>();
    JComboBox<repartidor>  cmbRepartidor = new JComboBox<>();
    JTextField txtFecha = new JTextField(16);

    JTable tabla;
    DefaultTableModel modelo;
    int idSeleccionado = -1;

    public ventanaEntregas() {
        setLayout(new BorderLayout(5, 5));
        armarPantalla();
        cargarCombos();
        cargarDatos();
    }

    void armarPantalla() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Registrar Entrega"));
        form.add(new JLabel("Pedido:"));
        form.add(cmbPedido);
        form.add(new JLabel("Repartidor:"));
        form.add(cmbRepartidor);
        form.add(new JLabel("Fecha (dd-MM-yyyy HH:mm):"));

        //se pone la fecha actual como valor por defecto
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        txtFecha.setText(ahora);
        form.add(txtFecha);

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");
        form.add(btnAgregar); form.add(btnEditar);
        form.add(btnEliminar); form.add(btnLimpiar);
        add(form, BorderLayout.NORTH);

        //tabla de entregas
        modelo = new DefaultTableModel(
                new String[]{"ID", "ID Pedido", "Direccion", "Repartidor", "Fecha/Hora"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        //eventos
        btnAgregar.addActionListener(e -> agregar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        btnEditar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona una entrega"); return; }
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                // buscar el pedido en el combo por id
                int pid = (int) modelo.getValueAt(fila, 1);
                for (int i = 0; i < cmbPedido.getItemCount(); i++) {
                    if (cmbPedido.getItemAt(i).getId() == pid) { cmbPedido.setSelectedIndex(i); break; }
                }
                // buscar el repartidor en el combo por nombre
                String rep = (String) modelo.getValueAt(fila, 3);
                for (int i = 0; i < cmbRepartidor.getItemCount(); i++) {
                    if (cmbRepartidor.getItemAt(i).getNombre().equals(rep)) { cmbRepartidor.setSelectedIndex(i); break; }
                }
                txtFecha.setText((String) modelo.getValueAt(fila, 4));
                btnEditar.setText("Confirmar");
            } else {
                confirmarEdicion();
                btnEditar.setText("Editar");
            }
        });
    }

    void agregar() {
        if (!validar()) return;
        pedido p     = (pedido) cmbPedido.getSelectedItem();
        repartidor r = (repartidor) cmbRepartidor.getSelectedItem();
        entrega ent  = new entrega(p, r, txtFecha.getText().trim());
        if (daoEntrega.create(ent)) {
            JOptionPane.showMessageDialog(this, "Entrega registrada");
            limpiar(); cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void confirmarEdicion() {
        if (!validar()) return;
        entrega ent = new entrega(idSeleccionado,
                (pedido) cmbPedido.getSelectedItem(),
                (repartidor) cmbRepartidor.getSelectedItem(),
                txtFecha.getText().trim()
        );
        if (daoEntrega.update(ent)) {
            JOptionPane.showMessageDialog(this, "Entrega actualizada");
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiar(); cargarDatos();
    }

    void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona una entrega"); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar entrega #" + id + "?") != JOptionPane.YES_OPTION) return;
        if (daoEntrega.delete(id)) {
            JOptionPane.showMessageDialog(this, "Eliminada");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    boolean validar() {
        if (cmbPedido.getSelectedItem() == null)     { JOptionPane.showMessageDialog(this, "Selecciona un pedido"); return false; }
        if (cmbRepartidor.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Selecciona un repartidor"); return false; }
        if (txtFecha.getText().trim().isEmpty())     { JOptionPane.showMessageDialog(this, "Ingresa la fecha y hora"); return false; }
        return true;
    }

    //carga los combos con datos de la bd
    public void cargarCombos() {
        cmbPedido.removeAllItems();
        cmbRepartidor.removeAllItems();
        for (pedido p : daoPedido.readAll())       cmbPedido.addItem(p);
        for (repartidor r : daoRepartidor.readAll()) cmbRepartidor.addItem(r);
    }

    public void cargarDatos() {
        modelo.setRowCount(0);
        List<entrega> lista = daoEntrega.readAll();
        for (entrega e : lista) {
            modelo.addRow(new Object[]{
                    e.getId(),
                    e.getPedido().getId(),
                    e.getPedido().getDireccion(),
                    e.getRepartidor().getNombre(),
                    e.getFechaHora()
            });
        }
    }

    void limpiar() {
        idSeleccionado = -1;
        String ahora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        txtFecha.setText(ahora);
        tabla.clearSelection();
    }
}