package com.example.reactiveprogramminginsprong5.imperative;

import common.Input;

public class OrdersService {

    private final ShoppingCardService scService;

    public OrdersService(ShoppingCardService scService) {
        this.scService = scService;
    }

    void process() {
        Input input = new Input();
        var calculate = scService.calculate(input);

        System.out.println(scService.getClass().getSimpleName() + " execution completed");
    }

    public static void main(String[] args) {
        var start = System.currentTimeMillis();

        new OrdersService(new BlockingShoppingCardService()).process();
        new OrdersService(new BlockingShoppingCardService()).process();
        System.out.println("-------------------");
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
        System.out.println("-------------------");
    }
}
