package com.ldz.wrkflowTest;

import com.ldz.Main;
import com.ldz.controller.YamlLoadingController;
import com.ldz.generic.AbstractGUITask;
import com.ldz.model.Operation;
import com.ldz.model.Operations;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.SpringNode;
import com.ldz.view.UINodes.UIListNode;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.factory.NodeFactory;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.YamlToController;
import com.ldz.view.linker.LinkerEventManager;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.*;

/**
 * Created by loicd on 04/01/2017.
 *  Workflow : -> create 2 YAML, 1 node implementing IWorkflowExecution (uiNodeList here)
 *          -> add 2 input to list
 *          -> link yaml1 -> 1List
 *          -> link yaml2 -> 2Link
 *          -> press workflow button
 *          -> ensure that operation is well performed
 */
public class MainWorkflowTest extends FxRobot{

    private MainScene _mainScene = null;
    private YamlToController _yamlToController = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private final LinkerEventManager _linkerEventManager = LinkerEventManager.getInstance();
    private NodeFactory _nodeFactory = NodeFactory.getInstance();
    private static Stage _stage = null;

    //mocked data
    private YamlLoadingController _yamlLoadingControllerSpy = null;
    private Map<String, IYamlDomain> _mockedCarriedData1 = null;
    private Map<String, IYamlDomain> _mockedCarriedData2 = null;

    private Path _yamlNode1Path = null;
    private Path _yamlNode2Path = null;

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
                _mainScene = MainScene.getInstance();
                _yamlToController = YamlToController.getInstance();
                _yamlLoadingController = YamlLoadingController.getInstance();
            }
        };

        //Mocking carried data
        _mockedCarriedData1 = new HashMap<String, IYamlDomain>();
        _mockedCarriedData1.put("api/v1/test", new Operation());
        _mockedCarriedData1.put("api/v2/test", new Operation());

        _mockedCarriedData2 = new HashMap<String, IYamlDomain>();
        _mockedCarriedData2.put("api/v3/test", new Operation());

        _yamlNode1Path = new Path();
        _yamlNode2Path = new Path();

        _yamlLoadingControllerSpy = Mockito.spy(_yamlLoadingController);

        Mockito.when(_yamlLoadingControllerSpy.getFullRessourceName("api/v1/test")).thenReturn("api/v1/test");
        Mockito.when(_yamlLoadingControllerSpy.getFullRessourceName("api/v3/test")).thenReturn("api/v3/test");


        Mockito.when(_yamlLoadingControllerSpy.getPathFromFullRessourceName("api/v1/test")).thenReturn(_yamlNode1Path);
        Mockito.when(_yamlLoadingControllerSpy.getPathFromFullRessourceName("api/v3/test")).thenReturn(_yamlNode2Path);

        Mockito.when(_yamlLoadingControllerSpy.getOperationsFromPathAndFullRessourceName(Mockito.eq(_yamlNode1Path), Mockito.anyString())).thenReturn(_mockedCarriedData1);
        Mockito.when(_yamlLoadingControllerSpy.getOperationsFromPathAndFullRessourceName(Mockito.eq(_yamlNode2Path), Mockito.anyString())).thenReturn(_mockedCarriedData2);

        _nodeFactory.set_yamlLoadingController(_yamlLoadingControllerSpy);
    }

    @Test
    public void transitDataToItselfAndOutputNodes(){

        //coordinates
        final double yamlNode1X = 10;
        final double yamlNode1Y = 20;
        final double yamlNode2X = 30;
        final double yamlNode2Y = 40;
        final double listNode1X = 50;
        final double listNode1Y = 60;
        final double springNode1X = 70;
        final double springNode1Y = 80;

        //Creation of nodes
        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.createUINode(NodeFactory.NodeType.YAML, yamlNode1X, yamlNode1Y, "api/v1/test");
                _yamlToController.createUINode(NodeFactory.NodeType.YAML, yamlNode2X, yamlNode2Y, "api/v3/test");
                _yamlToController.createUINode(NodeFactory.NodeType.LIST, listNode1X, listNode1Y, "list1");
                _yamlToController.createUINode(NodeFactory.NodeType.SPRING, springNode1X, springNode1Y, "spring1");
            }
        };

        Assert.assertTrue(_yamlToController.getChilds().size() == 4);

        YamlNode yamlNode1 = null;
        YamlNode yamlNode2 = null;
        UIListNode listNode1 = null;
        SpringNode springNode1 = null;

        for(AbstractUiNode abstractUiNode : _yamlToController.getChilds()){
            if(abstractUiNode instanceof YamlNode){
                if(abstractUiNode.getId().equals("api/v1/test")){
                    yamlNode1 = (YamlNode) abstractUiNode;
                } else if (abstractUiNode.getId().equals("api/v3/test")){
                    yamlNode2 = (YamlNode) abstractUiNode;
                }
            } else if (abstractUiNode instanceof UIListNode){
                listNode1 = (UIListNode) abstractUiNode;
            } else if (abstractUiNode instanceof SpringNode){
                springNode1 = (SpringNode) abstractUiNode;
            }
        }

        //UINodes
        UINodePoint yaml1NodePoit = null;
        UINodePoint yaml2NodePoint = null;
        UINodePoint list1OutputPoint = null;

        UINodePoint spring1InputNodePoint = null;

        //Assert content ofcarried data
        //Assert that carried data contains only 1 entry
        for(UINodePoint uiNodePoint : yamlNode1.getChilds().get(0).getOutputChildren()){
            Assert.assertTrue(uiNodePoint.get_carriedData().keySet().size() == 1);
            if(uiNodePoint.get_carriedData().containsKey("api/v1/test")){
                yaml1NodePoit = uiNodePoint;
                Assert.assertTrue(uiNodePoint.get_carriedData().get("api/v1/test").equals(_mockedCarriedData1.get("api/v1/test")));
            }
        }
        for(UINodePoint uiNodePoint : yamlNode2.getChilds().get(0).getOutputChildren()){
            Assert.assertTrue(uiNodePoint.get_carriedData().keySet().size() == 1);
            if(uiNodePoint.get_carriedData().containsKey("api/v3/test")){
                yaml2NodePoint = uiNodePoint;
                Assert.assertTrue(uiNodePoint.get_carriedData().get("api/v3/test").equals(_mockedCarriedData2.get("api/v3/test")));
            }
        }

        for(UINodePoint uiNodePoint : listNode1.getChilds().get(0).getOutputChildren()){
            Assert.assertTrue(uiNodePoint.get_carriedData().keySet().size() == 1);
            list1OutputPoint = uiNodePoint;
        }

        for(UINodePoint uiNodePoint : springNode1.getChilds().get(0).getInputChildrens()){
            Assert.assertTrue(uiNodePoint.get_carriedData().keySet().size() == 1);
            spring1InputNodePoint = uiNodePoint;
        }

        //create input node of list
        final Button addInputButton = (Button) listNode1.lookup("#addInputListButton");

        new AbstractGUITask(){
            public void GUITask() {
                addInputButton.fire();
                addInputButton.fire();
            }
        };

        Assert.assertTrue(listNode1.getChilds().get(0).getInputChildrens().size() == 2);

        UINodePoint listInputPoint1 = listNode1.getChilds().get(0).getInputChildrens().get(0);
        UINodePoint listInputPoint2 = listNode1.getChilds().get(0).getInputChildrens().get(1);

        //create linkers
        createLink(listInputPoint1 , yaml1NodePoit);
        createLink(listInputPoint2 , yaml2NodePoint);
        createLink(spring1InputNodePoint, list1OutputPoint);

        //click on workflow button
        final Button workflowButton = (Button) lookup("#workflowButton").query();

        new AbstractGUITask(){
            public void GUITask() {
                workflowButton.fire();
            }
        };

        Map<String, Object> endOfNodeCarriedData = listNode1.getChilds().get(0).getOutputChildren().get(0).get_carriedData();
        Object endOfNodeData = endOfNodeCarriedData.entrySet().iterator().next().getValue();

        Assert.assertTrue(endOfNodeData instanceof Operations);
        Operations endOfNodeDataOperations = (Operations) endOfNodeData;
        Assert.assertTrue(endOfNodeDataOperations.get_operations().size() == 2);
        Assert.assertTrue(endOfNodeDataOperations.get_operations().contains(_mockedCarriedData1.get("api/v1/test")));
        Assert.assertTrue(endOfNodeDataOperations.get_operations().contains(_mockedCarriedData2.get("api/v3/test")));

        Map<String, Object> springNodeCarriedData = spring1InputNodePoint.get_carriedData();
        Object springInputCarriedData = springNodeCarriedData.entrySet().iterator().next().getValue();
        Assert.assertTrue(springInputCarriedData instanceof Operations);
        Assert.assertTrue(springInputCarriedData.equals(endOfNodeDataOperations));

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
            }
        };

    }

    private void createLink(UINodePoint pointTarget, final Node yamlStartNode1) {
        new AbstractGUITask(){
            public void GUITask() {
                yamlStartNode1.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.PRIMARY, 0, false, false
                        , false, false, true, false, false, false, false, false, null));
            }
        };

        //draggin mouse
        new AbstractGUITask(){
            public void GUITask() {
                yamlStartNode1.fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED, 100, 100, 100, 100, MouseButton.PRIMARY,
                        0, false,false,false,false,false,false,false,false,false,false,null));
            }
        };

        Bounds releaseBound = pointTarget.localToScreen(pointTarget.getBoundsInLocal());
        final double releaseX = releaseBound.getMinX() + ((releaseBound.getMaxX()-releaseBound.getMinX())/2);
        final double releaseY = releaseBound.getMinY() + ((releaseBound.getMaxY()-releaseBound.getMinY())/2);

        new AbstractGUITask(){
            public void GUITask() {
                yamlStartNode1.fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, releaseX, releaseY, releaseX, releaseY, MouseButton.PRIMARY,
                        0, false,false,false,false,false,false,false,false,false,false,null));
            }
        };
    }
}
