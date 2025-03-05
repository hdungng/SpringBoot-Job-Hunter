package vn.spring.jobhunter.util.error;

public class StorageException extends Exception {
    
    // Parameterless Constructor
    public StorageException() {}

    // Constructor that accepts a message
    public StorageException(String message)
    {
       super(message);
    }
}
