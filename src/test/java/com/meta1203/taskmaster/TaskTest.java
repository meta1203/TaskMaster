package com.meta1203.taskmaster;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.meta1203.taskmaster.collections.ConcurrentArrayList;

public class TaskTest {
	private static List<String> c = Arrays.asList("first", "second", "third", "final");
	
	@AfterEach
	void newLine() {
		System.out.println();
	}

	@Test
	void chainOrder() {
		List<String> l = new ConcurrentArrayList<>(Arrays.asList("first"));
		Task.execute(() -> {
			l.add("second");
			sleep(100);
		}).then(() -> {
			l.add("third");
			sleep(100);
		}).awaitUnsafe();
		l.add("final");

		System.out.println(printList(l) + " == " + c);
		Assertions.assertTrue(l.equals(c));
	}

	@Test
	void interceptOrder() {
		List<String> l = new ConcurrentArrayList<>(Arrays.asList("first"));
		Task<Void> t1 = Task.execute(() -> {l.add("second"); sleep(100);});
		Task<Void> t2 = t1.then(() -> {sleep(100); l.add("final"); sleep(100);});

		t1.awaitUnsafe();
		l.add("third");
		t2.awaitUnsafe();

		System.out.println(printList(l) + " == " + c);
		Assertions.assertTrue(l.equals(c));
	}

	@Test
	void handlerVerification() {
		Task<Void> t1 = Task.execute(() -> {
			sleep(100);
			throw new RuntimeException("we do a little trolling");
		});

		Task<Void> t2 = t1.handle((Throwable th) -> {
			synchronized (System.err) {
				System.out.println("Handling " + th.getClass().getName() + "...");
				th.printStackTrace();
			}
		});

		try {
			System.out.println(t1.await());
			Assertions.fail(); // shouldn't ever get here, so fail if it does
		} catch (Throwable e) {
			synchronized (System.err) {
				System.out.println("Understood: " + e.getClass().getName());
				e.printStackTrace();
			}
		}

		t2.awaitUnsafe(); // Exception is handled, so this shouldn't complain
	}
	
	@Test
	void groupTasks() {
		Task.awaitAllUnsafe(Task.execute(() -> {
			System.out.println("One is the loneliest number.");
		}), Task.execute(() -> {
			System.out.println("Two can be as bad as one.");
		}), Task.execute(() -> {
			throw new RuntimeException("What on earth is a 3?");
		}).handle((Throwable ex) -> {
			System.out.println("Recovered from " + ex.getClass().getSimpleName());
		}));
	}
	
	@Test
	void cal() {
		System.out.println("Testing ConcurrentArrayList...");
		List<Mutable> ml1 = new ConcurrentArrayList<>(Arrays.asList(new Mutable(1, 2l, "three"), new Mutable(3, 2l, "one"), new Mutable(3, 3l, "im a 3")));
		Assertions.assertTrue(ml1.size() == 3);
		
		for (Mutable m : ml1) {
			m.setOne(1);
			m.setTwo(1l);
			m.setThree("one");
		}
		
		List<Integer> ml2 = new ConcurrentArrayList<>(10000);
		List<Task<Void>> tasks = new ConcurrentArrayList<>();
		
		for (int x = 0; x < 10000; x++) {
			final int y = x;
			tasks.add(Task.execute(() -> {
				ml2.add(y);
				if (Math.random() * 100 < 1) System.out.print(ml2.get(ml2.size()-1) + ", ");
			}));
		}
		
		Task.awaitAllUnsafe(tasks);
		System.out.println();
	}
	
	@Test
	void multiexception() {
		List<Task<Void>> tasks = new ConcurrentArrayList<>();
		tasks.add(Task.execute(() -> {
			throw new RuntimeException("what, not butter!");
		}));
		tasks.add(Task.execute(() -> {
			throw new Error("hmm...");
		}));
		tasks.add(Task.execute(() -> {
			throw new RuntimeException("another exception");
		}));
		
		try {
			Task.awaitAllUnsafe(tasks);
		} catch (MultiException ex) {
			Assertions.assertTrue(ex.getCauses().size() == 3);
			return;
		}
		Assertions.fail();
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Assertions.fail(e);
		}
	}

	private String printList(List<?> l) {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for (Object o : l) {
			sj.add(o.toString());
		}
		return sj.toString();
	}
}
