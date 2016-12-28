package dragAndDropWorkspace;

import com.google.common.collect.ImmutableList;
import com.ldz.Main;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.YamlNode;
import com.ldz.view.YamlFileChooserDialog;
import com.ldz.view.YamlToController;
import com.ldz.view.YamlTree;
import com.sun.glass.ui.monocle.MonocleRobot;
import com.sun.javafx.scene.control.skin.MenuBarSkin;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.control.Menu;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by ldalzotto on 28/12/2016.
 */
public class MainDragAndDropTest extends FxRobot {

    private MainScene _mainScene = MainScene.getInstance();
    private YamlToController _yamlToController = YamlToController.getInstance();
    private static Stage _stage = null;

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
    }


    @Test
    public void teset(){
        sleep(1, TimeUnit.SECONDS);

        YamlFileChooserDialog fileChooserDialog = Mockito.mock(YamlFileChooserDialog.class);
        Mockito.when(fileChooserDialog.initializeYamlFileChooser())
                .thenReturn(new File("src/test/uber.yaml"));
        _mainScene.set_yamlFileChooserDialog(fileChooserDialog);


        clickOn(lookup("#fileMenu").query(), MouseButton.PRIMARY);

        final int xCoord = ThreadLocalRandom.current().nextInt(-300, 300);
        final int yCoord = ThreadLocalRandom.current().nextInt(-300, 300);

        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    _mainScene.get_menuFile().getItems().get(0).fire();
                    _yamlToController.createYamlNode(xCoord,yCoord,"node1");
                } catch (Exception e) {
                    e.printStackTrace();
                    latch.countDown();
                }
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        YamlNode node1 = lookup("#node1").query();

        Assert.assertTrue(node1.getLayoutX() == xCoord);
        Assert.assertTrue(node1.getLayoutY() == yCoord);

        final CountDownLatch latch2 = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    YamlNode node1 = lookup("#node1").query();
                    node1.fireEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, node1.getLayoutX(), node1.getLayoutY(),
                            node1.getLayoutX(), node1.getLayoutY(), MouseButton.SECONDARY, 0, false, false,
                            false, false, false, false, true, false, false, false, new PickResult(node1, new Point3D(0,0,0), 0)));
                    latch2.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                    latch2.countDown();
                }
            }
        });
        try {
            latch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(node1.get_initialCursorPosition().getX() == xCoord);
        Assert.assertTrue(node1.get_initialCursorPosition().getY() == yCoord);


        final int xCoordDelta = ThreadLocalRandom.current().nextInt(-40, 40);
        final int yCoordDelta = ThreadLocalRandom.current().nextInt(-40, 40);

        final CountDownLatch latch3 = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    YamlNode node1 = lookup("#node1").query();
                    node1.fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED, node1.getLayoutX()+xCoordDelta, node1.getLayoutY()+yCoordDelta,
                            node1.getLayoutX()+xCoordDelta, node1.getLayoutY()+yCoordDelta, MouseButton.SECONDARY, 0, false, false,
                            false, false, false, false, true, false, false, false, new PickResult(YamlToController.getInstance(), new Point3D(0,0,0), 0)));
                    node1.fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED, node1.getLayoutX()+xCoordDelta, node1.getLayoutY()+yCoordDelta,
                            node1.getLayoutX()+xCoordDelta, node1.getLayoutY()+yCoordDelta, MouseButton.SECONDARY, 0, false, false,
                            false, false, false, false, true, false, false, false, new PickResult(new HBox(), new Point3D(0,0,0), 0)));
                } catch (Exception e) {
                    e.printStackTrace();
                    latch3.countDown();
                }
                latch3.countDown();
            }
        });
        try {
            latch3.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(node1.getTranslateX() == ((xCoord+xCoordDelta)-xCoord));
        Assert.assertTrue(node1.getTranslateY() == ((yCoord+yCoordDelta)-yCoord));


        //Test if result is out of main yamlToController wokspace
        final CountDownLatch latch4 = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            public void run() {
                YamlNode node1 = lookup("#node1").query();
                node1.fireEvent(new MouseEvent(MouseEvent.MOUSE_DRAGGED, node1.getLayoutX()+9999, node1.getLayoutY()+999999,
                        node1.getLayoutX()+99999, node1.getLayoutY()+99999, MouseButton.SECONDARY, 0, false, false,
                        false, false, false, false, true, false, false, false, new PickResult(YamlTree.getInstance(), new Point3D(0,0,0), 0)));
                latch4.countDown();
            }
        });
        try {
            latch4.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(node1.getTranslateX() == ((xCoord+xCoordDelta)-xCoord));
        Assert.assertTrue(node1.getTranslateY() == ((yCoord+yCoordDelta)-yCoord));


        //Test when releasing mouse
        final CountDownLatch latch5 = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            public void run() {
                YamlNode node1 = lookup("#node1").query();
                node1.fireEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, node1.getLayoutX()+xCoordDelta, node1.getLayoutY()+yCoordDelta,
                        node1.getLayoutX()+xCoordDelta, node1.getLayoutY()+yCoordDelta, MouseButton.SECONDARY, 0, false, false,
                        false, false, false, false, false, false, false, false, new PickResult(YamlToController.getInstance(), new Point3D(0,0,0), 0)));
                latch5.countDown();
            }
        });
        try {
            latch5.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(node1.getTranslateX() == 0);
        Assert.assertTrue(node1.getTranslateY() == 0);
        Assert.assertTrue(node1.getLayoutX() == (((xCoord+xCoordDelta)-xCoord)) + xCoord);
        Assert.assertTrue(node1.getLayoutY() == (((yCoord+yCoordDelta)-yCoord)) + yCoord);
        Assert.assertTrue(node1.get_initialCursorPosition() == null);


        Platform.runLater(new Runnable() {
            public void run() {
                _yamlToController.getChildren().clear();
            }
        });
    }

}
