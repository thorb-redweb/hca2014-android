package dk.redweb.hca2014;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/28/13
 * Time: 14:17
 */
public class NotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotImplementedException(){}

    public NotImplementedException(String message){
        super(message);
    }
}
