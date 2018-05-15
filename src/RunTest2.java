import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.JFXPanel;

public class RunTest2
{
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		new JFXPanel();  //start javafx / task API code (why oh why does the Task API require JavaFX to be up?)
		
		
		{
			ThreadPoolExecutor tpe = new ThreadPoolExecutor(1, 1, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
			
			TaskThrows tw = new TaskThrows();
			
			Future<?> future = tpe.submit(tw);
			
			System.out.println("Expect an exception....");
			future.get();  //This should throw an ExecutionException........
			
			System.out.println("Yet it didn't throw... BUG!!!");
			
			tpe.shutdown();
		}
		
		System.out.println("Try with fix....");
		
		{
			ThreadPoolExecutor tpe = new WorkingThreadPoolExecutor(1, 1, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
			
			TaskThrows tw = new TaskThrows();
			
			Future<?> future = tpe.submit(tw);
			
			System.out.println("Expect an exception....");
			try
			{
				future.get();  //This should throw an ExecutionException........
			}
			catch (Exception e)
			{
				System.out.println("This is the expected path - thread pool with override works.... " + e);
			}
			tpe.shutdown();
		}
		
		Platform.exit();
	}
	
	private static class TaskThrows extends Task<Void>
	{
		@Override
		public Void call() throws Exception
		{
			throw new Exception("oops");
		}
	}
	
	private static class WorkingThreadPoolExecutor extends ThreadPoolExecutor
	{
		public WorkingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
		{
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
		{
			if (runnable instanceof FutureTask)
			{
				return (FutureTask<T>)runnable;
			}
			else
			{
				return super.newTaskFor(runnable, value);
			}
		}
	}
}
