# quarkus-grpc-unary-issue project

2 grpc services: service1 listening on 9000, service2 listening on 9001

HTTP test endpoint hosted in service1, listening on 8086

localhost:8086/test/service1  ->  calls grpc service1 ->  calls grpc service2 

localhost:8086/test/service2  ->  calls grpc service2


```
mvn clean install
# console 1
java -jar service1/target/service1-1.0.0-SNAPSHOT-runner.jar
# console 2
java -jar service2/target/service2-1.0.0-SNAPSHOT-runner.jar

# console 3
curl -s localhost:8086/test/service1   # first call works sometimes
curl -s localhost:8086/test/service1   # fails and the curl request hangs
```

Logs of service1:
```
2021-02-19 10:07:16,142 INFO  [io.qua.grp.run.GrpcServerRecorder] (vert.x-eventloop-thread-1) gRPC Server started on 0.0.0.0:9000 [SSL enabled: false]
2021-02-19 10:07:16,218 INFO  [io.quarkus] (main) service1 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.11.3.Final) started in 3.510s. Listening on: http://0.0.0.0:8086
2021-02-19 10:07:16,218 INFO  [io.quarkus] (main) Profile prod activated.
2021-02-19 10:07:16,219 INFO  [io.quarkus] (main) Installed features: [cdi, grpc-client, grpc-server, mutiny, resteasy, smallrye-context-propagation, vertx]
2021-02-19 10:07:24,969 INFO  [org.jsp.Service1Impl] (vert.x-worker-thread-4) Service1Impl: request from John
2021-02-19 10:07:25,122 INFO  [org.jsp.Service1Impl] (vert.x-worker-thread-4) Service2 reply: Service2 says hello to John
2021-02-19 10:07:35,019 INFO  [org.jsp.Service1Impl] (vert.x-worker-thread-7) Service1Impl: request from John
2021-02-19 10:07:35,025 ERROR [io.qua.ver.cor.run.VertxCoreRecorder] (vert.x-worker-thread-7) Uncaught exception received by Vert.x: io.grpc.StatusRuntimeException: INTERNAL: No value received for unary call
        at io.grpc.stub.ClientCalls.toStatusRuntimeException(ClientCalls.java:262)
        at io.grpc.stub.ClientCalls.getUnchecked(ClientCalls.java:243)
        at io.grpc.stub.ClientCalls.blockingUnaryCall(ClientCalls.java:156)
        at org.jsp.Service2Grpc$Service2BlockingStub.sayHello(Service2Grpc.java:190)
        at org.jsp.Service1Impl.sayHello(Service1Impl.java:24)
        at org.jsp.Service1Grpc$MethodHandlers.invoke(Service1Grpc.java:244)
        at io.grpc.stub.ServerCalls$UnaryServerCallHandler$UnaryServerCallListener.onHalfClose(ServerCalls.java:182)
        at io.quarkus.grpc.runtime.supports.BlockingServerInterceptor$ReplayListener$1.handle(BlockingServerInterceptor.java:107)
        at io.quarkus.grpc.runtime.supports.BlockingServerInterceptor$ReplayListener$1.handle(BlockingServerInterceptor.java:101)
        at io.vertx.core.impl.ContextImpl.lambda$executeBlocking$2(ContextImpl.java:313)
        at io.vertx.core.impl.TaskQueue.run(TaskQueue.java:76)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:834)

```

Direct calls to service2 work:
```
$ curl -s localhost:8086/test/service2
Service2 says hello to Jack
$ curl -s localhost:8086/test/service2
Service2 says hello to Jack
$ curl -s localhost:8086/test/service2
Service2 says hello to Jack

```
