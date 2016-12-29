package yamlTreeTest;

import com.google.common.collect.ImmutableList;
import com.ldz.Main;
import com.ldz.view.MainScene;
import com.ldz.view.YamlFileChooserDialog;
import com.ldz.view.YamlToController;
import com.ldz.view.YamlTree;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by ldalzotto on 28/12/2016.
 */
public class Drag_from_yamlTree_to_workspace extends FxRobot {

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
        FxToolkit.setupStage(new Consumer<Stage>() {
            public void accept(Stage stage) {
                stage.show();
            }
        });
    }

    @Before
    public void setup() throws Exception {
        Main app = (Main) FxToolkit.setupApplication(Main.class);
        System.out.println("setting up application");


        Platform.runLater(new Runnable() {
            public void run() {
                _mainScene = MainScene.getInstance();
                _yamlToController = YamlToController.getInstance();
            }
        });
    }


    @Test
    public void test(){
        sleep(1, TimeUnit.SECONDS);

        YamlFileChooserDialog fileChooserDialog = Mockito.mock(YamlFileChooserDialog.class);
        Mockito.when(fileChooserDialog.initializeYamlFileChooser())
                .thenReturn(new File("src/test/uber.yaml"));
        _mainScene.set_yamlFileChooserDialog(fileChooserDialog);

        clickOn(lookup("#fileMenu").query(), MouseButton.PRIMARY);
        Platform.runLater(new Runnable() {
            public void run() {
                _mainScene.get_menuFile().getItems().get(0).fire();
            }
        });

        clickOn(lookup("#yamlTree").query(), MouseButton.PRIMARY);

        try {
            Field yamlTreeField = _mainScene.getClass().getDeclaredField("_yamlTree");
            yamlTreeField.setAccessible(true);
            final YamlTree yamlTree = (YamlTree) yamlTreeField.get(_mainScene);

            Assert.assertTrue(yamlTree.getRoot() != null);
            Assert.assertTrue(yamlTree.getRoot().getChildren().size() == 5);

            //vérification du nom des ressources
            for(TreeItem<String> stringTreeItem : yamlTree.getRoot().getChildren()){
                Assert.assertTrue(_fullRessourcesName.contains(stringTreeItem.getValue()));
            }

            clickOn(yamlTree.getRoot().getValue(), MouseButton.PRIMARY);
            yamlTree.getRoot().setExpanded(true);
            sleep(500, TimeUnit.MILLISECONDS);

            final CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        for(int i = 0; i < _fullRessourcesName.size(); i++){
                            lookup(_fullRessourcesName.get(i)).query().fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED,
                                    0,0,0,0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                            yamlTree.getSelectionModel().select(i);
                            lookup(_fullRessourcesName.get(i)).query().fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED,
                                    0,0,0,0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                            lookup(_fullRessourcesName.get(i)).query().fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED,
                                    0,0,0,0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true,
                                    new PickResult(_yamlToController, new Point3D(0,0,0), 0)));
                        }
                    } catch (Exception e) {
                        latch.countDown();
                    }
                    latch.countDown();
                }
            });
            latch.await();

                YamlToController yamlToController = lookup("#YamlToController").query();
                Assert.assertTrue(yamlToController != null);
                Assert.assertTrue(!yamlToController.getChilds().isEmpty());
                Assert.assertTrue(yamlToController.getChilds().size() == _fullRessourcesName.size());



        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse(e.getMessage(), true);
        }

        Platform.runLater(new Runnable() {
            public void run() {
                _yamlToController.getChildren().clear();
            }
        });
    }
}
