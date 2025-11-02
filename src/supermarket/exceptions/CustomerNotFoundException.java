// Create a new exception
package supermarket.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(int customerId) {
        super("Customer with ID " + customerId + " not found.");
    }
}

