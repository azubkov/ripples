package com.artit.ripples.imposition.threaded;

import com.artit.ripples.ArtiMarkFrame;
import com.artit.ripples.ImageManager;
import com.artit.ripples.ImagesFinder;
import com.artit.ripples.WaterMarkModel;
import com.artit.ripples.imposition.MassImgFilter;
import com.artit.ripples.imposition.linear.Marker;
import com.artit.ripples.utils.ExecutorTask;
import com.artit.ripples.utils.Utils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedMassImgFilter extends MassImgFilter.Stub{
    private static final Logger LOG = Logger.getLogger(StepLoadTracker.class);
    private static int POOL_SIZE = 5;
    private ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);


    @Override
    public void start(final ExecutorTask executorTask) {
        List<String> filenames = getFilenames();


        final JFrame f =  new JFrame("Marking...");
        Container content = f.getContentPane();
        final JProgressBar progressBar = new JProgressBar();


        final StepLoadTracker stepLoadTracker = new StepLoadTracker(filenames.size(), new ExecutorTask() {
            @Override
            protected void perform() {
                f.setVisible(false);
                executorTask.run();
            }
        }, 10*1000*filenames.size()/POOL_SIZE );
        stepLoadTracker.waitLoading();

        final Marker marker = new Marker(getWaterMarkImage());


        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Marking...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(400, 200);
        f.setVisible(true);
        progressBar.setValue(0);
        progressBar.setMaximum(stepLoadTracker.getSteps());


        for (final String filename : filenames) {
            executorService.submit(new ExecutorTask() {
                @Override
                protected void perform() {
                    try {
                        Image imgStorage = ImageManager.open(ImageManager.fromFileOpener,
                                ImageManager.imagesFinder.getDirectory() + filename);

                        imgStorage = marker.convertImage(imgStorage);

                        File file = new File(ImageManager.imagesFinder.getDirectory() + WaterMarkModel.getImageOutputDirectoryName() + filename);
                        file.delete();
                        file.mkdirs();
                        ImageIO.write((BufferedImage)imgStorage, Utils.getFileExtension(filename), file);
                    }catch(Throwable t){
                        LOG.error(t,t);
                    }
                    finally{
                        stepLoadTracker.stepLoaded();
                        progressBar.setValue(stepLoadTracker. getCounter());
                    }
                }
            });
        }
    }

    private List<String> getFilenames() {
        ImagesFinder imagesFinder = new ImagesFinder();
        imagesFinder.setDirectory(targetDirectory);
        Set<String> extensionsSet = new HashSet<String>(Arrays.asList(extensions));
        return imagesFinder.getFilenames(extensionsSet.contains("jpg") || extensionsSet.contains("jpeg"), extensionsSet.contains("png"), extensionsSet.contains("gif"));
    }

    private Image getWaterMarkImage() {
        Rectangle panelRect = ArtiMarkFrame.panelWatermarkView.getBounds();

        int leftSpare = Math.min(ArtiMarkFrame.watermarkImgComp.getX(), ArtiMarkFrame.figureImgComp.getX());
        int widthRequed = Math.max(ArtiMarkFrame.watermarkImgComp.getX() + ArtiMarkFrame.watermarkImgComp.getWidth(), ArtiMarkFrame.figureImgComp.getX() + ArtiMarkFrame.figureImgComp.getWidth());
        int topSpare = Math.min(ArtiMarkFrame.watermarkImgComp.getY(), ArtiMarkFrame.figureImgComp.getY());
        int heightRequed = Math.max(ArtiMarkFrame.watermarkImgComp.getY() + ArtiMarkFrame.watermarkImgComp.getHeight(), ArtiMarkFrame.figureImgComp.getY() + ArtiMarkFrame.figureImgComp.getHeight());
        widthRequed -= leftSpare;
        heightRequed -= topSpare;
        ArtiMarkFrame.watermarkImgComp.setLocation(ArtiMarkFrame.watermarkImgComp.getX() - leftSpare, ArtiMarkFrame.watermarkImgComp.getY() - topSpare);
        ArtiMarkFrame.figureImgComp.setLocation(ArtiMarkFrame.figureImgComp.getX() - leftSpare, ArtiMarkFrame.figureImgComp.getY() - topSpare);
        ArtiMarkFrame.panelWatermarkView.setSize(widthRequed, heightRequed);

        BufferedImage panelImg = (BufferedImage) ImageManager.getImageFromComponent(ArtiMarkFrame.panelWatermarkView);
        ArtiMarkFrame.panelWatermarkView.setBounds(panelRect);
        ArtiMarkFrame.watermarkImgComp.setLocation(ArtiMarkFrame.watermarkImgComp.getX() + leftSpare, ArtiMarkFrame.watermarkImgComp.getY() + topSpare);
        ArtiMarkFrame.figureImgComp.setLocation(ArtiMarkFrame.figureImgComp.getX() + leftSpare, ArtiMarkFrame.figureImgComp.getY() + topSpare);

        return panelImg;
    }


}
