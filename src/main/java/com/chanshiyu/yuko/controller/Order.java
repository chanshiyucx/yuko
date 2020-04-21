package com.chanshiyu.yuko.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author SHIYU
 * @description
 * @since 2020/4/21 10:27
 */
@RestController
@RequestMapping("/order")
public class Order {

    private static Queue<DeferredResult<Object>> queue = new ConcurrentLinkedDeque<>();

    @GetMapping("/createOrder")
    public DeferredResult<Object> createOrder() {
        DeferredResult<Object> deferredResult = new DeferredResult<>(3000L, "create order failed");
        queue.add(deferredResult);
        return deferredResult;
    }

    @GetMapping("/create")
    public String create() {
        String uuid = UUID.randomUUID().toString();
        DeferredResult<Object> deferredResult = queue.poll();
        assert deferredResult != null;
        deferredResult.setResult(uuid);
        return uuid;
    }

}
