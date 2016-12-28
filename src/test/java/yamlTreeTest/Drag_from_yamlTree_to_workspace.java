package yamlTreeTest;

import com.google.common.collect.ImmutableList;
import com.ldz.view.MainScene;
import com.ldz.view.UINodes.generic.AbstractUiNode;
import com.ldz.view.YamlFileChooserDialog;
import com.ldz.view.YamlToController;
import com.ldz.view.YamlTree;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * Created by ldalzotto on 28/12/2016.
 */
public class Drag_from_yamlTree_to_workspace extends ApplicationTest {

    private MainScene _mainScene = null;

    private static final ImmutableList<String> _fullRessourcesName = ImmutableList.of("/v1/products",
            "/v1/estimates/price",
            "/v1/estimates/time",
            "/v1/me",
            "/v1/history");


    public void start(Stage stage) throws Exception {
        stage.setScene(null);
        MainScene mainScene = new MainScene();
        _mainScene = mainScene;
        stage.setScene(_mainScene);
        stage.show();
        _mainScene.initialize();

        YamlFileChooserDialog fileChooserDialog = Mockito.mock(YamlFileChooserDialog.class);
        Mockito.when(fileChooserDialog.initializeYamlFileChooser())
                .thenReturn(new File("src/test/uber.yaml"));
        _mainScene.set_yamlFileChooserDialog(fileChooserDialog);
    }

    @Test
    public void test(){
        sleep(1, TimeUnit.SECONDS);
        clickOn("#fileMenu", MouseButton.PRIMARY).clickOn("#loadFileSubMenu", MouseButton.PRIMARY)
                .clickOn("#yamlTree", MouseButton.PRIMARY);

        try {
            Field yamlTreeField = _mainScene.getClass().getDeclaredField("_yamlTree");
            yamlTreeField.setAccessible(true);
            YamlTree yamlTree = (YamlTree) yamlTreeField.get(_mainScene);

            Assert.assertTrue(yamlTree.getRoot() != null);
            Assert.assertTrue(yamlTree.getRoot().getChildren().size() == 5);

            //vérification du nom des ressources
            for(TreeItem<String> stringTreeItem : yamlTree.getRoot().getChildren()){
                Assert.assertTrue(_fullRessourcesName.contains(stringTreeItem.getValue()));
            }

            doubleClickOn(yamlTree.getRoot().getValue(), MouseButton.PRIMARY);

            //Create a YamlToCntrollerNode
            for(int i = 0; i < _fullRessourcesName.size(); i++){
                moveTo(_fullRessourcesName.get(i));
                press(MouseButton.PRIMARY);
                moveTo("#YamlToController");
                release(MouseButton.PRIMARY);
            }

            YamlToController yamlToController = lookup("#YamlToController").query();
            Assert.assertTrue(yamlToController != null);
            Assert.assertTrue(!yamlToController.getChilds().isEmpty());
            Assert.assertTrue(yamlToController.getChilds().size() == _fullRessourcesName.size());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse(e.getMessage(), true);
        }

        _mainScene.setRoot(new BorderPane());

    }
}
