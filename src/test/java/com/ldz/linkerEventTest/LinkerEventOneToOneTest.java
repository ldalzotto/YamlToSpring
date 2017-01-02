package com.ldz.linkerEventTest;

import com.ldz.Main;
import com.ldz.controller.YamlLoadingController;
import com.ldz.generic.AbstractGUITask;
import com.ldz.model.Operation;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.LinkerEventHandler;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.SpringNode;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.factory.NodeFactory;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.YamlToController;
import com.ldz.view.stages.SpringNodeCreatorScene;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by loicd on 02/01/2017.
 *   Workflow -> creation of 1 Yaml Node and 1 Spring Node
 *     -> The yaml node contains 1 output
 *     -> Spring node got only one input node
 *     -> create link first to first
 *     -> data is correctly passed
 *     -> recreate link first to first
 *     -> data is correctly passed and is the same
 */
public class LinkerEventOneToOneTest extends FxRobot{

    private MainScene _mainScene = null;
    private YamlToController _yamlToController = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private YamlLoadingController _yamlLoadingControllerSpy = null;
    private Map<String, IYamlDomain> _mockedCarriedData = null;
    private NodeFactory _nodeFactory = NodeFactory.getInstance();
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
                _mainScene = MainScene.getInstance();
                _yamlToController = YamlToController.getInstance();
                _yamlLoadingController = YamlLoadingController.getInstance();
            }
        };

        //Mocking carried data
        _mockedCarriedData = new HashMap<String, IYamlDomain>();
        _mockedCarriedData.put("api/v1/test", new Operation());

        _yamlLoadingControllerSpy = Mockito.spy(_yamlLoadingController);
        Mockito.when(_yamlLoadingControllerSpy.getOperationsFromPath(Mockito.any(Path.class))).thenReturn(_mockedCarriedData);
        Mockito.when(_yamlLoadingControllerSpy.getPathFromRessourceName(Mockito.anyString())).thenReturn(new Path());
        _nodeFactory.set_yamlLoadingController(_yamlLoadingControllerSpy);
    }

    @Test
    public void test(){


        final int yamlNode1X = ThreadLocalRandom.current().nextInt(-100,100);
        final int yamlNode1Y = ThreadLocalRandom.current().nextInt(-100,100);
        final int springNode1X = ThreadLocalRandom.current().nextInt(-100,100);
        final int springNode1Y = ThreadLocalRandom.current().nextInt(-100,100);


        //Creation of nodes
        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.createUINode(NodeFactory.NodeType.YAML, yamlNode1X, yamlNode1Y, "api/v1/test");
                _yamlToController.createUINode(NodeFactory.NodeType.SPRING, springNode1X, springNode1Y, "spring1");
                //_yamlToController.createYamlNode(yamlNode1X, yamlNode1Y, "api/v1/test");
                //_yamlToController.createSpringNode(springNode1X, springNode1Y, "spring1");
            }
        };

        Assert.assertTrue(_yamlToController.getChilds().size() == 2);

        YamlNode yamlNode = null;
        SpringNode springNode = null;
        for(AbstractUiNode abstractUiNode : _yamlToController.getChilds()){
            if(abstractUiNode instanceof YamlNode){
                yamlNode = (YamlNode) abstractUiNode;
            } else if (abstractUiNode instanceof SpringNode){
                springNode = (SpringNode) abstractUiNode;
            }
        }

        //create link between nodes
        List<Node> yamlStartNodes = new ArrayList<Node>();
        //get the two start nodes
        Iterator<LinkerEventHandler> linkerEventHandlerIterator1 = _yamlToController.get_nodeLinkerEventHandlerMap().keySet().iterator();
        LinkerEventHandler linkerEventHandler1 = null;

        while (linkerEventHandlerIterator1.hasNext()){
            LinkerEventHandler linkerEventHandler = linkerEventHandlerIterator1.next();
            if(linkerEventHandler1 == null){
                linkerEventHandler1 = linkerEventHandler;
            }

            Map<Node, Node> link = _yamlToController.get_nodeLinkerEventHandlerMap().get(linkerEventHandler);
            Assert.assertTrue(link.keySet().size() == 1);
            Iterator<Node> nodeIterator = link.keySet().iterator();
            yamlStartNodes.add(nodeIterator.next());
        }

        //the end node is the spring one
        final Node yamlStartNode1 = yamlStartNodes.get(0);
        UINodePoint springEndNode =  springNode.getChilds().get(0).getChilds().get(0);

        createLink(springNode, yamlStartNode1);

        final Line linkLineBefore = linkerEventHandler1.get_line();

        Assert.assertTrue(linkerEventHandler1.get_line().isVisible());
        Assert.assertTrue(springEndNode.get_carriedData().equals(((UINodePoint)yamlStartNode1).get_carriedData()));

        createLink(springNode, yamlStartNode1);

        Assert.assertTrue(linkerEventHandler1.get_line().isVisible());
        Assert.assertTrue(springEndNode.get_carriedData().equals(((UINodePoint)yamlStartNode1).get_carriedData()));

        //Assert about the same position of line
        Assert.assertTrue(linkLineBefore.equals(linkerEventHandler1.get_line()));

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
                SpringNodeCreatorScene springNodeCreatorScene = SpringNodeCreatorScene.getInstance();
                springNodeCreatorScene.getWindow().hide();
            }
        };


    }

    private void createLink(SpringNode springNode, final Node yamlStartNode1) {
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


        UINodePoint springInputUiNodePoint = springNode.getChilds().get(0).getChilds().get(0);
        Bounds releaseBound = springInputUiNodePoint.localToScreen(springInputUiNodePoint.getBoundsInLocal());
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
