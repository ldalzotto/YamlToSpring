package com.ldz.linkerEventTest;

import com.ldz.Main;
import com.ldz.generic.AbstractGUITask;
import com.ldz.view.MainScene;
import com.ldz.view.YamlToController;
import com.ldz.view.menu.YamlWorkspaceContextMenu;
import com.ldz.view.stages.SpringNodeCreatorStage;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

/**
 * Created by ldalzotto on 30/12/2016.
 */
public class LinkerEventTest extends FxRobot {


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

    @After
    public void clear() throws Exception {

    }

    @Test
    public void test(){

    }
}
