package exceptions;

public class Error {
    private boolean status;
    private String message;
    private int line;

    public Error(boolean status) {
        this.status = status;
    }

    public Error(boolean status, String message, int line) {
        this.status = status;
        this.message = message;
        this.line = line;
    }

    public Error(String message, int line) {
        this.message = message;
        this.line = line;
    }

    public Error() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
