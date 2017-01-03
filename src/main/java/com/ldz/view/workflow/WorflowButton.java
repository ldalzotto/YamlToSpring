package com.ldz.view.workflow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Created by loicd on 03/01/2017.
 */
public class WorflowButton extends Button {

    private final WorkflowManager _workflowManager = WorkflowManager.getInstance();
    private static WorflowButton _instance = null;

    public static WorflowButton getInstance(){
        if(_instance == null){
            _instance = new WorflowButton();
        }
        return _instance;
    }

    private WorflowButton(){
        this.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("Executing the workflow");
                _workflowManager.executeWorkflow();
            }
        });
    }

}
