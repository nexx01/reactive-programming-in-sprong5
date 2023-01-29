package com.example.reactiveprogramminginsprong5.callback;

import common.Input;

public class OrdersService {

    private final ShoppingCardService scService;

    public OrdersService(ShoppingCardService scService) {
        this.scService = scService;
    }

    void process() {
        Input input = new Input();
        scService.calculate(input,output -> {
            System.out.println(scService.getClass().getSimpleName() + " execution completed");
        });

    }

    public static void main(String[] args) {
        var start = System.currentTimeMillis();

        var ordersServiceAsync = new OrdersService(new AsyncShoppingCardService());
        var ordersServiceSync = new OrdersService(new SyncShoppingCardService());

        ordersServiceAsync.process();
        ordersServiceAsync.process();
        ordersServiceSync.process();

        System.out.println("-------------------");
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
        System.out.println("-------------------");
    }
}
