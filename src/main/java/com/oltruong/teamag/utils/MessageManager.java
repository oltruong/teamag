package com.oltruong.teamag.utils;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Olivier Truong
 */
public class MessageManager {

    public static final int INFORMATION = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    public static final int FATAL = 3;


    public void displayMessage(int type, String titleKey, Object... args) {
        displayMessageWithDescription(type, titleKey, null, args);
    }

    public void displayMessageWithDescription(int type, String titleKey, String descriptionKey, Object... args) {
        String descriptionMessage = "";
        if (descriptionKey != null) {
            descriptionMessage = getMessage(descriptionKey, args);
        }
        FacesMessage message = new FacesMessage(getSeverity(type), getMessage(titleKey), descriptionMessage);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public String getMessage(String msgKey, Object... args) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale, classLoader);
        String msgValue = bundle.getString(msgKey);
        return MessageFormat.format(msgValue, args);
    }

    private FacesMessage.Severity getSeverity(int type) {
        FacesMessage.Severity severity = null;

        switch (type) {
            case INFORMATION:
                severity = FacesMessage.SEVERITY_INFO;
                break;
            case WARNING:
                severity = FacesMessage.SEVERITY_WARN;
                break;
            case ERROR:
                severity = FacesMessage.SEVERITY_ERROR;
                break;
            case FATAL:
                severity = FacesMessage.SEVERITY_FATAL;
                break;
            default:
                throw new IllegalArgumentException("Type unknown [" + type + "]");

        }
        return severity;

    }
}
