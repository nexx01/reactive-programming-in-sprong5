package com.example.reactiveprogramminginsprong5.futures;

import com.example.reactiveprogramminginsprong5.callback.AsyncShoppingCardService;
import com.example.reactiveprogramminginsprong5.callback.SyncShoppingCardService;
import common.Input;
import common.Output;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OrdersService {

    private final ShoppingCardService scService;

    public OrdersService(ShoppingCardService scService) {
        this.scService = scService;
    }

    void process() {
        Input input = new Input();
        var result = scService.calculate(input);

        System.out.println(scService.getClass().getSimpleName() + " execution completed");

        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        var start = System.currentTimeMillis();

        var ordersService = new OrdersService(new FutureShoppingCardService());

        ordersService.process();
        ordersService.process();

        System.out.println("-------------------");
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
        System.out.println("-------------------");
    }
}
