package fr.oltruong.teamag.backingbean;

import fr.oltruong.teamag.utils.MessageManager;
import org.slf4j.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Controller {

    @Inject
    private MessageManager messageManager;

    @Inject
    private Logger logger;

    protected MessageManager getMessageManager() {
        return messageManager;
    }

    protected String getMessage(String msgKey, Object... args) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale, classLoader);
        String msgValue = bundle.getString(msgKey);
        return MessageFormat.format(msgValue, args);
    }

    public Logger getLogger() {
        return logger;
    }

}
