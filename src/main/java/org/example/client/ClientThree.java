package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc_stubs.GrpcServer;
import org.example.grpc_stubs.ServertestGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class ClientThree {
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

        StreamObserver<GrpcServer.TerrainRequest> onCompleted =
                newStub.clientStreamTerrainCalcul(new StreamObserver<GrpcServer.TerrainResponse>() {
                    @Override
                    public void onNext(GrpcServer.TerrainResponse value) {
                        System.out.println("onNext");
                        System.out.println("value == " + value.getPrix());
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
                GrpcServer.TerrainRequest terrainRequest= GrpcServer.TerrainRequest.newBuilder()
                        .setLabel("TerrainA")
                        .setSurface(80)
                        .setTaux(0.15)
                        .build();
                onCompleted.onNext(terrainRequest);
                counter ++;
                System.out.println("request : "+counter);
                System.out.println("terrainRequest : "+terrainRequest.getLabel()+" "
                        +terrainRequest.getSurface()+" "+terrainRequest.getTaux());
                if(counter==5){
                    onCompleted.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);

    }
}
