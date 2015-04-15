package example.project.cylindris;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import example.project.cylindris.ImportTestRenderer;
public class CubeTestFragment extends Fragment {
    private GLSurfaceView mGLView;


    public CubeTestFragment()
    {

        super();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mGLView = new CubeTestFragmentSurfaceView(this.getActivity().getApplicationContext());
        return mGLView;

    }



}
class CubeTestFragmentSurfaceView extends GLSurfaceView
{
    private final CubeTestRenderer  mRenderer;
    public int currentFront;
    public long timer;
    Shape currentShape = new Shape();




    public CubeTestFragmentSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new CubeTestRenderer();
        mRenderer.context = context;
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        // Turn off for continuous render when no user events ex. touch
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        currentFront = 8;
        timer = 1;
        run();
    }

    private void run() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.


        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:

                float x = e.getX();
                float y = e.getY();
                DisplayMetrics metrics = this.getContext().getResources().getDisplayMetrics();
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;

                //left or right
                if(y<=height*.8 && y>=height*.2) {
                    float rotateAngle = -22.5f;
                    if (x > width / 2) {
                        rotateAngle = -1 * rotateAngle;
                        if(currentShape.shiftRight(mRenderer.occupationMatrix)) {
                            currentFront = ((currentFront - 1) % 16) < 0 ? (16 + (currentFront - 1) % 16) : (currentFront - 1) % 16;
                            mRenderer.setAngle(mRenderer.getAngle() + rotateAngle);
                            updateColors(mRenderer.active,currentShape.xCoords,currentShape.yCoords,currentShape.currentType);
                        }

                    }
                    else{
                        if(currentShape.shiftLeft(mRenderer.occupationMatrix)) {
                            currentFront = ((currentFront + 1) % 16) < 0 ? (16 + (currentFront + 1) % 16) : (currentFront + 1) % 16;
                            mRenderer.setAngle(mRenderer.getAngle() + rotateAngle);
                            updateColors(mRenderer.active,currentShape.xCoords,currentShape.yCoords,currentShape.currentType);
                        }
                    }

                }
                //down
                else if(y>height*.8){
                    if(!currentShape.shiftDown(mRenderer.occupationMatrix)){
                        clearComplete(mRenderer.occupationMatrix, mRenderer.active, mRenderer.passive);
                        Random shapeRandomizer = new Random();
                        Shape.type newtype = Shape.type.T;
                        int choice = shapeRandomizer.nextInt(7);
                        switch(choice){
                            case 0: newtype = Shape.type.L; break;
                            case 1: newtype = Shape.type.J; break;
                            case 2: newtype = Shape.type.Z; break;
                            case 3: newtype = Shape.type.S; break;
                            case 4: newtype = Shape.type.O; break;
                            case 5: newtype = Shape.type.T; break;
                            case 6: newtype = Shape.type.I; break;
                        }

                        if(!currentShape.initialize(mRenderer.occupationMatrix,currentFront,newtype)){
                            //game over action
                        }
                    }
                    updateColors(mRenderer.active,currentShape.xCoords,currentShape.yCoords,currentShape.currentType);
                }
                //rotate
                else if(y<height*.2){

                    if(currentShape.rotate(mRenderer.occupationMatrix)){
                        updateColors(mRenderer.active,currentShape.xCoords,currentShape.yCoords,currentShape.currentType);
                    }
                }

                requestRender();
                break;
        }

        return true;
    }

    public void updateColors(CubeModel[][] cubes, int[] xcoords, int[] ycoords, Shape.type type){
        for(int i=0;i<4;i++){
            switch(type){
                case L:
                    cubes[ycoords[i]][xcoords[i]].color[0] = 1f;
                    cubes[ycoords[i]][xcoords[i]].color[1] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[2] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;
                case J:
                    cubes[ycoords[i]][xcoords[i]].color[0] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[1] = 1f;
                    cubes[ycoords[i]][xcoords[i]].color[2] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;
                case Z:
                    cubes[ycoords[i]][xcoords[i]].color[0] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[1] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[2] = 1f;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;
                case S:
                    cubes[ycoords[i]][xcoords[i]].color[0] = .3f;
                    cubes[ycoords[i]][xcoords[i]].color[1] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[2] = .6f;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;
                case O:
                    cubes[ycoords[i]][xcoords[i]].color[0] = .8f;
                    cubes[ycoords[i]][xcoords[i]].color[1] = .4f;
                    cubes[ycoords[i]][xcoords[i]].color[2] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;
                case T:
                    cubes[ycoords[i]][xcoords[i]].color[0] = 0;
                    cubes[ycoords[i]][xcoords[i]].color[1] = .4f;
                    cubes[ycoords[i]][xcoords[i]].color[2] = 1f;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;
                case I:
                    cubes[ycoords[i]][xcoords[i]].color[0] = 1;
                    cubes[ycoords[i]][xcoords[i]].color[1] = 1;
                    cubes[ycoords[i]][xcoords[i]].color[2] = 1;
                    cubes[ycoords[i]][xcoords[i]].color[3] = 0;
                    break;

            }
        }


    }


    public boolean clearComplete(boolean[][] occupationMatrix, CubeModel[][] activeCubes, CubeModel[][] passiveCubes){

        for(int i=14;i>=0;i--){
            int takenBlocks = 0;
            for(int j=0;j<16;j++){
                if(occupationMatrix[i][j]){
                    takenBlocks++;
                }
            }
            if(takenBlocks==16){
                mRenderer.completedRows++;
                if(mRenderer.completedRows>95){
                    return true;//congratulations, you have absolutely no life
                }
                shiftActiveToPassive(occupationMatrix,activeCubes,passiveCubes,i);
            }
        }
        return false;


    }

    public void shiftActiveToPassive(boolean[][] occupationMatrix, CubeModel[][] activeCubes, CubeModel[][] passiveCubes,int layer){
        //allocate a line and shift the passive tower down
        mRenderer.allocateLine(passiveCubes.length-1-mRenderer.completedRows);
        int look = passiveCubes.length-1-mRenderer.completedRows;
        for(int i=passiveCubes.length-1-mRenderer.completedRows;i<passiveCubes.length-1;i++){
            for(int j=0;j<16;j++){
                passiveCubes[i][j]= passiveCubes[i+1][j];
            }
        }
        //copy the completed line from active to passive
        for(int i=0;i<16;i++){
            passiveCubes[passiveCubes.length-1][i] = activeCubes[layer][i];
        }
        //shift active down
        for(int i=layer;i<activeCubes.length-1;i++){
            for(int j=0;j<16;j++) {
                occupationMatrix[i][j] = occupationMatrix[i+1][j];
                activeCubes[i][j] = activeCubes[i+1][j];
            }
        }
        //clear top row
        try{
//            int offsetAngle = 0;
            Random value = new Random();
            for(int i=0;i<16;i++){
                occupationMatrix[activeCubes.length-1][i]=false;
//                activeCubes[activeCubes.length-1][i] = new CubeModel(mRenderer.context, "testcube", activeCubes[activeCubes.length-2][i].angleOffset, false);
                activeCubes[activeCubes.length-1][i].color[0] = value.nextFloat();
                activeCubes[activeCubes.length-1][i].color[1] = value.nextFloat();
                activeCubes[activeCubes.length-1][i].color[2] = value.nextFloat();
                activeCubes[activeCubes.length-1][i].color[3] = 0;
//                offsetAngle+=22.5;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}