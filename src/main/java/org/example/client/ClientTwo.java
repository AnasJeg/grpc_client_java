package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc_stubs.GrpcServer;
import org.example.grpc_stubs.ServertestGrpc;

public class ClientTwo {
    public static void main(String[] args) {
        //asyn
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5050)
                .usePlaintext()
                .build();
        ServertestGrpc.ServertestStub newStub=ServertestGrpc.newStub(managedChannel);
        GrpcServer.TerrainRequest request= GrpcServer.TerrainRequest.newBuilder()
                .setLabel("TerrainA")
                .setSurface(80)
                .setTaux(0.15)
                .build();
        newStub.terrainCalcul(request, new StreamObserver<GrpcServer.TerrainResponse>() {
            @Override
            public void onNext(GrpcServer.TerrainResponse value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });


    }
}
