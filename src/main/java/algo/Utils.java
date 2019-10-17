package algo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Utils {
    private static double[][] data;
    private static String[] rawLabels;
    private static double[] y;
    private static Map<String, List<Integer>> class2indices;
    private static int nRows;
    private static int featureNum = 0;
    private static double[][] xTrain;
    private static double[][] xEval;
    private static double[] yEval;
    private static double[] yTrain;
    public static void readClassificationData(String dataName, int classNum) throws FileNotFoundException {
        String line = null;
        String[] tmp = null;
        if (dataName.equals("iris")) {
            class2indices = new HashMap<>();
            // 获取数据的条数
            nRows = 0;
            FileInputStream fileInputStream = new FileInputStream("./data/iris.data");
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNext()) {
                nRows++;
                line = scanner.nextLine();
            }
            featureNum = line.trim().split(",").length - 1;
            System.out.println(String.format("iris data has %d records", nRows));
            // 解析数据
            fileInputStream = new FileInputStream("./data/iris.data");
            scanner = new Scanner(fileInputStream);
            data = new double[nRows][featureNum];
            rawLabels = new String[nRows];
            int index = 0;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                tmp = line.trim().split(",");
                for (int i = 0; i < featureNum; i++) {
                    data[index][i] = Double.valueOf(tmp[i]);
                }
                rawLabels[index] = tmp[featureNum];
                index++;
            }
            for (int i = 0; i < nRows; i++) {
                String label = rawLabels[i];
                List<Integer> indices = class2indices.get(label);
                if (indices == null) {
                    indices = new ArrayList<Integer>();
                }
                indices.add(i);
                class2indices.put(label, indices);
            }
        }
        if (classNum > 0 && classNum < class2indices.size()) {
            // 需要进行截断
            Map<String, List<Integer>> tmpClass2Indices = new HashMap<>();
            Iterator<Map.Entry<String, List<Integer>>> iterator =  class2indices.entrySet().iterator();
            Map.Entry<String, List<Integer>> entry;
            int tempIndex = 0;
            while(tempIndex < classNum && iterator.hasNext()) {
                entry = iterator.next();
                tmpClass2Indices.put(entry.getKey(), entry.getValue());
                tempIndex++;
            }
            class2indices = tmpClass2Indices;
        }
        int length = 0;
        Iterator<Map.Entry<String, List<Integer>>> iterator = class2indices.entrySet().iterator();
        Map.Entry<String, List<Integer>> entry = null;
        while (iterator.hasNext()) {
            length += iterator.next().getValue().size();
        }
        double [][] dataTmp = new double[length][];
        String [] rawLabelsTmp = new String[length];
        List<Integer> indices = null;
        int[] indexOrder = new int[length];
        for (int i = 0; i < length; i++) {
            indexOrder[i] = i;
        }
        iterator = class2indices.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            entry = iterator.next();
            indices = entry.getValue();
            length = indices.size();
            for (int i = 0; i < length; i++) {
                dataTmp[index] = data[indices.get(i)];
                rawLabelsTmp[index] = rawLabels[indices.get(i)];
                index++;
            }
        }
        // shuffle(indexOrder);
        length = indexOrder.length;
        data = new double[length][];
        rawLabels = new String[length];
        for (int i = 0; i < length; i++) {
            data[i] = dataTmp[indexOrder[i]];
            rawLabels[i] = rawLabelsTmp[indexOrder[i]];
        }
        y = new double[length];
        Map<String, Integer> class2Label = new HashMap<>();
        iterator = class2indices.entrySet().iterator();
        index = 0;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (class2Label.get(entry.getKey()) == null) {
                class2Label.put(entry.getKey(), index);
                index++;
            }
        }
        for (int i = 0 ; i < length; i++) {
            y[i] = class2Label.get(rawLabels[i]);
        }
    }

    public static void readRegressionData(String dataName) throws FileNotFoundException {
        String line = null;
        if (dataName.equals("boston")) {
            // to get the nLines
            nRows = 0;
            FileInputStream fileInputStream = new FileInputStream("./data/housing.data");
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNextLine()) {
                nRows++;
                line = scanner.nextLine();
            }
            featureNum = line.trim().split(",").length - 1;
            System.out.println(String.format("boston housing data has %d records", nRows));
            // parse data
            fileInputStream = new FileInputStream("./data/housing.data");
            scanner = new Scanner(fileInputStream);
            data = new double[nRows][featureNum];
            y = new double[nRows];
            int index = 0;
            String [] tmp = null;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                tmp = line.trim().split(",");
                for (int i = 0; i < featureNum; i++) {
                    data[index][i] = Double.valueOf(tmp[i]);
                }
                y[index] = Double.valueOf(tmp[featureNum]);
                index++;
            }
        }
    }

    private static  void shuffle(int [] indices) {
        Random random = new Random();
        int length = indices.length;
        for (int i =0; i < length; i++) {
            int a, b, tmp;
            a = i;
            b = random.nextInt(length);
            tmp = indices[a];
            indices[a] = indices[b];
            indices[b] = tmp;
        }
    }

    public static double[][] getData() {
        return data;
    }

    public static String[] getRawLabels() {
        return rawLabels;
    }

    public static Map<String, List<Integer>> getClass2indices() {
        return class2indices;
    }

    public static int getnRows() {
        return nRows;
    }

    public static int getFeatureNum() {
        return featureNum;
    }

    public static void splitTrainAndTest(double testSize, boolean shuffle) {
        int size = data.length;
        int evalNum = (int)(size * testSize);
        xEval = new double[evalNum][];
        yEval = new double[evalNum];
        int trainNum = size - evalNum;
        xTrain = new double[trainNum][];
        yTrain = new double[trainNum];
        int[] indices = new int[size];
        for (int i = 0; i < size; i++) {
            indices[i] = i;
        }
        if (shuffle) {
            Utils.shuffle(indices);
        }
        for (int i = 0; i < trainNum; i++) {
            xTrain[i] = data[indices[i]];
            yTrain[i] = y[indices[i]];
        }
        for (int i = 0; i < evalNum; i++) {
            xEval[i] = data[indices[i + trainNum]];
            yEval[i] = y[indices[i + trainNum]];
        }
    }

    public static double[] getY() {
        return y;
    }

    public static double[][] getxTrain() {
        return xTrain;
    }

    public static double[][] getxEval() {
        return xEval;
    }

    public static double[] getyEval() {
        return yEval;
    }

    public static double[] getyTrain() {
        return yTrain;
    }

    /**
     * 将数据进行标准化
     * @param data 待标准化数据
     */
    public static void standardlization(double[][] data) {
        int m = data.length;
        int n = data[0].length;
        // TODO: 对数据进行标准化
    }

    /**
     * 将数据进行归一化
     * @param data 待归一化数据
     */
    public static double[][] getNormalizationParameters(double[][] data) {
        int m = data.length;
        int n = data[0].length;
        double[] min = new double[n], max = new double[n];
        double[][] parameters = new double[n][];
        for (int i = 0; i < n; i++){
            min[i] = 1.0 / 0.0;
            max[i] = -1.0 / 0.0;
            for (int k = 0; k < m; k++) {
                if (data[k][i] < min[i]) {
                    min[i] = data[k][i];
                }
                if (data[k][i] > max[i]) {
                    max[i] = data[k][i];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            parameters[i] = new double[2];
            parameters[i][0] = min[i];
            parameters[i][1] = max[i] - min[i];
        }
        return parameters;
    }
}
