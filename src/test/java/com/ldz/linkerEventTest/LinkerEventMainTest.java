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
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.*;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ldalzotto on 30/12/2016.
 * Workflow -> creation of 1 Yaml Node and 1 Spring Node
 *     -> The yaml node contains multiple output
 *     -> The spring node must conains only 1 input node
 *     -> create link between them
 *     -> data is correctly passed
 *     -> only 1 data per Spring nodes nodes
 */
public class LinkerEventMainTest extends FxRobot {


    private MainScene _mainScene = null;
    private YamlToController _yamlToController = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private YamlLoadingController _yamlLoadingControllerSpy = null;
    private Map<String, IYamlDomain> _mockedCarriedData = null;
    private final LinkerEventManager _linkerEventManager = LinkerEventManager.getInstance();
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

        //Assert that there are two output nodes
        Assert.assertTrue(yamlNode.getChilds().size() == 1);
        Assert.assertTrue(yamlNode.getChilds().get(0).getChilds().size() == 2);
        Assert.assertTrue(yamlNode.getChilds().get(0).getOutputChildren().size() == 2);
        Assert.assertTrue(yamlNode.getChilds().get(0).getInputChildrens().size() == 0);

        //Assert content ofcarried data
        //Assert that carried data contains only 1 entry
        for(UINodePoint uiNodePoint : yamlNode.getChilds().get(0).getOutputChildren()){
            Assert.assertTrue(uiNodePoint.get_carriedData().keySet().size() == 1);
            if(uiNodePoint.get_carriedData().containsKey("api/v1/test")){
                Assert.assertTrue(uiNodePoint.get_carriedData().get("api/v1/test").equals(_mockedCarriedData.get("api/v1/test")));
            }
            if(uiNodePoint.get_carriedData().containsKey("api/v2/test")){
                Assert.assertTrue(uiNodePoint.get_carriedData().get("api/v2/test").equals(_mockedCarriedData.get("api/v2/test")));
            }
        }

        //ensure that yaml nodes posses Linker
        //Two input points
        Assert.assertTrue(_linkerEventManager.get_nodeLinkerEventHandlerMap().keySet().size() == 2);
        Iterator<LinkerEventHandler> linkerEventHandlerIterator = _linkerEventManager.get_nodeLinkerEventHandlerMap().keySet().iterator();
        while (linkerEventHandlerIterator.hasNext()){
            LinkerEventHandler linkerEventHandler = linkerEventHandlerIterator.next();

            //Identity of two input points
            Assert.assertTrue(_linkerEventManager.get_nodeLinkerEventHandlerMap().get(linkerEventHandler).containsKey(yamlNode.getChilds().get(0).getChilds().get(0))
                    || _linkerEventManager.get_nodeLinkerEventHandlerMap().get(linkerEventHandler).containsKey(yamlNode.getChilds().get(0).getChilds().get(1)));

        }

        //Assert about the spring node
        Assert.assertTrue(springNode.getChilds().size() == 1);
        Assert.assertTrue(springNode.getChilds().get(0).getChilds().size() == 1);
        Assert.assertTrue(springNode.getChilds().get(0).getOutputChildren().size() == 0);
        Assert.assertTrue(springNode.getChilds().get(0).getInputChildrens().size() == 1);


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
        UINodePoint springEndNode =  springNode.getChilds().get(0).getChilds().get(0);

        new AbstractGUITask(){
            public void GUITask() {
                yamlStartNode1.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.PRIMARY, 0, false, false
                , false, false, true, false, false, false, false, false, null));
            }
        };

        Bounds pointLocal = linkerEventHandler1.get_line().screenToLocal(yamlStartNode1.localToScreen(yamlStartNode1.getBoundsInLocal()));
        Assert.assertTrue(linkerEventHandler1.get_line().getStartX() == pointLocal.getMaxX());
        Assert.assertTrue(linkerEventHandler1.get_line().getStartY() == (pointLocal.getMaxY()+pointLocal.getMinY())/2);

        //draggin mous
        new AbstractGUITask(){
            public void GUITask() {
                yamlStartNode1.fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED, 100, 100, 100, 100, MouseButton.PRIMARY,
                        0, false,false,false,false,false,false,false,false,false,false,null));
            }
        };

        Assert.assertTrue(linkerEventHandler1.get_line().isVisible());
        Assert.assertTrue(linkerEventHandler1.get_line().getEndX() != 0);
        Assert.assertTrue(linkerEventHandler1.get_line().getEndY() != 0);


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

        Assert.assertTrue(linkerEventHandler1.get_line().isVisible());
        Assert.assertTrue(springEndNode.get_carriedData().equals(((UINodePoint)yamlStartNode1).get_carriedData()));

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
            }
        };

    }
}
