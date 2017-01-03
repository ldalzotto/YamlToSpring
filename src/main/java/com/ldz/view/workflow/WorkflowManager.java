package com.ldz.view.workflow;

import com.ldz.view.UINodes.UINodePoint;
import com.ldz.view.UINodes.generic.node.AbstractUiNode;
import com.ldz.view.linker.LinkerEventHandler;
import com.ldz.view.linker.LinkerEventManager;
import javafx.scene.Node;

import java.util.*;

/**
 * Created by loicd on 03/01/2017.
 */
public class WorkflowManager {

    private static WorkflowManager _instance = null;
    private final LinkerEventManager _linkerEventManager = LinkerEventManager.getInstance();
    private Map<AbstractUiNode, List<AbstractUiNode>> _linkAbstractUiNodeMap = null;

    public static WorkflowManager getInstance(){
        if(_instance==null){
            _instance = new WorkflowManager();
        }
        return _instance;
    }

    private WorkflowManager(){
        _linkAbstractUiNodeMap = new HashMap<AbstractUiNode, List<AbstractUiNode>>();
    }

    public void executeWorkflow(){
        initiateWorkflowData();

        //get first root element
        List<AbstractUiNode> nodesToEvaluate = getAllFinalnodeToEvaluate();

        //getTargets of rootNode
        for(AbstractUiNode abstractUiNode : nodesToEvaluate){
            evaluateAllSources(abstractUiNode);
        }

    }

    private void evaluateAllSources(AbstractUiNode targetNode){
            //has source ? if yes, evaluate sources first
            Iterator<Map.Entry<AbstractUiNode, List<AbstractUiNode>>> entryIterator = _linkAbstractUiNodeMap.entrySet().iterator();
            while (entryIterator.hasNext()){
                Map.Entry<AbstractUiNode, List<AbstractUiNode>> entry = entryIterator.next();
                if(entry.getValue().contains(targetNode)){
                    evaluateAllSources(entry.getKey());
                    if(targetNode instanceof IWorkflowExecution){
                        ((IWorkflowExecution) targetNode).executeFromInput(targetNode.getChilds().get(0).getInputChildrens());
                        //entryIterator.remove();
                        //_linkAbstractUiNodeMap.remove(targetNode);
                    }
                }
            }

    }

    private List<AbstractUiNode> getAllFinalnodeToEvaluate() {
        List<AbstractUiNode> globalEndNodes = new ArrayList<AbstractUiNode>();
        for(List<AbstractUiNode> listList : _linkAbstractUiNodeMap.values()){
            globalEndNodes.addAll(listList);
        }

        List<AbstractUiNode> nodesToEvaluate = new ArrayList<AbstractUiNode>();
        for(AbstractUiNode abstractUiNode : globalEndNodes){
            if(!_linkAbstractUiNodeMap.containsKey(abstractUiNode)){
                nodesToEvaluate.add(abstractUiNode);
            }
        }
        return nodesToEvaluate;
    }

    private void initiateWorkflowData() {
        Map<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapMap = _linkerEventManager.get_nodeLinkerEventHandlerMap();
        Collection<Map<Node, Node>> mapValues = linkerEventHandlerMapMap.values();

        Map<Node, Node> uniqueLinkNodeMap = new HashMap<Node, Node>();

        //contstruct a unique map
        for(Map<Node, Node> nodeNodeMap : mapValues){
            uniqueLinkNodeMap.putAll(nodeNodeMap);
        }

        for(Node inputNode : uniqueLinkNodeMap.keySet()){
            if(inputNode instanceof UINodePoint){

                AbstractUiNode parentAbstractUiNode = retrieveMainNodeParent((UINodePoint) inputNode);
                //retrieve otput
                AbstractUiNode parentOutputUiNode = null;
                Node outputNode = uniqueLinkNodeMap.get(inputNode);
                if(outputNode != null && outputNode instanceof UINodePoint){
                    parentOutputUiNode = retrieveMainNodeParent((UINodePoint) outputNode);
                }

                boolean isValueNeedTobeAdded = isValueNeedsToBeAddedToMainMap(parentAbstractUiNode, parentOutputUiNode);
                if(isValueNeedTobeAdded){
                    if(_linkAbstractUiNodeMap.get(parentAbstractUiNode) != null){
                        List<AbstractUiNode> nodeToBeInserted = _linkAbstractUiNodeMap.get(parentAbstractUiNode);
                        nodeToBeInserted.add(parentOutputUiNode);
                        _linkAbstractUiNodeMap.put(parentAbstractUiNode, nodeToBeInserted);
                    } else {
                        List<AbstractUiNode> nodeToBeInserted = new ArrayList<AbstractUiNode>();
                        nodeToBeInserted.add(parentOutputUiNode);
                        _linkAbstractUiNodeMap.put(parentAbstractUiNode, nodeToBeInserted);
                    }
                }
            }
        }
    }

    private AbstractUiNode retrieveMainNodeParent(UINodePoint uiNodePoint){
        return uiNodePoint.retrieveMainNodeParent();
    }

    private boolean isValueNeedsToBeAddedToMainMap(AbstractUiNode input, AbstractUiNode output){
        boolean isNeeded = false;
        if(!_linkAbstractUiNodeMap.containsKey(input)){
            isNeeded = true;
            return isNeeded;
        }

        //il contient déjà la clé
        //est-ce qu'il contient déjà l'output ?
        if(_linkAbstractUiNodeMap.get(input).contains(output)){
            isNeeded = false;
            return isNeeded;
        } else {
            //il contient l'input mais pas l'output -> on ajoute
            isNeeded = true;
            return isNeeded;
        }

    }

/*    public void executeWorkflow(){
        Map<LinkerEventHandler, Map<Node, Node>> linkerEventHandlerMapMap = _linkerEventManager.get_nodeLinkerEventHandlerMap();
        Collection<Map<Node, Node>> mapValues = linkerEventHandlerMapMap.values();

        List<Node> startList = new ArrayList<Node>();
        List<Node> endList = new ArrayList<Node>();

        Map<Node, Node> uniqueLinkNodeMap = new HashMap<Node, Node>();

        for(Map<Node, Node> nodeNodeMap : mapValues){
            startList.addAll(nodeNodeMap.keySet());
            endList.addAll(nodeNodeMap.values());

            //contstruct a unique map
            uniqueLinkNodeMap.putAll(nodeNodeMap);
        }

        Node rootNode = null;
        //get the beginning
        for(Node node : startList){
            if(!endList.contains(node)){
                //we got our root node
                rootNode = node;
            }
        }

        if(rootNode!=null && rootNode instanceof UINodePoint){
            startExecution((UINodePoint) rootNode, uniqueLinkNodeMap);
        }

    }*/

/*    private void startExecution(UINodePoint uiNodePoint, Map<Node, Node> nodeNodeMap){
        boolean isOver = false;
        UINodePoint currentNode = uiNodePoint;
        while (!isOver){
            if(!nodeNodeMap.keySet().contains(currentNode)){
                isOver = true;
            }

            if(currentNode instanceof IWorkflowExecution){
                //currentNode.ex
            }

            currentNode = (UINodePoint) nodeNodeMap.get(currentNode);

        }
    }*/

}
