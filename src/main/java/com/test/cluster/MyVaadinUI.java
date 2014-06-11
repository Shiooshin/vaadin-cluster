package com.test.cluster;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI implements Broadcaster.BroadcastListener {

    VerticalLayout layout = new VerticalLayout();


    @Override
    protected void init(VaadinRequest request) {
        Broadcaster.register(this);
        layout.setMargin(true);
        setContent(layout);

        Label getSession = new Label("Session : " + getSession().getSession().getId());
        final String sessionString = "Id : " + getId() + 
                " sessionId : " + getSession().getSession().getId() + 
                " isNew : " + getSession().getSession().isNew();
        layout.addComponent(getSession);

        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Broadcaster.broadcast(sessionString);
            }
        });
        button.setDebugId("debugId");
        layout.addComponent(button);
    }

    // Must also unregister when the UI expires    
    @Override
    public void detach() {
        Broadcaster.unregister(this);
        super.detach();
    }

    @Override
    public void receiveBroadcast(final String message) {
        // Must lock the session to execute logic safely
        access(new Runnable() {
            @Override
            public void run() {
                // Show it somehow
                layout.addComponent(new Label(message));
            }
        });
    }

}
