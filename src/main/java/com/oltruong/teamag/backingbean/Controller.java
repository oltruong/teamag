package com.oltruong.teamag.backingbean;

import com.oltruong.teamag.utils.MessageManager;
import javax.inject.Inject;
import org.slf4j.Logger;

public abstract class Controller {

    @Inject
    private MessageManager messageManager;

    @Inject
    private Logger logger;

    protected MessageManager getMessageManager() {
        return messageManager;
    }

    protected String getMessage(String msgKey, Object... args) {
        return getMessageManager().getMessage(msgKey, args);

    }

    public Logger getLogger() {
        return logger;
    }

}
