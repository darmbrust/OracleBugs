# Nasty Java 8 API interaction with Callable and Task APIs
This is a quick example application that shows what happens if you use the Java Task API https://docs.oracle.com/javase/8/javafx/api/javafx/concurrent/Task.html - and then make the mistake of noting that the signature is the same, and you can also implement https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html

When you submit the newly generated work item to an ExecutorCompletionService using as a Callable, the task will not operate properly / will not notify of state changes / will block indefinitely.

Absolutely Brilliant.... 