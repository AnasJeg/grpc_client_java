package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc_stubs.GrpcServer;
import org.example.grpc_stubs.ServertestGrpc;


public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5050)
                .usePlaintext()
                .build();
        ServertestGrpc.ServertestBlockingStub grpcServerBlockingStub=ServertestGrpc.newBlockingStub(managedChannel);
        GrpcServer.TerrainRequest request= GrpcServer.TerrainRequest.newBuilder()
                .setLabel("TerrainA")
                .setSurface(80)
                .setTaux(0.15)
                .build();
        GrpcServer.TerrainResponse response= grpcServerBlockingStub.terrainCalcul(request);
        System.out.println(response);

    }
}
