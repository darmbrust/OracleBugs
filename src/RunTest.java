import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;

public class RunTest
{
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		new JFXPanel();  //start javafx / task API code (why oh why does the Task API require JavaFX to be up?)
		ExecutorCompletionService<Void> ecs = new ExecutorCompletionService<>(Executors.newSingleThreadExecutor());
		
		TaskWorks tw = new TaskWorks();
		ecs.submit(tw, null);
		
		System.out.println("Wait for works");
		tw.get();
		System.out.println("Done");
		
		TaskFails tf = new TaskFails();
		ecs.submit(tf, null);
		
		System.out.println("Wait for fails - submitted as runnable");
		tf.get();
		System.out.println("Done");
		
		tf = new TaskFails();
		ecs.submit(tf);
		
		System.out.println("Wait for fails - submitted as callable - should block 2 seconds");
		tf.get();
		System.out.println("Done - you won't see this");
		
	}
	
	private static class TaskWorks extends Task<Void>
	{
		/**
		 * @see javafx.concurrent.Task#call()
		 */
		@Override
		public Void call() throws Exception
		{
			updateProgress(1, 2);
			Thread.sleep(2000);
			updateProgress(2, 2);
			return null;
		}
	}
	
	private static class TaskFails extends Task<Void> implements Callable<Void>
	{
		/**
		 * @see javafx.concurrent.Task#call()
		 */
		@Override
		public Void call() throws Exception
		{
			updateProgress(1, 2);
			Thread.sleep(2000);
			updateProgress(2, 2);
			return null;
		}
	}

}
