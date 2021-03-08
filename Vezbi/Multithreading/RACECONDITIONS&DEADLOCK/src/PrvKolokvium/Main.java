package PrvKolokvium;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

    static class fileFilter implements FilenameFilter {
        @Override
        public boolean accept(File file, String s) {
            return s.endsWith(".mat");
        }
    }

    static class FileScanner extends Thread{
        private final java.io.File directoryToScan;
        private static ArrayList<File> matFiles;

        private java.util.List<File> matrixFiles = new java.util.ArrayList<>();
        FileScanner(java.io.File directoryToScan) {
            this.directoryToScan = directoryToScan;
            matFiles = new ArrayList<File>();
        }
        // todo: implement recursive directory scan
        @Override
        public void run() {
            findMATFiles(this.directoryToScan.getAbsolutePath());
        }

        public void findMATFiles(String directoryURL){
            File currentDirectory = new File(directoryURL);
            if (!currentDirectory.exists()){
                System.err.println("Directory not found");
                return;
            }
            fileFilter fileFilter = new fileFilter();
            File []files = currentDirectory.listFiles(fileFilter);
            assert files != null;
            FileScanner.matFiles.addAll(Arrays.asList(files));

            files = currentDirectory.listFiles();
            assert files != null;
            for ( File f: files){
                if (f.isDirectory() ){
                    findMATFiles(f.getAbsolutePath());
                }
            }

        }
    }

    static class Reader extends Thread{
        private final String matrixFile;
        int[][] matrix;
        Reader(String matrixFile) {
            this.matrixFile = matrixFile;
        }
        /**
         * This method should execute in background
         */
        public void run() {
            // todo: complete this method according to the text description
            // todo: The variable in should provide the readInt() method
            DataInputStream in = null;
            try {
                in = new DataInputStream(new FileInputStream(new File(matrixFile)));
                int n = in.readInt();
                this.matrix = new int[n][n];
                int[][] matrix = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        matrix[i][j] = in.readInt();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Writer extends Thread {
        private final String outputPath;
        private final int[][] matrix;
        Writer(String outputPath, int[][] matrix) {
            this.outputPath = outputPath;
            this.matrix = matrix;
        }
        public void run() {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputPath), true));
                int n = matrix.length;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        // todo: write the element
                        writer.write(matrix[i][j] + " ");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Semaphore semaphore = new Semaphore(10);

    static class Transformer extends Thread {
        private final int[][] matrix;
        private final int row;
        private final int column;
        private int result;
        Transformer(int[][] matrix, int row, int column) {
            this.matrix = matrix;
            this.row = row;
            this.column = column;
        }
        public void run() {
            // todo: allow maximum 10 parallel executions
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int n = matrix.length;
            for (int k = 0; k < n; k++) {
                result += matrix[row][k] * matrix[k][column];
            }
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Transformer> transformers = new ArrayList<>();
        Reader reader = new Reader("data/matrix.mat");
        // todo: execute file reading in background
        reader.start();
        // todo: wait for the matrix to be read
        reader.join();
        // transform the matrix
        int n = reader.matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Transformer t = new Transformer(reader.matrix, i, j);
                transformers.add(t);
                // todo: execute the background transformation
                t.start();
            }
        }
        // todo: wait for all transformers to finish
        for (Transformer transformer: transformers ){
            transformer.join();
        }
        int[][] result = new int[n][n];
        for (Transformer t : transformers) {
            result[t.row][t.column] = t.result;
        }
        Writer writer = new Writer("data/out/matrix2.bin", result);
        // todo: execute file writing in background
        writer.start();
        FileScanner scanner = new FileScanner(new File("data"));
        // todo: execute file scanning in background
        scanner.start();
        // todo: show the results
        System.out.println(scanner.matrixFiles);
    }
}

