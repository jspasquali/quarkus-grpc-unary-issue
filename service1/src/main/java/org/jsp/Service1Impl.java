package org.jsp;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.common.annotation.Blocking;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Service1Impl extends Service1Grpc.Service1ImplBase {
  private static final Logger LOG = Logger.getLogger(Service1Impl.class);

  @Inject
  @GrpcService("service2")
  Service2Grpc.Service2BlockingStub service2;

  @Override
  @Blocking
  public void sayHello(HelloRequest1 request, StreamObserver<HelloReply1> responseObserver) {
    LOG.infov("Service1Impl: request from {0}", request.getName());

    HelloReply2 reply = service2.sayHello(HelloRequest2.newBuilder().setName(request.getName()).build());
    LOG.infov("Service2 reply: {0}", reply.getMessage());

    String message = "Service1 --> " + reply.getMessage();
    responseObserver.onNext(HelloReply1.newBuilder().setMessage(message).build());
    responseObserver.onCompleted();
  }
}
