package dao;

import db.conexion;
import model.repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//dao para manejar los repartidores en la base de datos
public class repartidordao {

    //insertar un nuevo repartidor
    public boolean create(repartidor r) {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getNombre());
            ps.executeUpdate();

            //obtener el id que genero la bd
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                r.setId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear repartidor: " + e.getMessage());
            return false;
        }
    }

    //traer todos los repartidores
    public List<repartidor> readAll() {
        List<repartidor> lista = new ArrayList<>();
        String sql = "SELECT * FROM repartidores";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new repartidor(rs.getInt("id"), rs.getString("nombre")));
            }

        } catch (SQLException e) {
            System.out.println("Error en la lectura de los repartidores: " + e.getMessage());
        }
        return lista;
    }

    //actualizar el nombre de un repartidor
    public boolean update(repartidor r) {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getNombre());
            ps.setInt(2, r.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos del repartidor: " + e.getMessage());
            return false;
        }
    }

    //eliminar repartidor por id
    public boolean delete(int id) {
        String sql = "DELETE FROM repartidores WHERE id = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar al repartidor: " + e.getMessage());
            return false;
        }
    }
}