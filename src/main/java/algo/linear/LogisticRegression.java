package algo.linear;

import algo.base.Classifiler;

import java.util.Random;

public class LogisticRegression implements Classifiler {
    private boolean inception;
    private boolean verbose;
    private double[] weights;
    private double learningRate;
    private int nFeatures;
    private int maxIter;
    private double[] probas;
    private static final double eps = 1e-10;
    private static final double eps2 = 1e-20;
    public LogisticRegression(boolean inception, boolean verbose) {
        this(1e-3, 10000000, inception, verbose);
}

    public LogisticRegression(double learningRate, int maxIter, boolean inception, boolean verbose) {
        this.learningRate = learningRate;
        this.maxIter = maxIter;
        this.inception = inception;
        this.verbose = verbose;
        if (verbose) {
            System.out.println(String.format("maxIter: %d", maxIter));
            System.out.println(String.format("learning rate: %f", learningRate));
            System.out.println("inception: " + (inception == true ? "1": "0"));
        }
    }

    private double getProba(double[] sample) {
        double proba = 0;
        int length = sample.length;
        for (int i = 0; i < length; i++) {
            proba += (weights[i] * sample[i]);
        }
        if (inception) {
            proba += weights[length];
        }
        return 1.0 / (1 + Math.exp(-1.0 * proba));
    }

    private void init (double[][] xTrain, int[] labels) throws Exception {
        if (xTrain.length != labels.length) {
            throw new Exception("请检查x和y的形状");
        }
        nFeatures = xTrain[0].length;
        if (verbose) {
            System.out.println(String.format("nFeatures: %d", nFeatures));
        }
        if (inception) {
            weights = new double[nFeatures + 1];
        } else {
            weights = new double[nFeatures];
        }
        int size = weights.length;
        for (int i = 0; i < size; i++) {
            weights[i] = Math.random();
        }
        int m = xTrain.length;
        probas = new double[m];
    }

    @Override
    public void fit(double[][] xTrain, int[] labels){
        try {
            init(xTrain, labels);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateProbas(xTrain);
        double originLoss = getLoss(xTrain, labels), currentLoss;
        // 记录最终迭代的次数
        int iter = 0;
        for (int i = 0; i < maxIter; i++) {
            update(xTrain, labels);
            updateProbas(xTrain);
            currentLoss = getLoss(xTrain, labels);
            if ((i + 1) % 100 == 0 && verbose) {
                System.out.println(String.format("iter:[%d/%d], currentLoss: %.5f", i+1, maxIter, currentLoss));
            }
            double temp = originLoss - currentLoss;
            if (temp > 0 && (Math.abs(temp / originLoss) < eps)) {
                break;
            }
            iter = i + 1;
        }
        System.out.println(String.format("the algorithm stops at iteration: %d", iter));
    }

    private double log(double value) {
        return Math.log(value > eps2 ? value : eps2 );
    }

    private void updateProbas(double[][] xTrain) {
        int m = xTrain.length;
        for (int i = 0; i < m; i++) {
            probas[i] = getProba(xTrain[i]);
        }
    }

    private double getLoss(double[][] xTrain, int[] labels) {
        int m = xTrain.length;
        double loss = 0.0;
        for (int i = 0; i < m; i++) {
            double proba = probas[i];
            loss += (labels[i] * log(proba) + (1 - labels[i]) * log(1 - proba));
        }
        return -1.0 * loss / m;
    }

    private void update(double[][] xTrain, int[] labels) {
        int m = xTrain.length;
        int n = nFeatures;
        for (int i = 0 ; i < n ; i++) {
            double temp = 0.0;
            for (int k = 0; k < m; k++) {
                temp += xTrain[k][i] * (probas[k] - labels[k] * 1.0);
            }
            temp = learningRate * temp / m;
            weights[i] -= temp;
        }
        // TODO 截距项单独处理
        if (inception) {
            double temp = 0.0;
            for (int i = 0; i < m; i++) {
                temp += (probas[i] - labels[i] * 1.0);
            }
            temp = learningRate * temp / m;
            weights[nFeatures] -= temp;
        }
    }

    @Override
    public double[] predictProba(double[][] xEval) {
        int length = xEval.length;
        double[] predProbas = new double[length];
        for (int i = 0; i < length; i++) {
            predProbas[i] = getProba(xEval[i]);
        }
        return predProbas;
    }

    @Override
    public int[] predict(double[][] xEval) {
        double[] probas = predictProba(xEval);
        int length = xEval.length;
        int[] predLabels = new int[length];
        for (int i = 0; i < length; i++) {
            predLabels[i] = probas[i] >= 0.5 ? 1 : 0;
        }
        return  predLabels;
    }

    @Override
    public double getAccuracy(int[] yTrue, int[] yPred) {
        int length = yPred.length;
        int count = 0;
        for (int i = 0 ; i < length; i++) {
            if (yTrue[i] == yPred[i]) {
                count++;
            }
        }
        return 1.0 * count / length;
    }

    @Override
    public double score(double[][] xEval, int[] yEval) {
        int[] predLabels = predict(xEval);
        return getAccuracy(yEval, predLabels);
    }
}
