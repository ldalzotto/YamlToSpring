import com.ldz.view.MainScene;
import com.ldz.view.YamlFileChooserDialog;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ldalzotto on 26/12/2016.
 */
public class MainSceneTest extends ApplicationTest {

    private MainScene _mainScene = null;

    public void start(Stage stage) throws Exception {
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
        clickOn("#fileMenu", MouseButton.PRIMARY).clickOn("#loadFileSubMenu", MouseButton.PRIMARY);
    }
}
