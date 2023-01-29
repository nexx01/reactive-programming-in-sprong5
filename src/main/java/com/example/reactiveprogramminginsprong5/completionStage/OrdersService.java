package com.example.reactiveprogramminginsprong5.completionStage;

import common.Input;

import java.util.concurrent.ExecutionException;

public class OrdersService {

    private final ShoppingCardService scService;

    public OrdersService(ShoppingCardService scService) {
        this.scService = scService;
    }

    void process() {
        Input input = new Input();
        scService.calculate(input)
                .thenAccept(
                        v -> {
                            System.out.println(scService.getClass().getSimpleName() + " execution completed");
                        }
                );


        System.out.println(scService.getClass().getSimpleName() + " execution called");

    }

    public static void main(String[] args) throws InterruptedException {
        var start = System.currentTimeMillis();

        var ordersService = new OrdersService(new CompletionStageShoppingCardService());

        ordersService.process();
        ordersService.process();

        System.out.println("-------------------");
        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
        System.out.println("-------------------");

        Thread.sleep(1000);
    }
}
