package com.ldz.addInputHandlerTest;

import com.ldz.Main;
import com.ldz.generic.AbstractGUITask;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.UINodes.addInputHandler.IIsInputAddable;
import com.ldz.view.UINodes.factory.NodeFactory;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.YamlToController;
import com.ldz.view.stages.SpringNodeCreatorScene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

/**
 * Created by loicd on 04/01/2017.
 * Workflow -> creation of 1 IsInputAddable node
 *          -> click on the add button
 *          -> input UINodePoint is correctly added and displayed to screen
 */
public class MainAddInputHandlerTest extends FxRobot{


    private YamlToController _yamlToController = null;
    private static Stage _stage = null;

    @BeforeClass
    public static void setupCurrentStage() throws Exception{


        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");


        _stage = FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        Main app = (Main) FxToolkit.setupApplication(Main.class);
        System.out.println("setting up application");

        new AbstractGUITask() {
            @Override
            public void GUITask() {
                _yamlToController = YamlToController.getInstance();
            }
        };

    }

    @Test
    public void test(){

        //creation of IsInputAddable
        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.createUINode(NodeFactory.NodeType.LIST, 0,0, "this is a list");
            }
        };

        AbstractUiNode uiListNode = _yamlToController.getChilds().get(0);
        Assert.assertTrue(uiListNode instanceof IIsInputAddable);
        Assert.assertTrue(uiListNode.getChilds().get(0).getInputChildrens().size() == 0);

        final Button addInputButton = (Button) uiListNode.lookup("#addInputListButton");

        new AbstractGUITask(){
            public void GUITask() {
                addInputButton.fire();
            }
        };

        Assert.assertTrue(uiListNode.getChilds().get(0).getInputChildrens().size() == 1);
        Assert.assertTrue(!uiListNode.getChilds().get(0).getInputChildrens().get(0).get_carriedData().values().isEmpty());
        Assert.assertTrue(uiListNode.getChilds().get(0).getInputChildrens().get(0).get_carriedData().values().iterator().next() instanceof IYamlDomain);
        Assert.assertTrue(uiListNode.getChilds().get(0).getInputChildrens().get(0).isVisible());

        new AbstractGUITask(){
            public void GUITask() {
                addInputButton.fire();
                addInputButton.fire();
                addInputButton.fire();
            }
        };

        Assert.assertTrue(uiListNode.getChilds().get(0).getInputChildrens().size() == 4);

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
                SpringNodeCreatorScene springNodeCreatorScene = SpringNodeCreatorScene.getInstance();
                springNodeCreatorScene.getWindow().hide();
            }
        };
    }

}
