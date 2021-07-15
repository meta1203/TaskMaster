package com.meta1203.taskmaster;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Hunter Hancock
 *
 * @param <T>
 */
public class Task<T> {
	private static Executor ex = Executors.newWorkStealingPool();
	
	/**
	 * Override the default Executor for all new Tasks.
	 * <p>
	 * Default is {@link Executors}.newWorkStealingPool()
	 * @param ex The new {@link Executor} to use for new Tasks
	 */
	public static void setExecutor(Executor ex) {
		Task.ex = ex;
	}
	
	private CompletableFuture<T> cf;
	
	private Task(CompletableFuture<T> cf) {
		this.cf = cf;
	}
	
	/**
	 * Creates a new Task with given {@link Supplier}
	 * @param <T> The type returned by s
	 * @param s The provided {@link Supplier} to execute
	 */
	public static <T> Task<T> execute(Supplier<T> s) {
		return new Task<T>(CompletableFuture.supplyAsync(s, ex));
	}
	
	public static Task<Void> execute(Runnable r) {
		return new Task<Void>(CompletableFuture.runAsync(r, ex));
	}
	
	/**
	 * Run something after the Task completes
	 * @param c A {@link Consumer} that accepts the output of the task
	 * @return A new Task encapsulating all chained Tasks
	 */
	public Task<Void> then(Consumer<T> c) {
		return new Task<Void>(cf.thenAcceptAsync(c, ex));
	}
	
	/**
	 * Run something after the Task completes
	 * @param <R> The type {@link Function} f returns
	 * @param f The {@link Function} that accepts the output of the task and returns a new value
	 * @return A new Task encapsulating all chained Tasks, containing the result of f
	 */
	public <R> Task<R> then(Function<T, R> f) {
		return new Task<R>(cf.thenApplyAsync(f, ex));
	}
	
	/**
	 * Run something after the Task completes
	 * @param r What to run when the Task completes
	 * @return A new Task encapsulating all chained Tasks
	 */
	public Task<Void> then(Runnable r) {
		return new Task<Void>(cf.thenRunAsync(r, ex));
	}
	
	
	/**
	 * Combines a given value with the result of the previous Task
	 * @param <I> The type of the value to inject
	 * @param toInject The value to inject
	 * @param biconsumer The BiConsumer to consume the two values
	 * @return A new Task encapsulating all chained Tasks
	 */
	public <I> Task<Void> combine(I toInject, BiConsumer<I, T> biconsumer) {
		return new Task<Void>(CompletableFuture.runAsync(() -> {
			try {
				T t = this.cf.get();
				biconsumer.accept(toInject, t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}, ex));
	}
	
	/**
	 * Inject a value to be used in another .then()
	 * @param <R>
	 * @param java.util.function.Supplier<R> s
	 * @return A new Task<R> encapsulating all chained Tasks, containing result of s
	 */
	public <R> Task<R> then(Supplier<R> s) {
		return new Task<R>(CompletableFuture.supplyAsync(() -> {
			try {
				this.cf.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return s.get();
		}, ex));
	}
	
	/**
	 * Adds an asynchronous exception handler to the given task, should it need one.
	 * @param handler A {@link Consumer} to handle a given exception
	 * @return A new Task encapsulating the previous Task, and its exception handler
	 */
	public Task<Void> handle(Consumer<Throwable> handler) {
		return new Task<Void>(CompletableFuture.runAsync(() -> {
			try {
				this.cf.get();
			} catch (InterruptedException e) {
				handler.accept(e);
			} catch (ExecutionException e) {
				handler.accept(e.getCause());
			}
		}, ex));
	}
	
	/**
	 * Adds an asynchronous exception handler to the given task, should it need one.
	 * @param handler A {@link Function} to handle a given exception
	 * @return A new Task encapsulating the previous Task, and its exception handler
	 */
	public Task<T> handle(Function<Throwable, T> handler) {
		return new Task<T>(cf.handleAsync(new BiFunction<T, Throwable, T>() {
			@Override
			public T apply(T t, Throwable e) {
				if (e != null) {
					if (e instanceof ExecutionException) return handler.apply(e.getCause());
					return handler.apply(e);
				}
				return t;
			}
		}, ex));
	}
	
	/**
	 * Retrieve the value returned by the Task, waiting for completion if necessary.
	 * <p>
	 * Allows synchronous handling of any potential exception that occurred while executing the Task
	 * @return The value returned by the Task
	 * @throws Throwable A potential exception that occurred while executing the Task
	 */
	public T await() throws Throwable {
		try {
			return cf.get();
		} catch (ExecutionException e) {
			throw e.getCause();
		}
	}
	
	/**
	 * Retrieve the value returned by the Task, waiting for completion if necessary.
	 * <p>
	 * Any exception that may have occurred while executing the Task will be thrown as a RuntimeException.
	 * Use this function if you do not expect an exception, or wish for any exceptions that occur in the Task
	 * to be handled as a RuntimeException.
	 * @return The value returned by the Task
	 */
	public T awaitUnsafe() {
		try {
			return cf.get();
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Wait for all given Tasks to complete.
	 * <p>
	 * Allows synchronous handling of any potential exception that occurred while executing the Task
	 * @param tasks A list of Tasks to wait for
	 * @throws Throwable A potential exception that occurred while executing the Task
	 */
	public static void awaitAll(Consumer<GroupTaskException> handler, Task<?>... tasks) throws GroupTaskException {
		for (Task<?> t : tasks) {
			try {
				t.cf.get();
			} catch (ExecutionException e) {
				handler.accept(new GroupTaskException(t, e.getCause()));
			} catch (InterruptedException e) {
				handler.accept(new GroupTaskException(t, e));
			}
		}
	}
	
	/**
	 * Wait for all given Tasks to complete.
	 * <p>
	 * Any exception that may have occurred while executing the Task will be thrown as a RuntimeException.
	 * Use this function if you do not expect an exception, or wish for any exceptions that occur in the Task
	 * to be handled as a RuntimeException.
	 * @param tasks A list of Tasks to wait for
	 */
	public static void awaitAllUnsafe(Task<?>... tasks) {
		MultiException mex = new MultiException();
		for (Task<?> t : tasks) {
			try {
				t.cf.get();
			} catch (ExecutionException e) {
				mex.addCause(new GroupTaskException(t, e.getCause()));
			} catch (InterruptedException e) {
				mex.addCause(new GroupTaskException(t, e));
			}
		}
		mex.throwRuntimeMe();
	}

	@Override
	public String toString() {
		return "Task[" + cf.toString() + "]";
	}
}
