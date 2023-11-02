package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc_stubs.GrpcServer;
import org.example.grpc_stubs.ServertestGrpc;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ClientTwo {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5050)
                .usePlaintext()
                .build();
        ServertestGrpc.ServertestStub newStub=ServertestGrpc.newStub(managedChannel);
        GrpcServer.TerrainRequest req= GrpcServer.TerrainRequest.newBuilder()
                .setLabel("TerrainA")
                .setSurface(80)
                .setTaux(0.15)
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        newStub.terrainCalcul(req, new StreamObserver<GrpcServer.TerrainResponse>() {
            @Override
            public void onNext(GrpcServer.TerrainResponse value) {
                System.out.println(value.getLabel());
                System.out.println(value.getPrix());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    // System.in.read();
    }
}
