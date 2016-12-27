package yamlTreeTest;

import com.google.common.collect.ImmutableList;
import com.ldz.view.MainScene;
import com.ldz.view.YamlFileChooserDialog;
import com.ldz.view.YamlTree;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * Created by ldalzotto on 27/12/2016.
 */
public class FullRessourceName_withoutBasePath extends ApplicationTest {

    private MainScene _mainScene = null;

    private static final ImmutableList<String> _fullRessourcesName = ImmutableList.of("/products",
            "/estimates/price",
            "/estimates/time",
            "/me",
            "/history");

    public void start(Stage stage) throws Exception {
        MainScene mainScene = new MainScene();
        _mainScene = mainScene;
        stage.setScene(_mainScene);
        stage.show();
        _mainScene.initialize();

        YamlFileChooserDialog fileChooserDialog = Mockito.mock(YamlFileChooserDialog.class);
        Mockito.when(fileChooserDialog.initializeYamlFileChooser())
                .thenReturn(new File("src/test/uberWithoutBasepath.yaml"));
        _mainScene.set_yamlFileChooserDialog(fileChooserDialog);
    }

    @Test
    public void fullRessourceName_withoutBasePath(){
        sleep(1, TimeUnit.SECONDS);
        clickOn("#fileMenu", MouseButton.PRIMARY).clickOn("#loadFileSubMenu", MouseButton.PRIMARY)
                .clickOn("#yamlTree", MouseButton.PRIMARY);

        try {
            Field yamlTreeField = _mainScene.getClass().getDeclaredField("_yamlTree");
            yamlTreeField.setAccessible(true);
            YamlTree yamlTree = (YamlTree) yamlTreeField.get(_mainScene);

            Assert.assertTrue(yamlTree.getRoot() != null);
            Assert.assertTrue(yamlTree.getRoot().getChildren().size() == 5);

            for(TreeItem<String> stringTreeItem : yamlTree.getRoot().getChildren()){
                //vérification du nom des ressources
                Assert.assertTrue(_fullRessourcesName.contains(stringTreeItem.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse(e.getMessage(), true);
        }
        _mainScene.setRoot(new BorderPane());
    }
}
