package model;

public enum estadoPedido {
    PENDIENTE,   // el pedido esta esperando a ser retirado por un repartidor
    EN_REPARTO,  //el pedido esta en camino, ya fue tomado por un repartidor
    ENTREGADO   //para pedidos entregados
}
