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
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.UINodes.generic.node.UINodePoint;
import com.ldz.view.YamlToController;
import com.ldz.view.menu.YamlWorkspaceContextMenu;
import com.ldz.view.stages.SpringNodeCreatorScene;
import com.ldz.view.stages.SpringNodeCreatorStage;
import javafx.stage.Stage;
import org.junit.*;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
public class LinkerEventTest extends FxRobot {


    private MainScene _mainScene = null;
    private YamlToController _yamlToController = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();
    private YamlLoadingController _yamlLoadingControllerSpy = null;
    private SpringNodeCreatorStage _springNodeCreatorStage = null;
    private Map<String, IYamlDomain> _mockedCarriedData = null;
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
                _springNodeCreatorStage = SpringNodeCreatorStage.getInstance();
            }
        };

        //Mocking carried data
        _mockedCarriedData = new HashMap<String, IYamlDomain>();
        _mockedCarriedData.put("api/v1/test", new Operation());
        _mockedCarriedData.put("api/v2/test", new Operation());

        _yamlLoadingControllerSpy = Mockito.spy(_yamlLoadingController);
        Mockito.when(_yamlLoadingControllerSpy.getOperationsFromPath(Mockito.any(Path.class))).thenReturn(_mockedCarriedData);
        Mockito.when(_yamlLoadingControllerSpy.getPathFromRessourceName(Mockito.anyString())).thenReturn(new Path());
        _yamlToController.set_yamlLoadingController(_yamlLoadingControllerSpy);
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
                _yamlToController.createYamlNode(yamlNode1X, yamlNode1Y, "api/v1/test");
                _yamlToController.createSpringNode(springNode1X, springNode1Y, "spring1");
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
        Assert.assertTrue(_yamlToController.get_nodeLinkerEventHandlerMap().keySet().size() == 2);
        Iterator<LinkerEventHandler> linkerEventHandlerIterator = _yamlToController.get_nodeLinkerEventHandlerMap().keySet().iterator();
        while (linkerEventHandlerIterator.hasNext()){
            LinkerEventHandler linkerEventHandler = linkerEventHandlerIterator.next();

            //Identity of two input points
            Assert.assertTrue(_yamlToController.get_nodeLinkerEventHandlerMap().get(linkerEventHandler).containsKey(yamlNode.getChilds().get(0).getChilds().get(0))
                    || _yamlToController.get_nodeLinkerEventHandlerMap().get(linkerEventHandler).containsKey(yamlNode.getChilds().get(0).getChilds().get(1)));

        }

        //Assert about the spring node
        Assert.assertTrue(springNode.getChilds().size() == 1);
        Assert.assertTrue(springNode.getChilds().get(0).getChilds().size() == 1);
        Assert.assertTrue(springNode.getChilds().get(0).getOutputChildren().size() == 0);
        Assert.assertTrue(springNode.getChilds().get(0).getInputChildrens().size() == 1);



        //create link between nodes

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
                _springNodeCreatorStage.close();
                _springNodeCreatorStage.refreshInstance();
                SpringNodeCreatorScene springNodeCreatorScene = SpringNodeCreatorScene.getInstance();
                springNodeCreatorScene.getWindow().hide();
            }
        };

    }
}
