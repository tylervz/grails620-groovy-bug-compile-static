# Grails 6.2.0 Groovy bug - Static Compilation

This is a sample Grails 6.2.0 application, created to demonstrate a bug with Apache Groovy 3.0.21.

## Running locally

Dependencies:

-   Grails 6.2.0
-   JDK 11

You can run the application with the `bootRun` Gradle task:
`./gradlew bootRun`

And then you will be able to access the application by visiting this URL in an internet browser: <http://localhost:8080/>.

However, an error is thrown at startup due to [this line in `SessionItemGroupController.groovy`](https://github.com/tylervz/grails620-groovy-bug-compile-static/blob/5031b3a1901577f1ed0bf9fe9fa7b434a95e8c6b/grails-app/controllers/myapp/sessionitem/SessionItemGroupController.groovy#L41):

```
> Task :compileGroovy FAILED
startup failed:
grails-app/controllers/myapp/sessionitem/SessionItemGroupController.groovy: 57: [Static type checking] - Cannot call myapp.sessionitem.SessionItemGroup#getAll(java.lang.Iterable <java.io.Serializable>) with arguments [java.util.List <java.lang.Long>]
 @ line 41, column 56.
   temGroup> sessionItemGroups = SessionIte
                                 ^
```

`SessionItemGroup` is a domain class.

On the `workaround` branch of this repository, the line in `SessionItemGroupController.groovy` has been modified to 
`SessionItemGroup.getAll(longIds as Iterable<Serializable>)` so that the application starts without an exception.

You could instead annotate the `deleteMultiple()` method with the `groovy.transform.CompileDynamic` annotation to avoid an exception being thrown,
but both of these workarounds should not be necessary because `java.util.List` implements `java.lang.Iterable`
and `java.lang.Long` implements `java.lang.Number` which in turn implements `java.io.Serializable`.

## Background

This same code did not cause an exception when running in Grails 6.1.2.
Grails 6.2.0 upgraded from Groovy 3.0.11 to Groovy 3.0.21.

## Stacktrace

When running `./gradlew bootRun --stacktrace`, the following stacktrace is printed:

```
> Task :compileGroovy FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileGroovy'.
> Compilation failed; see the compiler error output for details.

* Try:
> Run with --debug option to get more log output.
> Run with --scan to get full insights.

* Exception is:
org.gradle.api.tasks.TaskExecutionException: Execution failed for task ':compileGroovy'.
        at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.lambda$executeIfValid$1(ExecuteActionsTaskExecuter.java:142)
        at org.gradle.internal.Try$Failure.ifSuccessfulOrElse(Try.java:282)
        at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeIfValid(ExecuteActionsTaskExecuter.java:140)
        at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:128)
        at org.gradle.api.internal.tasks.execution.CleanupStaleOutputsExecuter.execute(CleanupStaleOutputsExecuter.java:77)
        at org.gradle.api.internal.tasks.execution.FinalizePropertiesTaskExecuter.execute(FinalizePropertiesTaskExecuter.java:46)
        at org.gradle.api.internal.tasks.execution.ResolveTaskExecutionModeExecuter.execute(ResolveTaskExecutionModeExecuter.java:51)
        at org.gradle.api.internal.tasks.execution.SkipTaskWithNoActionsExecuter.execute(SkipTaskWithNoActionsExecuter.java:57)
        at org.gradle.api.internal.tasks.execution.SkipOnlyIfTaskExecuter.execute(SkipOnlyIfTaskExecuter.java:57)
        at org.gradle.api.internal.tasks.execution.CatchExceptionTaskExecuter.execute(CatchExceptionTaskExecuter.java:36)
        at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.executeTask(EventFiringTaskExecuter.java:77)
        at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:55)
        at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:52)
        at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
        at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:199)
        at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
        at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
        at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
        at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
        at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
        at org.gradle.internal.operations.DefaultBuildOperationExecutor.call(DefaultBuildOperationExecutor.java:73)
        at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter.execute(EventFiringTaskExecuter.java:52)
        at org.gradle.execution.plan.LocalTaskNodeExecutor.execute(LocalTaskNodeExecutor.java:69)
        at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:322)
        at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:309)
        at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:302)
        at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:288)
        at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.execute(DefaultPlanExecutor.java:462)
        at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.run(DefaultPlanExecutor.java:379)
        at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
        at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:49)
Caused by: org.gradle.api.internal.tasks.compile.CompilationFailedException: Compilation failed; see the compiler error output for details.
        at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:297)
        at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:67)
        at org.gradle.api.internal.tasks.compile.GroovyCompilerFactory$DaemonSideCompiler.execute(GroovyCompilerFactory.java:97)
        at org.gradle.api.internal.tasks.compile.GroovyCompilerFactory$DaemonSideCompiler.execute(GroovyCompilerFactory.java:76)
        at org.gradle.api.internal.tasks.compile.daemon.AbstractDaemonCompiler$CompilerWorkAction.execute(AbstractDaemonCompiler.java:135)
        at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:63)
        at org.gradle.workers.internal.AbstractClassLoaderWorker$1.create(AbstractClassLoaderWorker.java:49)
        at org.gradle.workers.internal.AbstractClassLoaderWorker$1.create(AbstractClassLoaderWorker.java:43)
        at org.gradle.internal.classloader.ClassLoaderUtils.executeInClassloader(ClassLoaderUtils.java:100)
        at org.gradle.workers.internal.AbstractClassLoaderWorker.executeInClassLoader(AbstractClassLoaderWorker.java:43)
        at org.gradle.workers.internal.IsolatedClassloaderWorker.run(IsolatedClassloaderWorker.java:49)
        at org.gradle.workers.internal.IsolatedClassloaderWorker.run(IsolatedClassloaderWorker.java:30)
        at org.gradle.workers.internal.WorkerDaemonServer.run(WorkerDaemonServer.java:87)
        at org.gradle.workers.internal.WorkerDaemonServer.run(WorkerDaemonServer.java:56)
        at org.gradle.process.internal.worker.request.WorkerAction$1.call(WorkerAction.java:138)
        at org.gradle.process.internal.worker.child.WorkerLogEventListener.withWorkerLoggingProtocol(WorkerLogEventListener.java:41)
        at org.gradle.process.internal.worker.request.WorkerAction.run(WorkerAction.java:135)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
        at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
        at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
        at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
        at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:414)
        at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
        at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:49)
```

## Additonal documentation

- [User Guide](https://docs.grails.org/6.2.0/guide/index.html)
- [API Reference](https://docs.grails.org/6.2.0/api/index.html)
- [Grails Guides](https://guides.grails.org/index.html)
