package AI;

import Main.GamePanel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class PathFind {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList=new ArrayList<>();
    public Deque<Node> pathList=new ArrayDeque<>();
    Node startNode;
    public Node endNode;
    Node currentNode;
    boolean goalReached=false;
    int step=0;

    public PathFind(GamePanel gp){
        this.gp=gp;
        instantiateNode();
    }
    public void instantiateNode(){
        node=new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col=0;
        int row=0;
        while(col<gp.maxWorldCol && row<gp.maxWorldRow){
            node[col][row]=new Node(col,row);
            col++;
            if(col==gp.maxWorldCol){
                row++;
                col=0;
            }
        }
    }

    public void resetNodes(){
        int col=0;
        int row=0;

        while(col<gp.maxWorldCol && row<gp.maxWorldRow){
            node[col][row].open=false;
            node[col][row].checked=false;
            node[col][row].solid=false;
            node[col][row].parent=null;
            node[col][row].fCost=0;
            node[col][row].gCost=0;
            node[col][row].hCost=0;
            col++;
            if(col==gp.maxWorldCol){
                row++;
                col=0;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached=false;
        step=0;
    }

    public void setNodes(int startCol, int startRow, int endCol, int endRow) {
        resetNodes();

        if (startCol < 0 || startRow < 0 || endCol < 0 || endRow < 0
                || startCol >= gp.maxWorldCol || startRow >= gp.maxWorldRow
                || endCol >= gp.maxWorldCol || endRow >= gp.maxWorldRow) {
            return;
        }

        if (isSolid(endCol, endRow)) {
            int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}};
            boolean replaced = false;
            for (int[] d : dirs) {
                int nc = endCol + d[0], nr = endRow + d[1];
                if (nc >= 0 && nr >= 0 && nc < gp.maxWorldCol && nr < gp.maxWorldRow && !isSolid(nc, nr)) {
                    endCol = nc; endRow = nr; replaced = true; break;
                }
            }
            if (!replaced && isSolid(endCol, endRow)) return;
        }

        startNode = node[startCol][startRow];
        currentNode = startNode;
        endNode = node[endCol][endRow];
        openList.add(currentNode);

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                int tileNum = gp.tileManager.mapTileNum[gp.currentMap][col][row];
                if (gp.tileManager.tiles[tileNum].collision) {
                    node[col][row].solid = true;
                }
            }
        }

        for (int i = 0; i < gp.iTile[gp.currentMap].length; i++) {
            if (gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible) {
                int iCol = gp.iTile[gp.currentMap][i].x / gp.tileSize;
                int iRow = gp.iTile[gp.currentMap][i].y / gp.tileSize;
                if (iCol >= 0 && iCol < gp.maxWorldCol && iRow >= 0 && iRow < gp.maxWorldRow) {
                    node[iCol][iRow].solid = true;
                }
            }
        }

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                getCost(node[col][row]);
            }
        }
    }

    private boolean isSolid(int col, int row) {
        int tileNum = gp.tileManager.mapTileNum[gp.currentMap][col][row];
        return gp.tileManager.tiles[tileNum].collision;
    }

    public void getCost(Node node){
        int xDist=Math.abs(node.col-startNode.col);
        int yDist=Math.abs(node.row-startNode.row);
        node.gCost=xDist+yDist;

        xDist=Math.abs(node.col-endNode.col);
        yDist=Math.abs(node.row-endNode.row);
        node.hCost=xDist+yDist;

        node.fCost=node.gCost+node.hCost;
    }

    public void search(){
        while(!goalReached && step<500){
            int col=currentNode.col;
            int row=currentNode.row;

            currentNode.checked=true;
            openList.remove(currentNode);

            if(row-1>=0){
                openNode(node[col][row-1]);
            }
            if(row+1<gp.maxWorldRow){
                openNode(node[col][row+1]);
            }
            if(col-1>=0){
                openNode(node[col-1][row]);
            }
            if(col+1<gp.maxWorldCol){
                openNode(node[col+1][row]);
            }

            int bestNodeIndex=0;
            int bestNodeFCost=999;


            for(int i=0;i<openList.size();i++){
                if(openList.get(i).fCost<bestNodeFCost){
                    bestNodeFCost=openList.get(i).fCost;
                    bestNodeIndex=i;
                }
               else if(openList.get(i).fCost==bestNodeFCost){
                   if(openList.get(i).gCost<openList.get(bestNodeIndex).gCost){
                       bestNodeIndex=i;
                   }
                }
            }
            if(openList.isEmpty()){
                break;
            }
            currentNode=openList.get(bestNodeIndex);
            if(currentNode==endNode){
                goalReached=true;
                trackThePath();
            }
            step++;
        }
    }

    public void openNode(Node node){
        if(!node.open && !node.checked && !node.solid){
            node.open=true;
            node.parent=currentNode;
            openList.add(node);
        }
    }

    public void trackThePath(){
        Node current=endNode;
        while(current!=null && current!=startNode){
            pathList.addFirst(current);
            current=current.parent;
        }
    }
}
