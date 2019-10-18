package algo.linear;

import algo.utilities.Helper;

public class LinearRegression {
    private static final double eps = 1e-12;
    private static int nFeatures;
    private boolean verbose;
    private boolean inception;
    private double learningRate;
    private boolean normalization;
    private int maxIter;
    private double[] weights;
    private double[][] normalizationParameters;
    private double[] yHat;
    private boolean usePenalty;
    private String penalty;
    private double lambda;
    public LinearRegression(double learningRate, int maxIter, boolean inception, boolean normalization,
                            String penalty, double lambda, boolean verbose) {
        this.inception = inception;
        this.learningRate = learningRate;
        this.maxIter = maxIter;
        this.verbose = verbose;
        this.normalization = normalization;
        this.penalty = penalty;
        if (penalty.equals("l1") || penalty.equals("lasso")) {
            this.penalty = "l1";
        }
        if (penalty.equals("l2") || penalty.equals("ridge")) {
            this.penalty = "l2";
        }
        if (this.penalty.equals("l1") || this.penalty.equals("l2")) {
            usePenalty = true;
        } else{
            usePenalty = false;
        }
        if (verbose) {
            System.out.println(String.format("learningRate: %f", learningRate));
            System.out.println(String.format("maxIter: %d", maxIter));
            System.out.println(String.format("inception: %d", inception ? 1 : 0));
            System.out.println(String.format("normalization: %d", inception ? 1 : 0));
            System.out.println(String.format("usePenalty: %d", usePenalty ? 1: 0));
        }
        if (usePenalty) {
            this.lambda = lambda;
        }
    }

    public LinearRegression(double learningRate, int maxIter, boolean inception, boolean normalization, boolean verbose) {
        this(learningRate, maxIter, inception, normalization, "None", 0.0, verbose);
    }

    public LinearRegression(boolean inception, boolean normalization,boolean verbose) {
        this(1e-3, 1000, inception, normalization, verbose);
    }

    public void fit(double[][] xTrain, double[] yTrain) {
        try {
            init(xTrain, yTrain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int iter = 0;
        xTrain = normalizationTransform(xTrain);
        updateYHat(xTrain);
        double originLoss  =getLoss(xTrain, yTrain), currentLoss = 0.0;
        for (int i = 0; i < maxIter; i++) {
            update(xTrain, yTrain);
            updateYHat(xTrain);
            currentLoss = getLoss(xTrain, yTrain);
            double temp = originLoss - currentLoss;
            iter = i + 1;
            if (temp > 0 && (temp / originLoss <= eps)) {
                break;
            }
            if (temp < 0) {
                break;
            }
            if ((i + 1) % 100 == 0 && verbose) {
                System.out.println(String.format("iteration:[%d/%d], current loss: %.10f", iter, maxIter, currentLoss));
            }
        }
        System.out.println(String.format("the algorithm stops as iteration: %d", iter));
    }

    private void init(double[][] xTrain, double[] yTrain) throws Exception {
        if (xTrain.length != yTrain.length) {
            throw new Exception("请检查x和y的形状");
        }
        nFeatures = xTrain[0].length;
        if (inception) {
            weights = new double[nFeatures + 1];
        } else {
            weights = new double[nFeatures];
        }
        int length = weights.length;
        for (int i = 0; i <length; i++) {
            // weights[i] = Math.random();
            weights[i] = 1.0 / (i + 1);
        }
        if (normalization) {
            normalizationParameters = Helper.getNormalizationParameters(xTrain);
        } else {
            normalizationParameters = null;
        }
        int m = xTrain.length;
        yHat = new double[m];
    }

    private void updateYHat(double[][] xTrain) {
        int m = xTrain.length;
        int n = xTrain[0].length;
        for (int i = 0; i < m; i++) {
            yHat[i] = 0.0;
            for (int k = 0; k < n; k++) {
                yHat[i] += weights[k] * xTrain[i][k];
            }
            if (inception) {
                yHat[i] += weights[n];
            }
        }
    }

    private double getLoss(double[][] xTrain, double[] yTrain) {
        double loss = 0.0;
        int m = xTrain.length;
        double tmp = 0.0;
        for (int i = 0; i < m; i++) {
            tmp = yTrain[i] - yHat[i];
            loss += (tmp * tmp);
        }
        loss = loss / (2 * m);
        return loss;
    }

    private double sign(double x) {
        if (x == 0) {
            return 0;
        }
        return x > 0 ? 1.0 : -1.0;
    }

    private double[] getDifferentialOnPenalty(int m) {
        int n = weights.length;
        double[] partialPenalty = new double[n];
        if (this.penalty.equals("l1")) {
            // lasso
            for (int i = 0; i < n;i++) {
                partialPenalty[i] =  lambda * sign(weights[i]) / m;
            }
        }
        else if (this.penalty.equals("l2")) {
            // ridge
            for (int i = 0; i < n; i++) {
                partialPenalty[i] = lambda * weights[i] / m;
            }
        } else {
            for (int i = 0; i < n; i++) {
                partialPenalty[i] = 0.0;
            }
        }
        return partialPenalty;
    }

    private void update(double[][] xTrain, double[] yTrain) {
        int n = nFeatures;
        double delta = 0.0;
        int m = xTrain.length;
        for (int i = 0; i < m; i++) {
            delta += (yHat[i] - yTrain[i]);
        }
        delta /= m;
        delta *= learningRate;
        int lenght = weights.length;
        double[] partialPenalty = getDifferentialOnPenalty(m);
        for (int i = 0; i < lenght; i++) {
            weights[i] -= (delta * weights[i]);
            weights[i] += partialPenalty[i];
        }
    }

    public double[] predict(double [][] xEval) {
        int m = xEval.length;
        int n = xEval[0].length;
        double[] result = new double[m];
        for (int i = 0; i < m; i++) {
            result[i] = 0.0;
            for (int k = 0; k < n; k++) {
                result[i] += weights[k] * xEval[i][k];
            }
            if (inception) {
                result[i] += weights[n];
            }
        }
        return result;
    }

    public double score(double[][] xEval, double[] yEval) {
        int m = xEval.length;
        int n = nFeatures;
        if (normalization) {
            xEval = normalizationTransform(xEval);
        }
        double[] yHat = predict(xEval);
        double score = 0.0;
        for (int i = 0; i < m; i++) {
            double temp = yHat[i] - yEval[i];
            score += (temp * temp);
        }
        score /= (2 * m);
        return score;
    }

    private double[][] normalizationTransform(double[][] data) {
        int m = data.length;
        int n = nFeatures;
        double [][] result = new double[m][n];
        for (int i = 0; i < m; i++){
            for (int k =0 ; k < n; k++) {
                result[i][k] = ((data[i][k] - normalizationParameters[k][0]) / normalizationParameters[k][1]);
            }
        }
        return result;
    }
}
