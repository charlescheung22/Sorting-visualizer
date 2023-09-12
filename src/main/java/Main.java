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
    final int LIST_SIZE = 40;
    List<ColoredData> instructions;
    List<Integer> currentlyColored = new ArrayList<>();
    Material white;
    Material red;
    Material blue;
    boolean start = false;

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
        List<Geometry> displayList = new ArrayList<>();
        Collections.shuffle(list);


        Material white = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        white.setColor("Color", ColorRGBA.White);
        Material red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);

        cam.setLocation(new Vector3f(LIST_SIZE / 2, LIST_SIZE / 2 / 4, 40f));


        for (int i = 0; i < LIST_SIZE; i++) {
            Box tempBox = new Box(0.25f, 0.25f * list.get(i), 0.25f);
            Geometry tempGeometry = new Geometry("Box " + i, tempBox);
            tempGeometry.setMaterial(white);
            tempGeometry.setLocalTranslation(i, 0.25f * list.get(i), 0);
            displayList.add(tempGeometry);

            rootNode.attachChild(tempGeometry);
        }


        BubbleNaive<Integer> bubbleNaive = new BubbleNaive<>(list);
        bubbleNaive.sort();
        instructions = bubbleNaive.getColoredData();

    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!start) {
            return;
        }
        for (ColoredData data : instructions) {
            for (int i : currentlyColored) {
                Geometry tempGeometry = (Geometry) rootNode.getChild("Box " + i);
                tempGeometry.setMaterial(white);
            }
            currentlyColored.clear();

            if (data instanceof ComparisonData) {
                ComparisonData tempData = (ComparisonData) data;
                int index1 = tempData.getIndex1();
                int index2 = tempData.getIndex2();
                Geometry tempGeometry = (Geometry) rootNode.getChild("Box " + index1);
                tempGeometry.setMaterial(red);
                tempGeometry = (Geometry) rootNode.getChild("Box " + index2);
                tempGeometry.setMaterial(red);
                currentlyColored.add(index1);
                currentlyColored.add(index2);
                wait(500);
            } else if (data instanceof SwapData) {
                SwapData tempData = (SwapData) data;
                int index1 = tempData.getIndex1();
                int index2 = tempData.getIndex2();
                Geometry tempGeometry = (Geometry) rootNode.getChild("Box " + index1);
                tempGeometry.setMaterial(blue);
                tempGeometry = (Geometry) rootNode.getChild("Box " + index2);
                tempGeometry.setMaterial(blue);
                currentlyColored.add(index1);
                currentlyColored.add(index2);
                wait(500);
            } else if (data instanceof SortedData) {
                return;
            }
        }
    }

    private void initKeys() {
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Pause");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                start = true;
            }
        }
    };



    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}