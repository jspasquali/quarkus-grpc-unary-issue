package org.jsp;

import javax.inject.Singleton;

import org.jboss.logging.Logger;

import io.grpc.stub.StreamObserver;

@Singleton
public class Service2Impl extends Service2Grpc.Service2ImplBase {
  private static final Logger LOG = Logger.getLogger(Service2Impl.class);

  @Override
  public void sayHello(HelloRequest2 request, StreamObserver<HelloReply2> responseObserver) {
    LOG.infov("Received request from {0}", request.getName());
    String name = request.getName();
    String message = "Service2 says hello to " + name;
    responseObserver.onNext(HelloReply2.newBuilder().setMessage(message).build());
    responseObserver.onCompleted();
  }
}
