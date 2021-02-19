package org.jsp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.smallrye.common.annotation.Blocking;
import org.jboss.logging.Logger;

import io.quarkus.grpc.runtime.annotations.GrpcService;

@Path("/test")
public class TestResource {
  private static final Logger LOG = Logger.getLogger(TestResource.class);

  @Inject
  @GrpcService("service1")
  Service1Grpc.Service1BlockingStub service1;

  @Inject
  @GrpcService("service2")
  Service2Grpc.Service2BlockingStub service2;

  @GET
  @Path("/service1")
  public String helloService1() {
    LOG.infov("helloService1");
    HelloReply1 reply = service1.sayHello(HelloRequest1.newBuilder().setName("John").build());
    return reply.getMessage();
  }

  @GET
  @Path("/service2")
  public String helloService2() {
    LOG.infov("helloService2");
    HelloReply2 reply = service2.sayHello(HelloRequest2.newBuilder().setName("Jack").build());
    return reply.getMessage();
  }
}