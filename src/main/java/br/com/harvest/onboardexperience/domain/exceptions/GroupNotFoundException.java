package br.com.harvest.onboardexperience.domain.exceptions;

public class GroupNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 714112649980240140L;

    private static final String GROUP_NOT_FOUND = "Group not found";

    public GroupNotFoundException(String message) {
        super(GROUP_NOT_FOUND + ": " + message);
    }

    public GroupNotFoundException(String message, Throwable cause) {
        super(GROUP_NOT_FOUND + ": " + message, cause);
    }

    public GroupNotFoundException(String message, String customCause) {
        super(GROUP_NOT_FOUND + ": " +  message);
    }
}
