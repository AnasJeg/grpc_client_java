package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc_stubs.GrpcServer;
import org.example.grpc_stubs.ServertestGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class ClientFour {
    public static void main(String[] args) {
        //asyn
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",5050)
                .usePlaintext()
                .build();
        ServertestGrpc.ServertestStub newStub=ServertestGrpc.newStub(managedChannel);


        StreamObserver<GrpcServer.TerrainRequest> onCompleted =
                newStub.fullStreamTerrainCalcul(new StreamObserver<GrpcServer.TerrainResponse>() {
                    @Override
                    public void onNext(GrpcServer.TerrainResponse value) {
                        System.out.println("Label = "+ value.getLabel());
                        System.out.println("Prix = " + value.getPrix());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }
                });

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int counter=0;
            @Override
            public void run() {
                GrpcServer.TerrainRequest request= GrpcServer.TerrainRequest.newBuilder()
                        .setLabel("TerrainA")
                        .setSurface(80)
                        .setTaux(0.15)
                        .build();
                onCompleted.onNext(request);

                ++counter;
                System.out.println("request : "+counter);
                if(counter==3){
                    onCompleted.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }
}