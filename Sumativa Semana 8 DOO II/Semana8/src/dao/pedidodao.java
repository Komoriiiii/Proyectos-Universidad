package dao;

import db.conexion;
import model.pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//dao para manejar los pedidos
public class pedidodao {

    public boolean create(pedido p) {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getDireccion());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getEstado());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) p.setId(rs.getInt(1));
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear pedido: " + e.getMessage());
            return false;
        }
    }

    //trae todos los pedidos con filtro opcional por tipo y estado
    //si el filtro viene vacio o null se ignora
    public List<pedido> readAll(String filtroTipo, String filtroEstado) {
        List<pedido> lista = new ArrayList<>();

        //se arma la query segun los filtros
        String sql = "SELECT * FROM pedidos WHERE 1=1";
        if (filtroTipo != null && !filtroTipo.isEmpty()) sql += " AND tipo = '" + filtroTipo + "'";
        if (filtroEstado != null && !filtroEstado.isEmpty()) sql += " AND estado = '" + filtroEstado + "'";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error en la lectura de los pedidos: " + e.getMessage());
        }
        return lista;
    }

    //sin filtros
    public List<pedido> readAll() {
        return readAll(null, null);
    }

    public boolean update(pedido p) {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getDireccion());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getEstado());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el pedido: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el pedido: " + e.getMessage());
            return false;
        }
    }
}