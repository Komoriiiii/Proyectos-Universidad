package model;

public class PedidoEncomienda extends Pedido {
    private double peso;
    private String tipo;

    public PedidoEncomienda(String id, String cliente, String direccion, double peso, String tipo) {
        super(id, cliente, direccion);
        this.peso = peso;
        this.tipo = tipo;
    }

    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 60;
        if (peso > 10) {
            tiempo = tiempo + 30;
        }
        if (tipo.equals("fragil")) {
            tiempo = tiempo + 15;
        }
        return tiempo;
    }

    @Override
    public void asignarRepartidor() {
        if (peso > 10) {
            this.repartidor = "Repartidor " + (int) (Math.random() * 100);
            System.out.println("Repartidor: " + this.repartidor);
        } else {
            super.asignarRepartidor();
        }
    }
}
