package model;
import java.util.ArrayList;

interface Despachable {
    void despachar (Pedido pedido);
}
interface Cancelable {
    void cancelar (Pedido pedido);
}
interface Rastreable {
    void verHistorial();
}
