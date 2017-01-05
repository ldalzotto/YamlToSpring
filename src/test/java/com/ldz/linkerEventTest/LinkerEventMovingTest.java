package com.ldz.linkerEventTest;

import com.ldz.Main;
import com.ldz.controller.YamlLoadingController;
import com.ldz.generic.AbstractGUITask;
import com.ldz.model.Operation;
import com.ldz.model.Path;
import com.ldz.model.generic.IYamlDomain;
import com.ldz.view.linker.LinkerEventHandler;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.SpringNode;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.factory.NodeFactory;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.linker.LinkerEventManager;
import com.ldz.view.YamlToController;
import com.ldz.view.stages.SpringNodeCreatorScene;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.stage.Stage;
import org.junit.*;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ldalzotto on 30/12/2016.
 *  Workflow -> creation of 1 Yaml Node and 2 Spring Node
 *     -> Spring node got 1 input
 *     -> Yaml node got 1 output
 *     -> create link between 1->1 and 2->2
 *     -> links are correctly created (check position)
 *     -> move yaml node
 *     -> lanks are correctly moves (check position)
 */
public class LinkerEventMovingTest extends FxRobot{


    private MainScene _mainScene = null;
    private YamlToController _yamlToController = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private YamlLoadingController _yamlLoadingControllerSpy = null;
    private Map<String, IYamlDomain> _mockedCarriedData = null;
    private NodeFactory _nodeFactory = NodeFactory.getInstance();
    private final LinkerEventManager _linkerEventManager = LinkerEventManager.getInstance();
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
        _mockedCarriedData.put("api/v2/test", new Operation());

        _yamlLoadingControllerSpy = Mockito.spy(_yamlLoadingController);
        Mockito.when(_yamlLoadingControllerSpy.getOperationsFromPathAndFullRessourceName(Mockito.any(Path.class), Mockito.anyString())).thenReturn(_mockedCarriedData);
        Mockito.when(_yamlLoadingControllerSpy.getPathFromFullRessourceName(Mockito.anyString())).thenReturn(new Path());
        _nodeFactory.set_yamlLoadingController(_yamlLoadingControllerSpy);
    }

    @Test
    public void test(){
        final int yamlNode1X = ThreadLocalRandom.current().nextInt(-100,100);
        final int yamlNode1Y = ThreadLocalRandom.current().nextInt(-100,100);
        final int springNode1X = ThreadLocalRandom.current().nextInt(-100,100);
        final int springNode1Y = ThreadLocalRandom.current().nextInt(-100,100);
        final int springNode2X = ThreadLocalRandom.current().nextInt(-100,100);
        final int springNode2Y = ThreadLocalRandom.current().nextInt(-100,100);

        //Creation of nodes
        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.createUINode(NodeFactory.NodeType.YAML, yamlNode1X, yamlNode1Y, "api/v1/test");
                _yamlToController.createUINode(NodeFactory.NodeType.SPRING, springNode1X, springNode1Y, "spring1");
                _yamlToController.createUINode(NodeFactory.NodeType.SPRING, springNode2X, springNode2Y, "spring2");
                //_yamlToController.createYamlNode(yamlNode1X, yamlNode1Y, "api/v1/test");
                //_yamlToController.createSpringNode(springNode1X, springNode1Y, "spring1");
                //_yamlToController.createSpringNode(springNode2X, springNode2Y, "spring2");
            }
        };

        Assert.assertTrue(_yamlToController.getChilds().size() == 3);

        YamlNode yamlNode = null;
        SpringNode springNode1 = null;
        SpringNode springNode2 = null;
        for(AbstractUiNode abstractUiNode : _yamlToController.getChilds()){
            if(abstractUiNode instanceof YamlNode){
                yamlNode = (YamlNode) abstractUiNode;
            } else if (abstractUiNode instanceof SpringNode){
                if(abstractUiNode.get_nodeName().getText().equals("spring1")){
                    springNode1 = (SpringNode) abstractUiNode;
                } else {
                    springNode2 = (SpringNode) abstractUiNode;
                }
            }
        }

        //create link between nodes
        List<Node> yamlStartNodes = new ArrayList<Node>();
        //get the two start nodes
        Iterator<LinkerEventHandler> linkerEventHandlerIterator1 = _linkerEventManager.get_nodeLinkerEventHandlerMap().keySet().iterator();
        LinkerEventHandler linkerEventHandler1 = null;
        LinkerEventHandler linkerEventHandler2 = null;

        while (linkerEventHandlerIterator1.hasNext()){
            LinkerEventHandler linkerEventHandler = linkerEventHandlerIterator1.next();
            if(linkerEventHandler1 == null){
                linkerEventHandler1 = linkerEventHandler;
            } else if(linkerEventHandler2 == null){
                linkerEventHandler2 = linkerEventHandler;
            }
            Map<Node, Node> link = _linkerEventManager.get_nodeLinkerEventHandlerMap().get(linkerEventHandler);
            Assert.assertTrue(link.keySet().size() == 1);
            Iterator<Node> nodeIterator = link.keySet().iterator();
            yamlStartNodes.add(nodeIterator.next());
        }

        //the end node is the spring one
        final Node yamlStartNode1 = yamlStartNodes.get(0);
        final Node yamlStartNode2 = yamlStartNodes.get(1);

        //create linker events
        createLinkerEvent(springNode1, linkerEventHandler1, yamlStartNode1);
        createLinkerEvent(springNode2, linkerEventHandler2, yamlStartNode2);

        //get endLinePositions
        final Point2D line1End = new Point2D(linkerEventHandler1.get_line().getEndX(), linkerEventHandler1.get_line().getEndY());
        final Point2D line2End = new Point2D(linkerEventHandler2.get_line().getEndX(), linkerEventHandler2.get_line().getEndY());

        //move the YAML node
        final int deltaX = ThreadLocalRandom.current().nextInt(-50,50);
        final int deltaY = ThreadLocalRandom.current().nextInt(-50,50);

        final YamlNode yamlNodeToMove = yamlNode;

        //get screen coordinates of yamlTo Move
        final Bounds yamlNodeToMoveScreenBounds = yamlNodeToMove.localToScreen(yamlNodeToMove.getBoundsInLocal());

        new AbstractGUITask(){
            public void GUITask() {
                yamlNodeToMove.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, yamlNodeToMoveScreenBounds.getMinX(), yamlNodeToMoveScreenBounds.getMinY(),
                        yamlNodeToMoveScreenBounds.getMinX(), yamlNodeToMoveScreenBounds.getMinY(), MouseButton.SECONDARY, 0, false,false,false,false,false,false,true, false, false
                        , false, null));
                yamlNodeToMove.fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED, yamlNodeToMoveScreenBounds.getMinX()+deltaX, yamlNodeToMoveScreenBounds.getMinY()+deltaY,
                        yamlNodeToMoveScreenBounds.getMinX()+deltaX, yamlNodeToMoveScreenBounds.getMinY()+deltaY, MouseButton.SECONDARY, 0, false,false,false,false,false,false,true, false, false
                        , false, new PickResult(YamlToController.getInstance(), new Point3D(0,0,0), 0)));
                yamlNodeToMove.fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, yamlNodeToMoveScreenBounds.getMinX()+deltaX, yamlNodeToMoveScreenBounds.getMinY()+deltaY,
                        yamlNodeToMoveScreenBounds.getMinX()+deltaX, yamlNodeToMoveScreenBounds.getMinY()+deltaY, MouseButton.SECONDARY, 0, false,false,false,false,false,false,false, false, false
                        , false, null));
            }
        };

        //get endLinePositions
        final Point2D line1EndAfterMove = new Point2D(linkerEventHandler1.get_line().getEndX(), linkerEventHandler1.get_line().getEndY());
        final Point2D line2EndAfterMove = new Point2D(linkerEventHandler2.get_line().getEndX(), linkerEventHandler2.get_line().getEndY());

        //check positions of all linkers
        Bounds yamlStartNode1BoundsScreen = yamlStartNode1.localToScreen(yamlStartNode1.getBoundsInLocal());
        Point2D line1Start = new Point2D(linkerEventHandler1.get_line().getStartX(), linkerEventHandler1.get_line().getStartY());
        Point2D line1StartScreen = linkerEventHandler1.get_line().localToScreen(line1Start);

        Assert.assertTrue(yamlStartNode1BoundsScreen.contains(line1StartScreen));
        //Assert that the end line has not moved
        Assert.assertTrue(line1End.equals(line1EndAfterMove));

        Bounds yamlStartNode2BoundsScreen = yamlStartNode2.localToScreen(yamlStartNode2.getBoundsInLocal());
        Point2D line2Start = new Point2D(linkerEventHandler2.get_line().getStartX(), linkerEventHandler2.get_line().getStartY());
        Point2D line2StartScreen = linkerEventHandler2.get_line().localToScreen(line2Start);

        Assert.assertTrue(yamlStartNode2BoundsScreen.contains(line2StartScreen));
        //Assert that the end line has not moved
        Assert.assertTrue(line2End.equals(line2EndAfterMove));


        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
                SpringNodeCreatorScene springNodeCreatorScene = SpringNodeCreatorScene.getInstance();
                springNodeCreatorScene.getWindow().hide();
            }
        };

    }

    private void createLinkerEvent(SpringNode springNode1, LinkerEventHandler linkerEventHandler1, final Node yamlStartNode1) {
        UINodePoint springEndNode =  springNode1.getChilds().get(0).getChilds().get(0);

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


        UINodePoint springInputUiNodePoint = springNode1.getChilds().get(0).getChilds().get(0);
        Bounds releaseBound = springInputUiNodePoint.localToScreen(springInputUiNodePoint.getBoundsInLocal());
        final double releaseX = releaseBound.getMinX() + ((releaseBound.getMaxX()-releaseBound.getMinX())/2);
        final double releaseY = releaseBound.getMinY() + ((releaseBound.getMaxY()-releaseBound.getMinY())/2);

        new AbstractGUITask(){
            public void GUITask() {
                yamlStartNode1.fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, releaseX, releaseY, releaseX, releaseY, MouseButton.PRIMARY,
                        0, false,false,false,false,false,false,false,false,false,false,null));
            }
        };

        Assert.assertTrue(linkerEventHandler1.get_line().isVisible());
        Assert.assertTrue(springEndNode.get_carriedData().equals(((UINodePoint)yamlStartNode1).get_carriedData()));
    }
}