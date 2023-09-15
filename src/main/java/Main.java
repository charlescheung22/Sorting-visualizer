import java.awt.*;

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

import sorting.aggregator.*;
import sorting.iterator.*;
import visualization.*;

public class Main extends SimpleApplication {
    final int LIST_SIZE = 500;
    List<ColoredData> instructions;
    int instructionsSize;
    int instructionsIndex = 0;
    List<Integer> currentlyColored = new ArrayList<>();
    static Material white;
    static Material red;
    static Material blue;
    boolean start = false;
    List<Geometry> displayList = new ArrayList<>();
    boolean step = false;
    static final boolean SHOW_SETTINGS = true;

    public static void main(String[] args) {
        Main app = new Main();

        AppSettings settings = new AppSettings(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int i = 0; // note: there are usually several, let's pick the first
        settings.setResolution(gd.getDisplayMode().getWidth(),gd.getDisplayMode().getHeight());
        settings.setFrequency(gd.getDisplayMode().getRefreshRate());
        settings.setBitsPerPixel(gd.getDisplayMode().getBitDepth());
        settings.setFullscreen(gd.isFullScreenSupported());
        settings.setTitle("Sorting-visualizer");
        app.setSettings(settings);

        app.setShowSettings(SHOW_SETTINGS);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Setup colors and camera, application startups
        white = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        white.setColor("Color", ColorRGBA.White);
        blue.setColor("Color", ColorRGBA.Blue);
        red.setColor("Color", ColorRGBA.Red);

        setDisplayFps(true);
        setDisplayStatView(false);  // stats and fps menu bottom left

        cam.setLocation(new Vector3f((((float) LIST_SIZE) / 2 - 0.5f), ((float) LIST_SIZE) / 4, LIST_SIZE * 0.75f));

        List<Integer> list = IntStream.range(1, LIST_SIZE + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(list);


        for (int i = 0; i < LIST_SIZE; i++) {
            Box tempBox = new Box(0.25f, 0.25f * list.get(i), 0.25f);
            Geometry tempGeometry = new Geometry("Box " + list.get(i), tempBox);
            tempGeometry.setMaterial(white);
            tempGeometry.setLocalTranslation(i, 0.25f * list.get(i), 0);
            displayList.add(tempGeometry);

            rootNode.attachChild(tempGeometry);
        }


        ColoredDataAggregator sortingAggregator = new QuickAggregator<>(list);
        sortingAggregator.sort();
        instructions = sortingAggregator.getColoredData();
        instructionsSize = instructions.size();

        initKeys();
    }

//    protected List<ColoredData> getColoredDataAggregate(Class<ColoredDataAggregator> class, List<T extends Comparable<T>> list) {
//        ColoredDataAggregator tmpclass = new ColoredDataAggregator(list);
//
//        return coloredDataAggregator.getColoredData();
//    }  TODO: factory method for colored data aggregators

    @Override
    public void simpleUpdate(float tpf) {
        if (!start) {return;} else {
            // Clear colored boxes
            for (int i : currentlyColored) {
                Geometry tempGeometry = displayList.get(i);
                tempGeometry.setMaterial(white);
            }
            currentlyColored.clear();

            if (instructionsIndex < instructionsSize) {
                ColoredData data = instructions.get(instructionsIndex);
                instructionsIndex++;

                if (data instanceof ComparisonData) {  // TODO need to use subtype polymorphism
                    ComparisonData tempData = (ComparisonData) data;
                    int index1 = tempData.getIndex1();
                    int index2 = tempData.getIndex2();
                    Geometry tempGeometry = displayList.get(index1);
                    tempGeometry.setMaterial(blue);
                    tempGeometry = displayList.get(index2);
                    tempGeometry.setMaterial(blue);
                    currentlyColored.add(index1);
                    currentlyColored.add(index2);

                } else if (data instanceof SwapData) {  // Massive code smell :weary:
                    SwapData tempData = (SwapData) data;
                    int index1 = tempData.getIndex1();
                    int index2 = tempData.getIndex2();

                    Geometry tempGeometry1 = displayList.get(index1);
                    tempGeometry1.setMaterial(red);

                    if (index1 == index2) {  // zwischenzug
                        currentlyColored.add(index1);

                    } else {
                        Geometry tempGeometry2 = displayList.get(index2);
                        tempGeometry2.setMaterial(red);

                        tempGeometry1.setLocalTranslation(index2, tempGeometry1.getLocalTranslation().getY(), 0);
                        tempGeometry2.setLocalTranslation(index1, tempGeometry2.getLocalTranslation().getY(), 0);

                        displayList.set(index1, tempGeometry2);
                        displayList.set(index2, tempGeometry1);
                        currentlyColored.add(index1);
                        currentlyColored.add(index2);
                    }

                } else if (data instanceof CheckData) {
                    CheckData tempData = (CheckData) data;
                    int index = tempData.getIndex1();
                    Geometry tempGeometry = displayList.get(index);
                    tempGeometry.setMaterial(blue);
                    currentlyColored.add(index);

                } else if (data instanceof TerminatedData) {
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
        inputManager.addMapping("Shuffle", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("Step", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Shuffle");
        inputManager.addListener(actionListener, "Step");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {  // switches here don't work??? idfk
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

                ColoredDataAggregator sortingAggregator = new QuickAggregator<>(list);
                sortingAggregator.sort();
                instructions = sortingAggregator.getColoredData();
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



// TODO add a way to change the sorting algorithm with eg. number keys