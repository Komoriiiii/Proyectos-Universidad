package model;
import java.util.ArrayList;

public class ControladorEnvios implements Despachable, Cancelable, Rastreable {
    private ArrayList<Pedido> historial;

    public ControladorEnvios() {
        this.historial = new ArrayList<>();
    }
    @Override
    public void despachar(Pedido pedido) {
        if (pedido.isCancelado()){
            System.out.println("Error: No se puede despachar un pedido cancelado");
            return;
        }
        if (pedido.getRepartidor()== null){
            System.out.println("Error: Se debe asignar un repartidor primero");
            return;
        }
        pedido.setDespachado(true);
        historial.add(pedido);
        System.out.println("Pedido " + pedido.getId() + " despachado");
    }
    @Override
    public void cancelar(Pedido pedido) {
        if (pedido.isDespachado()){
            System.out.println("Error: No se puede cancelar un pedido ya despachado");
            return;
        }
        pedido.setCancelado(true);
        historial.add(pedido);
        System.out.println("Pedido " + pedido.getId() + " cancelado");
    }
    @Override
    public void verHistorial(){
        System.out.println("\n----- Historial de Pedidos -----");
        if (historial.isEmpty()){
            System.out.println("No hay pedidos en el historial");
        } else {
            for (int i=0;i<historial.size();i++){
                Pedido p = historial.get(i);
                String estado = p.isCancelado() ? "Cancelado" : "Despachado";
                System.out.println((i+1)+". "+p.getId()+ " - "+p.getCliente()+" - "+estado);
            }
        }
        System.out.println("--------------------------------");
    }
}
