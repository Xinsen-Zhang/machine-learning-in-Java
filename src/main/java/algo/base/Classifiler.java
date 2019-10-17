package algo.base;

public interface Classifiler {
    void fit(double[][] xTrain, int[] labels);
    int[] predict(double[][] xEval);
    double[] predictProba(double[][] xEval);
    double getAccuracy(int[] yTrue, int[] yPred);
    double score(double[][] xEval, int[] yEval);
}
