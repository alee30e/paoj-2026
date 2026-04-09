package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


public class Order {
    private OrderState o;
    private List<OrderState> history = new ArrayList<>();
    public Order(OrderState o){
        this.o = o;
    }
    public void nextState() throws OrderIsAlreadyFinalException{
        switch(o){
            case DELIVERED, CANCELED -> throw new OrderIsAlreadyFinalException("Comanda e deja intr o stare finala");
            case PLACED, PROCESSED, SHIPPED -> {
                history.add(o);
                switch (o){
                    case PLACED -> o = OrderState.PROCESSED;
                    case PROCESSED -> o = OrderState.SHIPPED;
                    case SHIPPED -> o = OrderState.DELIVERED;
                }
            }
        }
        System.out.println("Order state updated to: " + o);
    }

    public void cancel(){
        if (o == OrderState.CANCELED || o == OrderState.DELIVERED)
            throw new CannotCancelFinalOrderException("Comanda nu poate fi anulata - stare curenta finala");
        history.add(o);
        o = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState(){
        if (history.isEmpty())
            throw new CannotRevertInitialOrderStateException("Nu se poate face undo - istoric gol");
        int i = history.size() - 1;
        OrderState undoOrderState = history.get(i);
        o = undoOrderState;
        history.remove(i);
        System.out.println("Order state reverted to: " + o);
    }

}
