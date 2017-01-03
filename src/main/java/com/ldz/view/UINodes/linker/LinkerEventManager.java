package com.ldz.view.UINodes.linker;

import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.UINodePoints;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by loicd on 03/01/2017.
 */
public class LinkerEventManager {

    private static LinkerEventManager _instance = null;

    public static LinkerEventManager getInstance(){
        if(_instance == null){
            _instance = new LinkerEventManager();
        }
        return _instance;
    }

    /**
     * This attribute represents all {@link LinkerEventHandler} and their start and end node
     * The order is : Map<{@link LinkerEventHandler}, Map<beginNode, endNode>>
     *     endNode may be null
     *  One {@link LinkerEventHandler} is added for every {@link UINodePoints} output nodes.
     */
    private Map<LinkerEventHandler, Map<Node, Node>> _nodeLinkerEventHandlerMap = new HashMap<LinkerEventHandler, Map<Node, Node>>();

    private LinkerEventManager(){
        _nodeLinkerEventHandlerMap = new HashMap<LinkerEventHandler, Map<Node, Node>>();
    }


    public void putLinkers(Map<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapMap){
        _nodeLinkerEventHandlerMap.putAll(linkerEventHandlerMapMap);
    }

    public LinkerEventHandler getLinkEventHandlerFromAssociatedPoint(UINodePoint uiNodePoint){
        for (Map.Entry<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapEntry : _nodeLinkerEventHandlerMap.entrySet()) {
            for (Map.Entry<Node, Node> nodeNodeEntry : linkerEventHandlerMapEntry.getValue().entrySet()) {
                if (nodeNodeEntry.getValue() != null &&
                        nodeNodeEntry.getValue().equals(uiNodePoint)) {
                    return linkerEventHandlerMapEntry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Reset the linker inside _nodeLinkerEventHandlerMap
     * @param linkerEventHandler the linker to reset
     */
    public void resetLinkerFromMainWorkspace(LinkerEventHandler linkerEventHandler){
        for (Map.Entry<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapEntry : _nodeLinkerEventHandlerMap.entrySet()) {
            if (linkerEventHandlerMapEntry.getKey().equals(linkerEventHandler)) {
                for (Map.Entry<Node, Node> nodeNodeEntry : _nodeLinkerEventHandlerMap.get(linkerEventHandler).entrySet()) {
                    nodeNodeEntry.setValue(null);
                }
                return;
            }
        }
    }

    public Map<LinkerEventHandler, Map<Node, Node>> get_nodeLinkerEventHandlerMap() {
        return _nodeLinkerEventHandlerMap;
    }
}
