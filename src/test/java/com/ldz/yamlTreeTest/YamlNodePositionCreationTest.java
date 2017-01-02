package com.ldz.yamlTreeTest;

import com.google.common.collect.ImmutableList;
import com.ldz.Main;
import com.ldz.generic.AbstractGUITask;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.YamlFileChooserDialog;
import com.ldz.view.YamlToController;
import com.ldz.view.YamlTree;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by loicd on 02/01/2017.
 * Workflow -> create a yaml Node from yaml tree by drag & drop
 *      -> check position of node
 */
public class YamlNodePositionCreationTest extends FxRobot{

    private MainScene _mainScene = null;
    private static Stage _stage = null;

    private YamlToController _yamlToController = null;

    private static final ImmutableList<String> _fullRessourcesName = ImmutableList.of("/v1/products",
            "/v1/estimates/price",
            "/v1/estimates/time",
            "/v1/me",
            "/v1/history");



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

        new AbstractGUITask(){
            public void GUITask() {
                _mainScene = MainScene.getInstance();
                _yamlToController = YamlToController.getInstance();
            }
        };
    }


    @Test
    public void test(){

        YamlFileChooserDialog fileChooserDialog = Mockito.mock(YamlFileChooserDialog.class);
        Mockito.when(fileChooserDialog.initializeYamlFileChooser())
                .thenReturn(new File("src/test/uber.yaml"));
        _mainScene.set_yamlFileChooserDialog(fileChooserDialog);


        new AbstractGUITask(){
            public void GUITask() {
                _mainScene.get_menuFile().getItems().get(0).fire();
            }
        };

        //get final mouse screen position
        final int finalMouseScreenX = ThreadLocalRandom.current().nextInt(-100,100);
        final int finalMouseScreenY = ThreadLocalRandom.current().nextInt(-100,100);

        try {
            Field yamlTreeField = _mainScene.getClass().getDeclaredField("_yamlTree");
            yamlTreeField.setAccessible(true);
            final YamlTree yamlTree = (YamlTree) yamlTreeField.get(_mainScene);

            //clickOn(yamlTree.getRoot().getValue(), MouseButton.PRIMARY);
            yamlTree.getRoot().setExpanded(true);
            sleep(200, TimeUnit.MILLISECONDS);

            new AbstractGUITask(){
                public void GUITask() {
                    for(int i = 0; i < _fullRessourcesName.size(); i++) {
                        try {
                            lookup(_fullRessourcesName.get(i)).query().fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED,
                                    0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                            yamlTree.getSelectionModel().select(i);
                            lookup(_fullRessourcesName.get(i)).query().fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED,
                                    finalMouseScreenX, finalMouseScreenY, finalMouseScreenX, finalMouseScreenY, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                            lookup(_fullRessourcesName.get(i)).query().fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED,
                                    finalMouseScreenX, finalMouseScreenY, finalMouseScreenX, finalMouseScreenY, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true,
                                    new PickResult(_yamlToController, new Point3D(0, 0, 0), 0)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            YamlToController yamlToController = lookup("#YamlToController").query();
            Assert.assertTrue(yamlToController != null);
            Assert.assertTrue(!yamlToController.getChilds().isEmpty());
            Assert.assertTrue(yamlToController.getChilds().size() == _fullRessourcesName.size());

            //getYamlNode1Screen bound
            AbstractUiNode yamlNode1 = yamlToController.getChilds().get(0);
            Bounds yamlNode1BoundScreen = yamlNode1.localToScreen(yamlNode1.getBoundsInLocal());

            //assert position
            Assert.assertTrue( Math.round(yamlNode1BoundScreen.getMinX()) <= finalMouseScreenX  && finalMouseScreenX <= Math.round(yamlNode1BoundScreen.getMaxX()) );
            Assert.assertTrue( Math.round(yamlNode1BoundScreen.getMinY()) <= finalMouseScreenY  && finalMouseScreenY <= Math.round(yamlNode1BoundScreen.getMaxX()) );


        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse(e.getMessage(), true);
        }

        new AbstractGUITask(){
            public void GUITask() {
                _yamlToController.getChildren().clear();
            }
        };
    }
}
