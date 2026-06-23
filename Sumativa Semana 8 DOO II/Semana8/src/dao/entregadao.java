package dao;

import db.conexion;
import model.entrega;
import model.pedido;
import model.repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//dao para las entregas, necesita hacer join con pedidos y repartidores
public class entregadao {

    public boolean create(entrega e) {
        String sql = "INSERT INTO entregas (pedido_id, repartidor_id, fecha_hora) VALUES (?, ?, ?)";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, e.getPedido().getId());
            ps.setInt(2, e.getRepartidor().getId());
            ps.setString(3, e.getFechaHora());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) e.setId(rs.getInt(1));
            return true;

        } catch (SQLException ex) {
            System.out.println("Error al crear entrega: " + ex.getMessage());
            return false;
        }
    }

    //trae todas las entregas con join para obtener datos del pedido y repartidor
    public List<entrega> readAll() {
        List<entrega> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.fecha_hora, " +
                "p.id as pid, p.direccion, p.tipo, p.estado, " +
                "r.id as rid, r.nombre " +
                "FROM entregas e " +
                "JOIN pedidos p ON e.pedido_id = p.id " +
                "JOIN repartidores r ON e.repartidor_id = r.id";

        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pedido p = new pedido(rs.getInt("pid"), rs.getString("direccion"),
                        rs.getString("tipo"), rs.getString("estado"));
                repartidor r = new repartidor(rs.getInt("rid"), rs.getString("nombre"));
                lista.add(new entrega(rs.getInt("id"), p, r, rs.getString("fecha_hora")));
            }

        } catch (SQLException e) {
            System.out.println("Error en la lectura de las entregas: " + e.getMessage());
        }
        return lista;
    }

    public boolean update(entrega e) {
        String sql = "UPDATE entregas SET pedido_id = ?, repartidor_id = ?, fecha_hora = ? WHERE id = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, e.getPedido().getId());
            ps.setInt(2, e.getRepartidor().getId());
            ps.setString(3, e.getFechaHora());
            ps.setInt(4, e.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Error en la actualizacion de la entrega: " + ex.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar la entrega: " + e.getMessage());
            return false;
        }
    }
}