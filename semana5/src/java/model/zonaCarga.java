package model;

import java.util.ArrayList;
import java.util.List;

public class zonaCarga {
    private List<Pedido> pedidos;

    public zonaCarga() {
        pedidos = new ArrayList<>();
    }

    public synchronized void agregarPedido(Pedido p) {
        pedidos.add(p);
        System.out.println("[ZONA] Pedido #" + p.getId() + " agregado");
    }

    public synchronized Pedido retirarPedido(){
        if(pedidos.isEmpty()){ //se devuelve null si no hay pedidos
            return null;
        }
        // se remueve el primero pedido de la lista y lo devuelve
        Pedido pedido = pedidos.remove(0);
        return pedido;
    }

    //verifica si es que hay pedidos pendientes

    public synchronized boolean hayPedidos(){
        return !pedidos.isEmpty();
    }

    //devuelve cuantos pedidos hay

    public synchronized int cantidadPedidos(){
        return pedidos.size();
    }
}
