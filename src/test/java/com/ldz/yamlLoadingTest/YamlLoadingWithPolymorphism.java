package com.ldz.yamlLoadingTest;

import com.google.common.collect.ImmutableList;
import com.ldz.Main;
import com.ldz.controller.YamlLoadingController;
import com.ldz.generic.AbstractGUITask;
import com.ldz.model.Path;
import com.ldz.model.Schema;
import com.ldz.view.MainScene;
import com.ldz.view.YamlFileChooserDialog;
import com.ldz.view.YamlTree;
import javafx.scene.control.TreeItem;
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
import java.util.LinkedHashMap;

/**
 * Created by loicd on 09/01/2017.
 */
public class YamlLoadingWithPolymorphism extends FxRobot {

    private MainScene _mainScene = null;
    private static Stage _stage = null;
    private YamlLoadingController _yamlLoadingController = YamlLoadingController.getInstance();

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
            }
        };
    }

    @Test
    public void fullRessourceName_withoutBasePath(){

        YamlFileChooserDialog fileChooserDialog = Mockito.mock(YamlFileChooserDialog.class);
        Mockito.when(fileChooserDialog.initializeYamlFileChooser())
                .thenReturn(new File("src/test/petStore.yaml"));
        _mainScene.set_yamlFileChooserDialog(fileChooserDialog);

        //clickOn(lookup("#fileMenu").query(), MouseButton.PRIMARY);
        new AbstractGUITask(){
            public void GUITask() {
                _mainScene.get_menuFile().getItems().get(0).fire();
            }
        };

        //clickOn(lookup("#yamlTree").query(), MouseButton.PRIMARY);

        try {
            Field yamlTreeField = _mainScene.getClass().getDeclaredField("_yamlTree");
            yamlTreeField.setAccessible(true);
            YamlTree yamlTree = (YamlTree) yamlTreeField.get(_mainScene);


            LinkedHashMap<String, Schema> definitionsMap = _yamlLoadingController.get_swaggerYamlFile().getDefinitions();

            Assert.assertTrue(definitionsMap.get("Pet") != null);
            Assert.assertTrue(definitionsMap.get("Pet").getAllOf() != null);
            Assert.assertTrue(definitionsMap.get("Pet").getAllOf().size() == 2);

            //correctly valuated
            for(Schema schema : definitionsMap.get("Pet").getAllOf()){
                Assert.assertTrue(schema.get$ref() == null);
                Assert.assertTrue(schema.getProperties().containsKey("id") || (schema.getProperties().containsKey("name") && schema.getProperties().containsKey("tag")));
            }


        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse(e.getMessage(), true);
        }

    }
}
