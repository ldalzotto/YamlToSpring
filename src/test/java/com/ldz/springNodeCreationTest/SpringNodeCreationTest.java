package com.ldz.springNodeCreationTest;

import com.ldz.Main;
import com.ldz.generic.AbstractGUITask;
import com.ldz.generic.AbstractGUITaskWithoutCompletion;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.SpringNode;
import com.ldz.view.YamlToController;
import com.ldz.view.YamlTree;
import com.ldz.view.menu.YamlWorkspaceContextMenu;
import com.ldz.view.stages.SpringNodeCreatorScene;
import com.ldz.view.stages.SpringNodeCreatorStage;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.*;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ldalzotto on 29/12/2016.
 * Workflow :
 *  - right click on workspace
 *  - select creata yaml node on context menu
 *  - enter controller name on popup
 *  -> node created
 */
public class SpringNodeCreationTest extends FxRobot{

    private MainScene _mainScene = null;
    private YamlToController _yamlToController = null;
    private YamlWorkspaceContextMenu _yamlWorkspaceContextMenu = null;
    private SpringNodeCreatorStage _springNodeCreatorStage = null;
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
                _yamlWorkspaceContextMenu = YamlWorkspaceContextMenu.getInstance(_yamlToController);
                _springNodeCreatorStage = SpringNodeCreatorStage.getInstance();
            }
        };
    }



    @Test
    public void testNotdisplayed(){

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, false, false, true, false, false, false, new PickResult(YamlTree.getInstance(), new Point3D(0,0,0), 0)));

                SpringNode springNode = new SpringNode(0,0,"", null, null, Color.ALICEBLUE);

                _yamlToController.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, false, false, true, false, false, false, new PickResult(springNode, new Point3D(0,0,0), 0)));
            }
        };

        //check that context is not displayed
        Assert.assertTrue(!_yamlWorkspaceContextMenu.isShowing());

    }
    @Test
    public void isDisplayed(){
        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, false, false, true, false, false, false, new PickResult(_yamlToController, new Point3D(0,0,0), 0)));
            }
        };

        //check if context is well displayed
        Assert.assertTrue(_yamlWorkspaceContextMenu.isShowing());

        MenuItem springCreationMenuitem = null;
        boolean isMenuCorrectlyBuilt = false;
        for(MenuItem menuItem : _yamlWorkspaceContextMenu.getItems()){
            if(menuItem.getId().equals(YamlWorkspaceContextMenu.MENU_CREATE_PRING_ID)){
                isMenuCorrectlyBuilt = true;
                springCreationMenuitem = menuItem;
            }
        }

        final MenuItem finalMenuItem = springCreationMenuitem;

        Assert.assertTrue(isMenuCorrectlyBuilt);

        new AbstractGUITask(){
            public void GUITask() {
                _yamlWorkspaceContextMenu.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, false, false, true, false, false, false, new PickResult(new ContextMenuContent(_yamlWorkspaceContextMenu), new Point3D(0,0,0), 0)));
            }
        };

        //popUp window must not be diplayed because wrong button
        Assert.assertTrue(!_springNodeCreatorStage.isShowing());


        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, false, false, true, false, false, false, new PickResult(_yamlToController, new Point3D(0,0,0), 0)));
                _yamlWorkspaceContextMenu.fireEvent(new MouseEvent(MouseEvent.MOUSE_EXITED_TARGET, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, false, false, true, false, false, false, new PickResult(new ContextMenuContent(_yamlWorkspaceContextMenu), new Point3D(0,0,0), 0)));
            }
        };

        //popUp window must not be diplayed because of exit event
        Assert.assertTrue(!_springNodeCreatorStage.isShowing());


        //random node coordinates
        final int firstNodeScreenX = ThreadLocalRandom.current().nextInt(-100, 100);
        final int firstNodeScreenY = ThreadLocalRandom.current().nextInt(-100, 100);

        new AbstractGUITaskWithoutCompletion(250){
            public void GUITask() {
                _yamlWorkspaceContextMenu.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED,
                        firstNodeScreenX,firstNodeScreenY,firstNodeScreenX,firstNodeScreenY, MouseButton.SECONDARY, 0,
                        false, false, false, false, true, false, false, false, false, false, new PickResult(new ContextMenuContent(_yamlWorkspaceContextMenu).new MenuItemContainer(_yamlWorkspaceContextMenu.getItems().get(0)), new Point3D(0,0,0), 0)));
            }
        };

        //popUp window must be displayed
        Assert.assertTrue(_springNodeCreatorStage.isShowing());
        Assert.assertTrue(_springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName() != null);
        Assert.assertTrue(_springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText().isEmpty()
           || _springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText() == null
           || _springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText().equals(""));

        final Button button = _springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlNameOkButton();
        _springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().setText("controllerTest");

        new AbstractGUITask(){
            public void GUITask() {
                button.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, true, false, false, false, false, false, new PickResult(button, new Point3D(0,0,0), 0)));
            }
        };

        //spring node must be created with controllerTest name
        Assert.assertTrue(!_springNodeCreatorStage.isShowing());
        new AbstractGUITaskWithoutCompletion(300){
            public void GUITask() {

            }
        };
        Assert.assertTrue(_yamlToController.getChilds().size() == 1);
        Assert.assertTrue(_yamlToController.getChilds().get(0) instanceof SpringNode);
        Assert.assertTrue(((SpringNode)_yamlToController.getChilds().get(0)).get_nodeName().getText().equals("controllerTest"));

        //Check node position
        Assert.assertTrue(((SpringNode)_yamlToController.getChilds().get(0)).getLayoutX() == _yamlToController.screenToLocal(firstNodeScreenX,firstNodeScreenY).getX());
        Assert.assertTrue(((SpringNode)_yamlToController.getChilds().get(0)).getLayoutY() == _yamlToController.screenToLocal(firstNodeScreenX,firstNodeScreenY).getY());


        //controller name is resetted
        new AbstractGUITaskWithoutCompletion(250){
            public void GUITask() {
                _yamlWorkspaceContextMenu.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, true, false, false, false, false, false, new PickResult(new ContextMenuContent(_yamlWorkspaceContextMenu).new MenuItemContainer(_yamlWorkspaceContextMenu.getItems().get(0)), new Point3D(0,0,0), 0)));
            }
        };

        Assert.assertTrue(_springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText().isEmpty()
                || _springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText() == null
                || _springNodeCreatorStage.get_SpringNodeCreatorScene().get_yamlControllerName().getText().equals(""));

        new AbstractGUITask(){
            public void GUITask() {
                button.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, 0,0,0,0, MouseButton.SECONDARY, 0,
                        false, false, false, false, true, false, false, false, false, false, new PickResult(button, new Point3D(0,0,0), 0)));
            }
        };

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
