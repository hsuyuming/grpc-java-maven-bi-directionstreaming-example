package com.github.abehsu.grpc.calculator.server;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.FindMaximumRequest;
import com.proto.calculator.FindMaximumResponse;
import io.grpc.stub.StreamObserver;


public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
//    @Override
//    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {
//        StreamObserver<FindMaximumRequest> requestObserver = new StreamObserver<FindMaximumRequest>() {
//
//            List<Integer> numberList = new ArrayList<>();
//
//            @Override
//            public void onNext(FindMaximumRequest findMaximumRequest) {
//                numberList.add(findMaximumRequest.getNumber());
//                responseObserver.onNext(FindMaximumResponse.newBuilder()
//                        .setMaxNumber(Collections.max(numberList))
//                        .build()
//                );
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                //skip error
//                responseObserver.onCompleted();
//            }
//
//            @Override
//            public void onCompleted() {
//                responseObserver.onCompleted();
//
//            }
//        };
//
//        return requestObserver;
//    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {
        StreamObserver<FindMaximumRequest> requestObserver = new StreamObserver<FindMaximumRequest>() {

            int currentMaximum;

            @Override
            public void onNext(FindMaximumRequest findMaximumRequest) {

                int currentNumber = findMaximumRequest.getNumber();
                if (currentNumber > currentMaximum) {
                    currentMaximum = currentNumber;
                    responseObserver.onNext(FindMaximumResponse.newBuilder()
                            .setMaxNumber(currentMaximum)
                            .build()
                    );
                }else{
                    //nothing
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //skip error
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(FindMaximumResponse.newBuilder()
                        .setMaxNumber(currentMaximum)
                        .build()
                );
                responseObserver.onCompleted();

            }
        };

        return requestObserver;
    }



}
