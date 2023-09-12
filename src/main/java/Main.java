import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Geometry;

import sorting.*;
import visualization.*;

public class Main extends SimpleApplication {
    final int LIST_SIZE = 100;
    List<ColoredData> instructions;
    int instructionsSize;
    int instructionsIndex = 0;
    List<Integer> currentlyColored = new ArrayList<>();
    Material white;
    Material red;
    Material blue;
    boolean start = false;
    List<Geometry> displayList = new ArrayList<>();
    boolean step = false;

    public static void main(String[] args) {
        Main app = new Main();

        AppSettings settings = new AppSettings(true);
        settings.setResolution(800, 600);
        settings.setTitle("Sorting-visualizer");
        app.setSettings(settings);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        List<Integer> list = IntStream.range(1, LIST_SIZE + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(list);


        white = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        white.setColor("Color", ColorRGBA.White);
        red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);

        cam.setLocation(new Vector3f(LIST_SIZE / 2, LIST_SIZE / 2 / 4, LIST_SIZE));


        for (int i = 0; i < LIST_SIZE; i++) {
            Box tempBox = new Box(0.25f, 0.25f * list.get(i), 0.25f);
            Geometry tempGeometry = new Geometry("Box " + list.get(i), tempBox);
            tempGeometry.setMaterial(white);
            tempGeometry.setLocalTranslation(i, 0.25f * list.get(i), 0);
            displayList.add(tempGeometry);

            rootNode.attachChild(tempGeometry);
        }


        BubbleNaive<Integer> bubbleNaive = new BubbleNaive<>(list);
        bubbleNaive.sort();
        instructions = bubbleNaive.getColoredData();
        instructionsSize = instructions.size();

        initKeys();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!start) {
            return;
        } else {
            for (int i : currentlyColored) {
                Geometry tempGeometry = displayList.get(i);
                tempGeometry.setMaterial(white);
            }
            currentlyColored.clear();

            if (instructionsIndex < instructionsSize) {
                ColoredData data = instructions.get(instructionsIndex);
                instructionsIndex++;

                if (data instanceof ComparisonData) {
                    ComparisonData tempData = (ComparisonData) data;
                    int index1 = tempData.getIndex1();
                    int index2 = tempData.getIndex2();
                    Geometry tempGeometry = displayList.get(index1);
                    tempGeometry.setMaterial(blue);
                    tempGeometry = displayList.get(index2);
                    tempGeometry.setMaterial(blue);
                    currentlyColored.add(index1);
                    currentlyColored.add(index2);

                } else if (data instanceof SwapData) {
                    SwapData tempData = (SwapData) data;
                    int index1 = tempData.getIndex1();
                    int index2 = tempData.getIndex2();
                    Geometry tempGeometry1 = displayList.get(index1);
                    tempGeometry1.setMaterial(red);
                    Geometry tempGeometry2 = displayList.get(index2);
                    tempGeometry2.setMaterial(red);

                    tempGeometry1.setLocalTranslation(index2, tempGeometry1.getLocalTranslation().getY(), 0);
                    tempGeometry2.setLocalTranslation(index1, tempGeometry2.getLocalTranslation().getY(), 0);

                    displayList.set(index1, tempGeometry2);
                    displayList.set(index2, tempGeometry1);
                    currentlyColored.add(index1);
                    currentlyColored.add(index2);

                } else if (data instanceof SortedData) {
                    start = false;
                }
            }
        }
        if (step) {
            start = false;
            step = false;
        }
    }

    private void initKeys() {
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shuffle", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Step", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Shuffle");
        inputManager.addListener(actionListener, "Step");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                start = !start;
            } else if (name.equals("Shuffle") && !keyPressed) {
                List<Integer> list = IntStream.range(1, LIST_SIZE + 1).boxed().collect(Collectors.toList());
                Collections.shuffle(list);


                rootNode.detachAllChildren();
                displayList.clear();
                for (int i = 0; i < LIST_SIZE; i++) {
                    Box tempBox = new Box(0.25f, 0.25f * list.get(i), 0.25f);
                    Geometry tempGeometry = new Geometry("Box " + list.get(i), tempBox);
                    tempGeometry.setMaterial(white);
                    tempGeometry.setLocalTranslation(i, 0.25f * list.get(i), 0);
                    displayList.add(tempGeometry);

                    rootNode.attachChild(tempGeometry);
                }

                BubbleNaive<Integer> bubbleNaive = new BubbleNaive<>(list);
                bubbleNaive.sort();
                instructions = bubbleNaive.getColoredData();
                instructionsSize = instructions.size();
                instructionsIndex = 0;
                start = false;
            } else if (name.equals("Step") && !keyPressed) {
                step = true;
                start = true;
            }
        }
    };
}