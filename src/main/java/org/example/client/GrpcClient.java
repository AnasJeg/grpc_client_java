package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc_stubs.GrpcServerGrpc;
import org.example.grpc_stubs.GrpcServerOuterClass;

public class GrpcClient {
    public static void main(String[] args) {
        // sync
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5050)
                .usePlaintext()
                .build();
        GrpcServerGrpc.GrpcServerBlockingStub grpcServerBlockingStub=GrpcServerGrpc.newBlockingStub(managedChannel);
        GrpcServerOuterClass.DashbordRequest request= GrpcServerOuterClass.DashbordRequest.newBuilder()
                        .setRef("test")
                                .setMarque("hp")
                                        .setPrix(1234.66)
                                                .build();
       GrpcServerOuterClass.DashbordResponse response= grpcServerBlockingStub.dashbordMachines(request);
        System.out.println(response);

    }
}
