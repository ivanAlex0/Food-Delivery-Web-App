package foodPanda.model.states;

import foodPanda.model.OrderStatus;

public interface StateInterface {

    OrderStatus changeState(OrderStatus newStatus);
}
