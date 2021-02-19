package org.jsp;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.smallrye.common.annotation.Blocking;
import org.jboss.logging.Logger;

import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.mutiny.Uni;

//@Singleton
public class Service1ImplMutinyBlocking2 extends MutinyService1Grpc.Service1ImplBase {
  private static final Logger LOG = Logger.getLogger(Service1ImplMutinyBlocking2.class);

  @Inject
  @GrpcService("service2")
  MutinyService2Grpc.MutinyService2Stub service2;

  @Override
  @Blocking
  public Uni<HelloReply1> sayHello(HelloRequest1 request) {
    LOG.infov("Service1ImplMutinyBlocking2: request from {0}", request.getName());

    Uni<HelloReply2> reply2Uni = service2.sayHello(HelloRequest2.newBuilder().setName(request.getName()).build());
    HelloReply2 reply2 = reply2Uni.await().indefinitely();
    LOG.infov("Service2 reply: {0}", reply2.getMessage());

    HelloReply1 reply1 = HelloReply1.newBuilder().setMessage(reply2.getMessage()).build();
    return Uni.createFrom().item(reply1);
  }
}

