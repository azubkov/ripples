package com.artit.ripples.imposition;

import com.artit.ripples.utils.ExecutorTask;

import java.util.Arrays;

public interface MassImgFilter {
    void setExtensions(String...extensions);
    /**directory for processing, without subdirs checking*/
    void setTargetDirectory(String dir);

    void start(ExecutorTask executorTask);

    public static abstract class Stub implements MassImgFilter{
        protected String targetDirectory;
        protected String[] extensions;

        @Override
        public void setTargetDirectory(String dir) {
            System.err.println("setTargetDirectory : "+dir);
            targetDirectory = dir;
        }

        @Override
        public void setExtensions(String... extensions) {
            System.err.println("setExtensions : "+ Arrays.asList(extensions));
            this.extensions = extensions;
        }
    }
}
