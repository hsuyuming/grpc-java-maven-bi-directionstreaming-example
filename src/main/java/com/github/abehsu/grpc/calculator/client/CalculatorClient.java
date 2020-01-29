package com.github.abehsu.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.FindMaximumRequest;
import com.proto.calculator.FindMaximumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    public void run(){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        doBiDiStreamingCall(channel);

        channel.shutdown();

    }

    private void doBiDiStreamingCall(ManagedChannel channel) {

        CountDownLatch latch = new CountDownLatch(1);

        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        StreamObserver<FindMaximumRequest> requestObserver =  asyncClient.findMaximum(new StreamObserver<FindMaximumResponse>() {
            @Override
            public void onNext(FindMaximumResponse findMaximumResponse) {
                System.out.println("Receive server response");
                System.out.println("Max Result :" + findMaximumResponse.getMaxNumber());
            }

            @Override
            public void onError(Throwable throwable) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending messages");
                latch.countDown();
            }
        });

        Arrays.asList(5,3,7,2,8,6,8).forEach(number -> {
            System.out.println("Sending: " + number);
            requestObserver.onNext(
                    FindMaximumRequest.newBuilder().setNumber(number).build()
            );
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        CalculatorClient main = new CalculatorClient();
        main.run();
    }
}
