package org.example.utils.users;

import org.springframework.http.HttpStatus;

public class Error {

    private final int code;
    private final String message;
    private final String description;

    public Error(int code) {
        this(code, "", "");
    }

    public Error(int code, String message, String description) {

        this.code = code;
        this.description = description;

        if (message != null && !message.isBlank()) {
            this.message = message;
        } else {
            var status = HttpStatus.resolve(code);
            if (status != null)
                this.message = status.toString();
            else
                this.message = "";
        }
    }

    public String getRedirectPath() {

        String path = String.format("redirect:/error/%d", code);

        path = String.format("%s?message=%s", path, message);

        if (description != null && !description.isBlank())
            path = String.format("%s&description=%s", path, message);

        return path;
    }
}
